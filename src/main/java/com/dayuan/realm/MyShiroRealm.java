package com.dayuan.realm;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.dayuan.bean.UserInfo;
import com.dayuan.service.AdminService;
import com.dayuan.service.UserInfoService;
import com.dayuan.utils.PatternUtils;

/****
 * 自定义Realm
 * 
 * @author Swinglife
 * 没有注解
 */
public class MyShiroRealm extends AuthorizingRealm {
	
	/**
	 * 用于返回realm name的字符串
	 */
	private static final String REALM_NAME = "my_realm";
	
	@Resource
	private UserInfoService userInfoService;
	
//	@Resource
//	private AdminService adminService;

	/***
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		System.out.println("do Get Authorization Info---获取授权信息");
		// 根据自己系统规则的需要编写获取授权信息，这里为了快速入门只获取了用户对应角色的资源url信息
		String username = (String) pc.fromRealm(getName()).iterator().next();
		
		// if (username != null) {
		// List<String> pers =
		// accountService.getPermissionsByUserName(username);
		// if (pers != null && !pers.isEmpty()) {
		// SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// for (String each : pers) {
		// //将权限资源添加到用户信息中
		// info.addStringPermission(each);
		// }
		// return info;
		// }
		// }
		Set<String> roles = new HashSet<String>();
		System.out.println("admin come");
		roles.add("admin");
		
		return new SimpleAuthorizationInfo(roles);
	}

	/***
	 * 获取认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
		System.out.println("do Get Authentication Info---获取身份验证信息");
		UsernamePasswordToken token = (UsernamePasswordToken) at;
		// 通过表单接收的用户名
		// String username = token.getUsername();

		UserInfo userInfo = new UserInfo();
		if (PatternUtils.isMobile(token.getUsername())) {
			// 根据手机号查询用户信息
			userInfo.setPhone(token.getUsername());
		} else if (PatternUtils.isEmail(token.getUsername())) {
			// 根据邮箱查询用户信息
			userInfo.setEmail(token.getUsername());
		} else {
			// 根据用户名查询用户信息
			userInfo.setUsername(token.getUsername());
		}

		userInfo = userInfoService.selectUserInfoByParam(userInfo);
		
		if (!String.valueOf(token.getPassword()).equals(userInfo.getPassword())) {
			throw new AuthenticationException("用户名或密码不正确！");
		}
		
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(token.getUsername(), String.valueOf(token.getPassword()), getName());
		
		
		
		return  simpleAuthenticationInfo;
	}
	
	@Override
	public String getName() {
		return REALM_NAME;
	}

}