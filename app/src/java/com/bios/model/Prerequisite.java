package com.bios.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Represents a Prerequisite Object
 * @author kongyujian
 */
public class Prerequisite {

    private String courseID;
    private String prerequisiteID;

    /**
     * Constructs a prerequisite with the specified courseID and prerequisiteID
     *
     * @param courseID the course's ID
     * @param prerequisiteID the prerequisite's ID
     */
    public Prerequisite(String courseID, String prerequisiteID) {
        this.courseID = courseID;
        this.prerequisiteID = prerequisiteID;
    }

    /**
     * Retrieves the Course ID
     * @return the courseID
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Sets the Course ID specified to the Course ID
     * @param courseID the courseID to set
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
    
    
    /**
     * Retrieves the Prerequisites ID
     * @return the prerequisiteID
     */
    public String getPrerequisiteID() {
        return prerequisiteID;
    }

    /**
     * Sets the prerequisiteID specified to the prerequisite ID
     * @param prerequisiteID the prerequisiteID to set
     */
    public void setPrerequisiteID(String prerequisiteID) {
        this.prerequisiteID = prerequisiteID;
    }

}
