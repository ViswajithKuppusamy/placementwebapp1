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

/**
 * Servlet implementation class UpdateDriveServlet
 */
public class UpdateDriveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateDriveServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int driveId = Integer.parseInt(request.getParameter("id"));
        String driveName = request.getParameter("drive_name");
        String driveDetails = request.getParameter("drive_details");
        String driveDate = request.getParameter("drive_date");
        String registrationDeadline = request.getParameter("registration_deadline");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            String query = "UPDATE placement_drives SET drive_name = ?, drive_details = ?, drive_date = ?, registration_deadline = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, driveName);
            ps.setString(2, driveDetails);
            ps.setDate(3, java.sql.Date.valueOf(driveDate));
            ps.setDate(4, java.sql.Date.valueOf(registrationDeadline));
            ps.setInt(5, driveId);

            int i = ps.executeUpdate();
            if(i > 0) {
                response.sendRedirect("coordinator_success.jsp");
            } else {
                response.sendRedirect("coordinator_error.jsp");
            }
        } catch(Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while updating the drive.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/coordinator_error.jsp");
            dispatcher.forward(request, response);
        }
	}

}
