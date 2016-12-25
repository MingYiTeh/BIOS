/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

/**
 *Represents a Bid Object
 * @author kongyujian
 */
public class Bid {
    private String userID;
    private double amount;
    private String courseID;
    private String sectionID;
    private String status;

    /**
     * Constructs a Bid with the specified userID, amount, courseID,sectionID
     * @param userID the Student's ID
     * @param amount the Student has bid
     * @param courseID the Course's ID
     * @param sectionID the Section's ID
     * @param status the status of bid
     */
    //bug 4
    public Bid(String userID, double amount, String courseID, String sectionID, String status){
        this.userID = userID;
        this.amount = amount;
        this.courseID = courseID;
        this.sectionID = sectionID;
        this.status = status;
    }

    /**
     * Retrieves the ID of the Student
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the Student
     * @param userID the user's ID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Retrieves the amount the Student has bid.
     * @return the bid amount
     */
    public double getAmount() {
        return amount;
    }



    /**
     * @param amount the amount to set
     * Sets the bid amount of the Student
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Retrieves the status of the bid
     * @return the status
     */
    public String getStatus(){
        return status;
    }

    /**
     * Sets the status of the bid
     * @param status the status to set
     */
    public void setStatus(String status){
        this.status =status;
    }

    /**
     * Retrieves the Course ID the Student has bid
     * @return the course's ID
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Sets the Course ID the Student has bid
     * @param courseID the course's ID to set
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    /**
     * Retrieves the Section ID the Student has bid
     * @return the sectionID
     */
    public String getSectionID() {
        return sectionID;
    }

    /**
     * Sets the Section ID the Student has bid
     * @param sectionID the sectionID to set
     */
    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    /**
     * Retrieves the student and bid details
     * @return the string details
     */
    @Override
    public String toString() {
        return "Bid{" + "userID=" + userID + ", amount=" + amount + ", courseID=" + courseID + ", sectionID=" + sectionID + ", status=" + status + '}';
    }



}
