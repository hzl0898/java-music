package com.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entity.Money;
import com.service.MoneyService;
import com.entity.Users;
import com.entity.Cate;
import com.service.UsersService;
import com.service.CateService;
import com.util.PageHelper;
import com.util.VeDate;

//定义为控制器
@Controller
// 设置路径
@RequestMapping(value = "/money", produces = "text/plain;charset=utf-8")
public class MoneyController extends BaseController {
	// @Autowired的作用是自动注入依赖的ServiceBean
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private CateService cateService;

	// 准备添加数据
	@RequestMapping("createMoney.action")
	public String createMoney() {
		List<Users> usersList = this.usersService.getAllUsers();
		this.getRequest().setAttribute("usersList", usersList);
		List<Cate> cateList = this.cateService.getAllCate();
		this.getRequest().setAttribute("cateList", cateList);
		this.getRequest().setAttribute("mno", "M" + VeDate.getStringDatex());
		return "admin/addmoney";
	}

	// 添加数据
	@RequestMapping("addMoney.action")
	public String addMoney(Money money) {
		money.setAddtime(VeDate.getStringDateShort());
		money.setStatus("待缴费");
		this.moneyService.insertMoney(money);
		return "redirect:/money/createMoney.action";
	}

	// 通过主键删除数据
	@RequestMapping("deleteMoney.action")
	public String deleteMoney(String id) {
		this.moneyService.deleteMoney(id);
		return "redirect:/money/getAllMoney.action";
	}

	// 批量删除数据
	@RequestMapping("deleteMoneyByIds.action")
	public String deleteMoneyByIds() {
		String[] ids = this.getRequest().getParameterValues("moneyid");
		if (ids != null) {
			for (String moneyid : ids) {
				this.moneyService.deleteMoney(moneyid);
			}
		}
		return "redirect:/money/getAllMoney.action";
	}

	// 更新数据
	@RequestMapping("updateMoney.action")
	public String updateMoney(Money money) {
		this.moneyService.updateMoney(money);
		return "redirect:/money/getAllMoney.action";
	}

	// 更新状态
	@RequestMapping("status.action")
	public String status(String id) {
		String status = "";
		Money money = this.moneyService.getMoneyById(id);
		if (status.equals(money.getStatus())) {
			status = "";
		}
		money.setStatus(status);
		this.moneyService.updateMoney(money);
		return "redirect:/money/getAllMoney.action";
	}

	// 显示全部数据
	@RequestMapping("getAllMoney.action")
	public String getAllMoney(String number) {
		List<Money> moneyList = this.moneyService.getAllMoney();
		PageHelper.getUserPage(moneyList, "money", "getAllMoney", 10, number, this.getRequest());
		return "admin/listmoney";
	}

	// 按条件查询数据 (模糊查询)
	@RequestMapping("queryMoneyByCond.action")
	public String queryMoneyByCond(String cond, String name, String number) {
		Money money = new Money();
		if (cond != null) {
			if ("mno".equals(cond)) {
				money.setMno(name);
			}
			if ("usersid".equals(cond)) {
				money.setUsersid(name);
			}
			if ("cateid".equals(cond)) {
				money.setCateid(name);
			}
			if ("total".equals(cond)) {
				money.setTotal(name);
			}
			if ("addtime".equals(cond)) {
				money.setAddtime(name);
			}
			if ("status".equals(cond)) {
				money.setStatus(name);
			}
			if ("memo".equals(cond)) {
				money.setMemo(name);
			}
		}

		List<String> nameList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		nameList.add(cond);
		valueList.add(name);
		PageHelper.getPage(this.moneyService.getMoneyByLike(money), "money", nameList, valueList, 10, number,
				this.getRequest(), "query");
		name = null;
		cond = null;
		return "admin/querymoney";
	}

	// 按主键查询数据
	@RequestMapping("getMoneyById.action")
	public String getMoneyById(String id) {
		Money money = this.moneyService.getMoneyById(id);
		this.getRequest().setAttribute("money", money);
		List<Users> usersList = this.usersService.getAllUsers();
		this.getRequest().setAttribute("usersList", usersList);
		List<Cate> cateList = this.cateService.getAllCate();
		this.getRequest().setAttribute("cateList", cateList);
		return "admin/editmoney";
	}

}
