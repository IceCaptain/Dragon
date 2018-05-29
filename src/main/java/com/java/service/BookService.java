package com.java.service;

import java.util.List;

import com.java.entity.Book;

/**
 * 图书Service接口
 * @author zxy
 *
 */
public interface BookService {
	
	/**
	 * 分页查询图书信息
	 * @param book
	 * @param flag
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<Book> list(Book book,Integer flag, List<Integer> userIds,Integer page,Integer pageSize);
	
	/**
	 * 查询总记录数
	 * @param book
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Long getCount(Book book,Integer flag, List<Integer> userIds);
	
	/**
	 * 分页查询未借阅书籍
	 * @return
	 */
	public List<Book> findNotLendBook(Integer page,Integer pageSize);
	
	/**
	 * 获取未借阅书籍的总记录数
	 * @return
	 */
	public Long getNotLendBookCount();
	
	/**
	 * 根据Id获取图书信息
	 * @param id
	 */
	public Book getById(Integer id);
	
	/**
	 * 随机获取相应类别的n本图书，去除id=bookId的图书
	 * @param n
	 * @param bookId
	 * @return
	 */
	public List<Book> randomList(Integer bookTypeId,Integer bookId,Integer n);
	
	/**
	 * 添加或修改图书信息
	 * @param book
	 */
	public void save(Book book);
	
	/**
	 * 根据Id删除图书信息
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 给编号为Id的图书添加借阅计时 返回值 0：图书借阅时间到期  1：图书归还成功
	 * @param id
	 */
	public Integer lendTimer(Integer id);
	
	/**
	 * 查询所有已借阅的图书信息
	 * @return
	 */
	public List<Book> findAllLendBook();
	
}
