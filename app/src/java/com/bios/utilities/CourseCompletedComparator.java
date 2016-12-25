/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.CompletedCourse;
import java.util.Comparator;
/**
 * Represents a Course Completed Comparator object
 * @author Teh Ming Yi
 */
public class CourseCompletedComparator implements Comparator<CompletedCourse> {

    /**
   * Makes a comparison between two CompletedCourse objects based on the courseID as well as userID
   *
   * @param o1 the first completed course
   * @param o2 the second completed course
   * @return the higher course completed value after the compare
   */
    @Override
    public int compare(CompletedCourse o1, CompletedCourse o2) {
        String prefix1 = o1.getCourseID();
        String prefix2 = o2.getCourseID();

        String code1 = o1.getUserID();
        String code2 = o2.getUserID();

        if (prefix1.compareToIgnoreCase(prefix2) == 0){
            return code1.compareToIgnoreCase(code2);
        } else {
            return prefix1.compareToIgnoreCase(prefix2);
        }
    }

}
