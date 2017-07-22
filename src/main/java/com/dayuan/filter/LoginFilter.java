package com.dayuan.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dayuan.constant.ResourcePerm;

@WebFilter("/*")
public class LoginFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		// 获得用户请求的URI
		String path = req.getRequestURI();

		System.out.println(path);

		for (ResourcePerm resourcePerm : ResourcePerm.values()) {
			if (path.indexOf("/" + resourcePerm) != -1) {// 注意：登录页面千万不能过滤
				// 不然过滤器就。。。。。自行调试不要偷懒！这样记忆深刻
				if (session.getAttribute("userInfo") == null) {
					System.out.println("用户未登录");
					resp.setHeader("loginStatus", "0");
					return;
				}
			}
		}
		filterChain.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}