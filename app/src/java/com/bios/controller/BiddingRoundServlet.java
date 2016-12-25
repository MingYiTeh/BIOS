/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A class that allows the Admin to modify a bidding round
 *
 * @author kongyujian
 */
public class BiddingRoundServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method and modifies the bidding
     * round.It retrieves a boolean value on whether to advance the bidding
     * round or not. If value is true, the bidding round will be advanced by 1.
     * After which, the user will be redirected back to the Admin Dashboard.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String shouldAdvance = request.getParameter("advance");

        incrementBiddingRound(shouldAdvance);

        response.sendRedirect(request.getContextPath() + "/admin_home");
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

    /**
     * This method increments a bidding round and clears the round. Firstly, the
     * round's status is checked. If the status is stopped,clearing logic is
     * carried out. The sectionDAO is retrieved. Running through the sectionDAO,
     * we check if bids exist for each section. The bids are then sorted based
     * on the prices of the bids. If there are more bids than vacancies, a
     * clearing price is determined and all bids below that are failed while all
     * bids above it are considered successful.Else, all bids will be
     * successful. If a bid is successful, a new SectionStudent object is
     * created and the bid's success will be updated in the bidDAO and the
     * database. Conversely, if the bid fails, it would be updated in both the
     * bidDAO, database and the student will be refunded for the failed bid.
     *
     * @param shouldAdvance whether the round should be incremented
     */
    public static void incrementBiddingRound(String shouldAdvance) {
        if (shouldAdvance != null && shouldAdvance.equals("true")) {
            ConnectionManager manager = new ConnectionManager();
            manager.modifyBiddingRound();
            int round = manager.getBiddingRound();
            String status = manager.getBiddingRoundStatus();
            if (round == 1 && status.equals("started")) {
                // This indicates the start of the round 1!

            } else if (round == 1 && status.equals("stopped")) {
                // This means that we should start clearing
                //TODO TEST THIS THING
                RoundOneBidDAO.getInstance().drop();
                // add all round 1 pending bids into roundonebiddao
                ArrayList<Bid> allBids = BidDAO.getInstance().getPendingBids();
                for (Bid bid : allBids) {
                    RoundOneBidDAO.getInstance().addBid(bid);
                }
// We need to clear round 1 and we need to prepare round 2
                ArrayList<Section> allSections = SectionDAO.getInstance().getSectionList();
                for (Section section : allSections) {
                    allBids = BidDAO.getInstance().getBids(section.getCourseID(), section.getSectionID());
                    //sorted from highest to lowest bid
                    allBids.sort(new BidComparator());

                    int vacancy = section.getSize();
                    boolean startfailing = false;

                    double clearingPrice = 0;
                    if (allBids.size() >= vacancy) {
                        clearingPrice = allBids.get(vacancy - 1).getAmount();
                        if (allBids.size() > vacancy) {
                            if (allBids.get(vacancy).getAmount() == clearingPrice || allBids.get(vacancy - 2).getAmount() == clearingPrice) {
                                startfailing = true;
                            }
                        } else if (vacancy != 0 && vacancy != 1 && allBids.get(vacancy - 2).getAmount() == clearingPrice) {
                            startfailing = true;
                        }
                    } else if (allBids.size() == 0) {
                        clearingPrice = 0;
                    }

                    if (allBids.size() < vacancy) {
                        for (Bid b : allBids) {
                            SectionStudent successfulBid = new SectionStudent(b.getCourseID(), b.getUserID(), b.getSectionID(), b.getAmount());
                            SectionStudentDAO.getInstance().addSectionStudent(successfulBid);
                            manager.addSectionStudent(successfulBid);
                            Bid r1Bid = RoundOneBidDAO.getInstance().getBid(b.getUserID(), b.getCourseID(), b.getSectionID());
                            r1Bid.setStatus("success");
                            b.setStatus("success");
                            manager.updateBid(b.getUserID(), b.getAmount(), b.getCourseID(), b.getSectionID(), "success");
                        }

                    } else {

                        for (int i = 0; i < allBids.size(); i++) {
                            Bid bid = allBids.get(i);
                            if (bid.getAmount() >= 10 && bid.getAmount() >= clearingPrice) {
                                System.out.println("bidSuccess");
                                if (bid.getAmount() < clearingPrice && i >= (vacancy - 1)) {
                                    manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                    Student stud = StudentDAO.retrieve(bid.getUserID());
                                    stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                    bid.setStatus("failed");
                                    Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                    r1Bid.setStatus("failed");
                                    manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "failed");
                                } // This block captures the special case where all users at clearing price have bidded the same amount
                                else if (bid.getAmount() == clearingPrice && i < (vacancy - 1)) {//need the if-else here
                                    if (i != allBids.size() - 1) {
                                        manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                        Student stud = StudentDAO.retrieve(bid.getUserID());
                                        stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                        bid.setStatus("failed");
                                        Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                        r1Bid.setStatus("failed");
                                        startfailing = true;
                                        manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "failed");
                                    } else {
                                        SectionStudent successfulBid = new SectionStudent(bid.getCourseID(), bid.getUserID(), bid.getSectionID(), bid.getAmount());
                                        SectionStudentDAO.getInstance().addSectionStudent(successfulBid);
                                        manager.addSectionStudent(successfulBid);
                                        bid.setStatus("success");
                                        Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                        r1Bid.setStatus("success");
                                        manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "success");
                                    }
                                } else if (bid.getAmount() == clearingPrice && i == (vacancy - 1)) {
                                    if (startfailing) {
                                        manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                        Student stud = StudentDAO.retrieve(bid.getUserID());
                                        stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                        bid.setStatus("failed");
                                        Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                        r1Bid.setStatus("failed");
                                        manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "failed");
                                    } else {
                                        SectionStudent successfulBid = new SectionStudent(bid.getCourseID(), bid.getUserID(), bid.getSectionID(), bid.getAmount());
                                        SectionStudentDAO.getInstance().addSectionStudent(successfulBid);
                                        manager.addSectionStudent(successfulBid);
                                        bid.setStatus("success");
                                        Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                        r1Bid.setStatus("success");
                                        manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "success");
                                    }
                                } else if (bid.getAmount() == clearingPrice && i > (vacancy - 1)) {
                                    Student stud = StudentDAO.retrieve(bid.getUserID());
                                    stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                    Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                    r1Bid.setStatus("failed");
                                    bid.setStatus("failed");
                                    manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "failed");
                                } else {
                                    SectionStudent successfulBid = new SectionStudent(bid.getCourseID(), bid.getUserID(), bid.getSectionID(), bid.getAmount());
                                    SectionStudentDAO.getInstance().addSectionStudent(successfulBid);
                                    manager.addSectionStudent(successfulBid);
                                    Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                    r1Bid.setStatus("success");
                                    bid.setStatus("success");
                                    manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "success");
                                }

                            } else {
                                manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                Student stud = StudentDAO.retrieve(bid.getUserID());
                                stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                bid.setStatus("failed");
                                Bid r1Bid = RoundOneBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                r1Bid.setStatus("failed");
                                manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "failed");
                            }
                        }
                    }
                }
            } else if (round == 2 && status.equals("started")) {
                // Do nothing to clear when round starts

            } else if (round == 2 && status.equals("stopped")) {
                // Perform clearning logic for round 2
                RoundTwoBidDAO.getInstance().drop();
                // add all round 1 pending bids into roundonebiddao
                ArrayList<Bid> allBids = BidDAO.getInstance().getPendingBids();
                for (Bid bid : allBids) {
                    RoundTwoBidDAO.getInstance().addBid(bid);
                }
                ArrayList<Section> allSections = SectionDAO.getInstance().getSectionList();

                // For every section, we need to check to see if the bids made for that section will pass.
                for (Section section : allSections) {
                    String courseSelected = section.getCourseID();
                    String sectionSelected = section.getSectionID();

                    // How many students have already been enrolled in the section successfully
                    ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentListWithID(courseSelected, sectionSelected);

                    // Size of the class minus the already enrolled number, which essentially means vacancy
                    int vacancy = section.getSize() - sectionStudentList.size();

                    // Minimum bid value that has to be shown to user, default is 10
//                    double minBidValue = 10;
                    // This lists the bids that are still pending approval in round 2, and contains the bids
                    // that user has selected, and these bids are sorted from highest to lowest
                    ArrayList<Bid> round2Bids = BidDAO.getInstance().getPendingBidsWithID(courseSelected, sectionSelected);
                    round2Bids.sort(new BidComparator());

                    // This is to re-compute the minimum bid value
//                    if (round2Bids.size() > vacancy) {
//                        // Since there are still vacancies, the minBidValue will remain the same
//                        minBidValue = round2Bids.get(vacancy - 1).getAmount() + 1;
//                    }
                    String prevStatus = "";

                    for (int i = 0; i < round2Bids.size(); i++) {
                        Bid bid = round2Bids.get(i);

                        // If there are vacancies still
                        if (vacancy >= round2Bids.size()) {
                            SectionStudent successfulBid = new SectionStudent(bid.getCourseID(), bid.getUserID(), bid.getSectionID(), bid.getAmount());
                            SectionStudentDAO.getInstance().addSectionStudent(successfulBid);
                            manager.addSectionStudent(successfulBid);
                            bid.setStatus("success");
                            Bid r2Bid = RoundTwoBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                            r2Bid.setStatus("success");
                            manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "success");
                        } else if (round2Bids.size() > vacancy) {
                            // If this bid is still within the vacancy requirements
                            if (i <= vacancy) {
                                // Even if they are within the vacancy requirements, we need
                                // to check to see if there are conflicting bids, this means
                                // we need to check if the bid right after the vacancy requirements
                                // has an equal amount
                                Bid firstFailedBid = round2Bids.get(vacancy);

                                if (bid.getAmount() == firstFailedBid.getAmount()) {
                                    if (i == vacancy) {
                                        if (prevStatus.equals("Unsuccessful")) {
                                            bid.setStatus("failed");
                                            Bid r2Bid = RoundTwoBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                            r2Bid.setStatus("failed");
                                            manager.updateBid(bid.getUserID(), bid.getAmount(), courseSelected, sectionSelected, "failed");
                                            prevStatus = "Unsuccessful";
                                            Student stud = StudentDAO.getInstance().findStudent(bid.getUserID());
                                            stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                            manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                        } else {
                                            bid.setStatus("failed");
                                            Bid r2Bid = RoundTwoBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                            r2Bid.setStatus("failed");
                                            manager.updateBid(bid.getUserID(), bid.getAmount(), courseSelected, sectionSelected, "failed");
                                            prevStatus = "Unsuccessful. Bid too low.";
                                            Student stud = StudentDAO.getInstance().findStudent(bid.getUserID());
                                            stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                            manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                        }
                                    } else {
                                        bid.setStatus("failed");
                                        Bid r2Bid = RoundTwoBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                        r2Bid.setStatus("failed");
                                        manager.updateBid(bid.getUserID(), bid.getAmount(), courseSelected, sectionSelected, "failed");
                                        prevStatus = "Unsuccessful";
                                        Student stud = StudentDAO.getInstance().findStudent(bid.getUserID());
                                        stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                        manager.refundEDollar(bid.getUserID(), bid.getAmount());
                                    }
                                } else {
                                    SectionStudent successfulBid = new SectionStudent(bid.getCourseID(), bid.getUserID(), bid.getSectionID(), bid.getAmount());
                                    SectionStudentDAO.getInstance().addSectionStudent(successfulBid);
                                    manager.addSectionStudent(successfulBid);
                                    bid.setStatus("success");
                                    Bid r2Bid = RoundTwoBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                    r2Bid.setStatus("success");
                                    manager.updateBid(bid.getUserID(), bid.getAmount(), bid.getCourseID(), bid.getSectionID(), "success");
                                    prevStatus = "Successful";
                                }

                            } else if (i > vacancy) {
                                bid.setStatus("failed");
                                Bid r2Bid = RoundTwoBidDAO.getInstance().getBid(bid.getUserID(), bid.getCourseID(), bid.getSectionID());
                                r2Bid.setStatus("failed");
                                manager.updateBid(bid.getUserID(), bid.getAmount(), courseSelected, sectionSelected, "failed");
                                prevStatus = "Unsuccessful. Bid too low.";
                                Student stud = StudentDAO.getInstance().findStudent(bid.getUserID());
                                stud.seteDollar(stud.geteDollar() + bid.getAmount());
                                manager.refundEDollar(bid.getUserID(), bid.getAmount());

                            }
                        }
                    }
                }
            }
        }
    }

}
