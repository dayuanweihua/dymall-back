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

/**
 * 用户session校验+资源权限
 */
@WebFilter("/*")
public class SessionFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public SessionFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		for (ResourcePerm resourcePerm : ResourcePerm.values()) {
			// 判断资源是否需要过滤
			if (req.getRequestURI().indexOf("/"+resourcePerm) != -1) {
				// 判断session是否有效
				if (session.getAttribute("userInfo") == null) {
					rep.setHeader("sessionstatus", "0");
					return;
				} 
			}
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
