/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Student;
import java.util.Comparator;

/**
 * Represents a Student Comparator object
 * @author Teh Ming Yi
 */
public class StudentComparator implements Comparator<Student> {

    /**
   * Makes a comparison between two student objects based on the studentID
   *
   * @param o1 the first student
   * @param o2 the second student
   * @return the higher student value after the compare
   */
    @Override
    public int compare(Student o1, Student o2) {
        return o1.getId().compareToIgnoreCase(o2.getId());
    }

}
