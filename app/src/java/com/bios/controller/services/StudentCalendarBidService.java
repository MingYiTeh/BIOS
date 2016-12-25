/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Represents a JSON Web Service for Displaying Student Calender
 * @author kongyujian
 */
public class StudentCalendarBidService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method uses the userID of a student to
     * generates a json object with a timetable of that particular student that
     * consists of both the student's exams and classes.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userID = request.getParameter("userID");
        System.out.println(userID);
        ArrayList<Bid> allBids = BidDAO.getInstance().getStudentBids(userID);

        JsonArray arr = new JsonArray();
        System.out.println(allBids.size());

        for (Bid bid : allBids) {
            if (bid.getStatus().equals("success") || bid.getStatus().equals("pending")) {
                JsonObject obj = new JsonObject();
                Course course = CourseDAO.getInstance().findCourse(bid.getCourseID());
                Section section = SectionDAO.getInstance().findSection(bid.getCourseID(), bid.getSectionID());

                DateFormat sourceFormat = new SimpleDateFormat("yyyyMMdd");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                // Adding a Course exam object
                obj.add("title", new JsonPrimitive(course.getTitle()));
                try {
                    Date source = sourceFormat.parse(course.getExamDate());
                    String target = df.format(source);
                    System.out.println(target);
                    System.out.println(sourceFormat.parse(course.getExamDate()).toString());
                    obj.add("start", new JsonPrimitive(target));
                    obj.add("end", new JsonPrimitive(target));
                    obj.add("className", new JsonPrimitive("bg-blue"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                obj.add("description", new JsonPrimitive(course.getDescription()));
                arr.add(obj);

                // Adding a Section class timetable object
                obj = new JsonObject();

                obj.add("title", new JsonPrimitive(course.getTitle()));
                obj.add("start", new JsonPrimitive(section.getStart()));
                System.out.println(section.getStart());
                obj.add("end", new JsonPrimitive(section.getEnd()));
                obj.add("className", new JsonPrimitive("bg-green"));
                obj.add("description", new JsonPrimitive(section.getCourseID()));
                JsonArray dow = new JsonArray();
                dow.add(new JsonPrimitive(section.getDay()));
                obj.add("dow", dow);

                arr.add(obj);
            }
        }

        response.setContentType("application/json");

        response.getWriter().write(arr.toString());
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
