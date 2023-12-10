package com.placement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class ScheduleInterviewServlet
 */
public class ScheduleInterviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleInterviewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentUsername = request.getParameter("student_username");
	    int profileId = Integer.parseInt(request.getParameter("profile_id"));
	    String interviewDate = request.getParameter("interview_date");
	    String interviewTime = request.getParameter("interview_time");
	    if (!interviewTime.contains(":")) {
	        interviewTime += ":00";
	    } else if (interviewTime.split(":").length == 2) {
	        interviewTime += ":00";
	    }
	    try {
//	    	
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

	        String query = "INSERT INTO interview_schedules (student_username, profile_id, interview_date, interview_time) VALUES (?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, studentUsername);
	        ps.setInt(2, profileId);
	        ps.setDate(3, java.sql.Date.valueOf(interviewDate));
	        ps.setTime(4, java.sql.Time.valueOf(interviewTime));

	        int i = ps.executeUpdate();
	        if(i > 0) {
	            response.sendRedirect("coordinator_success.jsp");
	        } else {
	            throw new Exception("No rows affected.");
	        }
	    } catch(Exception e) {
//	        StringWriter sw = new StringWriter();
//	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace();
//	        String stackTrace = sw.toString();
	        String errorMessage = "An error occurred while scheduling the interview: " + e.getMessage();
	        request.setAttribute("error", errorMessage);
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/coordinator_error.jsp");
	        dispatcher.forward(request, response);
	    }	}

}
