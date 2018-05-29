package com.java.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Notice;
import com.java.service.NoticeService;
import com.java.util.PageUtil;

/**
 * 通知Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Resource
	private NoticeService noticeService;
	
	/**
	 * 分页查询通知信息
	 * @param page
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public ModelAndView list(@PathVariable("page")Integer page){
		if(page==null){
			page=1;
		}
		ModelAndView mav=new ModelAndView();
		List<Notice> noticeList=noticeService.list(page, 20);
		Long total=noticeService.getCount();
		mav.addObject("noticeList", noticeList);
		mav.addObject("pageCode", PageUtil.genPagination("/notice/list", total, page, 20, ""));
		mav.addObject("title", "所有通知");
		mav.addObject("mainPage", "/notice/list");
		mav.addObject("mainPageKey", "#n");
		mav.setViewName("/index");
		return mav;
	}
	
	/**
	 * 根据Id查询通知信息
	 * @return
	 */
	@RequestMapping("/{id}")
	public ModelAndView view(@PathVariable("id")Integer id){
		ModelAndView mav=new ModelAndView();
		Notice notice=noticeService.getById(id);
		mav.addObject("notice", notice);
		mav.addObject("title", notice.getTitle());
		mav.addObject("mainPage", "/notice/view");
		mav.addObject("mainPageKey", "#n");
		mav.setViewName("/index");
		return mav;
	}

}
