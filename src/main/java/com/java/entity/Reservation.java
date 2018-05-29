package com.java.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 预约表实体
 * @author zxy
 *
 */
@Entity
@Table(name="t_reservation")
public class Reservation {
	
	@Id
	@GeneratedValue
	private Integer id;//编号
	
	@ManyToOne
	@JoinColumn(name="bookId")
	private Book book;//预约图书
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;//预约用户
	
	private Integer reservation_order;//预约序号

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getReservation_order() {
		return reservation_order;
	}

	public void setReservation_order(Integer reservation_order) {
		this.reservation_order = reservation_order;
	}

}
