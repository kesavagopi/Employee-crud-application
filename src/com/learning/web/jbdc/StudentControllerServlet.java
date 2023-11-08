package com.learning.web.jbdc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;
	
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		// create our student db Util...and pass in conn pool/datasource
		try {
			studentDbUtil=new StudentDbUtil(datasource);
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	} 


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
		try {
			// read the "command" parameter
			String theCommand=request.getParameter("command");
			//if the command is missing,then default ot listing students
			if(theCommand==null) {
				theCommand="LIST";
			}
			//route to appropriate method
			switch(theCommand) {
			
			case "LIST":
				listStudents(request,response);
				break;
			case "ADD":
				addStudent(request,response);
				break;
			case "LOAD":
				System.out.println(theCommand);
				loadStudent(request,response);
				break;
			case "UPDATE":
				updateStudent(request,response);
			case "DELETE":
				deleteStudent(request,response);
			default:
					listStudents(request,response);
				
			}
	        // list the students ... the MVC fashion
		}
		catch(Exception e) {
			throw new ServletException();
		}
		
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read stduent id from form data
		String theStudentId=request.getParameter("StudentId");
		
		// delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		// send them back to "list students" page
		listStudents(request,response);
		
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		int id=Integer.parseInt(request.getParameter("StudentId"));
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		
		// create a new student object
		Student theStudent=new Student(id,firstName,lastName,email);
		
		// preform update on database
		  studentDbUtil.updateStudent(theStudent);
		
		// send them back to the "list student" page
		  listStudents(request,response);
		
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student id from form data
		String theStudentId=request.getParameter("StudentId");
		
		// get student from database (db util)
		Student theStudent=studentDbUtil.getStudent(theStudentId);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT",theStudent);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher=request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from data
		   String firstName=request.getParameter("firstName");
		   String lastName=request.getParameter("lastName");
		   String email=request.getParameter("email");
		   
		// create a new student object
		   
		   Student theStudent=new Student(firstName,lastName,email);
		
		// add the student to the database
		   studentDbUtil.addStudent(theStudent);
		
		// send back to main page (the student list)
		   listStudents(request,response);
		
	}


	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		  // get students from db util
		  List<Student> students=studentDbUtil.getStudents();
		  // add students to the request
		    request.setAttribute("STUDENT_LIST",students);
		  
		  // send to JSP page(view)
		    RequestDispatcher dispatcher=request.getRequestDispatcher("/list_students.jsp");
		    dispatcher.forward(request, response);
		
	}

}
