/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CourseDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
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
 * A class that allows Students to drop a section
 *
 * @author kongyujian
 */
public class StudentDropSectionServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method and forwards the user's request
     * to Drop Section page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/StudentPortal/dropSection.jsp");
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method by allowing a student to drop a
     * successful bid only in an active round. Upon dropping a section
     * successfully, the particulars of the student enrolled in the section will
     * be removed from the database containing the list of students enrolled in
     * the respective sections. Upon successful dropping of the section, the student
     * wi;; be refunded with the amount of the bid in both the StudentDAO and database.
     * This method throws errors when a student tries to drop a section when there are
     * no successful bids or when the round is inactive. The user will then be
     * redirected back to the drop section page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<String> dropSectionErrors = new ArrayList<>();
        HttpSession session = request.getSession(true);
        if (session.getAttribute("authenticated.student") != null) {
        } else {
            System.out.println("hi" + session.getAttribute("authenticated.student"));
        }
        Student user = (Student) session.getAttribute("authenticated.student");
        String userID = request.getParameter("userID");
        System.out.println("dropSectionServlet" + userID);
        String courseID = request.getParameter("courseID");
        System.out.println("dropSectionServlet" + courseID);
        String sectionID = request.getParameter("sectionID");
        System.out.println("dropSectionServlet" + sectionID);

        ConnectionManager manager = new ConnectionManager();

        SectionStudentDAO current = SectionStudentDAO.getInstance();
        SectionStudent toBeDropped = current.findSectionStudent(courseID, sectionID, userID);
        if (manager.getBiddingRound() == 2 && manager.getBiddingRoundStatus().equals("started")) {
            if (user != null) {
                System.out.println("user not null");
                if (toBeDropped != null) {
                    System.out.println("im here");
                    double finalTotalAmount = user.geteDollar() + toBeDropped.getAmount();
                    manager.setEDollar(userID, finalTotalAmount);
                    user.seteDollar(finalTotalAmount);
                    current.deleteStudentSection(userID, toBeDropped.getAmount(), courseID, sectionID);
                    current.deleteStudentSectionFromDB(userID, toBeDropped.getAmount(), courseID, sectionID);
                    BidDAO.getInstance().deleteBid(userID,courseID,sectionID);
                    BidDAO.getInstance().deleteBidFromDB(userID,courseID,sectionID);
                    session.setAttribute("sectionSuccessfullyDropped", courseID);

                } else {
                    dropSectionErrors.add("no such successful bid");
                }
            } else {
                System.out.println("user is null");
                dropSectionErrors.add("user not found");
            }
        } else {
            System.out.println("Round 1");
            dropSectionErrors.add("inactive round");
        }
        response.sendRedirect(request.getContextPath() + "/student_drop_section");
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
