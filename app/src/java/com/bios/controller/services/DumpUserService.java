/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Has Nilofar
 * This is the class that allows an administrator to retrieve the information of a specific user
 * This web service will allow an administrator to retrieve the information for a section, and it's enrolled students. During round 2,
 * this should return the enrolled students bidded successfully in round 1. After round 2 is closed, this should return the enrolled
 * students who bidded successfully in round 1 & 2.
 * 
 */
package com.bios.controller.services;

import com.bios.model.Student;
import com.bios.model.StudentDAO;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a JSON Web Service for Dump User details
 * @author Teh Ming Yi
 */
public class DumpUserService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
    * Handles the HTTP <code>GET</code> method which takes in the userid of a
    * user and a token of an administrator. After the userid of the student and
    * token of the administrator is passed in, the method first verifies that
    * the token is valid. If the token is invalid, a json object is created
    * with the status "error" and an error message "invalid username/token".
    * Subsequently, if the token is valid, the method checks the respective
    * DAOs and retrieves the respective information of the user and places it
    * in the json object. If an error occurs, such as a missing input, the json
    * object will also reflect the error without displaying the user's
    * information.
     * @param request servlet requesty
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

        JsonElement userID = json.get("userid");

        if (userID == null) {
            errorList.add("missing userid");
        } else if (userID.getAsString().length() == 0) {
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

        Student s = StudentDAO.getInstance().retrieve(userID.getAsString());

        if (s == null) {
            allErrors.add(new JsonPrimitive("invalid userid"));
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrors);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        } else {
            responseObject.addProperty("status", "success");
            responseObject.addProperty("userid", s.getId());
            responseObject.addProperty("password", s.getPassword());
            responseObject.addProperty("name", s.getNameOfUser());
            responseObject.addProperty("school", s.getSchool());
            DecimalFormat df = new DecimalFormat("0.00");
            String result = df.format(s.geteDollar());
            double eDollar = Double.parseDouble(result);
            responseObject.addProperty("edollar", eDollar);
        }

        response.setContentType(
                "application/json");
        response.getWriter()
                .write(responseObject.toString());
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
