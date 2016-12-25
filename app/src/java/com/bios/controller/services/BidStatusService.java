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
import com.bios.model.MinBidDAO;
import com.bios.model.RoundOneBidDAO;
import com.bios.model.RoundTwoBidDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Represents a JSON Web Service for Bid Status
 * @author Teh Ming Yi
 */
public class BidStatusService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method to determine whether currently
     * placed bids will be successful at the end of the round. A json object
     * that contains the live bidding data relating to the bid will be created.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
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

        // Section that user has selected
        Section section = SectionDAO.getInstance().findSection(courseID, sectionID);

        Course course = CourseDAO.getInstance().findCourse(courseID);
        if (course == null) {
            allErrors.add(new JsonPrimitive("invalid course"));
        } else if (course != null && section == null) {
            allErrors.add(new JsonPrimitive("invalid section"));
        }

        if (allErrors.size() > 0) {
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        }

        // How many students have already been enrolled in the section successfully
        ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentListWithID(courseID, sectionID);

        // Size of the class minus the already enrolled number, which essentially means vacancy
        int vacancy = section.getSize() - sectionStudentList.size();

        // Minimum bid value that has to be shown to user, default is 10
        double minBidValue = 10;

        // This lists the bids that are still pending approval in round 2, and contains the bids
        // that user has selected, and these bids are sorted from highest to lowest
        ArrayList<Bid> allBids = BidDAO.getInstance().getPendingBidsWithID(courseID, sectionID);
