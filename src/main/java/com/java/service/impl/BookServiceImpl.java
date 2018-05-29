package com.java.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java.entity.Book;
import com.java.repository.BookRepository;
import com.java.runner.StartupRunner;
import com.java.service.BookService;
import com.java.util.StringUtil;

/**
 * 图书Service接口实现类
 * @author zxy
 *
 */
@Service("bookService")
public class BookServiceImpl implements BookService{
	
	@Resource
	private BookRepository bookRepository;
	
	@Resource
	private StartupRunner startupRunner;
	
	@Value("${lendTime}")
	private Long lendTime;
	
	@Value("${lagTime}")
	private Long lagTime;

	@Override
	public List<Book> list(Book book, Integer flag, List<Integer> userIds, Integer page, Integer pageSize) {
		// TODO Auto-generated method stub
		Page<Book> pageBook=bookRepository.findAll(new Specification<Book>() {
			
			@Override
			public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate predicate=cb.conjunction();
				if(book!=null){
					if(StringUtil.isNotEmpty(book.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+book.getName()+"%"));
					}
					if(StringUtil.isNotEmpty(book.getAuthor())){
						predicate.getExpressions().add(cb.like(root.get("author"), "%"+book.getAuthor()+"%"));
					}
					if(StringUtil.isNotEmpty(book.getPublisher())){
						predicate.getExpressions().add(cb.like(root.get("publisher"), "%"+book.getPublisher()+"%"));
					}
					if(userIds!=null){
						if(userIds.size()>0){
							predicate.getExpressions().add(cb.in(root.get("user").get("id")).value(userIds));
						}else{
							predicate.getExpressions().add(cb.in(root.get("user").get("id")).value(0));
						}
					}
					if(book.getBookType()!=null&&book.getBookType().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("bookType").get("id"), book.getBookType().getId()));
					}
				}
				if(flag!=null){
					if(flag==1){
						predicate.getExpressions().add(cb.isNotNull(root.get("lendDate")));
					}else{
						predicate.getExpressions().add(cb.isNull(root.get("lendDate")));
					}
				}
				return predicate;
			}
		}, new PageRequest(page-1, pageSize));
		return pageBook.getContent();
	}

	@Override
	public Long getCount(Book book,Integer flag,List<Integer> userIds) {
		// TODO Auto-generated method stub
		Long total=bookRepository.count(new Specification<Book>() {

			@Override
			public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate predicate=cb.conjunction();
				if(book!=null){
					if(StringUtil.isNotEmpty(book.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+book.getName()+"%"));
					}
					if(StringUtil.isNotEmpty(book.getAuthor())){
						predicate.getExpressions().add(cb.like(root.get("author"), "%"+book.getAuthor()+"%"));
					}
					if(StringUtil.isNotEmpty(book.getPublisher())){
						predicate.getExpressions().add(cb.like(root.get("publisher"), "%"+book.getPublisher()+"%"));
					}
					if(userIds!=null){
						if(userIds.size()>0){
							predicate.getExpressions().add(cb.in(root.get("user").get("id")).value(userIds));
						}else{
							predicate.getExpressions().add(cb.in(root.get("user").get("id")).value(0));
						}
					}
					if(book.getBookType()!=null&&book.getBookType().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("bookType").get("id"), book.getBookType().getId()));
					}
				}
				if(flag!=null){
					if(flag==1){
						predicate.getExpressions().add(cb.isNotNull(root.get("lendDate")));
					}else{
						predicate.getExpressions().add(cb.isNull(root.get("lendDate")));
					}
				}
				return predicate;
			}
		});
		return total;
	}

	@Override
	public List<Book> findNotLendBook(Integer page,Integer pageSize) {
		// TODO Auto-generated method stub
		return bookRepository.findNotLendBook((page-1)*pageSize,pageSize);
	}

	@Override
	public Long getNotLendBookCount() {
		// TODO Auto-generated method stub
		return bookRepository.getNotLendBookCount();
	}

	@Override
	public Book getById(Integer id) {
		// TODO Auto-generated method stub
		return bookRepository.findOne(id);
	}

	@Override
	public List<Book> randomList(Integer bookTypeId, Integer bookId, Integer n) {
		// TODO Auto-generated method stub
		return bookRepository.randomList(bookTypeId, bookId, n);
	}

	@Override
	public void save(Book book) {
		// TODO Auto-generated method stub
//		bookRepository.saveAndFlush(book);
		bookRepository.save(book);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		bookRepository.delete(id);
	}

	@Override
	public Integer lendTimer(Integer id) {
		// TODO Auto-generated method stub
		Integer flag=0;
		Book book=bookRepository.findOne(id);
		Date returnDate=new Date(book.getLendDate().getTime()+lendTime);
		int i=0;
		while (new Date(new Date().getTime()+lagTime).getTime() < returnDate.getTime()) {
			Integer state=startupRunner.getBookTimerState(book.getName());
			if(state!=null&&state==1){
				flag=1;
				break;
			}
			i++;
			try {
				Thread.sleep(1000);
//				int month = i / 60 / 60 / 24 / 30 % 12;
				int day = i / 60 / 60 / 24 % 30;
				int hour = i / 60 / 60 % 24;
				int minute = i / 60 % 60;
				int second = i % 60;
				System.out.println("已借" + day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(flag==0){
			System.out.println("《"+book.getName()+"》还书时间到了！");
		}
		return flag;
	}

	@Override
	public List<Book> findAllLendBook() {
		// TODO Auto-generated method stub
		return bookRepository.findAllLendBook();
	}

}
