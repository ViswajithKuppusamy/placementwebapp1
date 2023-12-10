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
import java.sql.SQLException;

/**
 * Servlet implementation class StudentUpdateProfileServlet
 */
public class StudentUpdateProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentUpdateProfileServlet() {
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
        String studentEmail = request.getParameter("student_email");
        String studentFullname = request.getParameter("student_fullname");
        int studentAcademicYear = Integer.parseInt(request.getParameter("student_academic_year"));
        String studentMajor = request.getParameter("student_major");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            PreparedStatement ps = con.prepareStatement(
                "UPDATE student_users SET student_email = ?, student_fullname = ?, student_academic_year = ?, student_major = ? WHERE student_username = ?");
            ps.setString(1, studentEmail);
            ps.setString(2, studentFullname);
            ps.setInt(3, studentAcademicYear);
            ps.setString(4, studentMajor);
            ps.setString(5, studentUsername);

            int i = ps.executeUpdate();
            if(i > 0) {
                response.sendRedirect("student_view_profile.jsp"); 
            } else {
                response.sendRedirect("student_update_profile.jsp");
            }
        } catch(Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during profile update.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
	}
	protected Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");
    }
}
