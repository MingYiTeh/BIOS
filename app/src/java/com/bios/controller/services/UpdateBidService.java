/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.controller.BidPlacementServlet;
import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CompletedCourse;
import com.bios.model.CompletedCourseDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.MinBidDAO;
import com.bios.model.Prerequisite;
import com.bios.model.PrerequisiteDAO;
import com.bios.model.RoundOneBidDAO;
import com.bios.model.RoundTwoBidDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.bios.utilities.BidComparator;
import com.bios.utilities.BootstrapValidator;
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
 * Represents a JSON Web Service for Update Bid
 * @author kongyujian
 */
public class UpdateBidService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
    * Handles the HTTP <code>GET</code> method which updates a bid. Firstly, the token
    * is checked for its validity. If the token is valid, the method then goes into the
    * respective DAOs to look for the bid. If the bid is valid and can be deleted, the
    * bid is then deleted from the DAO and the database. Subsequentyly, a json object
    * with the status "success" will be created. If any errors occur such as an
    * invalid course or missing token is detected, a json object with the status "error"
    * will be created with a json array of the erros that caused the failure to update.
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

        JsonObject reponseObject = new JsonObject();
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

        JsonElement jelement = new JsonParser().parse(requestObject);
        JsonObject json = jelement.getAsJsonObject();
        Bid existingBid = null;

        JsonElement amt = json.get("amount");
        JsonElement crse = json.get("course");
        JsonElement sec = json.get("section");
        JsonElement user = json.get("userid");

        if (amt == null) {
            errorList.add("missing amount");
        } else if (amt.getAsString().length() == 0) {
            errorList.add("blank amount");
        }

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
            reponseObject.addProperty("status", "error");
            reponseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(reponseObject.toString());
            return;
        }

        // There may be type errors for the double here
        double amount = amt.getAsDouble();
        String courseID = crse.getAsString();
        String sectionID = sec.getAsString();
        String userID = user.getAsString();

        Course course = CourseDAO.getInstance().findCourse(courseID);
        Section section = SectionDAO.getInstance().findSection(courseID, sectionID);
        Student student= StudentDAO.retrieve(userID);

        int round = manager.getBiddingRound();
        BootstrapValidator bv = new BootstrapValidator();
            if (bv.parseBidAmount(amt.getAsString()) != null) {
                allErrors.add(new JsonPrimitive("invalid amount"));
            }

        if (course == null) {
            allErrors.add(new JsonPrimitive("invalid course"));
        }

        // only check if course code is valid
        if (course != null && section == null) {
            allErrors.add(new JsonPrimitive("invalid section"));
        }

        if (student == null) {
            allErrors.add(new JsonPrimitive("invalid userid"));
        }

        if (allErrors.size() > 0) {
            reponseObject.addProperty("status", "error");
            reponseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(reponseObject.toString());
            return;
        }

        if (manager.getBiddingRoundStatus().equals("started")) {
            if (round == 2) {
                double minBidAmt = MinBidDAO.getInstance().getValue(courseID + "-" + sectionID);
                System.out.println(courseID + "-" + sectionID);
                System.out.println("UPDATE amount: " + amount);
                System.out.println("UPDATE min bid: " + minBidAmt);
                if (amount < minBidAmt) {
                    errorList.add("bid too low");
                }
            } else if (round == 1){
                if (amount < 10) {
                    errorList.add("bid too low");
                }
            }

            existingBid = BidDAO.getInstance().getStudentBidWithCourseID(userID, courseID);
            //no existing bid
            if (existingBid == null) {
                if (bv.parseEDollarEnough(userID, courseID, sectionID, amt.getAsString()) != null) {
                    errorList.add("insufficient e$");
                }
            } else if (existingBid.getStatus().equals("pending")) {
                if (bv.parseEDollarEnoughExistingBid(userID, courseID, sectionID, amt.getAsString()) != null) {
                    // Think too much alr, this line is not needed
//                    errorList.add("insufficient e$");
                }
            } else if (existingBid.getStatus().equals("success")) {
                errorList.add("course enrolled");
            }

            if (bv.parseClassTimeTableClash(userID, courseID, sectionID) != null) {
                errorList.add("class timetable clash");
            }

            if (bv.parseExamTimeTableClash(userID, courseID, sectionID) != null) {
                errorList.add("exam timetable clash");
            }

            if (bv.parseIncompletePrerequisite(userID, courseID) != null) {
                errorList.add("incomplete prerequisites");
            }

            if (bv.parseAlreadyComplete(userID, courseID) != null) {
                errorList.add("course completed");
            }

            if (bv.parseSectionLimit(userID, courseID, sectionID) != null) {
                errorList.add("section limit reached");
            }

            if (bv.parseNotOwnSchoolCourse(userID, courseID) != null) {
                errorList.add("not own school course");
            }

            ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentListWithID(courseID, sectionID);
            int vacancy = section.getSize() - sectionStudentList.size();
            if (vacancy <= 0) {
                errorList.add("no vacancy");
            }
        } else {
            errorList.add("round ended");
        }

        if (errorList.size() > 0) {
            Collections.sort(errorList);
            for (String s : errorList) {
                allErrors.add(new JsonPrimitive(s));
            }
            reponseObject.addProperty("status", "error");
            reponseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(reponseObject.toString());
            return;
        } else { //success state
            Bid newBid = new Bid(userID, amount, courseID, sectionID, "pending");
            if (existingBid != null) { //there is an existing bid
                MinBidDAO.getInstance().getMinBidList().remove(courseID+"-"+sectionID);
                MinBidDAO.getInstance().refresh();
                BidPlacementServlet.updateCurrentBid(userID, courseID, sectionID, amount, student);
            } else {
                student.seteDollar(student.geteDollar()-amount);
                manager.setEDollar(userID, student.geteDollar());
                BidDAO.getInstance().addBid(newBid);
                manager.addBid(newBid);
                //dk if this is needed
                if(round == 1){
                    RoundOneBidDAO.getInstance().addBid(newBid);
                } else if(round == 2){
                    RoundTwoBidDAO.getInstance().addBid(newBid);
                }
            }
            MinBidDAO.getInstance().refresh();
            reponseObject.addProperty("status", "success");
        }

        response.setContentType("application/json");
        response.getWriter().write(reponseObject.toString());
        return;
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
