/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * This web service will allow an administrator to retrieve the information for a section, and it's enrolled students. During round 2,
 * this should return the enrolled students bidded successfully in round 1. After round 2 is closed, this should return the enrolled students
 * who bidded successfully in round 1 & 2.

 */
package com.bios.controller.services;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.model.StudentDAO;
import com.bios.utilities.BidUserComparator;
import com.bios.utilities.ConnectionManager;
import com.bios.utilities.ErrorMsgComparator;
import com.bios.utilities.SectionStudentComparator;
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
 * Represents a JSON Web Service for Dump Section details
 * @author Teh Ming Yi
 */
public class DumpSectionService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
       * Handles the HTTP <code>GET</code> method which uses the courseid, a sectionid and  token of an administrator. After the
       * courseid, sectionid and token of the administrator is obtained, the method first verifies that the token is valid and present in the session
       * object. Subsequently, if  the token is valid, the method checks the respective DAOs and retrieves the respective information of the bids of
       * that particular courses section and places it in a json object. If an error occurs, such as a missing input, a json object will be created
       * with the status "error" and will reflect the errors in a json array within the object.
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

        String courseID = crse.getAsString();
        String sectionID = sec.getAsString();

        int round = manager.getBiddingRound();
        String status = manager.getBiddingRoundStatus();

        // Will now check to see if there are any errors here
        Course course = CourseDAO.getInstance().findCourse(crse.getAsString());
        Section section = null;
        if (course == null) {
            allErrors.add(new JsonPrimitive("invalid course"));
        } else {
            section = SectionDAO.getInstance().findSection(course.getCourseID(), sec.getAsString());
            if (section == null) {
                allErrors.add(new JsonPrimitive("invalid section"));
            }
        }

        if (allErrors.size() != 0) {
            responseObject.add("status", new JsonPrimitive("error"));
            responseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        }

        JsonArray allResults = new JsonArray();
        ArrayList<SectionStudent> allSecStudList = SectionStudentDAO.getInstance().getSectionStudentListWithID(crse.getAsString(), sec.getAsString());
        ArrayList<SectionStudent> secStudList = new ArrayList<>();

        for (SectionStudent secStud : allSecStudList) {
            if(secStud.getCourseID().equals(courseID) && secStud.getSectionID().equals(sectionID)){
                secStudList.add(secStud);
            }
//            String cID = secStud.getCourseID();
//            String sID = secStud.getSectionID();
//
//            ArrayList<Bid> halfBids = new ArrayList<>();
//            if (round == 1 || round == 2 && status.equals("started")) {
//                halfBids = BidDAO.getInstance().getSuccessfulBidsWithID(cID, sID);
//            } else if (round == 2 && status.equals("stopped")) {
//                halfBids = BidDAO.getInstance().getBids(sID, sID);
//            }
//            allBids.addAll(halfBids);
        }

        secStudList.sort(new SectionStudentComparator());
        for(SectionStudent secStud:secStudList){
            JsonObject obj = new JsonObject();
            obj.addProperty("userid",secStud.getUserID());
            obj.addProperty("amount",secStud.getAmount());
            allResults.add(obj);
        }

//        // After we sort all the bids, then we shall add them to be responded to
//        allBids.sort(new BidUserComparator());
//        for (Bid bid : allBids) {
//            JsonObject obj = new JsonObject();
//            obj.add("userid", new JsonPrimitive(bid.getUserID()));
//            obj.add("amount", new JsonPrimitive(bid.getAmount()));
//            allResults.add(obj);
//        }

        responseObject.add("status", new JsonPrimitive("success"));
        responseObject.add("students", allResults);

        response.setContentType("application/json");
        response.getWriter().write(responseObject.toString());
    }

    /**
     * Handles the HTTP <code>GET</code> method.
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
