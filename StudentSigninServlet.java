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
import java.sql.ResultSet;

/**
 * Servlet implementation class StudentSigninServlet
 */
public class StudentSigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentSigninServlet() {
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
        String studentPassword = request.getParameter("student_password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM student_users WHERE student_username = ? AND student_password = ?");
            ps.setString(1, studentUsername);
            ps.setString(2, studentPassword);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("student_username", studentUsername);
                response.sendRedirect("student_dashboard.jsp"); 
            } else {
                
                request.setAttribute("error", "Invalid username or password.");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/student_signin.jsp");
                dispatcher.forward(request, response);
            }
        } catch(Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during sign in.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
	}

}