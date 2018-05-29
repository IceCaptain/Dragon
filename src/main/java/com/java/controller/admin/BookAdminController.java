package com.java.controller.admin;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Book;
import com.java.entity.BookType;
import com.java.entity.Reservation;
import com.java.entity.User;
import com.java.lucene.BookIndex;
import com.java.mail.Mail;
import com.java.mail.MailInform;
import com.java.runner.StartupRunner;
import com.java.service.BookService;
import com.java.service.BookTypeService;
import com.java.service.ReservationService;
import com.java.service.UserService;
import com.java.util.DateUtil;
import com.java.util.PageUtil;
import com.java.util.StringUtil;

/**
 * 图书管理员Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/admin/book")
public class BookAdminController {
	
	@Resource
	private BookService bookService;
	
	@Resource
	private BookTypeService bookTypeService;
	
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
	
	@Value("${imageFilePath}")
	private String imageFilePath;
	
	@RequestMapping("/list/{page}")
	public ModelAndView list(@PathVariable("page")Integer page,Book s_book,@RequestParam(value="flag",required=false)Integer flag){
		if(page==null){
			page=1;
		}
		ModelAndView mav=new ModelAndView();
		StringBuffer param=new StringBuffer();
		List<Integer> userIds=null;
		if(StringUtil.isNotEmpty(s_book.getName())){
			param.append("name="+s_book.getName()+"&");
		}
		if(StringUtil.isNotEmpty(s_book.getAuthor())){
			param.append("author="+s_book.getAuthor()+"&");
		}
		if(StringUtil.isNotEmpty(s_book.getPublisher())){
			param.append("publisher="+s_book.getPublisher()+"&");
		}
		
		if(s_book.getUser()!=null&&StringUtil.isNotEmpty(s_book.getUser().getUserName())){
			userIds=userService.findIdByName(s_book.getUser().getUserName());
			param.append("user.userName="+s_book.getUser().getUserName()+"&");
		}
		if(s_book.getBookType()!=null&&s_book.getBookType().getId()!=null){
			param.append("bookType.id="+s_book.getBookType().getId()+"&");
		}
		if(flag!=null){
			param.append("flag="+flag+"&");
		}
		List<Book> bookList=bookService.list(s_book, flag, userIds, page, 8);
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
		Long total=bookService.getCount(s_book,flag,userIds);
		mav.addObject("bookList", bookList);
		mav.addObject("pageCode", PageUtil.genPagination("/admin/book/list", total, page, 8,param.toString()));
		mav.addObject("s_book", s_book);
		mav.addObject("flag", flag);
		mav.addObject("title", "图书列表");
		mav.addObject("menuTitle", "图书管理→图书列表");
		mav.addObject("mainPage", "/admin/book/list");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 根据Id获取图书信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/{id}")
	public ModelAndView view(@PathVariable("id")Integer id){
		ModelAndView mav=new ModelAndView();
		Book book=bookService.getById(id);
		mav.addObject("book", book);
		mav.addObject("title", "《"+book.getName()+"》");
		mav.addObject("menuTitle", "图书管理→图书列表→详细信息");
		mav.addObject("mainPage", "/admin/book/view");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 添加或修改图书信息
	 * @param book
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(Book book,@RequestParam(value="imageFile",required=false)MultipartFile file,@RequestParam(name="bookTypeId",required=false)Integer bookTypeId)throws Exception{
		if(!file.isEmpty()){
			String fileName=file.getOriginalFilename();
			String suffixName=fileName.substring(fileName.lastIndexOf("."));
			String newFileName=DateUtil.getCurrentDateStr()+suffixName;
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imageFilePath+newFileName));
			book.setImageName(newFileName);
		}
		if(bookTypeId!=null){
			book.setBookType(bookTypeService.getById(bookTypeId));
		}
		
		if(book.getId()!=null){
			String oldImageName=bookService.getById(book.getId()).getImageName();
			String newImageName=book.getImageName();
			if(oldImageName!=null&&newImageName!=null&&oldImageName.equals(newImageName)==false){
				File oldImage=new File(imageFilePath+bookService.getById(book.getId()).getImageName());
				oldImage.delete();
			}
			if(bookService.getById(book.getId()).getLendDate()!=null){
				book.setLendDate(bookService.getById(book.getId()).getLendDate());
			}
			if(bookService.getById(book.getId()).getUser()!=null){
				book.setUser(bookService.getById(book.getId()).getUser());
			}
			if(bookService.getById(book.getId()).getReservation_count()!=null){
				book.setReservation_count(bookService.getById(book.getId()).getReservation_count());
			}
			if(bookService.getById(book.getId()).getBookType().getId()!=bookTypeId){
				BookType bookTypeMinus=bookService.getById(book.getId()).getBookType();
				BookType bookTypePlus=bookTypeService.getById(bookTypeId);
				bookTypeMinus.setBookCount(bookTypeMinus.getBookCount()-1);
				bookTypePlus.setBookCount(bookTypePlus.getBookCount()+1);
				bookTypeService.save(bookTypeMinus);
				bookTypeService.save(bookTypePlus);
			}
			bookService.save(book);
			bookIndex.updateIndex(book);
			return "redirect:/admin/book/"+book.getId();
		}else{
			BookType bookType=bookTypeService.getById(bookTypeId);
			bookType.setBookCount(bookType.getBookCount()+1);
			bookTypeService.save(bookType);
			book.setReservation_count(0);
			bookService.save(book);
			bookIndex.addIndex(book);
			return "redirect:/admin/book/list/1";
		}
	}
	
	/**
	 * ajax返回下拉框图书类别信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bookTypeList")
	public Map<String,Object> bookTypeList(@RequestParam(name="bookTypeId",required=false)Integer bookTypeId){
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("success", true);
		result.put("bookTypeId", bookTypeId);
		result.put("data", bookTypeService.list());
		return result;
	}
	
	/**
	 * 跳转到图书添加页面
	 * @param notice
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "图书添加");
		mav.addObject("menuTitle", "图书管理→图书添加");
		mav.addObject("mainPage", "/admin/book/add");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 删除图书信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(@RequestParam("id")Integer id) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		Book book=bookService.getById(id);
		if(book.getUser()==null&&(book.getReservation_count()==null||book.getReservation_count()==0)){
			if(book.getImageName()!=null){
				File oldImage=new File(imageFilePath+book.getImageName());
				oldImage.delete();
			}
			bookService.delete(id);
			BookType bookType=bookTypeService.getById(book.getBookType().getId());
			bookType.setBookCount(bookType.getBookCount()-1);
			bookTypeService.save(bookType);
			bookIndex.deleteIndex(id.toString());
			map.put("success", "删除成功！");
		}else{
			map.put("error", "此图书已被借阅或预约，删除失败！");
		}
		return map;
	}
	
	/**
	 * 归还图书
	 * @return
	 */
	@RequestMapping("/return")
	@ResponseBody
	public Map<String,Object> returned(@RequestParam("id")Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		Book book=bookService.getById(id);
		Reservation reservation=reservationService.getFirstReservation(id);
		if(reservation!=null){
			map.put("userId", reservation.getUser().getId());
		}
		if(book.getUser()!=null){
			String userName=book.getUser().getUserName();
			book.setUser(null);
			book.setLendDate(null);
			bookService.save(book);
			startupRunner.loadBookTimerState(book.getName(), 1);
			startupRunner.loadLendMessage(userName, "您借阅的《"+book.getName()+"》已归还成功！");
			map.put("success", "归还成功！");
		}else{
			map.put("error", "此书无人借阅，归还失败！");
		}
		
		
		return map;
	}
	
	/**
	 * 归还图书后为第一预约人借阅图书
	 * @return
	 */
	@RequestMapping("/lend")
	public String lend(@RequestParam("bookId")Integer bookId,@RequestParam("userId")Integer userId){
		Book book=bookService.getById(bookId);
		User user=userService.getById(userId);
		Reservation reservation=reservationService.getByBookIdAndUserId(bookId, userId);
		synchronized (this) {
			book.setUser(user);
			Date nowDate=new Date(new Date().getTime()+lagTime);
			book.setLendDate(nowDate);
			book.setReservation_count(book.getReservation_count()-1);
			bookService.save(book);
		}
		//通知第一预约人 已为其借阅相关图书
		reservationService.delete(reservation.getId());
		startupRunner.loadLendMessage(user.getUserName(), "您预约的《"+book.getName()+"》已帮您借阅，请找管理员取书！");
		Mail.send(new MailInform(user.getEmail(), "尊敬的Topgun平台用户，"+user.getTrueName()+"您好：您预约的《"+book.getName()+"》已帮您借阅，请找管理员取书！"));
		Thread bookLendThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startupRunner.loadBookTimerState(book.getName(), 0);
				Integer flag=bookService.lendTimer(bookId);
				//图书到期 通知相关用户
				if(flag==0){
					startupRunner.loadLendMessage(book.getUser().getUserName(), "您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！");
					Mail.send(new MailInform(book.getUser().getEmail(), "尊敬的Topgun平台用户，"+book.getUser().getTrueName()+"您好：您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！"));
				}else{
					startupRunner.removeBookTimerState(book.getName());
				}
			}
		});
		bookLendThread.start();
		
		return "redirect:/admin/book/list/1";
	}
	
}
