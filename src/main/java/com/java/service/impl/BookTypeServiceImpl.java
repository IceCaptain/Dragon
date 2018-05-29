package com.java.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java.entity.BookType;
import com.java.repository.BookTypeRepository;
import com.java.service.BookTypeService;

/**
 * 图书类别Service接口实现类
 * @author zxy
 *
 */
@Service("bookTypeService")
public class BookTypeServiceImpl implements BookTypeService{
	
	@Resource
	private BookTypeRepository bookTypeRepository;

	@Override
	public List<BookType> countList() {
		// TODO Auto-generated method stub
		return bookTypeRepository.countList();
	}

	@Override
	public List<BookType> list() {
		// TODO Auto-generated method stub
		return bookTypeRepository.findAll();
	}

	@Override
	public List<BookType> list(BookType bookType, Integer page, Integer pageSize) {
		// TODO Auto-generated method stub
		Page<BookType> pageBookType=bookTypeRepository.findAll(new Specification<BookType>() {
			
			@Override
			public Predicate toPredicate(Root<BookType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate predicate=cb.conjunction();
				if(bookType!=null){
					
				}
				return predicate;
			}
		}, new PageRequest(page-1, pageSize, Sort.Direction.ASC,"orderNo"));
		return pageBookType.getContent();
	}

	@Override
	public Long getCount() {
		// TODO Auto-generated method stub
		return bookTypeRepository.count();
	}

	@Override
	public BookType getById(Integer id) {
		// TODO Auto-generated method stub
		return bookTypeRepository.findOne(id);
	}

	@Override
	public void save(BookType bookType) {
		// TODO Auto-generated method stub
		bookTypeRepository.save(bookType);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		bookTypeRepository.delete(id);
	}

}
