package com.learning.web.jbdc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
			
			myConn=datasource.getConnection();
			
			String sql="select * from student";
			
			myStmt=myConn.createStatement();
			
			myRs=myStmt.executeQuery(sql);
			
			while(myRs.next()) {
				String email=myRs.getString("email");
				out.println(email);
				out.println("<br/>");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	
	}

}
