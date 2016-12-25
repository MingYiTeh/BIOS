/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller;

import com.bios.model.BidDAO;
import com.bios.model.Student;
import com.bios.utilities.ConnectionManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A class that allows a student to drop a bid
 * @author kongyujian
 */
public class StudentDropBidServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method which redirects the user to the drop bid page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/StudentPortal/dropBid.jsp");
        rd.forward(request, response);
    }

    /**
    * Handles the HTTP <code>POST</code> method which  allows a student to drop a bid if no errors are encountered.
    * Firstly, the status of the round is checked as a bid can only be dropped during an active round. In an inactive round, an error message would be
    * put in the session object.
    * If the round is active, the bid would be deleted from the bidDAO and database and the student will be refunded with the bid's amount in both
    * the Student object and the database.
    * The user will then be redirected back to the drop bid page.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<String> dropBidErrors = new ArrayList<String>();

        HttpSession session = request.getSession(true);
        String userID = request.getParameter("userID");
        String courseID = request.getParameter("courseID");
        String sectionID = request.getParameter("sectionID");
        double amount = Double.parseDouble(request.getParameter("amount"));

        //Droppig a bid can be done only during active bidding rounds.
        ConnectionManager manager = new ConnectionManager();
        int bidRound = manager.getBiddingRound();
        String bidStatus = manager.getBiddingRoundStatus();
        System.out.println("status: "+ bidStatus);

        if (bidStatus.equals("stopped")){
            dropBidErrors.add("Bidding round is in an inactive round");
            session.setAttribute("dropBidErrors", dropBidErrors);
            response.sendRedirect(request.getContextPath()+"/student_drop_bid");
            return;
        }

        //When the input is valid, the app will cancel the bid and return back the full e$ credit. The updated e$ balance should be shown to the user.
        Student user = (Student) session.getAttribute("authenticated.student");
        double finalTotalAmount = user.geteDollar() + amount;
        manager.setEDollar(userID, finalTotalAmount);
        user.seteDollar(finalTotalAmount);

        BidDAO bidDAO = BidDAO.getInstance();
        bidDAO.deleteBid(userID, courseID, sectionID);
        bidDAO.deleteBidFromDB(userID, courseID, sectionID);

        session.setAttribute("student_drop_successful", courseID + "-" + sectionID + " was successfully dropped.");

        response.sendRedirect(request.getContextPath()+"/student_drop_bid");

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
