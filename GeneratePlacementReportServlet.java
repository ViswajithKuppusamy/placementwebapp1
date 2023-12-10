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
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class GeneratePlacementReportServlet
 */
public class GeneratePlacementReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneratePlacementReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("coordinator_username") == null) {
            response.sendRedirect("coordinator_login.jsp");
            return;
        }

        Map<String, Integer> reportData = new HashMap<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

           
            String queryInterviews = "SELECT COUNT(*) AS interview_count FROM interview_schedules";
            PreparedStatement psInterviews = con.prepareStatement(queryInterviews);
            ResultSet rsInterviews = psInterviews.executeQuery();
            if (rsInterviews.next()) {
                reportData.put("interviewCount", rsInterviews.getInt("interview_count"));
            }

            String queryStudentRegistrations = "SELECT COUNT(DISTINCT student_username) AS student_count FROM student_drive_registrations";
            PreparedStatement psStudentRegistrations = con.prepareStatement(queryStudentRegistrations);
            ResultSet rsStudentRegistrations = psStudentRegistrations.executeQuery();
            if (rsStudentRegistrations.next()) {
                reportData.put("studentCount", rsStudentRegistrations.getInt("student_count"));
            }

            String queryCompaniesParticipating = "SELECT COUNT(DISTINCT profile_name) AS company_count FROM job_profiles";
            PreparedStatement psCompaniesParticipating = con.prepareStatement(queryCompaniesParticipating);
            ResultSet rsCompaniesParticipating = psCompaniesParticipating.executeQuery();
            if (rsCompaniesParticipating.next()) {
                reportData.put("companyCount", rsCompaniesParticipating.getInt("company_count"));
            }

            rsInterviews.close();
            psInterviews.close();
            rsStudentRegistrations.close();
            psStudentRegistrations.close();
            rsCompaniesParticipating.close();
            psCompaniesParticipating.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("reportData", reportData);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/placement_report.jsp");
        dispatcher.forward(request, response);
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
