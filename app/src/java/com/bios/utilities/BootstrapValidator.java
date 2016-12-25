/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.SectionDAO;
//import com.bios.exceptions.*;
import com.bios.model.Bid;
import com.bios.model.Course;
import com.bios.model.BidDAO;
import com.bios.model.CompletedCourse;
import com.bios.model.CompletedCourseDAO;
import com.bios.model.CourseDAO;
import com.bios.model.Prerequisite;
import com.bios.model.PrerequisiteDAO;
import com.bios.model.Section;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a Bootstrap Validator object
 * @author Kong Yu Jian
 */
public class BootstrapValidator {

    public BootstrapValidator() {

    }

    /**
    * Santitizes white spaces, stripping off all instances of white spaces
    *
    * @param input the input to be santized
    * @return the sanitized String
    */
    public String sanitizeWhitespaces(String input) {
        return input.trim();
    }

    /**
    * Retrieves an ArrayList of errors when validating details of a course that is passed into the method
    *
    * @param courseID the course ID
    * @param school the school that the course belongs to
    * @param courseExamDate the course exam date
    * @param courseExamStart the course exam start
    * @param courseExamEnd the course exam end
    * @param courseTitle the course title
    * @param courseDesc the course description
    * @return an ArrayList of String that represents errors during validation of course
    */
    public ArrayList<String> validateCourses(String courseID, String school, String courseTitle,
            String courseDesc, String courseExamDate, String courseExamStart, String courseExamEnd) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (courseID == null || courseID.length() == 0) {
            addExceptionToList(allExceptions, "blank course");
        }

        if (school == null || school.length() == 0) {
            addExceptionToList(allExceptions, "blank school");
        }

        if (courseTitle == null || courseTitle.length() == 0) {
            addExceptionToList(allExceptions, "blank title");
        }

        if (courseDesc == null || courseDesc.length() == 0) {
            addExceptionToList(allExceptions, "blank description");
        }

        if (courseExamDate == null || courseExamDate.length() == 0) {
            addExceptionToList(allExceptions, "blank exam date");
        }

        if (courseExamStart == null || courseExamStart.length() == 0) {
            addExceptionToList(allExceptions, "blank exam start");
        }

        if (courseExamEnd == null || courseExamEnd.length() == 0) {
            addExceptionToList(allExceptions, "blank exam end");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code1 = parseExamDate(courseExamDate);
        String code2 = parseExamStart(courseExamStart);
        String code3 = null;
        if(code2==null){
        code3 = parseExamEnd(courseExamStart, courseExamEnd);
        }
        String code4 = parseTitle(courseTitle);
        String code5 = parseDescription(courseDesc);

        addExceptionToList(allExceptions, code1);
        addExceptionToList(allExceptions, code2);
        addExceptionToList(allExceptions, code3);
        addExceptionToList(allExceptions, code4);
        addExceptionToList(allExceptions, code5);

        return allExceptions;
    }

    /**
     * Retrieves an ArrayList of errors when validating details of a section that is passed into the method
     * 
     * @param courseID the course ID of the section
     * @param sectionID the section ID 
     * @param day the section class day
     * @param start the section class start timing
     * @param end the section class end timing
     * @param instructor the section instructor
     * @param venue the section venue
     * @param size the section size
     * 
     * @return an ArrayList of String that represents errors during validation of course
     */
    public ArrayList<String> validateSection(String courseID, String sectionID,
            String day, String start, String end, String instructor, String venue, String size) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (courseID == null || courseID.length() == 0) {
            addExceptionToList(allExceptions, "blank course");
        }

        if (sectionID == null || sectionID.length() == 0) {
            addExceptionToList(allExceptions, "blank section");
        }

        if (day == null || day.length() == 0) {
            addExceptionToList(allExceptions, "blank day");
        }

        if (start == null || start.length() == 0) {
            addExceptionToList(allExceptions, "blank start");
        }

        if (end == null || end.length() == 0) {
            addExceptionToList(allExceptions, "blank end");
        }

