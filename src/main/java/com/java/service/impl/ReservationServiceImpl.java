package com.java.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.entity.Reservation;
import com.java.repository.ReservationRepository;
import com.java.service.ReservationService;

/**
 * 预约Service接口实现类
 * @author zxy
 *
 */
@Service("reservationService")
public class ReservationServiceImpl implements ReservationService{
	
	@Resource
	private ReservationRepository reservationRepository;

	@Override
	public void save(Reservation reservation) {
		// TODO Auto-generated method stub
		reservationRepository.save(reservation);
	}

	@Override
	public Integer getLastReservationOrder(Integer bookId) {
		// TODO Auto-generated method stub
		return reservationRepository.getLastReservationOrder(bookId);
	}

	@Override
	public Reservation getByBookIdAndUserId(Integer bookId, Integer userId) {
		// TODO Auto-generated method stub
		return reservationRepository.getByBookIdAndUserId(bookId, userId);
	}

	@Override
	public Reservation getFirstReservation(Integer bookId) {
		// TODO Auto-generated method stub
		return reservationRepository.getFirstReservation(bookId);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		reservationRepository.delete(id);
	}

	@Override
	public List<Reservation> userReservation(Integer userId, Integer start, Integer pageSize) {
		// TODO Auto-generated method stub
		return reservationRepository.userReservation(userId, start, pageSize);
	}
	
	@Override
	public Long getReservationCount(Integer userId) {
		// TODO Auto-generated method stub
		return reservationRepository.getReservationCount(userId);
	}


	@Override
	public Integer getPreviousCount(Integer bookId, Integer reservation_order) {
		// TODO Auto-generated method stub
		return reservationRepository.getPreviousCount(bookId, reservation_order);
	}

}
