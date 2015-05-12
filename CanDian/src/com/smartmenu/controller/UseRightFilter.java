package com.smartmenu.controller;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet Filter implementation class UseRightFilter
 */
@WebFilter("/UseRightFilter")
public class UseRightFilter implements Filter {

    /**
     * Default constructor. 
     */
    public UseRightFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=UTF-8");
		long currTime=System.currentTimeMillis();
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, 2015);
		ca.set(Calendar.MONTH, 10);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 01);
		if(currTime<ca.getTimeInMillis()){
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}else{
			JSONObject json = new JSONObject();
			JSONArray jaMsg = new JSONArray();
			JSONArray jaData = new JSONArray();
			json.put("status", 1);
			jaMsg.add("SOFTWARE_EXPIRY");
			json.put("msg", jaMsg);
			json.put("data", jaData);
			String callbak=request.getParameter("callback");
			response.getWriter().write(callbak+"("+json.toString()+")");
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("init Filter");
	}

}
