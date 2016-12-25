/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.model.BidDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.bios.utilities.ConnectionManager;
import com.bios.utilities.ErrorMsgComparator;
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
import javax.servlet.http.HttpSession;

/**
 *Represents a JSON Web Service for Drop Section
 * @author Has Nilofar /** This web service allows the administrator to drop the
 * section of a user.
 * @author Has Nilofar
 */
public class DropSectionService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method by allowing the administrator to drop a user's
     * enrollment in a section. This web service requires a valid token userid, course and section,
     * and a bid of the student, that was successful. The bid can only be dropped if the round 2 is active.
     * If succesful, a json object with the status "success" will be created. Otherwise, this method creates a
     * json object with the status "error" and with a json array of the errors that caused the unsuccessful
     * dropping of the section.
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
        String requestObject = request.getParameter("r");
        String token = request.getParameter("token");
        ArrayList<String> errorList = new ArrayList<String>();

        JsonObject responseObject = new JsonObject();
        JsonArray allErrors = new JsonArray();

        if (requestObject == null) {
            errorList.add("missing r");
        } else if (requestObject.length() == 0) {
            errorList.add("blank r");
        }

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

        JsonElement jelementRequest = new JsonParser().parse(requestObject);
        JsonObject json = jelementRequest.getAsJsonObject();

        JsonElement crse = json.get("course");
        JsonElement sec = json.get("section");
        JsonElement user = json.get("userid");

        if (crse == null) {
            errorList.add("missing course");
        } else if (crse.getAsString().length() == 0) {
            errorList.add("blank course");
        }

        if (sec == null) {
            errorList.add("missing section");
        } else if (sec.getAsString().length() == 0) {
            errorList.add("blank section");
        }

        if (user == null) {
            errorList.add("missing userid");
        } else if (user.getAsString().length() == 0) {
            errorList.add("blank userid");
        }

        if (errorList.size() > 0) {
            errorList.sort(new ErrorMsgComparator());
            for (String s : errorList) {
                allErrors.add(new JsonPrimitive(s));
            }
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        }

        Student stud = StudentDAO.getInstance().retrieve(user.getAsString());
        Course c = CourseDAO.getInstance().findCourse(crse.getAsString());
        SectionStudent toBeDropped = SectionStudentDAO.getInstance().findSectionStudent(crse.getAsString(), sec.getAsString(), user.getAsString());
        Section s = SectionDAO.getInstance().findSection(crse.getAsString(),sec.getAsString());

        if (c == null) {
            allErrors.add(new JsonPrimitive("invalid course"));
        } else if (s == null) {
            allErrors.add(new JsonPrimitive("invalid section"));
        }
        if (stud == null) {
            allErrors.add(new JsonPrimitive("invalid userid"));
        }

        if (manager.getBiddingRound() == 2 && manager.getBiddingRoundStatus().equals("started")) {
            if (user != null) {
                if (toBeDropped != null) {
                    double currAmount = stud.geteDollar();
                    double amountFromDropping = toBeDropped.getAmount();
                    double finalAmount = currAmount + amountFromDropping;

                    manager.setEDollar(user.getAsString(), finalAmount);

                    String userid = user.getAsString();
                    String courseID = crse.getAsString();
                    String sectionID = sec.getAsString();
                    SectionStudentDAO.getInstance().deleteStudentSection(userid, amountFromDropping, courseID, sectionID);
                    SectionStudentDAO.getInstance().deleteStudentSectionFromDB(userid, amountFromDropping, courseID, sectionID);
                    BidDAO.getInstance().deleteBid(userid, courseID, sectionID);
                    BidDAO.getInstance().deleteBidFromDB(userid, courseID, sectionID);

                    responseObject.addProperty("status", "success");
                } else {
                    JsonPrimitive value = new JsonPrimitive("no such enrollment record");
                    allErrors.add(value);
                    responseObject.addProperty("status", "error");
                    responseObject.add("message", allErrors);
                }
            }

        } else {
            //round not active
            JsonPrimitive value = new JsonPrimitive("round not active");
            allErrors.add(value);
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrors);
        }
        response.setContentType("application/json");
        response.getWriter().write(responseObject.toString());
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
