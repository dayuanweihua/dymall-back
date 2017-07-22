package com.dayuan.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayuan.bean.UserInfo;
import com.dayuan.constant.ConstantCode;
import com.dayuan.service.UserInfoService;
import com.dayuan.utils.PatternUtils;
import com.dayuan.vo.ResultVo;

/**
 * 客户注册事务的控制器类，其中有方法：
 * 1、register(session,userInfo,code,passwordRepeat),客户用手机号码注册账户方法。
 * 2、sendCode(session,phone),给用户输入的手机号发送验证码方法。
 * 3、登录方法在LoginController，login(session,loginName,password,passwordRepeat),客户登录方法。
 * 4、queryUserInfo(phone,session)，通过手机号查询客户信息方法。
 * 
 * @author 24725
 *
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Resource
	private UserInfoService userInfoService;

	/**
	 * 前端通过register(type)函数的ajax的url参数定位到服务端的此方法。
	 * 1、客户使用手机号注册的方法，此方法和前端的function函数名register(type)相同
	 * 2、当用户点击注册按钮后（其onclick="register(1)"）前端会调用register(type)函数。
	 * 3、前端的register(type)函数发送给服务端客户输入的所有数据，和是否完成某些事件。
	 * 4、前端的register(type)函数要求服务端返回json数据类型，用来获取注册事务的状态信息。
	 * 
	 * @param session
	 * @param userInfo
	 * @param code
	 * @param passwordRepeat
	 * @return 返回给前端状态集resultVo
	 */
	@RequestMapping(value = "/registerByPhone.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo registerByPhone(HttpSession session, UserInfo userInfo, Integer code) {
		// resultVo对象是存储服务端处理前端发送过来的数据后设置的状态信息，是返回给前端页面的dataType数据。
		// 前端以json格式接收，前端以data调用发送来的json数据成员信息，例如：data.code,data.msg
		ResultVo resultVo = null;
		try {		
			
			if(!userInfo.getPhone().equals(String.valueOf(session.getAttribute("phone")))){
				resultVo = new ResultVo(ConstantCode.PARAM_ERROR.getCode(),"两次手机号不一致！");
				return resultVo;
			}
			Object obj = session.getAttribute("code");
			// 这里使用三元运算符来返回会话里存储的code。
			Integer codeFromSession = obj == null ? -1 : (Integer) obj;
			if (codeFromSession.intValue() != code.intValue()) {// 这里要转换为基本类型，才能用==比较值，对象直接用==是比较地址值。
				resultVo = new ResultVo(ConstantCode.CODE_ERROR.getCode(),"验证码不正确！");
				session.removeAttribute("code");// 每次比较code之后都要清除session里的code
				return resultVo;
			}

			if (!PatternUtils.isMobile(userInfo.getPhone())) {
				resultVo = new ResultVo(ConstantCode.PHONE_NUMBER_ERROR.getCode(),"电话号码不符合规则，请重新输入！");
				return resultVo;
			}
			// 过滤了所有条件后，就可以往数据库里插入数据了。
			userInfoService.insertUserInfo(userInfo);
			resultVo = new ResultVo("后台：注册成功！",userInfo);
			// 注册成功后，code不能再使用了，所以这里也需要清除code
			session.removeAttribute("code");
			return resultVo;
		} catch (Exception e) {
			if (resultVo != null) {
				resultVo = new ResultVo(ConstantCode.FAIL.getCode(),"网络不稳定，请稍后再试");				
			}
			logger.error("用户注册失败：" + e.getMessage());
			return resultVo;
		}
	}

	/**
	 * 1、$('#sendMobileCode').on('click',sendCode);前端通过类选择器，定位到这个元素后，当这个元素触发了click事件，就会调用sendCode函数
	 * 2、前端函数sendCode，通过ajax的url参数定位到服务端的此方法。
	 * 3、前端函数sendCode，通过ajax的data参数发送给服务端客户输入的手机号码信息。
	 * 4、前端函数sendCode，要求客户端返回json对象，用于判断客户是否获取到手机验证码
	 * 
	 * @param session
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/sendCode.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo sendCode(HttpSession session,String phone) {
		ResultVo resultVo = null;
		try {
			if (!PatternUtils.isMobile(phone)) {
				resultVo = new ResultVo(ConstantCode.PHONE_NUMBER_ERROR.getCode(),"电话不符合规则，请重新输入！");
				return resultVo;
			}
			if(userInfoService.selectUserInfoByPhone(phone)!=null){
				resultVo = new ResultVo(ConstantCode.PHONE_HAS_USED.getCode(),"手机号已被使用！");
				return resultVo;
			};
			// 随机生成6位数的手机验证码，并存储在session中,
			//并且要存储获取验证码的手机号，便于注册时判断是否是同一个手机号。
			int code = (int) ((Math.random() * 9 + 1) * 100000);
			session.setAttribute("code", code);
			session.setAttribute("phone",phone);
			System.out.println("验证码发送成功，code:" + code);
			resultVo = new ResultVo("短信发送成功",code);
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			if (resultVo != null) {
				resultVo = new ResultVo(ConstantCode.FAIL.getCode(),"您的网络不稳定，请稍后再试.");				
			}
			logger.error("用户注册失败：" + e.getMessage());
			return resultVo;
		}
	}

	

	@RequestMapping(value = "/registerByEmail.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo registerByEmail(UserInfo userInfo) {
		ResultVo resultVo = null;
		try {
			if (!PatternUtils.isEmail(userInfo.getEmail())) {
				// 邮箱不规则
			}
			resultVo = new ResultVo();
			userInfoService.insertUserInfo(userInfo);
			resultVo.setCode(ConstantCode.SUCCESS.getCode());
			resultVo.setMsg("注册成功");
			return resultVo;
		} catch (Exception e) {
			if (resultVo != null) {
				resultVo.setCode(ConstantCode.FAIL.getCode());
				resultVo.setMsg("网络不稳定，请稍后再试");
			}
			logger.error("用户注册失败：" + e.getMessage());
			return resultVo;
		}
	}
	
	/**
	 * 用于在主页检查用户是否登录
	 * @param session
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/queryUserInfo.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo queryUserInfo(HttpSession session) {
		System.out.println("用户进入主页，queryUserInfo");
		ResultVo resultVo = null;
		try {
			UserInfo userInfo = session.getAttribute("userInfo") == null ? null
					: (UserInfo) session.getAttribute("userInfo");
			// 这里用户就算未登陆也不能让用户强制登陆。我们只需要做判断用户。
			resultVo = new ResultVo();
			resultVo.setCode(ConstantCode.SUCCESS.getCode());
			resultVo.setData(userInfo);
			return resultVo;
		} catch (Exception e) {
			if (resultVo != null) {
				resultVo.setCode(ConstantCode.SUCCESS.getCode());
				resultVo.setMsg("网络不稳定，请刷新页面");
				logger.error("客户进入主页出现异常：" + e.getMessage());
			}
			e.printStackTrace();
			return resultVo;
		}
	}
}
