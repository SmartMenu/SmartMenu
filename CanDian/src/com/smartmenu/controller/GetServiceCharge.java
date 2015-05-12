package com.smartmenu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.smartmenu.model.TableHandler;

/**
 * Servlet implementation class GetServiceCharge
 */
@WebServlet("/GetServiceCharge")
public class GetServiceCharge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetServiceCharge() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableId = request.getParameter("tableId");
		String shopId = request.getParameter("shopId");
		TableHandler handler = new TableHandler();
		JSONObject json = handler.dealServiceChargeReq(tableId, shopId);
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
