/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.MinBidDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.utilities.BidComparator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Represents a JSON Web Service for Getting Live Bidding Table
 * @author Teh Ming Yi
 */
public class GetLiveBiddingTableServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method to determine whether currently placed
     * bids will be successful at the end of the round. A json object that contains the
     * live bidding data relating to the bid will be created.
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
         HttpSession session = request.getSession(true);
        String course_section_selected = request.getParameter("courseSectionID");
        int hyphenIndex = course_section_selected.indexOf("-");
        String courseSelected = course_section_selected.substring(0, hyphenIndex);
        String sectionSelected = course_section_selected.substring(hyphenIndex + 1);
        String userID = (String) session.getAttribute("username");

        // Section that user has selected
        Section section = SectionDAO.getInstance().findSection(courseSelected, sectionSelected);

        // How many students have already been enrolled in the section successfully
        ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentListWithID(courseSelected, sectionSelected);

        // Size of the class minus the already enrolled number, which essentially means vacancy
        int vacancy = section.getSize() - sectionStudentList.size();

        // Minimum bid value that has to be shown to user, default is 10
        double minBidValue = 10;

        // This lists the bids that are still pending approval in round 2, and contains the bids
        // that user has selected, and these bids are sorted from highest to lowest
        ArrayList<Bid> round2Bids = BidDAO.getInstance().getPendingBidsWithID(courseSelected, sectionSelected);
        round2Bids.sort(new BidComparator());

        // This JsonObject is to be used as the reponse to the user
        JsonObject object = new JsonObject();

        // This is to re-compute the minimum bid value
        if (round2Bids.size() >= vacancy && vacancy != 0){ // should this be >=? wha if vacancy==0?
            minBidValue = round2Bids.get(vacancy - 1).getAmount() + 1;
        }

        // We will first add in the 2 properties that have already been calculated
        object.addProperty("vacancy", vacancy);
        object.addProperty("courseSectionID",course_section_selected);
        object.addProperty("round2BidsSize", round2Bids.size());
        if(round2Bids.size()==0){
        MinBidDAO.getInstance().drop();
        MinBidDAO.getInstance().refresh();
        }
        System.out.println("minbidvalue "+MinBidDAO.getInstance().getValue(course_section_selected));
        // This allows us to store the minimum bids, and also to persist the bids such that it stores the min bid amount
        if (MinBidDAO.getInstance().updateMinBid(course_section_selected, minBidValue)){
            // Bid was updated successfully
            object.addProperty("minBidValue", minBidValue);
        } else {
            object.addProperty("minBidValue", MinBidDAO.getInstance().getValue(course_section_selected));
        }

        String prevStatus = "";

        JsonArray arrayOfBids = new JsonArray();
        for (int i = 0; i < round2Bids.size(); i++){
            Bid bid = round2Bids.get(i);
            JsonObject innerObject = new JsonObject();


            // If there are vacancies still
            if (vacancy >= round2Bids.size()){
                innerObject.addProperty("bidPrice", bid.getAmount());
                innerObject.addProperty("isSuccess", "Successful");
                //add temp success
            } else if (round2Bids.size() > vacancy){
                // If this bid is still within the vacancy requirements
                if (i <= vacancy){
                    // Even if they are within the vacancy requirements, we need
                    // to check to see if there are conflicting bids, this means
                    // we need to check if the bid right after the vacancy requirements
                    // has an equal amount
                    Bid firstFailedBid = round2Bids.get(vacancy);

                    if (bid.getAmount() == firstFailedBid.getAmount()){
                        if (i == vacancy){
                            if (prevStatus.equals("Unsuccessful")){
                                innerObject.addProperty("bidPrice", bid.getAmount());
                                innerObject.addProperty("isSuccess", "Unsuccessful");
                                prevStatus = "Unsuccessful";
                            } else {
                                innerObject.addProperty("bidPrice", bid.getAmount());
                                innerObject.addProperty("isSuccess", "Unsuccessful. Bid too low.");
                                prevStatus = "Unsuccessful. Bid too low.";
                            }
                        } else {
                            innerObject.addProperty("bidPrice", bid.getAmount());
                            innerObject.addProperty("isSuccess", "Unsuccessful");
                            prevStatus = "Unsuccessful";
                        }
                    } else {
                        innerObject.addProperty("bidPrice", bid.getAmount());
                        innerObject.addProperty("isSuccess", "Successful");
                        prevStatus = "Successful";
                    }

                } else if (i > vacancy){ //what if vacancy+1's bid amout
                    innerObject.addProperty("bidPrice", bid.getAmount());
                    innerObject.addProperty("isSuccess", "Unsuccessful. Bid too low.");
                    prevStatus = "Unsuccessful. Bid too low.";

                }
            }
            arrayOfBids.add(innerObject);
        }


        object.add("data", arrayOfBids);

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
