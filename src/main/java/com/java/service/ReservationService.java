package com.java.service;

import java.util.List;

import com.java.entity.Reservation;

/**
 * 预约Service接口
 * @author zxy
 *
 */
public interface ReservationService {
	
	/**
	 * 添加或修改预约
	 * @param reservation
	 */
	public void save(Reservation reservation);
	
	/**
	 * 查询指定图书的最后一条预约序号
	 * @param bookId
	 * @return
	 */
	public Integer getLastReservationOrder(Integer bookId);
	
	/**
	 * 根据图书Id和用户Id查询预约信息
	 * @param bookId
	 * @param userId
	 * @return
	 */
	public Reservation getByBookIdAndUserId(Integer bookId,Integer userId);
	
	/**
	 * 查询指定图书的第一条预约信息
	 * @param bookId
	 * @return
	 */
	public Reservation getFirstReservation(Integer bookId);
	
	/**
	 * 根据Id删除预约信息
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 分页查询相应用户的预约信息
	 * @param userId
	 * @return
	 */
	public List<Reservation> userReservation(Integer userId, Integer start, Integer pageSize);
	
	/**
	 * 获取相应用户预约信息的总记录数
	 * @param userId
	 * @return
	 */
	public Long getReservationCount(Integer userId);
	
	/**
	 * 查询指定图书在预约序号reservation_order之前的人数
	 * @param bookId
	 * @param reservation_order
	 * @return
	 */
	public Integer getPreviousCount(Integer bookId,Integer reservation_order);

}