        if (instructor == null || instructor.length() == 0) {
            addExceptionToList(allExceptions, "blank instructor");
        }

        if (venue == null || venue.length() == 0) {
            addExceptionToList(allExceptions, "blank venue");
        }

        if (size == null || size.length() == 0) {
            addExceptionToList(allExceptions, "blank size");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code1 = parseCourse(courseID);
        String code2 = parseSection(sectionID);
        String code3 = parseDayOfWeek(day);
        String code4 = parseClassStart(start);
        String code5 = null;
        if (code4 == null) {
            code5 = parseClassEnd(start, end);
        }
        String code6 = parseInstructor(instructor);
        String code7 = parseVenue(venue);
        String code8 = parseSize(size);

        addExceptionToList(allExceptions, code1);
        addExceptionToList(allExceptions, code2);
        addExceptionToList(allExceptions, code3);
        addExceptionToList(allExceptions, code4);
        addExceptionToList(allExceptions, code5);
        addExceptionToList(allExceptions, code6);
        addExceptionToList(allExceptions, code7);
        addExceptionToList(allExceptions, code8);

        return allExceptions;
    }

    /**
    * Retrieves an ArrayList of errors when validating details of a student that is passed into the method
    *
    * @param userID the student user ID
    * @param name the student name
    * @param password the student password
    * @param school the school the student belongs to
    * @param eDollar the student eDollar
    * 
    * @return an ArrayList of String that represents errors during validation of student
    */
    public ArrayList<String> validateStudents(String userID, String password, String name, String school, String eDollar) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (userID == null || userID.length() == 0) {
            addExceptionToList(allExceptions, "blank userid");
        }

        if (password == null || password.length() == 0) {
            addExceptionToList(allExceptions, "blank password");
        }

        if (name == null || name.length() == 0) {
            addExceptionToList(allExceptions, "blank name");
        }

        if (school == null || school.length() == 0) {
            addExceptionToList(allExceptions, "blank school");
        }

        if (eDollar == null || eDollar.length() == 0) {
            addExceptionToList(allExceptions, "blank e-dollar");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code1 = parseUserID(userID);
        String code2 = parseDuplicateUser(userID);
        String code3 = parseName(name);
        String code4 = parsePassword(password);
        String code5 = parseEDollar(eDollar);

        addExceptionToList(allExceptions, code1);
        addExceptionToList(allExceptions, code2);
        addExceptionToList(allExceptions, code3);
        addExceptionToList(allExceptions, code4);
        addExceptionToList(allExceptions, code5);

        return allExceptions;
    }

    /**
    * Retrieves an ArrayList of errors when validating details of a prerequisite course that is passed into the method
    *
    * @param courseID the course ID of the prerequisite
    * @param prerequisiteID the prerequisite ID
    * 
    * @return an ArrayList of String that represents errors during validation of prerequisites
    */
    public ArrayList<String> validatePrerequisites(String courseID, String prerequisiteID) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (courseID == null || courseID.length() == 0) {
            addExceptionToList(allExceptions, "blank course");
        }

        if (prerequisiteID == null || prerequisiteID.length() == 0) {
            addExceptionToList(allExceptions, "blank prerequisite");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code1 = parseInvalidCourse(courseID);
        String code2 = parseInvalidPrerequisite(prerequisiteID);

        addExceptionToList(allExceptions, code1);
        addExceptionToList(allExceptions, code2);

        return allExceptions;
    }

    /**
    * Retrieves an ArrayList of errors when validating details of a completed course that is passed into the method
    *
    * @param userID the user ID of student that completed this course
    * @param courseID the course ID
    * 
    * @return an ArrayList of String that represents errors during validation of completed courses
    */
    public ArrayList<String> validateCourseCompletion(String userID, String courseID) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (userID == null || userID.length() == 0) {
            addExceptionToList(allExceptions, "blank userid");
        }

