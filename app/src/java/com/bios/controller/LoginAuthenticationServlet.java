/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller;

import com.bios.model.Student;
import com.bios.utilities.ConnectionManager;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A class that authenticates the User ID and the password entered by the user
 * @author Teh Ming Yi
 */
public class LoginAuthenticationServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method and forwards the user's request to login page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/GlobalPages/login.jsp");
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method by checking the validity of the User ID entered. It also checks
     * if both the User ID and password matches. Upon successful authentication, a JWT Utility token is created and stored
     * in a session and the user is redirected to his or her respective homepage.
     * However, if the authentication fails, the user is redirected to the login page
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession(true);
            String userId = request.getParameter("userId");
            String password = request.getParameter("password");



            try {
                ConnectionManager manager = new ConnectionManager();
                Connection connection = manager.getConnection();
                String sql = "select * from bios.student where UserID=?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (userId.equals("admin") && password.equals("admin")){
                    String token = JWTUtility.sign("abcdefgh12345678",userId);
                    session.setAttribute("token", token);
                    session.setAttribute("username", "admin");
                    response.sendRedirect(request.getContextPath()+"/admin_home");
                    return;
                }

                while (rs.next()){
                    String serverUsername = rs.getString("UserID");
                    String serverPassword = rs.getString("UserPassword");
                    String name = rs.getString("NameOfUser");
                    String school = rs.getString("School");
                    double eDollar = rs.getDouble("EDollar");

                    if(userId.equals(serverUsername) && password.equals(serverPassword)){
                        String token = JWTUtility.sign("abcdefgh12345678",userId);
                        session.setAttribute("token", token);
                        session.setAttribute("username", serverUsername);
                        session.setAttribute("authenticated.student", new Student(serverUsername, serverPassword, name, school, eDollar));
                        response.sendRedirect(request.getContextPath()+"/student_home");
                        return;
                    } else{
                        session.setAttribute("loginErrorMsg", "username/password is incorrect");
                        response.sendRedirect(request.getContextPath()+"/login");
                        return;
                    }
                }

                if (!rs.next()){
                    session.setAttribute("loginErrorMsg", "username/password is incorrect");
                    response.sendRedirect(request.getContextPath()+"/login");
                    return;
                }
            } catch (SQLException e){
                e.printStackTrace();
                // This means that there was a connection error to database
            }




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
