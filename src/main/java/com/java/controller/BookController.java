package com.java.controller;

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
import com.java.lucene.BookIndex;
import com.java.mail.Mail;
import com.java.mail.MailInform;
import com.java.runner.StartupRunner;
import com.java.service.BookService;
import com.java.service.ReservationService;
import com.java.service.UserService;
import com.java.util.PageUtil;

/**
 * 图书Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/book")
public class BookController {
	
	@Resource
	private BookService bookService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ReservationService reservationService;
	
	@Resource
	private StartupRunner startupRunner;
	
	@Value("${lendTime}")
	private Long lendTime;
	
	@Value("${lagTime}")
	private Long lagTime;
	
	@Resource
	private BookIndex bookIndex;
	
	/**
	 * 根据Id获取图书信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/{id}")
	public ModelAndView view(@PathVariable("id")Integer id){
		ModelAndView mav=new ModelAndView();
		Book book=bookService.getById(id);
		List<Book> randomBookList=bookService.randomList(book.getBookType().getId(), book.getId(), 3);
		for(int i=0;i<randomBookList.size();i++){
			String bookName=randomBookList.get(i).getName();
			if(bookName.length()>10){
				randomBookList.get(i).setName(bookName.substring(0, 10)+" ...");
			}else{
				randomBookList.get(i).setName(bookName);
			}
		}
		mav.addObject("book", book);
		mav.addObject("randomBookList", randomBookList);
		mav.addObject("title", "《"+book.getName()+"》");
		mav.addObject("mainPage", "/book/view");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/index");
		return mav;
	}
	
	/**
	 * 借阅图书
	 * @return
	 */
	@RequestMapping("/lend")
	@ResponseBody
	public Map<String,Object> lend(@RequestParam("id")Integer id,@RequestParam("userName")String userName){
		Map<String,Object> map=new HashMap<String,Object>();
		Book book=bookService.getById(id);
		User user=userService.getByName(userName);
		if(book.getLendDate()==null){
			synchronized (this) {
				book.setUser(user);
				Date nowDate=new Date(new Date().getTime()+lagTime);
				book.setLendDate(nowDate);
				bookService.save(book);
				map.put("id", id);
				map.put("success", "借阅成功！");
				Thread bookLendThread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						startupRunner.loadBookTimerState(book.getName(), 0);
						Integer flag=bookService.lendTimer(id);
						if(flag==0){
							startupRunner.loadLendMessage(book.getUser().getUserName(), "您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！");
							Mail.send(new MailInform(book.getUser().getEmail(), "尊敬的Topgun平台用户，"+book.getUser().getTrueName()+"您好：您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！"));
						}
						else{
							startupRunner.removeBookTimerState(book.getName());
						}
					}
				});
				bookLendThread.start();
			}
		}else{
			map.put("error", "此书已被他人借阅，借阅失败！");
		}
		return map;
	}
	
	/**
	 * 图书借阅计时
	 * @return
	 */
	@RequestMapping("/lendTimer/{id}")
	public String lendTimer(@PathVariable("id")Integer id){
		Book book=bookService.getById(id);
		startupRunner.loadBookTimerState(book.getName(), 0);
		Integer flag=bookService.lendTimer(id);
		if(flag==0){
			startupRunner.loadLendMessage(book.getUser().getUserName(), "您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！");
			Mail.send(new MailInform(book.getUser().getEmail(), "尊敬的Topgun平台用户，"+book.getUser().getTrueName()+"您好：您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！"));
		}
		else{
			startupRunner.removeBookTimerState(book.getName());
		}
		return "redirect:/1";
	}
	
	
	/**
	 * 预约图书
	 * @return
	 */
	@RequestMapping("/reserve")
	@ResponseBody
	public Map<String,Object> reserve(@RequestParam("id")Integer id,@RequestParam("userName")String userName){
		Map<String,Object> map=new HashMap<String,Object>();
		Book book=bookService.getById(id);
		User user=userService.getByName(userName);
		if(book.getLendDate()!=null){
			if(book.getUser().getId()!=user.getId()){
				if(reservationService.getByBookIdAndUserId(book.getId(), user.getId())==null){
					synchronized(this){
						Integer reservation_order=reservationService.getLastReservationOrder(book.getId());
						Reservation reservation=new Reservation();
						reservation.setBook(book);
						reservation.setUser(user);
						if(reservation_order==null){
							reservation.setReservation_order(1);
						}else{
							reservation.setReservation_order(reservation_order+1);
						}
						book.setReservation_count(book.getReservation_count()+1);
						bookService.save(book);
						reservationService.save(reservation);
						map.put("success", "预约成功！");
					}
				}else{
					map.put("error", "您已预约过此书，请勿重复预约！");
				}
			}else{
				map.put("error", "您正在借阅此书，无需预约！");
			}
		}else{
			map.put("lendAble", "此书暂无人借阅，您无需预约！");
		}
		return map;
	}
	
	/**
	 * 查询图书信息
	 * @return
	 */
	@RequestMapping("/q/{page}")
	public ModelAndView search(@RequestParam("q")String q,@PathVariable("page")Integer page)throws Exception{
		ModelAndView mav=new ModelAndView();
		List<Book> bookList=bookIndex.searchBook(q);
		int pageSize=8;
		int toIndex=(bookList.size()>=page*pageSize)?(page*pageSize):(bookList.size());
		mav.addObject("bookList", bookList.subList((page-1)*pageSize, toIndex));
		mav.addObject("pageCode", PageUtil.genPagination("/book/q", bookList.size(), page, pageSize, "q="+q));
		mav.addObject("q", q);
		mav.addObject("resultTotal", bookList.size());
		mav.addObject("title", "搜索关键字'"+q+"'结果");
		mav.addObject("mainPage", "/book/result");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/index");
		return mav;
	}
	
	/**
	 * 删除借阅保留信息
	 * @param userName
	 * @param index
	 */
	@RequestMapping("/removeLendMessage")
	public String removeLendMessage(@RequestParam("userName")String userName,@RequestParam("index")Integer index){
		startupRunner.removeLendMessage(userName, index);
		return "redirect:/1";
	}
	

}
