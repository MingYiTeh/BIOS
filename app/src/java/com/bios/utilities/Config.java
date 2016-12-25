/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CompletedCourse;
import com.bios.model.CompletedCourseDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.MinBidDAO;
import com.bios.model.Prerequisite;
import com.bios.model.PrerequisiteDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.google.gson.JsonObject;
import is203.JWTUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Represents a Config object
 * @author Teh Ming Yi
 */
public class Config implements ServletContextListener {

    /**
    * Context Initialized the application start up for BidDAO, SectionDAO,CompletedCourseDAO,CourseDAO,PrerequisiteDAO,StudentDAO and SectionStudentDAO
    *
    * @param event the event from which the servlet context was initialized
    */
    public void contextInitialized(ServletContextEvent event) {
        // Do stuff during webapp's startup.
        // for BidDAO
        //System.out.println("HEYYY");
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.bid";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Bid> bidList = new ArrayList<>();

            while (rs.next()) {
                String userID = rs.getString("UserID");
                double amount = rs.getDouble("Amount");
                String courseID = rs.getString("CourseID");
                String sectionID = rs.getString("SectionID");
                String status = rs.getString("Status");

                Bid b = new Bid(userID, amount, courseID, sectionID, status);
                bidList.add(b);
            }
            BidDAO.getInstance().setBidList(bidList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        // for SectionDAO
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.section";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Section> sectionList = new ArrayList<>();

            while (rs.next()) {
                String courseID = rs.getString("CourseID");
                String sectionID = rs.getString("SectionID");
                int dayOfWeek = rs.getInt("DayOfWeek");
                String startTime = rs.getString("StartTime");
                String endTime = rs.getString("EndTime");
                String instructor = rs.getString("Instructor");
                String venue = rs.getString("Venue");
                int size = rs.getInt("Size");

                Section s = new Section(courseID, sectionID, dayOfWeek, startTime, endTime, instructor, venue, size);
                sectionList.add(s);
            }
            SectionDAO.getInstance().setSectionList(sectionList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        // for CompletedCourseDAO
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.coursecompleted";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<CompletedCourse> completedCourseList = new ArrayList<>();

            while (rs.next()) {
                String userID = rs.getString("UserID");
                String courseID = rs.getString("CourseID");

                CompletedCourse c = new CompletedCourse(userID, courseID);
                completedCourseList.add(c);
            }
            CompletedCourseDAO.getInstance().setCompletedCourseList(completedCourseList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        // for CourseDAO
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.course";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();

            while (rs.next()) {
                String courseID = rs.getString("CourseID");
                String school = rs.getString("School");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String examDate = rs.getString("examDate");
                String examStart = rs.getString("examStart");
                String examEnd = rs.getString("examEnd");

                Course c = new Course(courseID, school, title, description, examDate, examStart, examEnd);
                courseList.add(c);
            }
            CourseDAO.getInstance().setCourseList(courseList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        // for PrerequisiteDAO
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.prerequisite";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Prerequisite> prerequisiteList = new ArrayList<>();

            while (rs.next()) {
                String courseID = rs.getString("CourseID");
                String prerequisiteID = rs.getString("PrerequisiteID");

                Prerequisite p = new Prerequisite(courseID, prerequisiteID);
                prerequisiteList.add(p);
            }
            PrerequisiteDAO.getInstance().setPrerequisiteList(prerequisiteList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        // for StudentDAO
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.student";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();

            while (rs.next()) {
                String userID = rs.getString("UserID");
                String password = rs.getString("UserPassword");
                String name = rs.getString("NameOfUser");
                String school = rs.getString("School");
                double eDollar = rs.getDouble("EDollar");

                Student s = new Student(userID, password, name, school, eDollar);
                studentList.add(s);
            }
            StudentDAO.getInstance().setStudentList(studentList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        //SectionStudentDAO
        try {
            ConnectionManager manager = new ConnectionManager();
            Connection connection = manager.getConnection();
            String sql = "select * from bios.sectionstudent";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<SectionStudent> sectionStudentList = new ArrayList<>();

            while (rs.next()) {
                String courseID = rs.getString("CourseID");
                String sectionID = rs.getString("SectionID");
                String userID = rs.getString("UserID");
                double amount = rs.getDouble("Amount");

                SectionStudent s = new SectionStudent(courseID, userID, sectionID, amount);
                sectionStudentList.add(s);
            }
            SectionStudentDAO.getInstance().setSectionStudentList(sectionStudentList);
        } catch (SQLException e) {
            e.printStackTrace();
            // This means that there was a connection error to database
        }

        MinBidDAO.getInstance().refresh();

        event.getServletContext().setAttribute("config", this);
    }

    /**
   * Context destroyed during web application shutdown
   *
   * @param event the event to context destroy from
   */
    public void contextDestroyed(ServletContextEvent event) {
        // Do stuff during webapp's shutdown.
    }

}