        if (courseID == null || courseID.length() == 0) {
            addExceptionToList(allExceptions, "blank code");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code1 = parseCourseCompleteUserID(userID);
        String code2 = parseCourseCompletedCode(courseID);

        addExceptionToList(allExceptions, code1);
        addExceptionToList(allExceptions, code2);

        if (!allExceptions.isEmpty()) {
            return allExceptions;
        }

        String code3 = parseCourseCompletion(courseID, userID);

        addExceptionToList(allExceptions, code3);

        return allExceptions;
    }

    /**
    * Retrieves an ArrayList of errors when validating details of a bid that is passed into the method
    *
    * @param userID the bid user ID
    * @param amount the bid amount
    * @param bidCode the bid course ID
    * @param bidSection the bid section ID
    * 
    * @return an ArrayList of String that represents errors during validation of a bid
    */
    public ArrayList<String> validateBids(String userID, String amount, String bidCode, String bidSection) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (userID == null || userID.length() == 0) {
            addExceptionToList(allExceptions, "blank userid");
        }

        if (amount == null || amount.length() == 0) {
            addExceptionToList(allExceptions, "blank amount");
        }

        if (bidCode == null || bidCode.length() == 0) {
            addExceptionToList(allExceptions, "blank code");
        }

        if (bidSection == null || bidSection.length() == 0) {
            addExceptionToList(allExceptions, "blank section");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code1 = parseBidUserID(userID);
        String code2 = parseBidAmount(amount);
        String code3 = parseBidCode(bidCode);
        String code4 = null;

        if (code3 == null) {
            code4 = parseBidSection(bidCode, bidSection);
        }
        addExceptionToList(allExceptions, code1);
        addExceptionToList(allExceptions, code2);
        addExceptionToList(allExceptions, code3);
        addExceptionToList(allExceptions, code4);

        //if there are invalid fields, logic validations will not be carried out
        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code5 = parseNotOwnSchoolCourse(userID, bidCode);
        String code6 = parseClassTimeTableClash(userID, bidCode, bidSection);
        String code7 = parseExamTimeTableClash(userID, bidCode, bidSection);
        String code8 = parseIncompletePrerequisite(userID, bidCode);
        String code9 = parseAlreadyComplete(userID, bidCode);
        String code10 = parseSectionLimit(userID, bidCode, bidSection);
        String code11 = parseEDollarEnough(userID, bidCode, bidSection, amount);

        addExceptionToList(allExceptions, code5);
        addExceptionToList(allExceptions, code6);
        addExceptionToList(allExceptions, code7);
        addExceptionToList(allExceptions, code8);
        addExceptionToList(allExceptions, code9);
        addExceptionToList(allExceptions, code10);
        addExceptionToList(allExceptions, code11);

        return allExceptions;
    }


    /**
    * Retrieves an ArrayList of errors when validating details of an existing bid that is passed into the method
    *
    * @param userID the bid user ID
    * @param amount the bid amount
    * @param bidCode the bid course ID
    * @param bidSection the bid section ID
    * 
    * @return an ArrayList of String that represents errors during validation of an existing bid
    */
    public ArrayList<String> validateExistingBid(String userID, String amount, String bidCode, String bidSection) {
        ArrayList<String> allExceptions = new ArrayList<>();

        if (amount == null || amount.length() == 0) {
            addExceptionToList(allExceptions, "blank amount");
        }

        if (bidSection == null || bidSection.length() == 0) {
            addExceptionToList(allExceptions, "blank section");
        }

        for (String str : allExceptions) {
            if (str != null) {
                return allExceptions;
            }
        }

        String code2 = parseBidAmount(amount);
        String code4 = parseBidSection(bidCode, bidSection);
        String code6 = parseClassTimeTableClash(userID, bidCode, bidSection);
        String code11 = parseEDollarEnoughExistingBid(userID, bidCode, bidSection, amount);

        addExceptionToList(allExceptions, code2);
        addExceptionToList(allExceptions, code4);
        addExceptionToList(allExceptions, code6);
        addExceptionToList(allExceptions, code11);

        return allExceptions;
    }

