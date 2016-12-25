/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller;


import com.bios.exceptions.DataFormattingException;
import com.bios.model.Bid;
import com.bios.model.CompletedCourse;
import com.bios.model.Course;
import com.bios.model.DataError;
import com.bios.model.Prerequisite;
import com.bios.model.Section;
import com.bios.model.Student;
import com.bios.utilities.BootstrapCSVReader;
import com.bios.utilities.ConnectionManager;
import com.bios.utilities.BootstrapUnzipper;
import com.bios.utilities.BootstrapValidator;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

/**
 * A class that uploads and reads the file selected by the user in the bootstrap page
 * @author kongyujian
 */
@MultipartConfig
public class AdminBootstrapServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method and forwards the user's request to bootstrapping page
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/AdminPortal/AdminBootstrap.jsp");
        rd.forward(request, response);
    }


    /**
     * Handles the HTTP <code>POST</code> method by uploading the file into the server and unzipping the contents in the file.
     * It retrieves the description and selected file from request. The file will be read and uploading the file into a zip file,
     * until there are no more bytes to be read. Upon successful unzipping and validation of contents, existing table would be
     * deleted and the new contents from the zip file will updated in the server.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        // This part of code here will retrieve the file from the request
        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream inputStream = filePart.getInputStream();
        ServletContext context = getServletContext();

        //Uploaded to a file called bootstrap.zip
        File f = new File(context.getRealPath("/WEB-INF")+"/bootstrap.zip");
        OutputStream outputStream = new FileOutputStream(f);

        int read = 0;
        byte[] bytes = new byte[1024];
//the total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached
//
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done uploading! Now unzipping!");

        //TODO if the folder structure is invalid, let user know that nothing was uploaded
        //BootstrapValidator validator = new BootstrapValidator();
        ConnectionManager manager = new ConnectionManager();
        manager.deleteAllTables();

        BootstrapUnzipper unzipper = new BootstrapUnzipper();
        unzipper.unzip(context);

        BootstrapCSVReader csvReader = new BootstrapCSVReader();

        ArrayList<DataError> allCourses = csvReader.retrieveCourses(context);
        ArrayList<DataError> allSections = csvReader.retrieveSections(context);
        ArrayList<DataError> allStudents = csvReader.retrieveStudents(context);
        ArrayList<DataError> allPrerequisites = csvReader.retrievePrerequisites(context);
        ArrayList<DataError> allCompletedCourses = csvReader.retrieveCompletedCourses(context);
        ArrayList<DataError> allBids = csvReader.retrieveBids(context);

        int[] totalLines = csvReader.getTotalLines();
        int[] totalErrorLines = csvReader.getTotalErrorLines();

        session.setAttribute("courseErrors", allCourses);
        session.setAttribute("sectionErrors", allSections);
        session.setAttribute("studentErrors", allStudents);
        session.setAttribute("bidErrors", allBids);
        session.setAttribute("prerequisiteErrors", allPrerequisites);
        session.setAttribute("completedCourseErrors", allCompletedCourses);
        session.setAttribute("totalLines", totalLines);
        session.setAttribute("totalErrorLines", totalErrorLines);
        session.setAttribute("tableHeaders", new String[] {"course.csv", "section.csv", "student.csv", "bid.csv", "prerequisite.csv", "course_completed.csv"});

        if (!allCourses.isEmpty() && !allSections.isEmpty() && !allStudents.isEmpty()
                && !allBids.isEmpty() && !allPrerequisites.isEmpty() && !allCompletedCourses.isEmpty()){
            request.getSession().setAttribute("uploadSuccess", "error");
        } else {
            // If uploadSuccess is true, means upload was good, else upload was faulty, if null, upload was never made
            request.getSession().setAttribute("uploadSuccess", "success");
        }

        unzipper.deleteBootstrapFolder(context);
        //TODO we will check to see if there are any errors inside, if got error, we shall return special stuffs :)





        response.sendRedirect(request.getContextPath()+"/bootstrap");
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
