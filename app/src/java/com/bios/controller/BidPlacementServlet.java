/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */
package com.bios.controller;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CompletedCourseDAO;
import com.bios.model.Course;
import com.bios.model.CompletedCourse;
import com.bios.model.CourseDAO;
import com.bios.model.MinBidDAO;
import com.bios.model.PrerequisiteDAO;
import com.bios.model.Prerequisite;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.bios.utilities.BidComparator;
import com.bios.utilities.BootstrapValidator;
import com.bios.utilities.ConnectionManager;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A class that allows a Student to place a bid
 * @author Marc
 */
public class BidPlacementServlet extends HttpServlet {

    private static final String SUCCESS = "success";

    private BidDAO bidDAO;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method by storing all the possible courseSectionIDs in an ArrayList and placing it in the session object.
     * This is done through running through the sectionDAO and obtaining the courseID and sectionID of each and every section and then populating
     * the ArrayList.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //connect to database to get Course List and section list
        HttpSession session = request.getSession(true);

        SectionDAO sectionDAO = SectionDAO.getInstance();
        ArrayList<Section> sectionList = sectionDAO.getSectionList();
        ArrayList<String> courseSectionList = new ArrayList<>();

        for (int i = 0; i < sectionList.size(); i++) {
            Section s = sectionList.get(i);
            String courseID = s.getCourseID();
            String sectionID = s.getSectionID();
            String courseSectionID = courseID + "-" + sectionID;
            courseSectionList.add(courseSectionID);
        }

