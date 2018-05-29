package com.java.controller.admin;

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

import com.java.entity.Notice;
import com.java.entity.User;
import com.java.mail.Mail;
import com.java.mail.MailInform;
import com.java.service.NoticeService;
import com.java.service.UserService;
import com.java.util.PageUtil;

/**
 * 通知管理员Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/admin/notice")
public class NoticeAdminController {
	
	@Resource
	private NoticeService noticeService;
	
	@Resource
	private UserService userService;
	
	@Value("${lagTime}")
	private Long lagTime;
	
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
		List<Notice> noticeList=noticeService.list(page, 8);
		Long total=noticeService.getCount();
		mav.addObject("noticeList", noticeList);
		mav.addObject("pageCode", PageUtil.genPagination("/admin/notice/list", total, page, 8, ""));
		mav.addObject("title", "通知列表");
		mav.addObject("menuTitle", "通知管理→通知列表");
		mav.addObject("mainPage", "/admin/notice/list");
		mav.addObject("mainPageKey", "#n");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 向所有用户邮箱发布通知邮件
	 * @param id
	 * @return
	 */
	@RequestMapping("/publish")
	@ResponseBody
	public Map<String,Object> publish(@RequestParam("id")Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		Notice notice=noticeService.getById(id);
		List<User> userList=userService.findAll();
		for(User user:userList){
			if(user.getState()!=1){
				continue;
			}
			Mail.send(new MailInform(user.getEmail(), "尊敬的Topgun平台用户，"+user.getTrueName()+"您好，以下是一封平台通知：<br/>《"+notice.getTitle()+"》<br/>"+notice.getContent()));
		}
		map.put("success", "发布成功！");
		return map;
	}
	
	/**
	 * 根据Id删除通知
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(@RequestParam("id")Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		noticeService.delete(id);
		map.put("success", "删除成功！");
		return map;
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
		mav.addObject("menuTitle", "通知管理→通知列表→详细信息");
		mav.addObject("mainPage", "/admin/notice/view");
		mav.addObject("mainPageKey", "#n");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 跳转到通知添加页面
	 * @param notice
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "通知添加");
		mav.addObject("menuTitle", "通知管理→通知添加");
		mav.addObject("mainPage", "/admin/notice/add");
		mav.addObject("mainPageKey", "#n");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 添加或修改通知信息
	 * @param notice
	 * @return
	 */
	@RequestMapping("/save")
	public String save(Notice notice){
		if(notice.getId()!=null){
			notice.setReleaseDate(noticeService.getById(notice.getId()).getReleaseDate());
			noticeService.save(notice);
			return "redirect:/admin/notice/"+notice.getId();
		}else{
			Date nowDate=new Date(new Date().getTime()+lagTime);
			notice.setReleaseDate(nowDate);
			noticeService.save(notice);
			return "redirect:/admin/notice/list/1";
		}
	}

}
