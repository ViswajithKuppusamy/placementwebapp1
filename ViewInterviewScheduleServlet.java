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
 * Servlet implementation class ViewInterviewScheduleServlet
 */
public class ViewInterviewScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewInterviewScheduleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("student_username") == null) {
            
            response.sendRedirect("student_login.jsp");
            return;
        }

        String studentUsername = (String) session.getAttribute("student_username");
        List<InterviewSchedule> interviewSchedules = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/placement_portal", "root", "vamsi1998");

            String query = "SELECT * FROM interview_schedules WHERE student_username = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, studentUsername);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InterviewSchedule schedule = new InterviewSchedule(
                    rs.getInt("id"),
                    rs.getString("student_username"),
                    rs.getInt("profile_id"),
                    rs.getDate("interview_date"),
                    rs.getTime("interview_time")
                );
                interviewSchedules.add(schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }

        request.setAttribute("interviewSchedules", interviewSchedules);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view_interview_schedule.jsp");
        dispatcher.forward(request, response);
    }

    public static class InterviewSchedule {
        private int id;
        private String studentUsername;
        private int profileId;
        private Date interviewDate;
        private Time interviewTime;

        public InterviewSchedule(int id, String studentUsername, int profileId, Date interviewDate, Time interviewTime) {
            this.id = id;
            this.studentUsername = studentUsername;
            this.profileId = profileId;
            this.interviewDate = interviewDate;
            this.interviewTime = interviewTime;
        }

        // Getters
        public int getId() { return id; }
        public String getStudentUsername() { return studentUsername; }
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
