package com.java.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 图书类别实体
 * @author zxy
 *
 */
@Entity
@Table(name="t_bookType")
public class BookType {
	
	@Id
	@GeneratedValue
	private Integer id;//编号
	
	@Column(length=30)
	private String name;//类型名称
	
	private Integer orderNo;//排序序号
	
	private Integer bookCount;//图书数量

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getBookCount() {
		return bookCount;
	}

	public void setBookCount(Integer bookCount) {
		this.bookCount = bookCount;
	}

}
