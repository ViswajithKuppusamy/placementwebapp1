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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class ViewCompanyInterviewScheduleServlet
 */
public class ViewCompanyInterviewScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewCompanyInterviewScheduleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("comp_username") == null) {
            response.sendRedirect("comp_representative_login.jsp");
            return;
        }

        String representativeUsername = (String) session.getAttribute("comp_username");
        List<InterviewSchedule> interviewSchedules = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            String query = "SELECT i.*, j.profile_name FROM interview_schedules i " +
                    "JOIN job_profiles j ON i.profile_id = j.id " +
                    "JOIN company_representative c ON j.representative_username = c.comp_username " +
                    "WHERE c.comp_username = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, representativeUsername);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InterviewSchedule schedule = new InterviewSchedule(
                    rs.getInt("id"),
                    rs.getString("student_username"),
                    rs.getString("profile_name"), 
                    rs.getInt("profile_id"),      
                    rs.getDate("interview_date"),
                    rs.getTime("interview_time")
                );
                interviewSchedules.add(schedule);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }

        request.setAttribute("interviewSchedules", interviewSchedules);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view_company_interview_schedule.jsp");
        dispatcher.forward(request, response);
    }

    // Inner class to represent interview schedule
	public static class InterviewSchedule {
	    private int id;
	    private String studentUsername;
	    private String profileName;
	    private Date interviewDate;
	    private Time interviewTime;
	    private int profileId;

	    public InterviewSchedule(int id, String studentUsername, String profileName, int profileId, Date interviewDate, Time interviewTime) {
	        this.id = id;
	        this.studentUsername = studentUsername;
	        this.profileName = profileName;
	        this.profileId = profileId;
	        this.interviewDate = interviewDate;
	        this.interviewTime = interviewTime;
	    }

	    // Getters
	    public int getId() { return id; }
	    public String getStudentUsername() { return studentUsername; }
	    public String getProfileName() { return profileName; }
	    public int getProfileId() { return profileId; }
	    public Date getInterviewDate() { return interviewDate; }
	    public Time getInterviewTime() { return interviewTime; }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
