/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller;

import com.bios.model.BidDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.bios.utilities.ConnectionManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A class that allows the Admin to delete a bid
 * @author Jackson
 */
public class DeleteBidServlet extends HttpServlet {


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method and forwards the user's request to Admin dashboard.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(request.getContextPath()+"/admin_home");
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method and deletes the bid. It retrieves the userID, courseID, section ID, and amount bidded to verify the bid.
     * Upon verification, the bid will be deleted from bidDAO and database. The bid's amount will be refunded to the student, in both the Student object
     * and the database. The user is redirected back to the admin dashboard.
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
        String userID = request.getParameter("userID");
        String courseID = request.getParameter("courseID");
        String sectionID = request.getParameter("sectionID");
        double amount = Double.parseDouble(request.getParameter("amount"));
        ConnectionManager manager = new ConnectionManager();

        BidDAO bidDAO = BidDAO.getInstance();
        Student student = StudentDAO.getInstance().findStudent(userID);
        student.seteDollar(amount+student.geteDollar());
        manager.setEDollar(userID, amount+student.geteDollar());
        bidDAO.deleteBid(userID, courseID, sectionID);
        bidDAO.deleteBidFromDB(userID, courseID, sectionID);

        response.sendRedirect(request.getContextPath()+"/admin_home");


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
