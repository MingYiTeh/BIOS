/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.model.StudentDAO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import is203.JWTUtility;
import javax.servlet.http.HttpSession;

/**
 * Represents a JSON Web Service for Login Authentication
 * @author Teh Ming Yi
 */
public class AuthenticationService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>POST</code> method by checking the validity of the
     * User ID entered in the URL. It also checks if both the User ID and
     * password of admin matches. An authentication is successful when a valid
     * token is created. However, if the authentication fails, error message is
     * printed.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs This is for the web services
     * for authentication Service
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JsonObject object = new JsonObject();
        HttpSession session = request.getSession();
        
        
        
        if (username == null || username.length() == 0) {
            JsonArray array = new JsonArray();
            JsonPrimitive value = new JsonPrimitive("blank username");
            array.add(value);
            object.addProperty("status", "error");
            object.add("message", array);
            System.out.println(object);
            response.setContentType("application/json");
            response.getWriter().write(object.toString());
            return;
        }

        if (password == null || password.length() == 0) {
            JsonArray array = new JsonArray();
            JsonPrimitive value = new JsonPrimitive("blank password");
            array.add(value);
            object.addProperty("status", "error");
            object.add("message", array);
            System.out.println(object);
            response.setContentType("application/json");
            response.getWriter().write(object.toString());
            return;
        }
        
        if (username.equals("admin") && password.equals("admin")) {
            // Grant access and return token

            String token = JWTUtility.sign("abcdefgh12345678", username);
            object.addProperty("status", "success");
            object.addProperty("token", token);
            session.setAttribute("token", token);
        } else {
            JsonArray array = new JsonArray();
            JsonPrimitive value = new JsonPrimitive("invalid username/password");
            array.add(value);
            object.addProperty("status", "error");
            object.add("message", array);
        }

        System.out.println(object);
        response.setContentType("application/json");
        response.getWriter().write(object.toString());

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
