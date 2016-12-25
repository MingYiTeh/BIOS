/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import com.bios.utilities.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;

/**
 * Represents a Student Data Access Object that contains all the students 
 *
 * @author Teh Ming Yi
 */
public class StudentDAO {

    private static StudentDAO instance = null;
    private static ArrayList<Student> studentList = null;
    
    /**
     * @return the instance of a studentDAO, 
     * if there isn't one, a new studentList and StudentDAO will be created and returned
     */
    public static StudentDAO getInstance() {
        if (instance == null) {
            studentList = new ArrayList<>();
            instance = new StudentDAO();
        }
        return instance;
    }

    /**
     * Retrieves a Student with the specified User ID
     *
     * @param userId User ID of the Student
     * @return the Student with the specified User ID, null otherwise
     */
    public static Student retrieve(String userId) {
        //connect the database
        //pull out the record
        //close the database
        
        ConnectionManager manager = new ConnectionManager();
        try (Connection conn = manager.getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from Student where UserId = ?");) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(rs.getString("userId"), rs.getString("userPassword"), rs.getString("nameOfUser"), rs.getString("school"), rs.getDouble("edollar"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Adds a Student to the studentList
     *
     * @param student Student object to be passed into the studentList
     */
    public void addStudent(Student student){
        studentList.add(student);
    }
    /**
     * Retrieves a Student with the specified User ID
     *
     * @param studentID User ID of the Student
     * @return the Student with the specified User ID, null otherwise
     */
    public Student findStudent(String studentID) {
        for (Student student : studentList) {
            if (student.getId().equals(studentID)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of the students
     *
     * @return the Student's list
     */
    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    /**
     * @param studentList the studentList to set
     */
    public void setStudentList(ArrayList<Student> studentList) {
        this.studentList = studentList;
    }

}