//        ArrayList<Bid> allBids= null;
//        if (round == 1 && status.equals("started")){
//            allBids = BidDAO.getInstance().getBidList();
//        } else if (status.equals("stopped") && round == 1){
//            allBids = RoundOneBidDAO.getInstance().getBidList();
//        } else if(round == 2 && status.equals("started")){
//            allBids = BidDAO.getInstance().getPendingBids();
//        } else{
//            allBids = RoundTwoBidDAO.getInstance().getBidList();
//        }
        allBids.sort(new BidComparator());

        // This JsonObject is to be used as the reponse to the user
        JsonObject object = new JsonObject();

        object.addProperty("status", "success");
        DecimalFormat df = new DecimalFormat("0.00");
        JsonArray arrayOfBids = new JsonArray();
        if (round == 1 && status.equals("started")) {
            object.addProperty("vacancy", section.getSize());
            if(section.getSize() > allBids.size() && allBids.size()>0){
                minBidValue = allBids.get(allBids.size()-1).getAmount();
            }else if (allBids.size() >= section.getSize()){
                minBidValue = allBids.get(section.getSize()-1).getAmount();
            }
            object.addProperty("min-bid-amount", minBidValue);
            for (Bid bid : allBids) {
                JsonObject innerObject = new JsonObject();
                Student stud = StudentDAO.getInstance().retrieve(bid.getUserID());
                innerObject.add("userid", new JsonPrimitive(bid.getUserID()));
                innerObject.add("amount", new JsonPrimitive(bid.getAmount()));
                String result = df.format(stud.geteDollar());
                double eDollar = Double.parseDouble(result);
                innerObject.add("balance", new JsonPrimitive(eDollar));
                innerObject.add("status", new JsonPrimitive("pending"));
                arrayOfBids.add(innerObject);
            }

        } else if (round == 1 && status.equals("stopped")) {
            object.addProperty("vacancy", section.getSize() - BidDAO.getInstance().getSuccessfulBidsWithID(courseID, sectionID).size());
            
            allBids = BidDAO.getInstance().getBids(courseID,sectionID);
            if (allBids.size() >= vacancy && vacancy != 0) { // should this be >=? wha if vacancy==0?
                minBidValue = allBids.get(vacancy - 1).getAmount();
            }else if(allBids.size() < vacancy && allBids.size() > 0){
                minBidValue = allBids.get(allBids.size()-1).getAmount();
            }
            object.addProperty("min-bid-amount", minBidValue);
            allBids = BidDAO.getInstance().getBids(courseID, sectionID);

            for (Bid bid : allBids) {
                JsonObject innerObject = new JsonObject();
                Student stud = StudentDAO.getInstance().retrieve(bid.getUserID());
                innerObject.add("userid", new JsonPrimitive(bid.getUserID()));
                innerObject.add("amount", new JsonPrimitive(bid.getAmount()));
                String result = df.format(stud.geteDollar());
                double eDollar = Double.parseDouble(result);
                innerObject.add("balance", new JsonPrimitive(eDollar));
                innerObject.add("status", new JsonPrimitive(bid.getStatus()));
                arrayOfBids.add(innerObject);
            }
        } else if (round == 2 && status.equals("started")) {
            object.addProperty("vacancy", vacancy);

            // This is to re-compute the minimum bid value
//            if (allBids.size() >= vacancy && vacancy != 0) { // should this be >=? wha if vacancy==0?
//                minBidValue = allBids.get(vacancy - 1).getAmount() + 1;
//            }
            // This allows us to store the minimum bids, and also to persist the bids such that it stores the min bid amount

            if (MinBidDAO.getInstance().updateMinBid(courseID + "-" + sectionID, minBidValue)) {
                // Bid was updated successfully
                object.addProperty("min-bid-amount", minBidValue);
            } else {
                object.addProperty("min-bid-amount", MinBidDAO.getInstance().getValue(courseID + "-" + sectionID));
            }
       

            for (int i = 0; i < allBids.size(); i++) {
                Bid bid = allBids.get(i);
                JsonObject innerObject = new JsonObject();

                Student stud = StudentDAO.getInstance().retrieve(bid.getUserID());
                innerObject.add("userid", new JsonPrimitive(bid.getUserID()));
                innerObject.add("amount", new JsonPrimitive(bid.getAmount()));
                String result = df.format(stud.geteDollar());
                double eDollar = Double.parseDouble(result);
                innerObject.add("balance", new JsonPrimitive(eDollar));

                // If there are vacancies still
                if (vacancy >= allBids.size()) {
                    innerObject.add("status", new JsonPrimitive("success"));
                } else if (allBids.size() > vacancy) {
                    // If this bid is still within the vacancy requirements
                    if (i <= vacancy) {
                        Bid firstFailedBid = allBids.get(vacancy);

                        if (bid.getAmount() == firstFailedBid.getAmount()) {
                            innerObject.add("status", new JsonPrimitive("fail"));
                        } else {
                            innerObject.add("status", new JsonPrimitive("success"));
                        }
                    } else if (i > vacancy) { //what if vacancy+1's bid amout
                        innerObject.add("status", new JsonPrimitive("fail"));
                    }
                }
                arrayOfBids.add(innerObject);
            }

        } else {
            object.addProperty("vacancy", section.getSize() - BidDAO.getInstance().getSuccessfulBidsWithID(courseID, sectionID).size());
            allBids = BidDAO.getInstance().getSuccessfulBidsWithID(courseID, sectionID);
            allBids.sort(new BidComparator());
            minBidValue = allBids.get(allBids.size()-1).getAmount();
            object.addProperty("min-bid-amount", minBidValue);
            for (Bid bid : allBids) {
                JsonObject innerObject = new JsonObject();
                Student stud = StudentDAO.getInstance().retrieve(bid.getUserID());
                innerObject.add("userid", new JsonPrimitive(bid.getUserID()));
                innerObject.add("amount", new JsonPrimitive(bid.getAmount()));
                String result = df.format(stud.geteDollar());
                double eDollar = Double.parseDouble(result);
                innerObject.add("balance", new JsonPrimitive(eDollar));
                innerObject.add("status", new JsonPrimitive(bid.getStatus()));
                arrayOfBids.add(innerObject);
            }
        }

        
        
        object.add("students", arrayOfBids);

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

}
