package com.placement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class StudentSignupServlet
 */
public class StudentSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public StudentSignupServlet() {
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
        String studentEmail = request.getParameter("student_email");
        String studentFullname = request.getParameter("student_fullname");
        int studentAcademicYear = Integer.parseInt(request.getParameter("student_academic_year"));
        String studentMajor = request.getParameter("student_major");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO student_users (student_username, student_password, student_email, student_fullname, student_academic_year, student_major) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, studentUsername);
            ps.setString(2, studentPassword);
            ps.setString(3, studentEmail);
            ps.setString(4, studentFullname);
            ps.setInt(5, studentAcademicYear);
            ps.setString(6, studentMajor);

            int i = ps.executeUpdate();
            if(i > 0) {
                response.sendRedirect("student_signin.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("23000")) {
               
                request.setAttribute("error", "Username already exists. Please choose another one.");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/student_signup.jsp");
                dispatcher.forward(request, response);
            } else {
               
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	protected Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");
    }
}