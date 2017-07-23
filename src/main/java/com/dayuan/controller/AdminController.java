package com.dayuan.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.dayuan.service.UserInfoService;
import com.dayuan.utils.PatternUtils;
import com.dayuan.vo.ResultVo;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Resource
	private AdminService adminService;
	

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	//用户登录入口
	@RequestMapping(value = "/toStart.shtml", method = RequestMethod.GET)
	public String printHello(ModelMap model) {
		model.addAttribute("message", "欢迎，请登录!");
		return "login";
	}
	//用户访问注册入口
	@RequestMapping(value = "/adminRegister.shtml", method = RequestMethod.GET)
	public String adminRegister(ModelMap model) {
		model.addAttribute("message", "欢迎，请注册!");
		return "sign-up";
	}

	//注册接口
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
	//登录接口
	@RequestMapping(value = "/login.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ModelAndView loginCheck(Admin admin, HttpSession session) {
		if (admin == null) {
			System.out.println("请输入账号和密码登录！！");
			return new ModelAndView("login", "statu", "请输入账号和密码登录！");
		}
		try {
			Admin a = adminService.selectAdminByName(admin.getUsername());

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

	
	/*shiro test start接口----- /admin/shiroLogin.shtml  -------*/
	 
	@RequestMapping(value = "/shiroLogin.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo login(HttpSession session, String username, String password) {
		ResultVo resultVo = null;
		System.out.println("user check");
		try {
			if (username == null || password == null) {
				resultVo = new ResultVo();
				resultVo.setCode(ConstantCode.PARAM_EMPTY.getCode());
				resultVo.setMsg("请求参数不正确");
				return resultVo;
			}

			// shiro start
			// 登录后存放进shiro token
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			// shiro end

			// 登录成功
			Admin admin=adminService.selectAdminByName(token.getUsername());
			// 登录成功，存seesion
			session.setAttribute("admin", admin);

			resultVo = new ResultVo();
			resultVo.setCode(ConstantCode.SUCCESS.getCode());
			resultVo.setData(admin);
			resultVo.setMsg("登录成功！");
			System.out.println("登录成功！");
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			resultVo = new ResultVo();
			resultVo.setCode(ConstantCode.FAIL.getCode());
			resultVo.setMsg("网络不稳定，请稍后再试");
			logger.error("登录失败：" + e.getMessage());
			return resultVo;
		}
	}
	/*shiro test end-----------------------------*/

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