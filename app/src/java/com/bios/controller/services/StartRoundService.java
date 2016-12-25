/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.controller.BiddingRoundServlet;
import com.bios.utilities.ConnectionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a JSON Web Service for Start Round
 * @author Marc
 */
public class StartRoundService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method which starts a bidding round. It checks that the user is a verified admin
     * and proceeds to start the round if the round is not yet started. Subsequently, it creates a json object with the status
     * "success" and stores the current bidding round. However, if round 2 has already ended, it creates a json object with the
     * status "error" and stores the error message "round 2 ended" in a json array. Additionally, if the user is not a verified admin with a valid
     * token, it creates a json object with the status "error" and stores the error message in a json array "invalid username/token".
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //      String token = request.getParameter("token");

        JsonObject object = new JsonObject();
        if (request.getParameter("token") == null) {
            JsonArray array = new JsonArray();
            object.addProperty("status", "error");
            array.add(new JsonPrimitive("missing token"));
            object.add("message", array);

        } else if (request.getParameter("token").length()==0){


                JsonArray array = new JsonArray();
                object.addProperty("status", "error");
                array.add(new JsonPrimitive("blank token"));
                object.add("message", array);


        }else{
    String token = request.getParameter("token");
        try {
            String result = JWTUtility.verify(token, "abcdefgh12345678");
            if (result.equals("admin")) {

                JsonArray array = new JsonArray();
                ConnectionManager manager = new ConnectionManager();
                if (manager.getBiddingRoundStatus().equals("started")) {
                    // Stop the round here
                    object.addProperty("status", "success");
                    object.add("round", new JsonPrimitive(manager.getBiddingRound()));
                } else if (manager.getBiddingRound() == 1) {
                    BiddingRoundServlet.incrementBiddingRound("true");
                    object.addProperty("status", "success");
                    object.add("round", new JsonPrimitive(manager.getBiddingRound()));

                } else {
                    object.addProperty("status", "error");
                    array.add(new JsonPrimitive("round 2 ended"));
                    object.add("message", array);
                }
            }
        } catch (JWTException e) {
            // This means not an admin, or token expired
            JsonArray array = new JsonArray();
            JsonPrimitive value = new JsonPrimitive("invalid token");
            array.add(value);
            object.addProperty("status", "error");
            object.add("message", array);
        }
    }

    System.out.println (object);

    response.setContentType (

    "application/json");
    response.getWriter ()

.write(object.toString());
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */

    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
