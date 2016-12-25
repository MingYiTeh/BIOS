/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import java.util.ArrayList;

/**
 * Represents a CompletedCourse Data Access Object that contains all the completedCourses
 * @author kongyujian
 */
public class CompletedCourseDAO {

    private static CompletedCourseDAO instance = null;
    private static ArrayList<CompletedCourse> completedCourseList;


    /**
     * @return the instance of a completedCourseDAO,
     * if there isn't one, a new completedCourseList and CompletedCourseDAO will be created and returned
     */

    public static CompletedCourseDAO getInstance() {
        if (instance == null) {
            completedCourseList = new ArrayList<>();
            instance = new CompletedCourseDAO();
        }
        return instance;
    }

    /**
     * Retrieves the list of the completed courses
     * @return the CompletedCourse's list
     */
    public ArrayList<CompletedCourse> getCompletedCourseList() {
        return completedCourseList;
    }

    /**
     * Sets completedCourseList as the list which contains all the completed courses
     * @param completedCourseList the completedCourseList to set
     */
    public void setCompletedCourseList(ArrayList<CompletedCourse> completedCourseList) {
        this.completedCourseList = completedCourseList;
    }

    /**
     * Retrieves the Completed Course with the specified Course ID
     * @param courseID the course ID
     * @return the completedCourse with the specified Course ID, null otherwise
     */
    public CompletedCourse findCompletedCourse(String courseID){
        for (CompletedCourse course : completedCourseList){
            if (course.getCourseID().equals(courseID)){
                return course;
            }
        }
        return null;
    }

    /**
     * Adds the Course to the list of completed courses
     * @param course to add
     */
    public void addCompletedCourse(CompletedCourse course){
        completedCourseList.add(course);
    }

    /**
     * Retrieves a course which a student has completed, for a given course ID
     * @param userID the user ID
     * @param courseID the course ID
     * @return the completedCourse with the specified Course ID, null otherwise
     */
    public CompletedCourse findStudentCompletedCourseForID(String userID, String courseID){
        for (CompletedCourse course : completedCourseList){
            if (course.getUserID().equals(userID) && course.getCourseID().equals(courseID)){
                return course;
            }
        }
        return null;
    }

}
