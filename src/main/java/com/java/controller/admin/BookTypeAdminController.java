package com.java.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.BookType;
import com.java.service.BookTypeService;
import com.java.util.PageUtil;

/**
 * 图书类别管理员Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/admin/bookType")
public class BookTypeAdminController {
	
	@Resource
	private BookTypeService bookTypeService;
	
	/**
	 * 分页查询图书类别信息
	 * @param page
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public ModelAndView list(@PathVariable("page")Integer page,BookType bookType){
		if(page==null){
			page=1;
		}
		ModelAndView mav=new ModelAndView();
		List<BookType> bookTypeList=bookTypeService.list(bookType,page, 8);
		Long total=bookTypeService.getCount();
		mav.addObject("bookTypeList", bookTypeList);
		mav.addObject("pageCode", PageUtil.genPagination("/admin/bookType/list", total, page, 8, ""));
		mav.addObject("title", "图书类别列表");
		mav.addObject("menuTitle", "图书类别管理→图书类别列表");
		mav.addObject("mainPage", "/admin/bookType/list");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 根据Id查询图书类别信息
	 * @param page
	 * @return
	 */
	@RequestMapping("/{id}")
	public ModelAndView getById(@PathVariable("id")Integer id){
		ModelAndView mav=new ModelAndView();
		BookType bookType=bookTypeService.getById(id);
		mav.addObject("bookType", bookType);
		mav.addObject("title", "详细信息");
		mav.addObject("menuTitle", "图书类别管理→图书类别列表→详细信息");
		mav.addObject("mainPage", "/admin/bookType/view");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 添加或修改图书类别信息
	 * @param bookType
	 * @return
	 */
	@RequestMapping("/save")
	public String save(BookType bookType){
		if(bookType.getId()!=null){
			bookType.setBookCount(bookTypeService.getById(bookType.getId()).getBookCount());
			bookTypeService.save(bookType);
			return "redirect:/admin/bookType/"+bookType.getId();
		}else{
			bookType.setBookCount(0);
			bookTypeService.save(bookType);
			return "redirect:/admin/bookType/list/1";
		}
	}
	
	/**
	 * 跳转到通知添加页面
	 * @param notice
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "图书类别添加");
		mav.addObject("menuTitle", "图书类别管理→图书类别添加");
		mav.addObject("mainPage", "/admin/bookType/add");
		mav.addObject("mainPageKey", "#b");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 图书类别删除
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(@RequestParam("id")Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		if(bookTypeService.getById(id).getBookCount()==0){
			bookTypeService.delete(id);
			map.put("success", "删除成功！");
		}else{
			map.put("error", "此图书类别下有图书，删除失败！");
		}
		return map;
	}

}
