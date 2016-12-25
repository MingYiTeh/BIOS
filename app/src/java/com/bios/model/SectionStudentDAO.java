/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import com.bios.utilities.ConnectionManager;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a SectionStudentDAO object that contains all the Section Student
 * @author Teh Ming Yi
 */
public class SectionStudentDAO {

    private static SectionStudentDAO instance = null;
    private static ArrayList<SectionStudent> sectionStudentList;

    /**
     * @return the instance of a SectionStudentDAO, if there isn't one, a new
     * sectionStudentList and SectionStudentDAO will be created and returned
     */
    public static SectionStudentDAO getInstance() {
        if (instance == null) {
            sectionStudentList = new ArrayList<>();
            instance = new SectionStudentDAO();
        }
        return instance;
    }

    /**
     * Retrieves the list of the section student
     * @return the section student's list
     */
    public ArrayList<SectionStudent> getSectionStudentList() {
        return sectionStudentList;
    }

    /**
     * Retrieves the section student list with the specified Course ID and section ID
     * @param courseID the course ID
     * @param sectionID the section ID
     * @return the section student list with the specified Course ID and section ID
     */
    public ArrayList<SectionStudent> getSectionStudentListWithID(String courseID, String sectionID) {
        ArrayList<SectionStudent> list = new ArrayList<>();
        for (SectionStudent sectionStudent : sectionStudentList) {
            if (sectionStudent.getCourseID().equals(courseID) && sectionStudent.getSectionID().equals(sectionID)) {
                list.add(sectionStudent);
            }
        }
        return list;
    }

    /**
     * Retrieves the section student with the specified Course ID, section ID and user ID
     * @param courseID the course ID
     * @param sectionID the section ID
     * @param userID the user ID
     * @return the section student with the specified Course ID, section ID and user ID, null otherwise
     */
    public SectionStudent findSectionStudent(String courseID, String sectionID, String userID) {
        for (SectionStudent sectionStudent : sectionStudentList) {
            if (sectionStudent.getCourseID().equals(courseID)
                    && sectionStudent.getSectionID().equals(sectionID) && sectionStudent.getUserID().equals(userID)) {
                return sectionStudent;
            }
        }
        return null;
    }

    /**
     * Sets the list of all the section student
     * @param aSectionStudentList the section student list to set
     */
    public static void setSectionStudentList(ArrayList<SectionStudent> aSectionStudentList) {
        sectionStudentList = aSectionStudentList;
    }

    /**
     * Adds the section student to the list of section student offered
     * @param sectionStudent section student to add
     */
    public void addSectionStudent(SectionStudent sectionStudent) {
        sectionStudentList.add(sectionStudent);
    }

    /**
    * Deletes the Bid with the specified user ID, amount, courseID and sectionID from the database
    *
    * @param userID User ID of the Student
    * @param amount the bid amount
    * @param courseID course ID of the Bid
    * @param sectionID section ID of the Bid
    */
    public void deleteStudentSectionFromDB(String userID, double amount, String courseID, String sectionID) {
        ConnectionManager manager = new ConnectionManager();
        manager.deleteSectionStudent(userID, amount, courseID, sectionID);
    }

    /**
    * Deletes the Bid with the specified user ID, amount, courseID and sectionID from the SectionStudentDAO
    *
    * @param userID User ID of the Student
    * @param amount the bid amount
    * @param courseID course ID of the Bid
    * @param sectionID section ID of the Bid
    */
    public void deleteStudentSection(String userID, double amount, String courseID, String sectionID) {
        if (sectionStudentList != null) {
            Iterator iter = sectionStudentList.iterator();
            while (iter.hasNext()) {
                SectionStudent ss = (SectionStudent) iter.next();
                if (ss.getAmount() == amount && ss.getCourseID().equals(courseID) && ss.getSectionID().equals(sectionID)
                        && ss.getUserID().equals(userID)) {
                    iter.remove();

                }
            }
        }
    }

    /**
    * Retrieves section student with a user ID from the sectionStudentDAO
    *
    * @param userID User ID of the Student
    *
    * @return the specific section Student with the specified User ID, null otherwise
    */
    public SectionStudent findSectionByUser(String userID) {

        if (sectionStudentList != null) {
            for (SectionStudent sectionStudent : sectionStudentList) {
                if (sectionStudent.getUserID().equals(userID)) {
                    return sectionStudent;
                }
            }
        }
        return null;
    }

}
