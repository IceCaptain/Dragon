package com.java.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.java.entity.Notice;
import com.java.repository.NoticeRepository;
import com.java.service.NoticeService;

/**
 * 通知Service接口实现类
 * @author zxy
 *
 */
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{
	
	@Resource
	private NoticeRepository noticeRepository;

	@Override
	public List<Notice> list(Integer page, Integer pageSize) {
		// TODO Auto-generated method stub
		return noticeRepository.findAll(new PageRequest(page-1, pageSize, Sort.Direction.DESC,"releaseDate")).getContent();
	}

	@Override
	public Long getCount() {
		// TODO Auto-generated method stub
		return noticeRepository.count();
	}

	@Override
	public Notice getById(Integer id) {
		// TODO Auto-generated method stub
		return noticeRepository.findOne(id);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		noticeRepository.delete(id);
	}

	@Override
	public void save(Notice notice) {
		// TODO Auto-generated method stub
		noticeRepository.save(notice);
	}

}
