package com.smartmenu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.smartmenu.model.LoginHandler;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Login() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId=request.getParameter("username");
		String password=request.getParameter("password");
		String shopId=request.getParameter("shopid");
		String tableId=request.getParameter("tableid");
		System.out.println("/login: call login handler");
		LoginHandler login = new LoginHandler();
		JSONObject json = login.dealLogin(shopId, userId, password, tableId);
		System.out.println("/login: finished hanlding");
		PrintWriter out=response.getWriter();
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
