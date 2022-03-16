package com.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entity.Parks;
import com.service.ParksService;
import com.util.PageHelper;
import com.util.VeDate;

//定义为控制器
@Controller
// 设置路径
@RequestMapping(value = "/parks", produces = "text/plain;charset=utf-8")
public class ParksController extends BaseController {
	// @Autowired的作用是自动注入依赖的ServiceBean
	@Autowired
	private ParksService parksService;

	// 准备添加数据
	@RequestMapping("createParks.action")
	public String createParks() {
		this.getRequest().setAttribute("pno", "P" + VeDate.getStringDatex());
		return "admin/addparks";
	}

	// 添加数据
	@RequestMapping("addParks.action")
	public String addParks(Parks parks) {
		parks.setStatus("空闲");
		parks.setAddtime(VeDate.getStringDateShort());
		this.parksService.insertParks(parks);
		return "redirect:/parks/createParks.action";
	}

	// 通过主键删除数据
	@RequestMapping("deleteParks.action")
	public String deleteParks(String id) {
		this.parksService.deleteParks(id);
		return "redirect:/parks/getAllParks.action";
	}

	// 批量删除数据
	@RequestMapping("deleteParksByIds.action")
	public String deleteParksByIds() {
		String[] ids = this.getRequest().getParameterValues("parksid");
		if (ids != null) {
			for (String parksid : ids) {
				this.parksService.deleteParks(parksid);
			}
		}
		return "redirect:/parks/getAllParks.action";
	}

	// 更新数据
	@RequestMapping("updateParks.action")
	public String updateParks(Parks parks) {
		this.parksService.updateParks(parks);
		return "redirect:/parks/getAllParks.action";
	}

	// 更新状态
	@RequestMapping("status.action")
	public String status(String id) {
		String status = "";
		Parks parks = this.parksService.getParksById(id);
		if (status.equals(parks.getStatus())) {
			status = "";
		}
		parks.setStatus(status);
		this.parksService.updateParks(parks);
		return "redirect:/parks/getAllParks.action";
	}

	// 显示全部数据
	@RequestMapping("getAllParks.action")
	public String getAllParks(String number) {
		List<Parks> parksList = this.parksService.getAllParks();
		PageHelper.getUserPage(parksList, "parks", "getAllParks", 10, number, this.getRequest());
		return "admin/listparks";
	}

	// 按条件查询数据 (模糊查询)
	@RequestMapping("queryParksByCond.action")
	public String queryParksByCond(String cond, String name, String number) {
		Parks parks = new Parks();
		if (cond != null) {
			if ("pno".equals(cond)) {
				parks.setPno(name);
			}
			if ("areax".equals(cond)) {
				parks.setAreax(name);
			}
			if ("lengthx".equals(cond)) {
				parks.setLengthx(name);
			}
			if ("weightx".equals(cond)) {
				parks.setWeightx(name);
			}
			if ("status".equals(cond)) {
				parks.setStatus(name);
			}
			if ("addtime".equals(cond)) {
				parks.setAddtime(name);
			}
			if ("memo".equals(cond)) {
				parks.setMemo(name);
			}
		}

		List<String> nameList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		nameList.add(cond);
		valueList.add(name);
		PageHelper.getPage(this.parksService.getParksByLike(parks), "parks", nameList, valueList, 10, number,
				this.getRequest(), "query");
		name = null;
		cond = null;
		return "admin/queryparks";
	}

	// 按主键查询数据
	@RequestMapping("getParksById.action")
	public String getParksById(String id) {
		Parks parks = this.parksService.getParksById(id);
		this.getRequest().setAttribute("parks", parks);
		return "admin/editparks";
	}

}
