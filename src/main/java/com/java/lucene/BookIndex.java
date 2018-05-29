package com.java.lucene;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.java.entity.Book;
import com.java.util.StringUtil;

/**
 * 图书索引类
 * @author zxy
 *
 */
@Component
public class BookIndex {
	
	// /usr/local/bookIndex
	// D:/bookIndex
	
	@Value("${bookIndexDirPath}")
	private String bookIndexDirPath;
	
	
	private Directory dir;//索引目录
	
	/**
	 * 获取IndexWriter实例
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getIndexWriter()throws Exception{
		dir=FSDirectory.open(Paths.get(bookIndexDirPath));
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();//中文分词器
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
		IndexWriter writer=new IndexWriter(dir, iwc);
		return writer;
	}
	
	/**
	 * 添加图书索引
	 * @param book
	 * @throws Exception
	 */
	public void addIndex(Book book)throws Exception{
		IndexWriter writer=getIndexWriter();
		Document doc=new Document();
		doc.add(new StringField("id", book.getId().toString(), Field.Store.YES));
		doc.add(new TextField("name", book.getName(), Field.Store.YES));
		doc.add(new TextField("author", book.getAuthor(), Field.Store.YES));
		doc.add(new TextField("publisher", book.getPublisher(), Field.Store.YES));
		doc.add(new TextField("content", book.getContent(), Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
	}
	
	/**
	 * 删除图书索引
	 * @param bookId
	 * @throws Exception
	 */
	public void deleteIndex(String bookId)throws Exception{
		IndexWriter writer=getIndexWriter();
		writer.deleteDocuments(new Term("id",bookId));
		writer.forceMergeDeletes();//强制删除
		writer.commit();
		writer.close();
	}
	
	/**
	 * 更新图书索引
	 * @param book
	 * @throws Exception
	 */
	public void updateIndex(Book book)throws Exception{
		IndexWriter writer=getIndexWriter();
		Document doc=new Document();
		doc.add(new StringField("id", book.getId().toString(), Field.Store.YES));
		doc.add(new TextField("name", book.getName(), Field.Store.YES));
		doc.add(new TextField("author", book.getAuthor(), Field.Store.YES));
		doc.add(new TextField("publisher", book.getPublisher(), Field.Store.YES));
		doc.add(new TextField("content", book.getContent(), Field.Store.YES));
		writer.updateDocument(new Term("id", book.getId().toString()), doc);
		writer.close();
	}
	
	/**
	 * 查询图书信息
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<Book> searchBook(String q)throws Exception{
		dir=FSDirectory.open(Paths.get(bookIndexDirPath));
		IndexReader reader=DirectoryReader.open(dir);
		IndexSearcher is=new IndexSearcher(reader);
		
		BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();//构建与或检索Query
		
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();//中文分词器
		
		QueryParser nameParser=new QueryParser("name", analyzer);//指定检索范围在name字段
		Query nameQuery=nameParser.parse(q);//构建图书名称检索Query
		
		QueryParser authorParser=new QueryParser("author", analyzer);//指定检索范围在author字段
		Query authorQuery=authorParser.parse(q);//构建图书作者检索Query
		
		QueryParser publisherParser=new QueryParser("publisher", analyzer);//指定检索范围在publisher字段
		Query publisherQuery=publisherParser.parse(q);//构建图书出版社检索Query
		
		QueryParser contentParser=new QueryParser("content", analyzer);//指定检索范围在content字段
		Query contentQuery=contentParser.parse(q);//构建图书简介检索Query
		
		//添加检索Query，构建或关系
		booleanQuery.add(nameQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(authorQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(publisherQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(contentQuery, BooleanClause.Occur.SHOULD);
		
		TopDocs hits=is.search(booleanQuery.build(), 100);//返回检索库中得分最高的100条结果
		QueryScorer scorer=new QueryScorer(nameQuery);
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");//设置高亮的样式
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		List<Book> bookList=new LinkedList<Book>();
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);//实例化得分document
			Book book=new Book();
			book.setId(Integer.parseInt(doc.get("id")));
			String name=doc.get("name");
			String author=doc.get("author");
			String publisher=doc.get("publisher");
			String content=doc.get("content");
			
			//设置图书名称高亮
			if(name!=null){
				TokenStream tokenStream=analyzer.tokenStream("name", new StringReader(name));//检索name字段
				String hName=highlighter.getBestFragment(tokenStream, name);
				//检索结果有匹配关键字
				if(StringUtil.isNotEmpty(hName)){
					book.setName(hName);
				}else{
					book.setName(name);
				}
			}
			
			//设置图书作者高亮
			if(author!=null){
				TokenStream tokenStream=analyzer.tokenStream("author", new StringReader(author));//检索author字段
				String hAuthor=highlighter.getBestFragment(tokenStream, author);
				//检索结果有匹配关键字
				if(StringUtil.isNotEmpty(hAuthor)){
					book.setAuthor(hAuthor);
				}else{
					book.setAuthor(author);
				}
			}
			
			//设置图书出版社高亮
			if(publisher!=null){
				TokenStream tokenStream=analyzer.tokenStream("publisher", new StringReader(publisher));//检索publisher字段
				String hPublisher=highlighter.getBestFragment(tokenStream, publisher);
				//检索结果有匹配关键字
				if(StringUtil.isNotEmpty(hPublisher)){
					book.setPublisher(hPublisher);
				}else{
					book.setPublisher(publisher);
				}
			}
			
			//设置图书简介高亮
			if(content!=null){
				TokenStream tokenStream=analyzer.tokenStream("content", new StringReader(content));//检索content字段
				String hContent=highlighter.getBestFragment(tokenStream, content);
				//检索结果有匹配关键字
				if(StringUtil.isNotEmpty(hContent)){
					book.setContent(hContent);
				}else{
					if(content.length()>150){
						book.setContent(content.substring(0, 150));
					}else{
						book.setContent(content);
					}
				}
			}
			bookList.add(book);
		}
		reader.close();
		return bookList;
	}

}
