package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java.entity.BookType;

/**
 * 图书类别Repository接口
 * @author zxy
 *
 */
public interface BookTypeRepository extends JpaRepository<BookType, Integer>,JpaSpecificationExecutor<BookType>{
	
	/**
	 * 获取图书类别以及对应图书数量
	 * @return
	 */
	@Query(value="select t2.id,t2.name,t2.order_no,count(t1.id) as bookCount from t_book t1 right join t_book_type t2 on t1.book_type_id = t2.id group by t2.id order by t2.order_no",nativeQuery=true)
	public List<BookType> countList();

}
