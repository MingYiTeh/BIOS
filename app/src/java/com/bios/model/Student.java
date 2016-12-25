/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

/**
 * Represents a Student
 * @author Teh Ming Yi
 */
public class Student {
    private String id;
    private String password;
    private String nameOfUser;
    private String school;
    private double eDollar;
    
    /**
     * Constructs a Student with the specified ID, password, name, school and the amount of eDollars left in the Student's account
     * @param id the Student's ID
     * @param password the Student's password
     * @param nameOfUser the Student's name
     * @param school the Student's school
     * @param eDollar the amount of eDollars left in the Student's account
     */
    public Student(String id, String password, String nameOfUser, String school, double eDollar) {
        this.id = id;
        this.password = password;
        this.nameOfUser = nameOfUser;
        this.school = school;
        this.eDollar = eDollar;
    }
    
    /**
     * Retrieves the ID of the Student
     * @return the Student's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the password of the Student
     * @return the Student's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the name of the Student
     * @return the Student's name
     */
    public String getNameOfUser() {
        return nameOfUser;
    }

    /**
     * Retrieves the school of the Student
     * @return the Student's school
     */
    public String getSchool() {
        return school;
    }

    /**
     * Retrieves the amount of eDollars the Student has
     * @return the Student's eDollars
     */
    public double geteDollar() {
        return eDollar;
    }
    /**
     * Sets the  e$ balance of the student with the specified e$ amount
     *
     * @param eDollar e$ amount to be set
     */
    public void seteDollar(double eDollar){
        this.eDollar=eDollar;
    }

}