/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.controller.BiddingRoundServlet;
import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CourseDAO;
import com.bios.model.MinBidDAO;
import com.bios.model.SectionDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.bios.utilities.BidComparator;
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
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a JSON Web Service for Delete Bid
 * @author Marc
 */
public class DeleteBidService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method and deletes the bid. It retrieves the userID, courseID, section ID, and amount bidded to verify the bid.
     * Upon verification, the bid will be deleted from bidDAO and database. Upon successful deletion, a json object is created with the
     * status "success". If the bid deletion is unsuccessful, a json object with the status "error" is created together with a
     * json array of the errors that caused the unsuccessful deletion.
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

        String courseID = crse.getAsString();
        String sectionID = sec.getAsString();
        String userID = user.getAsString();

        BidDAO bidDAO = BidDAO.getInstance();
        CourseDAO courseDAO = CourseDAO.getInstance();
        SectionDAO sectionDAO = SectionDAO.getInstance();
        StudentDAO studentDAO = StudentDAO.getInstance();

        String roundStatus = manager.getBiddingRoundStatus();
        Bid bid = bidDAO.getBid(userID, courseID, sectionID);

        if (roundStatus.equals("stopped") || !bidDAO.deleteBid(userID, courseID, sectionID)) {
            responseObject.addProperty("status", "error");
            if (courseDAO.findCourse(courseID) == null) {
                allErrors.add(new JsonPrimitive("invalid course"));
            }
            if (sectionDAO.findSection(courseID, sectionID) == null) {
                allErrors.add(new JsonPrimitive("invalid section"));
            }
            if (studentDAO.findStudent(userID) == null) {
                allErrors.add(new JsonPrimitive("invalid userid"));
            }
            if (roundStatus.equals("stopped")) {
                allErrors.add(new JsonPrimitive("round ended"));
            }

            if (roundStatus.equals("started") && allErrors.size() == 0) {
                allErrors.add(new JsonPrimitive("no such bid"));
            }
            responseObject.add("message", allErrors);

            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        } else {
            // no errors detected in the deletion of bids
            MinBidDAO.getInstance().getMinBidList().remove(courseID+"-"+sectionID);
            MinBidDAO.getInstance().refresh();
            responseObject.addProperty("status", "success");
            Student stud = studentDAO.retrieve(userID);
            stud.seteDollar(stud.geteDollar()+bid.getAmount());
            manager.refundEDollar(userID, bid.getAmount());
            bidDAO.deleteBidFromDB(userID, courseID, sectionID);
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
