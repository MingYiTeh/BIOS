/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

/**
 * Represents a CompletedCourse Object
 * @author kongyujian
 */
public class CompletedCourse {

    private String userID;
    private String courseID;

     /**
     * Constructs a CompletedCourse with the specified userID and courseID
     * @param userID the user's ID
     * @param courseID the course's ID
     */
    public CompletedCourse(String userID, String courseID) {
        this.userID = userID;
        this.courseID = courseID;
    }
    
     /**
     * Retrieves the ID of the Student
     * @return the specified User ID
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
     * Retrieves the Course ID the Student has completed
     * @return the course's ID
     */
    public String getCourseID() {
        return courseID;
    }
    
    /**
     * Sets the Course ID the Student has completed
     * @param courseID the course's ID to set
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

}
