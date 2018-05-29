package com.java.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * 图书实体
 * @author zxy
 *
 */
@Entity
@Table(name="t_book")
public class Book {
	
	@Id
	@GeneratedValue
	private Integer id;//编号
	
	@Column(length=50)
	private String name;//图书名称
	
	@Column(length=100)
	private String imageName;//图片名称
	
	@Column(length=30)
	private String author;//作者
	
	@Column(length=50)
	private String publisher;//出版社

//	sqlite 数据库注释掉
//	@Lob
//	@Column(columnDefinition="TEXT")
	private String content;//简介
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;//借阅者
	
//	@Temporal(TemporalType.DATE)
	private Date lendDate;//借阅时间
	
	@Transient
	private Date returnDate;//还书时间
	
	private Integer reservation_count;//预约人数
	
	@ManyToOne
	@JoinColumn(name="bookTypeId")
	private BookType bookType;//图书类别
	
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

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getLendDate() {
		return lendDate;
	}

	public void setLendDate(Date lendDate) {
		this.lendDate = lendDate;
	}
	
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Integer getReservation_count() {
		return reservation_count;
	}

	public void setReservation_count(Integer reservation_count) {
		this.reservation_count = reservation_count;
	}

	public BookType getBookType() {
		return bookType;
	}

	public void setBookType(BookType bookType) {
		this.bookType = bookType;
	}

}
