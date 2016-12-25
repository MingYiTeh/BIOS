/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a Course object
 *
 * @author kongyujian
 */
public class Course {

    private String courseID;
    private String school;
    private String title;
    private String description;
    private String examDate;
    private String examStart;
    private String examEnd;

    /**
     * Constructs a Course with the specified course, school, title, description
     *
     * @param course the course ID
     * @param school the school
     * @param title the title of the course
     * @param description the description
     * @param examDate the examDate
     * @param examStart the examStart
     * @param examEnd the examEnd
     */
    public Course(String course, String school, String title, String description,
            String examDate, String examStart, String examEnd) {
        this.courseID = course;
        this.school = school;
        this.title = title;
        this.description = description;
        this.examDate = examDate;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }

    /**
     * Retrieves the Course ID
     *
     * @return the course ID
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Sets the course ID for the course
     *
     * @param course the course ID to set
     */
    public void setCourse(String course) {
        this.courseID = course;
    }

    /**
     * Retrieves the school which offers the course
     *
     * @return the school
     */
    public String getSchool() {
        return school;
    }

    /**
     * Sets the school which offers the course
     *
     * @param school the school to set
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Retrieves the title of the course
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the course
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the course description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the course description for the course
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the date of the examination
     *
     * @return the examDate
     */
    public String getExamDate() {
        return examDate;
    }

    /**
     * Set the date of the examination
     *
     * @param examDate the examDate to set
     */
    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    /**
     * Retrieves the start time of the exam
     *
     * @return the examStart
     */
    public String getExamStart() {
        return examStart;
    }

    /**
     * Sets the start time of the exam
     *
     * @param examStart the examStart to set
     */
    public void setExamStart(String examStart) {
        this.examStart = examStart;
    }

    /**
     * Retrieves the end time of the exam
     *
     * @return the examEnd
     */
    public String getExamEnd() {
        return examEnd;
    }

    /**
     * sets the end time of the exam
     *
     * @param examEnd the examEnd to set
     */
    public void setExamEnd(String examEnd) {
        this.examEnd = examEnd;
    }

    /**
     * Checks if the current course's exam timetable clashes with another
     * course's exam timetable
     *
     * @param another another course
     * @return true if exam timetable clashes; false if exam timetable does not
     * clash
     */
    public boolean examClashes(Course another) {
        String anotherStart = another.getExamStart();
        String anotherEnd = another.getExamEnd();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        try {
            Date anotherStartDate = dateFormat.parse(anotherStart);
            Date anotherEndDate = dateFormat.parse(anotherEnd);
            String start = getExamStart();
            String end = getExamEnd();
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            if (getExamDate().equals(another.getExamDate())) {
                if (anotherStartDate.before(startDate) && anotherEndDate.before(startDate)) {
                    //System.out.println("wont clash");
                    return false;
                } else if (anotherStartDate.after(endDate) && anotherEndDate.after(endDate)) {
                    //System.out.println("wont clash");
                    return false;
                } else {
                    //System.out.println("clash");
                    return true;
                }
            } else{
                return false;
            }

        } catch (ParseException e) {
            return true;
        }
    }

    /**
     * Checks if there are any prerequisites for this course and return the
     * prerequisites in an ArrayList
     *
     * @return an ArrayList of Prerequisites for this course. If no
     * prerequisites for this course, and empty ArrayList will be returned
     */
    public ArrayList<Prerequisite> getPrerequisiteList() {
        ArrayList<Prerequisite> result = new ArrayList<>();
        ArrayList<Prerequisite> preList = PrerequisiteDAO.getInstance().getPrerequisiteList();
        for (Prerequisite prereq : preList) {
            if (prereq.getCourseID().equals(courseID)) {
                result.add(prereq);
            }
        }
        return result;
    }
}
