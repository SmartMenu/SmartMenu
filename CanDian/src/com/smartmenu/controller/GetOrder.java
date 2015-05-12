package com.smartmenu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.smartmenu.model.OrderHandler;

/**
 * Servlet implementation class GetOrder
 */
@WebServlet("/GetOrder")
public class GetOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String shopId=request.getParameter("shopId");
		String posId=request.getParameter("posId");
		String tableId=request.getParameter("tableId");
		System.out.println("/getOrder: call order handler");
		OrderHandler handler = new OrderHandler();
		JSONObject json = handler.getOldOrder(shopId, posId, tableId);
		System.out.println("/getMenu: handler finished");
		PrintWriter out = response.getWriter();
		String callbak=request.getParameter("callback");
		out.write(callbak+"("+json.toString()+")");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