        session.setAttribute("courseSectionList", courseSectionList);
        RequestDispatcher rd = request.getRequestDispatcher("/StudentPortal/bidSection.jsp");
        rd.forward(request, response);

    }

    /**
     *Handles the HTTP <code>POST</code> method by checking a bid's validity and then placing it in the bidDAO and in the Database while deducting the
     * appropriate e$ from the user. The bid's details is obtained first from the session object. If the bid has been previously placed, it will be updated
     * with a new bid amount. Else, the bid is validated and then checked for it eligibility to be placed. If it is ineligible, the error would be shown.
     * If there are no errors, the bid will be placed in the BidDAO and database. Additionally, the student's e$ balance will be updated in both the database
     * and Student object to reflect the bid. The user is then redirected back to the bidSection page.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //get context and initialise DAOS
        ConnectionManager manager = new ConnectionManager();
        HttpSession session = request.getSession(true);
        BootstrapValidator validator = new BootstrapValidator();
        int round = manager.getBiddingRound();
        bidDAO = BidDAO.getInstance();
        //TODO need a DAO for sectionstudent
        //TODO need to check for the bidding round

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //get the path from which the request came from
        String path = request.getServletPath();

        ArrayList<String> checkBidValidity = new ArrayList<String>();
        //ArrayList<String> checkBidLogic = new ArrayList<String>();

        // This check is arbitrary, and is only checking to see which path we are getting from
        if (path.indexOf("bidSection") > 0) {
            Student student = (Student) session.getAttribute("authenticated.student");

            String courseSectionID = request.getParameter("courseID-sectionID");
            ArrayList<Bid> allStudentBids = BidDAO.getInstance().getStudentBids(student.getId());

            String courseID = courseSectionID.substring(0, courseSectionID.indexOf("-"));
            String sectionID = courseSectionID.substring(courseSectionID.indexOf("-") + 1, courseSectionID.length());

            String userID = student.getId();
            String amount = request.getParameter("amount");
            session.setAttribute("course-sectionID", courseSectionID);
            checkBidValidity = validator.validateBids(userID, amount, courseID, sectionID);

            Section section = SectionDAO.getInstance().findSection(courseID, sectionID);

            // How many students have already been enrolled in the section successfully
            ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentListWithID(courseID, sectionID);

            // Size of the class minus the already enrolled number, which essentially means vacancy
            int vacancy = section.getSize() - sectionStudentList.size();

            // Minimum bid value that has to be shown to user, default is 10
            double minBidValue = 10;

            // This lists the bids that are still pending approval in round 2, and contains the bids
            // that user has selected, and these bids are sorted from highest to lowest
            ArrayList<Bid> round2Bids = BidDAO.getInstance().getPendingBidsWithID(courseID, sectionID);
            round2Bids.sort(new BidComparator());
            // check to see during round 2, if user has bidded more than minbidvalue
            if (round2Bids.size() >= vacancy && vacancy != 0) { // should this be >=? wha if vacancy==0?
                minBidValue = round2Bids.get(vacancy - 1).getAmount() + 1;
            }

            // This allows us to store the minimum bids, and also to persist the bids such that it stores the min bid amount
            if (MinBidDAO.getInstance().updateMinBid(courseSectionID, minBidValue)) {
                // Bid was updated successfully
                minBidValue = minBidValue;
            } else {
                minBidValue = MinBidDAO.getInstance().getValue(courseSectionID);
            }

            try {
                double amountBidded = Double.parseDouble(amount);

                if (round == 2) {
                    if (amountBidded < minBidValue) {
                        // There's an error, and we should reject
                        checkBidValidity.add("bid too low");
                    } else if (updateCurrentBid(student.getId(), courseID, sectionID, amountBidded, student)) {
                        session.removeAttribute("courseID");
                        session.removeAttribute("sectionID");
                        session.removeAttribute("errors");
                        MinBidDAO.getInstance().updateMinBid(courseID + "-" + sectionID, amountBidded);
                        session.setAttribute("course-sectionID", courseID + "-" + sectionID);
                        session.setAttribute("bid_success", "Bid was successfully updated");
                        response.sendRedirect(request.getContextPath() + "/bidSection");
                        return;
                    }
                } else if (round == 1) {
                    if (updateCurrentBid(student.getId(), courseID, sectionID, amountBidded, student)) {
                        session.removeAttribute("courseID");
                        session.removeAttribute("sectionID");
                        session.removeAttribute("errors");
                        MinBidDAO.getInstance().updateMinBid(courseID + "-" + sectionID, amountBidded);
                        session.setAttribute("course-sectionID", courseID + "-" + sectionID);
                        session.setAttribute("bid_success", "Bid was successfully updated");
                        response.sendRedirect(request.getContextPath() + "/bidSection");
                        return;
                    }
                }

                //checking if student already bidded for this course previously
                ArrayList<Bid> biddedCourses = bidDAO.getStudentBids(userID);
                for (Bid bid : biddedCourses) {
                    if (bid.getCourseID().equals(courseID)) {
                        checkBidValidity.add("already bidded for this course");
                    }
                }

                // if the round it round 2, remove the error "not own school course" from the error list
                if (round == 2) {
                    Iterator<String> iter = checkBidValidity.iterator();
                    while (iter.hasNext()) {
                        if (iter.next().equals("not own school course")) {
                            iter.remove();
                        }
                    }
                }

                if (!checkBidValidity.isEmpty()) {
                    session.setAttribute("errors", checkBidValidity);
                    response.sendRedirect(request.getContextPath() + "/bidSection");
                } else {
                    Bid bid = new Bid(userID, amountBidded, courseID, sectionID, "pending");
                    bidDAO.addBid(bid);
                    bidDAO.addBidToDB(bid);
                    manager.setEDollar(student.getId(), student.geteDollar() - amountBidded);
                    student.seteDollar(student.geteDollar() - amountBidded);

                    session.removeAttribute("courseID");
                    session.removeAttribute("sectionID");
                    session.removeAttribute("errors");
                    MinBidDAO.getInstance().updateMinBid(courseID + "-" + sectionID, amountBidded);
                    session.setAttribute("course-sectionID", courseID + "-" + sectionID);
                    session.setAttribute("bid_success", "Bid was successfully placed");
                    response.sendRedirect(request.getContextPath() + "/bidSection");
                    return;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errors", checkBidValidity);
                response.sendRedirect(request.getContextPath() + "/bidSection");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/bidSection");
            return;
        }
    }

    /**
     * Handles the updating of a bid that already exists. If bid is updated, it will true, else it returns false
     * @param userID User ID of bid
     * @param courseID Course ID of bid
     * @param sectionID Section ID of bid
     * @param amount Amount of bid
     * @param student Student that made the bid
     * @return whether the current bid is being updated
     */
    public static boolean updateCurrentBid(String userID, String courseID, String sectionID, double amount, Student student) {
        //System.out.println("Bid user: " + userID);

        Bid bid = BidDAO.getInstance().getStudentBidWithCourseID(userID, courseID);
        if (bid != null) {
            String prevSectionID = bid.getSectionID();
            ConnectionManager manager = new ConnectionManager();
            System.out.println("Updated:" + bid);
            // If bid exists, this means we can update it and we should update it
            if (bid != null && amount >= 10) {
                //System.out.println("studentedollar"+student.geteDollar());
                //System.out.println("existingbidamt "+ bid.getAmount());
                double remainAmount = student.geteDollar() + bid.getAmount() - amount;
                //System.out.println("remainamt"+remainAmount);
                if (remainAmount < 0) {
                    return false;
                }
                //System.out.println("remainAmount"+remainAmount);
                student.seteDollar(remainAmount);
                manager.setEDollar(student.getId(), remainAmount);
                BidDAO.getInstance().deleteBid(userID, courseID, prevSectionID);
                bid = new Bid(userID, amount, courseID, sectionID, "pending");
                BidDAO.getInstance().addBid(bid);
                manager.updateBid(userID, amount, courseID, sectionID, "pending");
                //System.out.println("bidplacementservlet"+student.geteDollar());
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

}
