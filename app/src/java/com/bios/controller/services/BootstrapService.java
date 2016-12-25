/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.controller.services;

import com.bios.controller.BiddingRoundServlet;
import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.DataError;
import com.bios.model.MinBidDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import com.bios.utilities.BidComparator;
import com.bios.utilities.BootstrapCSVReader;
import com.bios.utilities.BootstrapUnzipper;
import com.bios.utilities.ConnectionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import is203.JWTException;
import is203.JWTUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/** 
 * Represents a JSON Web Service for Bootstrap
 * @author Teh Ming Yi
 */
@MultipartConfig
public class BootstrapService extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
    * Handles the HTTP <code>POST</code> method to perform the bootstrap function.
    * Firstly, the method checks for a valid token. If there are any errors such
    * as an valid token or a missing bootstrap file, a json object with the status
    * "error" is created together with a json array of the errors that caused it.
    * Else if the token is valid and there are no errors, the method then
    * then unzips the zip file and reads the csv files to upload the relevant data
    * into the respective DAOs and database. While it does this, ArrayLists are used
    * to keep track of the errors such as invalid inputs or bids that cannot be bidded
    * for in round 1. If these errors exist, then the json object would contain the
    * status "error" and store the errors. Otherwise, the json object will have the
    * status "success" to signifiy that the bootstrap was successful with no errors.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        Part filePart = request.getPart("bootstrap-file"); // Retrieves <input type="file" name="file">

        JsonObject responseObject = new JsonObject();
        JsonArray allErrorsArr = new JsonArray();

        if (token == null) {
            allErrorsArr.add(new JsonPrimitive("missing token"));
        }

        if (filePart == null) {
            allErrorsArr.add(new JsonPrimitive("missing bootstrap-file"));
        }

        if (allErrorsArr.size() > 0) {
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrorsArr);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        }

        if (token.length() == 0) {
            allErrorsArr.add(new JsonPrimitive("blank token"));
        }

        if (filePart.getSize() == 0) {
            allErrorsArr.add(new JsonPrimitive("blank bootstrap-file"));
        }

        if (allErrorsArr.size() > 0) {
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrorsArr);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        }

        try {
            String result = JWTUtility.verify(token, "abcdefgh12345678");
            if (!result.equals("admin")) {
                JsonPrimitive value = new JsonPrimitive("invalid token");
                allErrorsArr.add(value);
                responseObject.addProperty("status", "error");
                responseObject.add("message", allErrorsArr);
                response.setContentType("application/json");
                response.getWriter().write(responseObject.toString());
                return;
            }
        } catch (JWTException e) {
            // This means not an admin, or token expired
            JsonPrimitive value = new JsonPrimitive("invalid username/token");
            allErrorsArr.add(value);
            responseObject.addProperty("status", "error");
            responseObject.add("message", allErrorsArr);
            response.setContentType("application/json");
            response.getWriter().write(responseObject.toString());
            return;
        }

        InputStream inputStream = filePart.getInputStream();
        ServletContext context = getServletContext();

        //Uploaded to a file called bootstrap.zip
        File f = new File(context.getRealPath("/WEB-INF") + "/bootstrap.zip");
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
        ArrayList<ArrayList<DataError>> listOfErrors = new ArrayList<>();
        listOfErrors.add(allBids);
        listOfErrors.add(allCourses);
        listOfErrors.add(allCompletedCourses);
        listOfErrors.add(allPrerequisites);
        listOfErrors.add(allSections);
        listOfErrors.add(allStudents);

        JsonObject object = new JsonObject();

        if (!allCourses.isEmpty() || !allSections.isEmpty() || !allStudents.isEmpty()
                || !allBids.isEmpty() || !allPrerequisites.isEmpty() || !allCompletedCourses.isEmpty()) {
            object.add("status", new JsonPrimitive("error"));
        } else {
            object.add("status", new JsonPrimitive("success"));
        }

        int[] totalLines = csvReader.getTotalLines();
        int[] totalErrorLines = csvReader.getTotalErrorLines();
        String[] allHeaders = new String[]{"bid.csv", "course.csv", "course_completed.csv", "prerequisite.csv", "section.csv", "student.csv"};

        JsonObject item = new JsonObject();
        JsonArray itemArr = new JsonArray();

        item.add("bid.csv", new JsonPrimitive(totalLines[3] - totalErrorLines[3]));
        itemArr.add(item);
        item = new JsonObject();
        item.add("course.csv", new JsonPrimitive(totalLines[0] - totalErrorLines[0]));
        itemArr.add(item);
        item = new JsonObject();
        item.add("course_completed.csv", new JsonPrimitive(totalLines[5] - totalErrorLines[5]));
        itemArr.add(item);
        item = new JsonObject();
        item.add("prerequisite.csv", new JsonPrimitive(totalLines[4] - totalErrorLines[4]));
        itemArr.add(item);
        item = new JsonObject();
        item.add("section.csv", new JsonPrimitive(totalLines[1] - totalErrorLines[1]));
        itemArr.add(item);
        item = new JsonObject();
        item.add("student.csv", new JsonPrimitive(totalLines[2] - totalErrorLines[2]));
        itemArr.add(item);

        object.add("num-record-loaded", itemArr);

        LinkedHashMap<String, ArrayList<DataError>> allErrorMsgs = new LinkedHashMap<>();
        ArrayList<JsonObject> allErrorsArrList = new ArrayList<>();

        JsonArray allErrors = new JsonArray();
        //System.out.println(object.get("status").getAsString().equals("error"));
        if (object.get("status").getAsString().equals("error")) {

            for (int i = 0; i < allHeaders.length; i++) {

                ArrayList<DataError> list = listOfErrors.get(i);
                for (int j = 0; j < list.size(); j++) {
                    DataError de = list.get(j);


                    if (!allErrorMsgs.containsKey(de.getLineNum()+"")){
                        ArrayList<DataError> l = new ArrayList<>();
                        l.add(de);
                        allErrorMsgs.put(de.getLineNum()+"", l);
                    } else {
                        allErrorMsgs.get(de.getLineNum()+"").add(de);
                    }
                }

                Iterator<Entry<String, ArrayList<DataError>>> iter = allErrorMsgs.entrySet().iterator();
                while (iter.hasNext()){
                    Entry<String, ArrayList<DataError>> m = iter.next();
                    ArrayList<DataError> errorList = m.getValue();
                    JsonObject error = new JsonObject();

                    error.add("file", new JsonPrimitive(allHeaders[i]));

                    JsonArray errMsgs = new JsonArray();

                    for (DataError err: errorList){
                        error.add("line", new JsonPrimitive(err.getLineNum()));
                        errMsgs.add(new JsonPrimitive(err.getDescription()));
                    }

                    error.add("message", errMsgs);
                    allErrors.add(error);
                }
                allErrorMsgs = new LinkedHashMap<>();
                
            }


        }

        if (allErrors.size() > 0) {
            object.add("error", allErrors);
        }
        MinBidDAO.getInstance().drop();
        MinBidDAO.getInstance().refresh();


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
