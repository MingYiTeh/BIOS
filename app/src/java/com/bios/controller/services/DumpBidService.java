/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
This web service will allow an administrator to retrieve the bidding information of a specific section for the current bidding round.
If no bidding rounds are active, the information for the most recently concluded round is dumped.
 */
package com.bios.controller.services;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.RoundOneBidDAO;
import com.bios.model.RoundTwoBidDAO;
import com.bios.model.Section;
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
 * Represents a JSON Web Service for Dump Bid details
 * @author Has Nilofar
 */
public class DumpBidService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method which utilises a courseid and sectionid and a token of an administrator. After the courseid and
     * secitonid and token of the administrator is passed in, the method first verifies that the token is valid. If the token is valid, the
     * method checks the respective DAOs and retrieves the respective information of the bids placed and places it in the json object. If an
     * error occurs, such as a missing input, a json object is created with the status "error" with the error message respective to the error that
     * caused the unsuccessful dump.
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

        JsonArray allBids = new JsonArray();

        JsonElement jelementRequest = new JsonParser().parse(requestObject);
        JsonObject json = jelementRequest.getAsJsonObject();

        JsonElement course = json.get("course");
        JsonElement section = json.get("section");

        if (course == null) {
            errorList.add("missing course");
        } else if (course.getAsString().length() == 0) {
            errorList.add("blank course");
        }

        if (section == null) {
            errorList.add("missing section");
        } else if (section.getAsString().length() == 0) {
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

        ArrayList<Section> sectionList = SectionDAO.getInstance().getSectionList();
        Section s = SectionDAO.getInstance().findSection(course.getAsString(), section.getAsString());
        Course c = CourseDAO.getInstance().findCourse(course.getAsString());

        if (c == null) {
            JsonPrimitive value = new JsonPrimitive("invalid course");
            allErrors.add(value);
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrors);
        } else if (c != null && s == null) {
            JsonPrimitive value = new JsonPrimitive("invalid course");
            allErrors.add(value);
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrors);
        }

        int round = manager.getBiddingRound();
        String status = manager.getBiddingRoundStatus();
//
//        //round active: status is '-'
//        if (c != null && s != null && status.equals("started")) {
//            responseObject.addProperty("status", "success");
//            BidDAO bidDAO = BidDAO.getInstance();
//            ArrayList<Bid> successBids = bidDAO.getSuccessfulBidsWithID(c.getCourseID(), s.getSectionID());
//            Collections.sort(successBids, new BidComparator());
//            //arrange the names in ascending order - undone
//            if (successBids.size() == 0) {
//                System.out.println("PENDING:" + successBids.size());
//            }
//            for (int i = 0; i < successBids.size(); i++) {
//                Bid currBid = successBids.get(i);
//                System.out.println("Bid: " + currBid.getStatus());
//                JsonObject obj = new JsonObject();
//                obj.add("row", new JsonPrimitive((i + 1)));
//                obj.add("userid", new JsonPrimitive(currBid.getUserID()));
//                obj.add("amount", new JsonPrimitive(currBid.getAmount()));
//                obj.add("result", new JsonPrimitive("-"));
//
//                allActiveBids.add(obj);
//            }
//
//            responseObject.add("bids", allActiveBids);
//
//        } else if (c != null && s != null && status.equals("stopped")) {//notactive: status is successful/unsuccessful; bids should be in an array
//            responseObject.addProperty("status", "success");
//            ArrayList<Bid> allBids = BidDAO.getInstance().getBids(c.getCourseID(), s.getSectionID());
//            allBids.sort(new BidComparator());
//
//            for (int i = 0; i < allBids.size(); i++) {
//                Bid bid = allBids.get(i);
//
//                JsonObject obj = new JsonObject();
//
//                obj.add("row", new JsonPrimitive((i + 1)));
//                obj.add("userid", new JsonPrimitive(bid.getUserID()));
//                obj.add("amount", new JsonPrimitive(bid.getAmount()));
//                if (bid.getStatus().equals("success")) {
//                    obj.add("result", new JsonPrimitive("in"));
//                } else {
//                    obj.add("result", new JsonPrimitive("out"));
//                }
//                allInactiveBids.add(obj);
//            }
//
//            responseObject.add("bids", allInactiveBids);
//        }

        ArrayList<Bid> bidList = null;
        if (round == 1 && status.equals("started")){
            bidList = BidDAO.getInstance().getBidList();
        } else if (status.equals("stopped") && round == 1){
            bidList = RoundOneBidDAO.getInstance().getBidList();
        } else if(round == 2 && status.equals("started")){
            bidList = BidDAO.getInstance().getPendingBids();
        } else{
            bidList = RoundTwoBidDAO.getInstance().getBidList();
        }

        bidList.sort(new BidComparator());
        System.out.println("BIDLIST: " + bidList.size());
        int count = 1;

        for (int i = 0; i < bidList.size(); i++) {
            Bid bid = bidList.get(i);

            if (bid.getCourseID().equals(course.getAsString()) && bid.getSectionID().equals(section.getAsString())) {
                JsonObject obj = new JsonObject();


                obj.add("row", new JsonPrimitive(count));
                obj.add("userid", new JsonPrimitive(bid.getUserID()));
                obj.add("amount", new JsonPrimitive(bid.getAmount()));
                if (bid.getStatus().equals("pending")) {
                    obj.add("result", new JsonPrimitive("-"));
                } else if (bid.getStatus().equals("success")) {
                    obj.add("result", new JsonPrimitive("in"));
                } else {
                    obj.add("result", new JsonPrimitive("out"));
                }
                allBids.add(obj);
                count++;
            }

        }
        responseObject.addProperty("status", "success");
        responseObject.add("bids", allBids);
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
        //processRequest(request, response);
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
