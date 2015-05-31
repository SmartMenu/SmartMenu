package com.smartmenu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.smartmenu.model.MenuHandler2;

/**
 * Servlet implementation class GetMenu
 */
@WebServlet("/GetMenu")
public class GetMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMenu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String shopId = request.getParameter("shop_id");
		String callbak=request.getParameter("callback");
		String posId=request.getParameter("pos_id");
		String deviceId=request.getParameter("device_id");
		if(posId==null||!posId.equals("100"))
			posId="100";
		if(deviceId==null||!deviceId.equals("AA:EE:34:30"))
			deviceId="AA:EE:34:30";
		System.out.println("/getMenu: call menu handler deal the request");
		MenuHandler2 handler = new MenuHandler2();
		JSONObject json = handler.getMenu(shopId, posId, deviceId);
		System.out.println("/getMenu: deal with request completed");
		//response.setContentType("text/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(callbak+"("+json.toString()+")");
//		out.print(json.toString());
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
