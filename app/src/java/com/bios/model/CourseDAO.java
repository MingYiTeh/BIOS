/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import java.util.ArrayList;

/**
 * Represents a Course Data Access Object that contains all the courses
 * @author kongyujian
 */
// This CourseDAO object uses a Singleton pattern
public class CourseDAO {

    private static CourseDAO instance = null;
    private static ArrayList<Course> courseList;

    /**
     * @return the instance of a courseDAO, 
     * if there isn't one, a new courseList and CourseDAO will be created and returned
     */
    public static CourseDAO getInstance() {
        if (instance == null) {
            courseList = new ArrayList<>();
            instance = new CourseDAO();
        }
        return instance;
    }
    
    /**
     * Retrieves the list of all the courses offered
     * @return the course list
     */
    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    /**
     * Sets the list of all the courses offered
     * @param courseList the course list to set
     */
    public void setCourseList(ArrayList<Course> courseList) {
        CourseDAO.courseList = courseList;
    }

    // Will return null if no course is found
    /**
     * Retrieves the Course with the specified Course ID
     * @param courseID of the course to be returned
     * @return the Course with the specified Course ID, null otherwise
     */
    public Course findCourse(String courseID) {
        Course result = null;
        for (Course course : courseList) {
            if (course.getCourseID().equals(courseID)) {
                return course;
            }
        }
        return result;
    }
    
    /**
     * Adds the course to the list of courses offered
     * @param course to add
     */
    public void addCourse(Course course){
        courseList.add(course);
    }
    
}
