/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

/**
 * Represents a Section Student object
 * @author Teh Ming Yi
 */
public class SectionStudent {

    private String courseID;
    private String userID;
    private String sectionID;
    private double amount;

     /**
     * Constructs a SectionStudent with the specified courseID, userID,sectionID,amount
     *
     * @param courseID the course id
     * @param userID the user id
     * @param sectionID the section id
     * @param amount the amount
     *
     */
    public SectionStudent(String courseID, String userID, String sectionID, double amount) {
        this.courseID = courseID;
        this.userID = userID;
        this.sectionID = sectionID;
        this.amount = amount;
    }

    /**
     * Retrieves the course ID
     * @return the Course's ID
     */
    public String getCourseID() {
        return courseID;
    }

     /**
     * Sets the course ID
     *
     * @param courseID course ID to be set
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

      /**
     * Retrieves the ID of the user
     * @return the User's ID
     */
    public String getUserID() {
        return userID;
    }

     /**
     * Sets the user ID
     *
     * @param userID user ID to be set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

     /**
     * Retrieves the ID of the Section
     * @return the Section's ID
     */
    public String getSectionID() {
        return sectionID;
    }

     /**
     * Sets the section ID
     *
     * @param sectionID section ID to be set
     */
    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

     /**
     * Retrieves the amount
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

     /**
     * Sets the amount
     *
     * @param amount e$ amount to be set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }



}
