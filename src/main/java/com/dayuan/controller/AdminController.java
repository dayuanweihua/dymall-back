package com.dayuan.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dayuan.bean.Admin;
import com.dayuan.bean.AdminAndRole;
import com.dayuan.bean.Menu;
import com.dayuan.bean.RoleAndMenu;
import com.dayuan.bean.UserInfo;
import com.dayuan.constant.ConstantCode;
import com.dayuan.service.AdminService;
import com.dayuan.utils.PatternUtils;
import com.dayuan.vo.ResultVo;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Resource
	private AdminService adminService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@RequestMapping(value = "/toStart.shtml", method = RequestMethod.GET)
	public String printHello(ModelMap model) {
		model.addAttribute("message", "欢迎，请登录!");
		return "login";
	}

	@RequestMapping(value = "/adminRegister.shtml", method = RequestMethod.GET)
	public String adminRegister(ModelMap model) {
		model.addAttribute("message", "欢迎，请注册!");
		return "sign-up";
	}

	@RequestMapping(value = "/register.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ModelAndView register(Admin admin, HttpSession session) {
		if (admin == null) {
			return new ModelAndView("login", "statu", "请输入账号和密码登录！");
		}
		int code;
		try {
			code = adminService.insertAdmin(admin);
			if (code != 1) {
				return new ModelAndView("login", "statu", "注册失败！");
			}
			session.setAttribute("admin", admin);
			return new ModelAndView("login", "statu", "注册成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/login.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ModelAndView loginCheck(Admin admin, HttpSession session) {
		if (admin == null) {
			System.out.println("请输入账号和密码登录！！");
			return new ModelAndView("login", "statu", "请输入账号和密码登录！");
		}
		try {
			Admin a = adminService.selectAdminByName(admin.getAdminName());

			if (a == null || !(a.getPassword().equals(admin.getPassword()))) {
				System.out.println("账户或密码错误，请重新登录！");
				return new ModelAndView("login", "statu", "账户或密码错误，请重新登录！");

			}
			session.setAttribute("admin", admin);
			return new ModelAndView("index", "statu", "登录成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="/checkPerm.shtml",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView checkPerm(Admin admin,HttpServletRequest request){
		if(admin==null){
			return new ModelAndView("login","statu","请输入账号和密码登录！");
		}
		Admin a=adminService.selectAdminByName(admin.getAdminName());		
		if(!a.getPassword().equals(admin.getPassword())){		
			return new ModelAndView("login","statu","您输入的账号或密码有误，请重新输入！");
		}	
		
		redisTemplate.opsfor
		
		AdminAndRole adminAndRole=adminAndRoleService.selectRoleIdByAdminId(a.getId());
		RoleAndMenu roleAndMenu=roleAndMenuService.selectMenuIdByRoleId(adminAndRole.getRoleId());
		List<Menu> menus=menuService.selectMenus();
		for (Menu menu : menus) {
			if(menu.getId().intValue()==roleAndMenu.getMenuId()){
				//TODO 设置用户权限
			}
		}
		
		
		HttpSession session=request.getSession();
		session.setAttribute("adminname",admin.getAdminName());
		return new ModelAndView("homepage","admin",admin);
	}

}