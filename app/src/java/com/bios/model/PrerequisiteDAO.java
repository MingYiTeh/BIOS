/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import java.util.ArrayList;

/**
 * Represents a prerequisiteDAO object that contains all the prerequisites of all the course (if any)
 * @author kongyujian
 */
public class PrerequisiteDAO {

    private static PrerequisiteDAO instance = null;
    private static ArrayList<Prerequisite> prerequisiteList;

    /**
     * @return the instance of a preRequisiteDAO, 
     * if there isn't one, a new prerequisuteList and PrerequisuteDAO will be created and returned
     */
    public static PrerequisiteDAO getInstance() {
        if (instance == null) {
            prerequisiteList = new ArrayList<>();
            instance = new PrerequisiteDAO();
        }
        return instance;
    }
    
    /**
     * Retrieves the list of all the prerequisites
     * @return the prerequisiteList
     */
    public ArrayList<Prerequisite> getPrerequisiteList() {
        return prerequisiteList;
    }

    /**
     * Sets the specified prerequisite to the Prerequisite list
     * @param prerequisiteList the prerequisiteList to set
     */
    public void setPrerequisiteList(ArrayList<Prerequisite> prerequisiteList) {
        this.prerequisiteList = prerequisiteList;
    }
    
    /**
     * Retrieves the Prerequisite object with the specified course ID
     * @param courseID course to search
     * @return prerequisite object with the specified course ID
     */
    public Prerequisite findCourse(String courseID){
        for (Prerequisite pre : prerequisiteList){
            if (pre.getCourseID().equals(courseID)){
                return pre;
            }
        }
        return null;
    }
    
    /**
     * Retrieves the Prerequisite object with the specified prerequisite ID
     * @param prerequisiteID prerequisite to search
     * @return prerequisite object with the specified prerequisite ID
     */
    public Prerequisite findPrerequisite(String prerequisiteID){
        for (Prerequisite pre : prerequisiteList){
            if (pre.getPrerequisiteID().equals(prerequisiteID)){
                return pre;
            }
        }
        return null;
    }
    
    /**
     * Adds the Prerequisite Object to the list of Prerequisite
     * @param preq prerequisite to add
     */
    public void addPrerequisite(Prerequisite preq){
        prerequisiteList.add(preq);
    }
    
}
