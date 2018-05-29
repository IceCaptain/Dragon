package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java.entity.Book;

/**
 * 图书Repository接口 
 * @author zxy
 *
 */
public interface BookRepository extends JpaRepository<Book, Integer>,JpaSpecificationExecutor<Book>{
	
	/**
	 * 查询未借阅书籍
	 * @return
	 */
	@Query(value="select * from t_book where user_id is null limit ?1,?2",nativeQuery=true)
	public List<Book> findNotLendBook(Integer start,Integer pageSize);
	
	/**
	 * 获取未借阅书籍的总记录数
	 * @return
	 */
	@Query(value="select count(*) from t_book where user_id is null",nativeQuery=true)
	public Long getNotLendBookCount();
	
	/**
	 * 随机获取相应类别的n本图书，去除id=bookId的图书
	 * @param n
	 * @param bookId
	 * @return
	 */
	// rand() (mysql) random() (sqlite)
	@Query(value="select * from t_book where book_type_id=?1 and id not in(?2) order by random() limit ?3",nativeQuery=true)
	public List<Book> randomList(Integer bookTypeId,Integer bookId,Integer n);
	
	/**
	 * 查询所有已借阅的图书信息
	 * @return
	 */
	@Query(value="select * from t_book where lend_date is not null",nativeQuery=true)
	public List<Book> findAllLendBook();
	
}
