/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

/**
 * Represents a Section Object
 *
 * @author kongyujian
 */
public class Section {

    private String courseID;
    private String sectionID;
    private int day;
    private String start;
    private String end;
    private String instructor;
    private String venue;
    private int size;

    /**
     * Constructs a Section with the specified courseID, sectionID,
     * day,start,end,instructor,venue,size
     *
     * @param courseID the course's ID
     * @param sectionID the section's ID
     * @param day the day
     * @param start the start
     * @param end the end
     * @param instructor the instructor
     * @param venue the venue
     * @param size the size
     */
    public Section(String courseID, String sectionID, int day, String start, String end, String instructor, String venue, int size) {
        this.courseID = courseID;
        this.sectionID = sectionID;
        this.day = day;
        this.start = start;
        this.end = end;
        this.instructor = instructor;
        this.venue = venue;
        this.size = size;
    }

    /**
     * Retrieves the courseID
     *
     * @return the courseID
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Sets the specified Course ID to Section Object
     *
     * @param courseID the courseID to set
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    /**
     * Retrieves the Section ID
     *
     * @return the sectionID
     */
    public String getSectionID() {
        return sectionID;
    }

    /**
     * Sets the specified Section ID to Section Object The first character
     * should be an S followed by a positive numeric number (1-99)
     *
     * @param sectionID the sectionID to set
     */
    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    /**
     * Retrieves the day of the week of the section
     *
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day of the week of the section This must be a number between
     * 1(inclusive) and 7 (inclusive). 1 - Monday, 2 - Tuesday, ... , 7 -
     * Sunday.
     *
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Retrieves the start time of the class
     *
     * @return the start time
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the start time of the class Time should be in the format H:mm (8:30,
     * 12:00, 15:30)
     *
     * @param start the start time to set
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * Retrieves the end time of the class
     *
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the start time of the class Time should be later than the start
     * time. (11:45, 15:15, 18:45)
     *
     * @param end the end time to set
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Retrieves the instructor of the section
     *
     * @return the instructor
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     * Sets the instructor to the section. String venue should not be longer
     * than 100 characters.
     *
     * @param instructor the instructor to set
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    /**
     * Retrieves the venue where the class is going to be conducted
     *
     * @return the venue
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Sets the venue where the class is going to be conducted String venue
     * should not be longer than 100 characters.
     *
     * @param venue the venue to set
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Retrieves the size of the class
     *
     * @return the size of the class
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the class Size should be a positive numeric number
     *
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Checks if the current section's timetable clashes with another section's
     * timetable
     *
     * @param another another section
     * @return true if timetable clashes; false if timetable does not clash
     */
    public boolean clashes(Section another) {
        String anotherStart = another.getStart();
        String anotherEnd = another.getEnd();
        int anotherStartHour = Integer.parseInt(anotherStart.substring(0, anotherStart.indexOf(":")));
        int anotherStartMinutes = Integer.parseInt(anotherStart.substring(anotherStart.indexOf(":") + 1, anotherStart.length()));
        int anotherEndHour = Integer.parseInt(anotherEnd.substring(0, anotherEnd.indexOf(":")));
        int anotherEndMinutes = Integer.parseInt(anotherEnd.substring(anotherEnd.indexOf(":") + 1, anotherEnd.length()));

        String start = getStart();
        String end = getEnd();
        int startHour = Integer.parseInt(start.substring(0, start.indexOf(":")));
        int startMinutes = Integer.parseInt(start.substring(start.indexOf(":") + 1, start.length()));
        int endHour = Integer.parseInt(end.substring(0, end.indexOf(":")));
        int endMinutes = Integer.parseInt(end.substring(end.indexOf(":") + 1, end.length()));

        if (getDay() != another.getDay()) {
            return false;
        } else if (anotherStartHour != startHour || anotherStartMinutes != startMinutes) {
            if (anotherEndHour == startHour && anotherEndMinutes < startMinutes) {
                return false;
            }
            if (endHour == anotherStartHour && endMinutes < anotherStartMinutes) {
                return false;
            }
            if (endHour < anotherStartHour || startHour > anotherEndHour) {
                return false;
            }
        }
        return true;

    }

}
