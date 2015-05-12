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
 * Servlet implementation class MakeNewOrder
 */
@WebServlet("/MakeNewOrder")
public class MakeNewOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeNewOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data=request.getParameter("data");
		JSONObject json = JSONObject.fromObject(data);
		System.out.println("/makeNewOrder: call order handler deal the request");
		OrderHandler handler = new OrderHandler();
		JSONObject jRet=handler.makeNewOrder(json);
		System.out.println("/makeNewOrder: handle finished");
		PrintWriter out = response.getWriter();
		String callbak=request.getParameter("callback");
		out.write(callbak+"("+jRet.toString()+")");
	}

}
