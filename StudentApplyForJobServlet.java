package com.placement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class StudentApplyForJobServlet
 */
public class StudentApplyForJobServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentApplyForJobServlet() {
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
		HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("student_username") == null) {
            response.sendRedirect("student_signin.jsp");
            return;
        }

        String studentUsername = (String) session.getAttribute("student_username");
        int profileId = Integer.parseInt(request.getParameter("profileId"));
        int driveId = Integer.parseInt(request.getParameter("driveId"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            String query = "INSERT INTO student_job_applications (student_username, profile_id, drive_id) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, studentUsername);
            ps.setInt(2, profileId);
            ps.setInt(3, driveId);

            int i = ps.executeUpdate();
            if(i > 0) {
                response.sendRedirect("success.jsp"); 
            } else {
                response.sendRedirect("error.jsp"); 
            }
        } catch(Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during job application.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
	}

}
