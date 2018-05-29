package com.java.runner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;

import com.java.entity.Book;
import com.java.entity.BookTimerState;
import com.java.entity.LendMessage;
import com.java.mail.Mail;
import com.java.mail.MailInform;
import com.java.service.BookService;
import com.java.service.BookTypeService;
import com.java.service.NoticeService;

/**
 * 服务器启动加载
 * @author zxy
 *
 */
@Component
public class StartupRunner implements ServletContextListener{
	
	private static ServletContext application;
	
	@Resource
	private NoticeService noticeService;
	
	@Resource
	private BookTypeService bookTypeService;
	
	@Resource
	private BookService bookService;
	
	@Override
	@SuppressWarnings("static-access")
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		this.application=sce.getServletContext();
		List<Book> bookList=bookService.findAllLendBook();
		System.out.println("已借阅数量："+bookList.size());
		for(Book book:bookList){
			Thread bookLendThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					System.out.println("图书名："+book.getName());
					loadBookTimerState(book.getName(), 0);
					Integer flag=bookService.lendTimer(book.getId());
					if(flag==0){
						loadLendMessage(book.getUser().getUserName(), "您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！");
						Mail.send(new MailInform(book.getUser().getEmail(), "尊敬的Topgun平台用户，"+book.getUser().getTrueName()+"您好：您借阅的《"+book.getName()+"》已到期，请尽快归还给管理员！"));
					}
					else{
						removeBookTimerState(book.getName());
					}
				}
			});
			bookLendThread.start();
		}
		loadData();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 更新application中缓存
	 */
	public void loadData(){
		application.setAttribute("newestNoticeList", noticeService.list(1,5));
		application.setAttribute("bookTypeList", bookTypeService.list());
	}
	
	/**
	 * 为指定用户添加相关借阅信息
	 * @param userName
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	public void loadLendMessage(String userName,String message){
		List<LendMessage> lendMessageList=null;
		boolean flag=false;
		if(application.getAttribute("lendMessageList")!=null){
			lendMessageList= (ArrayList<LendMessage>) application.getAttribute("lendMessageList");
		}else{
			lendMessageList=new ArrayList<LendMessage>();
		}
		for(int i=0;i<lendMessageList.size();i++){
			if(lendMessageList.get(i).getUserName().equals(userName)){
				lendMessageList.get(i).getMessageList().add(message);
				flag=true;
				break;
			}
		}
		if(flag==false){
			LendMessage lendMessage=new LendMessage();
			lendMessage.setUserName(userName);
			List<String> messageList=new ArrayList<String>();
			messageList.add(message);
			lendMessage.setMessageList(messageList);
			lendMessageList.add(lendMessage);
		}
		application.setAttribute("lendMessageList", lendMessageList);
	}
	
	/**
	 * 删除指定用户相关借阅信息
	 * @param userName
	 * @param index
	 */
	@SuppressWarnings("unchecked")
	public void removeLendMessage(String userName,Integer index){
		List<LendMessage> lendMessageList= (ArrayList<LendMessage>) application.getAttribute("lendMessageList");
		for(int i=0;i<lendMessageList.size();i++){
			if(lendMessageList.get(i).getUserName().equals(userName)){
				lendMessageList.get(i).getMessageList().remove((int)index);
				break;
			}
		}
		application.setAttribute("lendMessageList", lendMessageList);
	}
	
	/**
	 * 为指定图书添加计时状态信息
	 * @param bookName
	 * @param state
	 */
	@SuppressWarnings("unchecked")
	public void loadBookTimerState(String bookName,Integer state){
		List<BookTimerState> bookTimerStateList=null;
		boolean flag=false;
		if(application.getAttribute("bookTimerStateList")!=null){
			bookTimerStateList=(ArrayList<BookTimerState>)application.getAttribute("bookTimerStateList");
		}else{
			bookTimerStateList=new ArrayList<BookTimerState>();
		}
		for(int i=0;i<bookTimerStateList.size();i++){
			if(bookTimerStateList.get(i).getName().equals(bookName)){
				bookTimerStateList.get(i).setState(state);
				flag=true;
				break;
			}
		}
		if(flag==false){
			BookTimerState bookTimerState=new BookTimerState();
			bookTimerState.setName(bookName);
			bookTimerState.setState(state);
			bookTimerStateList.add(bookTimerState);
		}
		application.setAttribute("bookTimerStateList", bookTimerStateList);
	}
	
	/**
	 * 获取指定图书的计时状态
	 * @param bookName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer getBookTimerState(String bookName){
		List<BookTimerState> bookTimerStateList=(ArrayList<BookTimerState>)application.getAttribute("bookTimerStateList");
		Integer state=null;
		for(int i=0;i<bookTimerStateList.size();i++){
			if(bookTimerStateList.get(i).getName().equals(bookName)){
				state=bookTimerStateList.get(i).getState();
				break;
			}
		}
		return state;
	}
	
	/**
	 * 删除指定图书计时状态信息
	 * @param bookName
	 */
	@SuppressWarnings("unchecked")
	public void removeBookTimerState(String bookName){
		List<BookTimerState> bookTimerStateList=(ArrayList<BookTimerState>)application.getAttribute("bookTimerStateList");
		for(int i=0;i<bookTimerStateList.size();i++){
			if(bookTimerStateList.get(i).getName().equals(bookName)){
				bookTimerStateList.remove(i);
				break;
			}
		}
		application.setAttribute("bookTimerStateList", bookTimerStateList);
	}

}
