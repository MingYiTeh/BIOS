/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CompletedCourse;
import com.bios.model.CompletedCourseDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.Prerequisite;
import com.bios.model.PrerequisiteDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.bios.utilities.BidJSONComparator;
import com.bios.utilities.ConnectionManager;
import com.bios.utilities.CourseComparator;
import com.bios.utilities.CourseCompletedComparator;
import com.bios.utilities.PrerequisiteComparator;
import com.bios.utilities.SectionComparator;
import com.bios.utilities.SectionStudentComparator;
import com.bios.utilities.StudentComparator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a JSON Web Service for Dump Table details
 * @author Marc
 */
public class DumpTableService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method that makes use of a token. If a token is valid and present in the session object, the method goes
     * on to create a json object. In the json object, the respective course, section, student, prerequisite, completed-course, bid and section-student
     * objects will be placed in the tables. If any errors occur, such as an invalid or blank token, a json object with the status "error" will be created a
     * with any error messages within a json array in the json object.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConnectionManager manager = new ConnectionManager();
        String token = request.getParameter("token");
        ArrayList<String> errorList = new ArrayList<String>();

        JsonObject responseObject = new JsonObject();
        JsonArray allErrors = new JsonArray();

        if (token == null) {
            errorList.add("missing token");
        } else if (token.length() == 0) {
            errorList.add("blank token");
        } else {
            try {
                String result = JWTUtility.verify(token, "abcdefgh12345678");
                if (!result.equals("admin")) {
                    errorList.add("invalid token");
                }
            } catch (JWTException e) { // do we need this?
                // This means not an admin, or token expired
                errorList.add("invalid username/token");
            }
        }

        JsonObject object = new JsonObject();
        object.add("status", new JsonPrimitive("success"));

        ArrayList<Course> courseList = CourseDAO.getInstance().getCourseList();
        ArrayList<Section> sectionList = SectionDAO.getInstance().getSectionList();
        ArrayList<Student> studentList = StudentDAO.getInstance().getStudentList();
        ArrayList<Prerequisite> prerequisiteList = PrerequisiteDAO.getInstance().getPrerequisiteList();
        ArrayList<Bid> bidList = BidDAO.getInstance().getBidList();
        ArrayList<CompletedCourse> completeList = CompletedCourseDAO.getInstance().getCompletedCourseList();
        ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentList();


        courseList.sort(new CourseComparator());
        sectionList.sort(new SectionComparator());
        studentList.sort(new StudentComparator());
        prerequisiteList.sort(new PrerequisiteComparator());
        bidList.sort(new BidJSONComparator());
        completeList.sort(new CourseCompletedComparator());
        sectionStudentList.sort(new SectionStudentComparator());

        JsonArray courseArray = new JsonArray();

        for(Course course : courseList){
            JsonObject courseObj = new JsonObject();
            courseObj.add("course", new JsonPrimitive(course.getCourseID()));
            courseObj.add("school", new JsonPrimitive(course.getSchool()));
            courseObj.add("title", new JsonPrimitive(course.getTitle()));
            courseObj.add("description", new JsonPrimitive(course.getDescription()));
            courseObj.add("exam date", new JsonPrimitive(course.getExamDate()));
            courseObj.add("exam start", new JsonPrimitive(course.getExamStart().replace(":","")));
            courseObj.add("exam end", new JsonPrimitive(course.getExamEnd().replace(":","")));
            courseArray.add(courseObj);
        }
        object.add("course", courseArray);


        JsonArray sectionArray = new JsonArray();

        for(Section section : sectionList){
            JsonObject obj = new JsonObject();
            obj.add("course", new JsonPrimitive(section.getCourseID()));
            obj.add("section", new JsonPrimitive(section.getSectionID()));
            obj.add("day", new JsonPrimitive(getDay(section.getDay())));
            obj.add("start", new JsonPrimitive(section.getStart().replace(":","")));
            obj.add("end", new JsonPrimitive(section.getEnd().replace(":","")));
            obj.add("instructor", new JsonPrimitive(section.getInstructor()));
            obj.add("venue", new JsonPrimitive(section.getVenue()));
            obj.add("size", new JsonPrimitive(section.getSize()));
            sectionArray.add(obj);
        }
        object.add("section", sectionArray);


        JsonArray studentArray = new JsonArray();

        for(Student student : studentList){
            Student stud = StudentDAO.getInstance().retrieve(student.getId());
            JsonObject obj = new JsonObject();
            obj.add("userid", new JsonPrimitive(student.getId()));
            obj.add("password", new JsonPrimitive(student.getPassword()));
            obj.add("name", new JsonPrimitive(student.getNameOfUser()));
            obj.add("school", new JsonPrimitive(student.getSchool()));
            obj.add("edollar", new JsonPrimitive(stud.geteDollar()));
            studentArray.add(obj);
        }
        object.add("student", studentArray);


        JsonArray prerequisiteArray = new JsonArray();

        for(Prerequisite pre : prerequisiteList){
            JsonObject obj = new JsonObject();
            obj.add("course", new JsonPrimitive(pre.getCourseID()));
            obj.add("prerequisite", new JsonPrimitive(pre.getPrerequisiteID()));
            prerequisiteArray.add(obj);
        }
        object.add("prerequisite", prerequisiteArray);


        JsonArray bidArray = new JsonArray();

        for(Bid bid : bidList){
            JsonObject obj = new JsonObject();
            obj.add("userid", new JsonPrimitive(bid.getUserID()));
            obj.add("amount", new JsonPrimitive(bid.getAmount()));
            obj.add("course", new JsonPrimitive(bid.getCourseID()));
            obj.add("section", new JsonPrimitive(bid.getSectionID()));
            bidArray.add(obj);
        }
        object.add("bid", bidArray);


        JsonArray completedArray = new JsonArray();

        for(CompletedCourse cc : completeList){
            JsonObject obj = new JsonObject();
            obj.add("userid", new JsonPrimitive(cc.getUserID()));
            obj.add("course", new JsonPrimitive(cc.getCourseID()));
            completedArray.add(obj);
        }
        object.add("completed-course", completedArray);


        JsonArray sectionStudentArray = new JsonArray();

        for(SectionStudent stud : sectionStudentList){
            JsonObject obj = new JsonObject();
            obj.add("userid", new JsonPrimitive(stud.getUserID()));
            obj.add("course", new JsonPrimitive(stud.getCourseID()));
            obj.add("section", new JsonPrimitive(stud.getSectionID()));
            obj.add("amount", new JsonPrimitive(stud.getAmount()));
            sectionStudentArray.add(obj);
        }
        object.add("section-student", sectionStudentArray);


        System.out.println(object);
        response.setContentType("application/json");
        response.getWriter().write(object.toString());
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


    public String getDay(int num){
        switch (num){
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
            default:
                return null;
        }
    }

}