    //NOTE plenty of repetitive code below, we can choose to create another class for this in the future
    /**
     * **************************************************************
     *
     * All the utility functions for course objects will be stored below!
     *
     ****************************************************************
     */

     /**
     * Evaluates whether the input string is a valid exam date. The field must be in the format yyyyMmdd
     *
     * @param input the input string
     * 
     * @return "invalid exam date" if exam date is invalid, null otherwise
     */
    private String parseExamDate(String input) {
        String result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        try {
            dateFormat.parse(input);
        } catch (ParseException e) {
            result = "invalid exam date";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid exam start time. The field must be in the format H:mm (8:30, 12:00, 15:30)
    *
    * @param input the input string
    * 
    * @return "invalid exam start" if exam start time is invalid, null otherwise
    */
    private String parseExamStart(String input) {
        String result = null;
        if (input.matches(".*[a-zA-Z]+.*")) {
            result = "invalid exam start";
            return result;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        try {
            dateFormat.parse(input);
        } catch (ParseException e) {
            result = "invalid exam start";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid exam start time. The field must be in the format H:mm and the end time should be later than the start time. (11:45, 15:15, 18:45)
    *
    * @param start the exam start time
    * @param end the exam end time
    * 
    * @return "invalid exam end" if exam end time is invalid, null otherwise
    */
    private String parseExamEnd(String start, String end) {
        String result = null;
        if (end.matches(".*[a-zA-Z]+.*")) {
            result = "invalid exam end";
            return result;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            if (endDate.before(startDate)) {
                result = "invalid exam end";
            }
        } catch (ParseException e) {
            result = "invalid exam end";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid title.The title field must not exceed 100 characters.
    *
    * @param input the input string
    * 
    * @return "invalid title" if the title is invalid, null otherwise
    */
    private String parseTitle(String input) {
        String result = null;
        if (input.length() > 100) {
            result = "invalid title";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid description. The description field must not exceed 1000 characters.
    *
    * @param input the input string
    * 
    * @return "invalid description: if the description is invalid, null otherwise
    */
    private String parseDescription(String input) {
        String result = null;
        if (input.length() > 1000) {
            result = "invalid description";
        }
        return result;
    }

    /**
     * **************************************************************
     *
     * All the utility functions for section objects will be stored below!
     *
     ****************************************************************
     */
     /**
     * Evaluates whether the input string is a courseID in which the course is found in the course.csv
     *
     * @param input the input string
     * 
     * @return "invalid course" if courseID cannot be found in the course.csv, null otherwise
     */
    private String parseCourse(String input) {
        String result = null;
        Course match = CourseDAO.getInstance().findCourse(input);
        if (match == null) {
            result = "invalid course";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid section ID. The first character should be an S followed by a positive numeric number (1-99). Check only if course is valid.
    *
    * @param input the input string
    * @return "invalid section" if sectionID is invalid, null otherwise
    */
    private String parseSection(String input) {
        String result = null;
        boolean hasUpperS = (input.indexOf('S') != -1);
        if (hasUpperS) {
            input = input.replace("S", "");
            try {
                int num = Integer.parseInt(input);
                if (num < 1 || num > 99) {
                    result = "invalid section";
                }
            } catch (NumberFormatException e) {
                result = "invalid section";
            }
        } else {
            result = "invalid section";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid day of week,The day field should be a number between 1(inclusive) and 7 (inclusive). 1 - Monday, 2 - Tuesday, ... , 7 - Sunday.
    *
    * @param input the input string
    * 
    * @return "invalid day" if the day is invalid, null otherwise
    */
    private String parseDayOfWeek(String input) {
        String result = null;
        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > 7) {
                result = "invalid day";
            }
        } catch (NumberFormatException e) {
            result = "invalid day";
        }

        return result;
    }

    /**
    * Evaluates whether the input string is a valid class start time. The field must  be in the format H:mm(8.30,12:00,15:30)
    *
    * @param input the input string
    * 
    * @return "invalid start" if the class start time is invalid, null otherwise
    */
    private String parseClassStart(String input) {
        String result = null;
        if (input.matches(".*[a-zA-Z]+.*")) {
            result = "invalid start";
            return result;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        try {
            dateFormat.parse(input);
        } catch (ParseException e) {
            result = "invalid start";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid class end time. The field must be in the format H:mm and the end time should be later than the start time. (11:45, 15:15, 18:45)
    *
    * @param input the input string
    * 
    * @return "invalid end" if the input class end time is invalid, null otherwise
    */
    private String parseClassEnd(String start, String end) {
        String result = null;
        if (end.matches(".*[a-zA-Z]+.*")) {
            result = "invalid end";
            return result;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            if (endDate.before(startDate)) {
                result = "invalid end";
            }
        } catch (ParseException e) {
            result = "invalid end";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid instructor. The instructor field must not exceed 100 characters.
    *
    * @param input the input string
    * @return "invalid instructor" if instructor is invalid, null otherwise
    */
    private String parseInstructor(String input) {
        String result = null;
        if (input.length() > 100) {
            result = "invalid instructor";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid venue. The venue field must not exceed 100 characters.
    *
    * @param input the input string
    * 
    * @return "invalid venue" if the venue is invalid, null otherwise
    */
    private String parseVenue(String input) {
        String result = null;
        if (input.length() > 100) {
            result = "invalid venue";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid size. The field must be a positive numeric number.
    *
    * @param input the input string
    * 
    * @return "invalid size" if the size is invalid, null otherwise
    */
    private String parseSize(String input) {
        String result = null;
        try {
            int num = Integer.parseInt(input);
            if (num < 1) {
                result = "invalid size";
            }
        } catch (NumberFormatException e) {
            result = "invalid size";
        }
        return result;
    }

    /**
     * **************************************************************
     *
     * All the utility functions for student objects will be stored below!
     *
     ****************************************************************
     */
     /**
     * Evaluates whether the input string is a valid userid. The userid field must not exceed 128 characters or duplicated userID
     *
     * @param input the input string
     * 
     * @return "invalid userid" if the userid is invalid, null otherwise
     */
    public String parseUserID(String input) {
        String result = null;
        if (input.length() > 128) {
            result = "invalid userid";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a userID that was used by another Student
    *
    * @param input the input string
    * 
    * @return "duplicate userid" if the userID is duplicated, null otherwise
    */
    public String parseDuplicateUser(String input) {
        String result = null;
        Student student = StudentDAO.getInstance().findStudent(input);
        if (student != null) {
            result = "duplicate userid";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid e-dollar amount. The e-dollar field must be a numeric value greater or equal to 0.0, and not more than 2 decimal places. Any other values will generate this error.
    *
    * @param input the input string
    * 
    * @return "invalid e-dollar" if the e-dollar amount is invalid, null otherwise
    */
    public String parseEDollar(String input) {
        String result = null;
        try {
            double eDollars = Double.parseDouble(input);
            if (eDollars < 0) {
                result = "invalid e-dollar";
            }
        } catch (NumberFormatException e) {
            result = "invalid e-dollar";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid password. The password field must not exceed 128 characters.
    *
    * @param input the input string
    * 
    * @return "invalid password" if the password is invalid, null otherwise
    */
    public String parsePassword(String input) {
        String result = null;
        if (input.length() > 128) {
            result = "invalid password";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid name. The name field must not exceed 100 characters.
    *
    * @param input the input string
    * 
    * @return "invalid name" if the name is invalid, null otherwise
    */
    public String parseName(String input) {
        String result = null;
        if (input.length() > 100) {
            result = "invalid name";
        }
        return result;
    }

    /**
     * **************************************************************
     *
     * All the utility functions for prerequisite objects will be stored below!
     *
     ****************************************************************
     */

     /**
     * Evaluates whether the input string is a valid course.The course is invalid if the course code is not found in the course.csv
     *
     * @param input the input string
     * 
     * @return "invalid course" if the course is invalid, null otherwise
     */
    public String parseInvalidCourse(String input) {
        String result = null;
        Course match = CourseDAO.getInstance().findCourse(input);
        if (match == null) {
            result = "invalid course";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid prerequisite. The prerequisite is invalid if the prerequisite ID is not found in the course.csv
    *
    * @param input the input string
    * 
    * @return "invalid prerequisite" if the prerequisite ID is not found in courseDAO, null otherwise
    */
    public String parseInvalidPrerequisite(String input) {
        String result = null;
        Course match = CourseDAO.getInstance().findCourse(input);
        if (match == null) {
            result = "invalid prerequisite";
        }
        return result;
    }

    /**
     * **************************************************************
     *
     * All the utility functions for course_completed objects will be stored
     * below!
     *
     ****************************************************************
     */
     /**
     * Evaluates whether the input string is a valid userID. The user ID is invalid it it is not found in student.csv
     *
     * @param input the input string
     * 
     * @return "invalid userid" if the user ID id not found in student.csv, null otherwise
     */
    public String parseCourseCompleteUserID(String input) {
        String result = null;
        Student match = StudentDAO.retrieve(input);
        if (match == null) {
            result = "invalid userid";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid completed course. The completed course is invalid if it is not found in the course.csv.
    *
    * @param input the input string
    * 
    * @return "invalid course" if the course completed ID is not found in course.csv, null otherwise
    */
    public String parseCourseCompletedCode(String input) {
        String result = null;
        Course match = CourseDAO.getInstance().findCourse(input);
        if (match == null) {
            result = "invalid course";
        }
        return result;
    }

    /**
    * Evaluates whether the course completed has completed the pre-requisite course.
    *
    * @param courseID the course ID of the completed course
    * @param userID the user ID of the student 
    * 
    * @return "invalid course completed" if the student has not completed the prerequisite course, null otherwise
    */
    public String parseCourseCompletion(String courseID, String userID) {
        String result = null;
        Prerequisite pre = PrerequisiteDAO.getInstance().findCourse(courseID);
        if (pre != null) {
            CompletedCourse course = CompletedCourseDAO.getInstance().findCompletedCourse(pre.getPrerequisiteID());
            if (course == null || !course.getUserID().equals(userID)) {
                result = "invalid course completed";
            }
        }
        return result;
    }

    /**
     * **************************************************************
     *
     * All the utility functions for bid objects will be stored below!
     *
     ****************************************************************
     */

     /**
     * Evaluates whether the input string is a valid user ID. The user ID is invalid if it is not found in student.csv
     *
     * @param input the input string
     * 
     * @return "invalid userid" if the user ID is not found in student.csv, null otherwise
     */
    public String parseBidUserID(String input) {
        String result = null;
        Student match = StudentDAO.getInstance().findStudent(input);
        if (match == null) {
            result = "invalid userid";
        }
        return result;
    }

    /**
    * Evaluates whether the input string is a valid bid amount.The amount must be a positive number(more than or equals to) e$10.00 and not more than 2 decimal places.
    *
    * @param input the input string
    * 
    * @return "invalid amount" if the bid amount is invalid, null otherwise
    */
    public String parseBidAmount(String input) {
        String result = null;
        try {
            double eDollars = Double.parseDouble(input);
            int indexOfDec = input.indexOf(".");
            if (indexOfDec != -1) {

                int numOfDec = input.length() - (indexOfDec + 1);
                if (eDollars < 10 || numOfDec > 2) {
                    result = "invalid amount";
                }
            } else //if not decimal place
             if (eDollars < 10) {
                    result = "invalid amount";
                }
        } catch (NumberFormatException e) {
            result = "invalid amount";
        }
        return result;
    }

    /**
    * Evaluated whether the string input is a valid bid course. The bid course is invalid if the course code is not found in the course.csv
    *
    * @param input the input string
    * 
    * @return "invalid course" if the bid course is invalid, null otherwise
    */
    public String parseBidCode(String input) {
        String result = null;
        Course match = CourseDAO.getInstance().findCourse(input);
        if (match == null) {
            result = "invalid course";
        }
        return result;
    }

    /**
    * Evaluate whether the section is a valid section. The section is invalid if it is not found in the section.csv (this is only tested for valid course code)
    *
    * @param courseID the course ID
    * @param sectionID the section ID
    * 
    * @return "invalid section" if the section is not found in section.csv, null otherwise
    */
    public String parseBidSection(String courseID, String sectionID) {
        String result = null;
        Section match = SectionDAO.getInstance().findSection(courseID, sectionID);
        if (match == null) {
            result = "invalid section";
        }
        return result;
    }

    /**
    * Evaluates whether the Student is bidding for a course which belongs to their school.This only happens in round 1 where students are allowed to bid for modules from their own school.
    *
    * @param userID the user ID
    * @param courseID the course ID
    * @return "not own school course" if the student is bidding for a course which does not belong to his/her school, null otherwise
    */
    public String parseNotOwnSchoolCourse(String userID, String courseID) {
        String result = null;
        Student student = StudentDAO.getInstance().retrieve(userID);
        Course courseBiddedFor = CourseDAO.getInstance().findCourse(courseID);
        if (student == null || courseBiddedFor == null || !student.getSchool().equals(courseBiddedFor.getSchool())) {
            result = "not own school course";
        }
        return result;
    }

    /**
    * Evaluates whether the bid's section class timetable clashes with the student's previous bid.
    * 
    * @param userID the user ID
    * @param courseID the course ID
    * @param sectionID the section ID
    * 
    * @return "class timetable clash" if the class timetable clashes, null otherwise
    */
    public String parseClassTimeTableClash(String userID, String courseID, String sectionID) {
        String result = null;
        Section currBidSection = SectionDAO.getInstance().findSection(courseID, sectionID);
        ArrayList<Bid> existingBids = BidDAO.getInstance().getStudentBids(userID);
        for (Bid bid : existingBids) {
            if (currBidSection == null) {
                return null;
            }
            if (!bid.getStatus().equals("success") && bid.getCourseID().equals(currBidSection.getCourseID()) && bid.getSectionID().equals(currBidSection.getSectionID())) {
                return null;
            }
            if (!bid.getStatus().equals("success") && bid.getCourseID().equals(currBidSection.getCourseID())) {
                return null;
            }
            Section biddedSection = SectionDAO.getInstance().findSection(bid.getCourseID(), bid.getSectionID());
            if (currBidSection.clashes(biddedSection)) {
                result = "class timetable clash";
            }
        }
        return result;
    }

    /**
    * Evaluates if the exam timetable for this bid clashes with any previous bid's exam timetable
    *
    * @param userID the user ID
    * @param courseID the course ID
    * @param sectionID the section ID
    * 
    * @return "exam timetable clash" if the bid's exam timetable clashes with any previous bids, null otherwise
    */
    public String parseExamTimeTableClash(String userID, String courseID, String sectionID) {
        String result = null;
        Course currBidCourse = CourseDAO.getInstance().findCourse(courseID);
        ArrayList<Bid> existingBids = BidDAO.getInstance().getStudentBids(userID);
        for (Bid bid : existingBids) {
            Course biddedCourse = CourseDAO.getInstance().findCourse(bid.getCourseID());
            if ( !bid.getStatus().equals("success") && currBidCourse.getCourseID().equals(bid.getCourseID())) {
                return null;
            }
            if (currBidCourse.examClashes(biddedCourse)) {

                return "exam timetable clash";
            }
        }
        return result;
    }

    /**
    * Evaluates whether the student completes the prerequisite courses of the bidded course
    *
    * @param userID the user ID
    * @param courseID the course ID
    * 
    * @return "incomplete prerequisites" if the prerequisite course was not completed, null otherwise
    */
    public String parseIncompletePrerequisite(String userID, String courseID) {
        String result = null;
        Course c = CourseDAO.getInstance().findCourse(courseID);

        ArrayList<Prerequisite> prereqList = null;
        if (c != null) {
            prereqList = c.getPrerequisiteList();
        }
        if (prereqList.size() == 0) {
            return null;
        }

        for (int i = 0; i < prereqList.size(); i++) {
            CompletedCourse course = CompletedCourseDAO.getInstance().findStudentCompletedCourseForID(userID, prereqList.get(i).getPrerequisiteID());

            if (course == null) {
                result = "incomplete prerequisites";
            }
        }
        return result;
    }

    /**
    * Evaluates whether the student has already completed this course.
    *
    * @param userID the user ID
    * @param courseID the course ID
    * 
    * @return "course completed" if the course has already been completed, null otherwise
    */
    public String parseAlreadyComplete(String userID, String courseID) {
        String result = null;
        CompletedCourse course = CompletedCourseDAO.getInstance().findStudentCompletedCourseForID(userID, courseID);
        if (course != null) {
            result = "course completed";
        }
        return result;
    }

    /**
    * Evaluated whether the student has already bidded for 5 sections.
    *
    * @param userID the user ID
    * @param courseID the course ID
    * @param sectionID the section ID
    * 
    * @return "section limit reached" if the section limit has reached, null otherwise
    */
    public String parseSectionLimit(String userID, String courseID, String sectionID) {
        String result = null;
        ArrayList<Bid> studentBids = BidDAO.getInstance().getStudentBids(userID);
        if (studentBids.size() >= 5) {
            result = "section limit reached";
        }
        return result;
    }

    /**
    * Evaluates whether the student has enough e-dollars to place the bid.If there is an update of a previous bid for the same course, account for the e$ gained back from cancellation
    *
    * @param userID the user ID
    * @param courseID the course ID
    * @param sectionID the section ID
    * @param amount the bid amount
    * 
    * @return "not enough e-dollar" if the student does not have enough e-dollar to bid for the course, null otherwise
    */
    public String parseEDollarEnough(String userID, String courseID, String sectionID, String amount) {
        String result = null;
        double bidAmount = Double.parseDouble(amount);
        Student student = StudentDAO.getInstance().retrieve(userID);
        DecimalFormat df = new DecimalFormat("0.00");
        String amt = df.format(student.geteDollar());
        double eDollar = Double.parseDouble(amt);
        if (student != null && bidAmount > eDollar) {
            result = "not enough e-dollar";
        }
        return result;
    }

    /**
    * Evaluates whether the student has enough e-Dollars to update an existing bid amount
    *
    * @param userID the user ID
    * @param courseID the course ID
    * @param sectionID the section ID
    * @param amount the bid amount
    * 
    * @return "not enough e-dollar" if the student does not have enough e-Dollar to update an existing bid, null otherwise
    */
    public String parseEDollarEnoughExistingBid(String userID, String courseID, String sectionID, String amount) {
        String result = null;
        ArrayList<Bid> existingBidList = BidDAO.getInstance().getStudentBids(userID);
        Student student = StudentDAO.retrieve(userID);
        for (Bid bid : existingBidList) {
            if (bid.getCourseID().equals(courseID)) {
                if (student.geteDollar() + bid.getAmount() - Double.parseDouble(amount) < 0) {
                    result = "not enough e-dollar";
                    break;
                }
            }
        }
        return result;
    }

    /**
     * **************************************************************
     *
     * Other utility functions below
     *
     ****************************************************************
     */

     /**
     * Convenience method to add an exception to the list
     *
     * @param list the list to be added
     * @param code the code in which to add into the list
     */
    private void addExceptionToList(ArrayList<String> list, String code) {
        if (code != null) {
            list.add(code);
        }
    }

}
