package com.placement;

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
import java.sql.Timestamp;

/**
 * Servlet implementation class CompanyRepresentativeFeedback
 */
public class CompanyRepresentativeFeedback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyRepresentativeFeedback() {
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
		String representativeUsername = request.getParameter("representative_username");
        String feedbackText = request.getParameter("feedback_text");

        if (representativeUsername == null || representativeUsername.isEmpty() || feedbackText == null || feedbackText.isEmpty()) {
           
            response.sendRedirect("error.jsp");
            return;
        }

        String dbUrl = "jdbc:mysql://localhost:3306/placement_portal";
        String dbUser = "root";
        String dbPassword = "vamsi1998"; 
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String insertQuery = "INSERT INTO feedback (username, feedback_text, submission_date) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            
            preparedStatement.setString(1, representativeUsername);
            preparedStatement.setString(2, feedbackText);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            int rowsInserted = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            if (rowsInserted > 0) {
                response.sendRedirect("representative_dashboard.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
        } catch (ClassNotFoundException | SQLException e) {
        	response.sendRedirect("error.jsp");
            return; 
        
        }

	}
	protected Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");
    }
}

