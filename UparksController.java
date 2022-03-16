package com.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entity.Uparks;
import com.service.UparksService;
import com.entity.Users;
import com.entity.Parks;
import com.service.UsersService;
import com.service.ParksService;
import com.util.PageHelper;
import com.util.VeDate;

//定义为控制器
@Controller
// 设置路径
@RequestMapping(value = "/uparks", produces = "text/plain;charset=utf-8")
public class UparksController extends BaseController {
	// @Autowired的作用是自动注入依赖的ServiceBean
	@Autowired
	private UparksService uparksService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private ParksService parksService;

	// 准备添加数据
	@RequestMapping("createUparks.action")
	public String createUparks() {
		List<Users> usersList = this.usersService.getAllUsers();
		this.getRequest().setAttribute("usersList", usersList);
		Parks parks = new Parks();
		parks.setStatus("空闲");
		List<Parks> parksList = this.parksService.getParksByCond(parks);
		this.getRequest().setAttribute("parksList", parksList);
		return "admin/adduparks";
	}

	// 添加数据
	@RequestMapping("addUparks.action")
	public String addUparks(Uparks uparks) {
		uparks.setAddtime(VeDate.getStringDateShort());
		this.uparksService.insertUparks(uparks);
		Parks parks = this.parksService.getParksById(uparks.getParksid());
		parks.setStatus("占用");
		this.parksService.updateParks(parks);
		return "redirect:/uparks/createUparks.action";
	}

	// 通过主键删除数据
	@RequestMapping("deleteUparks.action")
	public String deleteUparks(String id) {
		this.uparksService.deleteUparks(id);
		return "redirect:/uparks/getAllUparks.action";
	}

	// 批量删除数据
	@RequestMapping("deleteUparksByIds.action")
	public String deleteUparksByIds() {
		String[] ids = this.getRequest().getParameterValues("uparksid");
		if (ids != null) {
			for (String uparksid : ids) {
				this.uparksService.deleteUparks(uparksid);
			}
		}
		return "redirect:/uparks/getAllUparks.action";
	}

	// 更新数据
	@RequestMapping("updateUparks.action")
	public String updateUparks(Uparks uparks) {
		this.uparksService.updateUparks(uparks);
		return "redirect:/uparks/getAllUparks.action";
	}

	// 显示全部数据
	@RequestMapping("getAllUparks.action")
	public String getAllUparks(String number) {
		List<Uparks> uparksList = this.uparksService.getAllUparks();
		PageHelper.getUserPage(uparksList, "uparks", "getAllUparks", 10, number, this.getRequest());
		return "admin/listuparks";
	}

	// 按条件查询数据 (模糊查询)
	@RequestMapping("queryUparksByCond.action")
	public String queryUparksByCond(String cond, String name, String number) {
		Uparks uparks = new Uparks();
		if (cond != null) {
			if ("usersid".equals(cond)) {
				uparks.setUsersid(name);
			}
			if ("parksid".equals(cond)) {
				uparks.setParksid(name);
			}
			if ("way".equals(cond)) {
				uparks.setWay(name);
			}
			if ("addtime".equals(cond)) {
				uparks.setAddtime(name);
			}
			if ("memo".equals(cond)) {
				uparks.setMemo(name);
			}
		}

		List<String> nameList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		nameList.add(cond);
		valueList.add(name);
		PageHelper.getPage(this.uparksService.getUparksByLike(uparks), "uparks", nameList, valueList, 10, number,
				this.getRequest(), "query");
		name = null;
		cond = null;
		return "admin/queryuparks";
	}

	// 按主键查询数据
	@RequestMapping("getUparksById.action")
	public String getUparksById(String id) {
		Uparks uparks = this.uparksService.getUparksById(id);
		this.getRequest().setAttribute("uparks", uparks);
		List<Users> usersList = this.usersService.getAllUsers();
		this.getRequest().setAttribute("usersList", usersList);
		List<Parks> parksList = this.parksService.getAllParks();
		this.getRequest().setAttribute("parksList", parksList);
		return "admin/edituparks";
	}

}
