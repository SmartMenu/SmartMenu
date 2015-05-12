package com.smartmenu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.smartmenu.model.MenuHandler;

/**
 * Servlet implementation class GetItemSoldoutInfo
 */
@WebServlet("/GetItemSoldoutInfo")
public class GetItemSoldoutInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetItemSoldoutInfo() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String shopId = request.getParameter("shopId");
		MenuHandler handler = new MenuHandler();
		JSONObject json = handler.getItemSoldoutInfo(shopId);
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
