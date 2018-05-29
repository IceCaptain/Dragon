package com.java.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Book;
import com.java.entity.BookType;
import com.java.service.BookService;
import com.java.util.PageUtil;

/**
 * 用户根目录路径及其他请求
 * @author zxy
 *
 */
@Controller
public class IndexController {
	
	@Resource
	private BookService bookService;
	
	/**
	 * 根路径请求
	 * @return
	 */
	@RequestMapping("/{page}")
	public ModelAndView root(@PathVariable("page")Integer page,Book book,@RequestParam(value="flag",required=false)Integer flag){
		if(page==null){
			page=1;
		}
		ModelAndView mav=new ModelAndView();
		StringBuffer param=new StringBuffer();
		List<Book> bookList;
		Long total;
		if(flag!=null&&flag==1){
			bookList=bookService.findNotLendBook(page, 12); 
			total=bookService.getNotLendBookCount();
			param.append("flag=1");
		}else{
			bookList=bookService.list(book, null, null, page, 12);
			total=bookService.getCount(book, null, null);
		}
		for(int i=0;i<bookList.size();i++){
			String bookName=bookList.get(i).getName();
			if(bookName.length()>10){
				bookList.get(i).setName(bookName.substring(0, 10)+" ...");
			}else{
				bookList.get(i).setName(bookName);
			}
		}
		mav.addObject("bookList", bookList);
		mav.addObject("pageCode", PageUtil.genPagination("", total, page, 12,param.toString()));
		mav.addObject("title", "首页");
		mav.addObject("mainPage", "/book/list");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/index");
		return mav;
	}
	
	/**
	 * 请求相应图书类别的图书信息
	 * @return
	 */
	@RequestMapping("/{bookTypeId}/{page}")
	public ModelAndView root(@PathVariable("bookTypeId")Integer bookTypeId,@PathVariable("page")Integer page){
		if(page==null){
			page=1;
		}
		ModelAndView mav=new ModelAndView();
		StringBuffer param=new StringBuffer();
		Book book=new Book();
		BookType bookType=new BookType();
		bookType.setId(bookTypeId);
		book.setBookType(bookType);
		List<Book> bookList=bookService.list(book, null, null, page, 12);
		for(int i=0;i<bookList.size();i++){
			String bookName=bookList.get(i).getName();
			if(bookName.length()>10){
				bookList.get(i).setName(bookName.substring(0, 10)+" ...");
			}else{
				bookList.get(i).setName(bookName);
			}
		}
		Long total=bookService.getCount(book,null,null);
		mav.addObject("bookList", bookList);
		mav.addObject("pageCode", PageUtil.genPagination("/"+bookTypeId, total, page, 12,param.toString()));
		mav.addObject("title", "首页");
		mav.addObject("mainPage", "/book/list");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/index");
		return mav;
	}
	
	/**
	 * 关于本站
	 * @return
	 */
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "关于本站");
		mav.addObject("mainPage", "/common/aboutMe");
		mav.addObject("mainPageKey", "#a");
		mav.setViewName("/index");
		return mav;
	}
	
	/**
	 * 登录请求
	 * @return
	 */
	@RequestMapping("/")
	public String root(){
		return "redirect:/1";
	}
	
	/**
	 * 登录请求
	 * @return
	 */
	@RequestMapping("/login")
	public String login(){
		return "/login";
	}
}
