package com.java.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Book;
import com.java.entity.Reservation;
import com.java.entity.User;
import com.java.service.BookService;
import com.java.service.ReservationService;
import com.java.service.UserService;
import com.java.util.CryptographyUtil;
import com.java.util.PageUtil;
import com.java.util.StringUtil;

/**
 * 用户Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private BookService bookService;
	
	@Resource
	private ReservationService reservationService;
	
	@Value("${lendTime}")
	private Long lendTime;
	
	/**
	 * 用户个人信息默认页面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView index(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "主页");
		mav.addObject("menuTitle", "主页");
		mav.addObject("mainPage", "/user/common/default");
		mav.addObject("mainPageKey", "#d");
		mav.setViewName("/user/index");
		return mav;
	}
	
	/**
	 * 用户个人信息默认页面
	 * @return
	 */
	@RequestMapping("/message")
	public ModelAndView personalMessage(@RequestParam("userName")String userName){
		ModelAndView mav=new ModelAndView();
		User user=userService.getByName(userName);
		user.setPassword(null);
		mav.addObject("user", user);
		mav.addObject("title", "个人信息");
		mav.addObject("menuTitle", "个人信息");
		mav.addObject("mainPage", "/user/personal/message");
		mav.addObject("mainPageKey", "#p");
		mav.setViewName("/user/index");
		return mav;
	}
	
	/**
	 * 修改用户个人信息
	 * @param notice
	 * @return
	 */
	@RequestMapping("/messageEdit")
	public String messageEdit(User user){
		User oldUser=userService.getById(user.getId());
		oldUser.setTrueName(user.getTrueName());
		oldUser.setEmail(user.getEmail());
		userService.save(oldUser);
		return "redirect:/user/message?userName="+oldUser.getUserName();
	}
	
	/**
	 * 查询用户已借阅图书
	 * @param userName
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public ModelAndView bookList(@PathVariable("page")Integer page,@RequestParam("userName")String userName){
		if(page==null){
			page=1;
		}
		ModelAndView mav=new ModelAndView();
		User user=userService.getByName(userName);
		Book book=new Book();
		List<Integer> userIds=new ArrayList<Integer>();
		userIds.add(user.getId());
		List<Book> bookList=bookService.list(book, null, userIds, page, 8);
		Long total=bookService.getCount(book,null,userIds);
		StringBuffer param=new StringBuffer();
		if(StringUtil.isNotEmpty(userName)){
			param.append("userName="+userName);
		}
		for(int i=0;i<bookList.size();i++){
			String bookName=bookList.get(i).getName();
			String author=bookList.get(i).getAuthor();
			Date lendDate=bookList.get(i).getLendDate();
			if(lendDate!=null){
				Date returnDate=new Date(lendDate.getTime()+lendTime);
				bookList.get(i).setReturnDate(returnDate);
			}
			if(bookName.length()>12){
				bookList.get(i).setName(bookName.substring(0, 12)+" ...");
			}else{
				bookList.get(i).setName(bookName);
			}
			if(author.length()>12){
				bookList.get(i).setAuthor(author.substring(0, 12)+" ...");
			}else{
				bookList.get(i).setAuthor(author);
			}
		}
		mav.addObject("bookList", bookList);
		mav.addObject("pageCode", PageUtil.genPagination("/user/list", total, page, 8,param.toString()));
		mav.addObject("title", "我的图书");
		mav.addObject("menuTitle", "我的图书");
		mav.addObject("mainPage", "/user/book/list");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/user/index");
		return mav;
	}
	
	/**
	 * 查询用户已预约图书
	 * @param userName
	 * @return
	 */
	@RequestMapping("/reservation/{page}")
	public ModelAndView reservationList(@PathVariable("page")Integer page,@RequestParam("userName")String userName){
		if(page==null){
			page=1;
		}
		Integer pageSize=8;
		ModelAndView mav=new ModelAndView();
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		User user=userService.getByName(userName);
		List<Reservation> reservationList=reservationService.userReservation(user.getId(), (page-1)*pageSize, pageSize);
		Long total=reservationService.getReservationCount(user.getId());
		for(Reservation reservation:reservationList){
			Map<String,Object> map=new HashMap<String,Object>();
			Book book=bookService.getById(reservation.getBook().getId());
			String bookName=book.getName();
			String author=book.getAuthor();
			Date lendDate=book.getLendDate();
			if(lendDate!=null){
				Date returnDate=new Date(lendDate.getTime()+lendTime);
				book.setReturnDate(returnDate);
			}
			if(bookName.length()>12){
				book.setName(bookName.substring(0, 12)+" ...");
			}else{
				book.setName(bookName);
			}
			if(author.length()>12){
				book.setAuthor(author.substring(0, 12)+" ...");
			}else{
				book.setAuthor(author);
			}
			Integer previousCount=reservationService.getPreviousCount(reservation.getBook().getId(), reservation.getReservation_order());
			map.put("book", book);
			map.put("previousCount", previousCount);
			mapList.add(map);
		}
		StringBuffer param=new StringBuffer();
		if(StringUtil.isNotEmpty(userName)){
			param.append("userName="+userName);
		}
		mav.addObject("mapList", mapList);
		mav.addObject("pageCode", PageUtil.genPagination("/user/reservation", total, page, pageSize, param.toString()));
		mav.addObject("title", "我的预约");
		mav.addObject("menuTitle", "我的预约");
		mav.addObject("mainPage", "/user/book/reservation");
		mav.addObject("mainPageKey", "#r");
		mav.setViewName("/user/index");
		return mav;
	}
	
	/**
	 * 用户取消预约的图书
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping("/reservationCancel")
	@ResponseBody
	public Map<String,Object> reservationCancel(@RequestParam("id")Integer id,@RequestParam("userName")String userName){
		Map<String,Object> map=new HashMap<String,Object>();
		User user=userService.getByName(userName);
		Book book=bookService.getById(id);
		book.setReservation_count(book.getReservation_count()-1);
		bookService.save(book);
		Reservation reservation=reservationService.getByBookIdAndUserId(id, user.getId());
		reservationService.delete(reservation.getId());
		map.put("success", "预约取消成功！");
		return map;
	}
	
	/**
	 * 根据Id获取图书信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/book/{id}")
	public ModelAndView view(@PathVariable("id")Integer id,@RequestParam(value="userName",required=false)String userName){
		ModelAndView mav=new ModelAndView();
		Book book=bookService.getById(id);
		mav.addObject("book", book);
		mav.addObject("title", "《"+book.getName()+"》");
		if(StringUtil.isNotEmpty(userName)){
			Reservation reservation=reservationService.getByBookIdAndUserId(id, userService.getByName(userName).getId());
			Integer previousCount=reservationService.getPreviousCount(id, reservation.getReservation_order());
			mav.addObject("previousCount", previousCount);
			mav.addObject("menuTitle", "我的预约→详细信息");
		}else{
			mav.addObject("menuTitle", "我的图书→详细信息");
		}
		mav.addObject("mainPage", "/user/book/view");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/user/index");
		return mav;
	}
	
	/**
	 * 跳转到用户修改密码页面
	 * @param notice
	 * @return
	 */
	@RequestMapping("/preModifyPwd")
	public ModelAndView preModifyPwd(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "密码修改");
		mav.addObject("menuTitle", "密码修改");
		mav.addObject("mainPage", "/user/personal/modifyPwd");
		mav.addObject("mainPageKey", "#p");
		mav.setViewName("/user/index");
		return mav;
	}
	
	/**
	 * 用户修改密码
	 * @param id
	 * @return
	 */
	@RequestMapping("/modifyPwd")
	@ResponseBody
	public Map<String,Object> modifyPwd(@RequestParam("userName")String userName,@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword){
		Map<String,Object> map=new HashMap<String,Object>();
		User user=userService.getByName(userName);
		if(!user.getPassword().equals(oldPassword)){
			map.put("error", "原密码错误！");
		}else{
			user.setPassword(newPassword);
			userService.save(user);
			map.put("success", "密码修改成功！");
		}
		return map;
	}
	
	/**
	 * 跳转到用户注册页面
	 * @return
	 */
	@RequestMapping("/preRegister")
	public String preRegister(){
		return "/register";
	}
	
	/**
	 * 用户名唯一性验证
	 * @param userName
	 * @return
	 */
	@RequestMapping("/userNameValidation")
	@ResponseBody
	public Map<String,Object> userNameValidation(@RequestParam("userName")String userName){
		Map<String,Object> map=new HashMap<String,Object>();
		if(userService.getByName(userName)==null){
			map.put("valid", true);
		}else{
			map.put("valid", false);
		}
		return map;
	}
	
	/**
	 * 用户注册
	 * @return
	 */
	@RequestMapping("/register")
	@ResponseBody
	public Map<String,Object> register(User user){
		Map<String,Object> map=new HashMap<String,Object>();
		if(user==null||StringUtil.isEmpty(user.getUserName())||StringUtil.isEmpty(user.getTrueName())
				||StringUtil.isEmpty(user.getPassword())||StringUtil.isEmpty(user.getEmail())){
			map.put("error", "请输入注册相关信息！");
		}else{
			user.setRole(1);
			user.setState(0);
			user.setCode(CryptographyUtil.md5(user.getUserName(), user.getTrueName()));
			userService.save(user);
			map.put("success", "注册成功，等待管理员审核！");
		}
		return map;
	}
	
}
