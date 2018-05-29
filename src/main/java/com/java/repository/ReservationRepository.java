package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java.entity.Reservation;

/**
 * 预约Repository接口
 * @author zxy
 *
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer>,JpaSpecificationExecutor<Reservation>{
	
	/**
	 * 查询指定图书的最后一条预约序号
	 * @param bookId
	 * @return
	 */
	@Query(value="select reservation_order from t_reservation where book_id=?1 order by reservation_order desc limit 1",nativeQuery=true)
	public Integer getLastReservationOrder(Integer bookId);
	
	/**
	 * 根据图书Id和用户Id查询预约信息
	 * @param bookId
	 * @param userId
	 * @return
	 */
	@Query(value="select * from t_reservation where book_id=?1 and user_id=?2",nativeQuery=true)
	public Reservation getByBookIdAndUserId(Integer bookId,Integer userId);
	
	/**
	 * 查询指定图书的第一条预约信息
	 * @param bookId
	 * @return
	 */
	@Query(value="select * from t_reservation where book_id=?1 order by reservation_order asc limit 1",nativeQuery=true)
	public Reservation getFirstReservation(Integer bookId);
	
	/**
	 * 分页查询相应用户的预约信息
	 * @param userId
	 * @return
	 */
	@Query(value="select * from t_reservation where user_id=?1 limit ?2,?3",nativeQuery=true)
	public List<Reservation> userReservation(Integer userId, Integer start, Integer pageSize);
	
	/**
	 * 获取相应用户预约信息的总记录数
	 * @param userId
	 * @return
	 */
	@Query(value="select count(*) from t_reservation where user_id=?1",nativeQuery=true)
	public Long getReservationCount(Integer userId);
	
	/**
	 * 查询指定图书在预约序号reservation_order之前的人数
	 * @param bookId
	 * @param reservation_order
	 * @return
	 */
	@Query(value="select count(*) from t_reservation where book_id=?1 and reservation_order< ?2",nativeQuery=true)
	public Integer getPreviousCount(Integer bookId,Integer reservation_order);

}
