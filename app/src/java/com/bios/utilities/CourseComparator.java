/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Course;
import java.util.Comparator;
/**
 * Represents a Course Comparator object
 * @author Teh Ming Yi
 */
public class CourseComparator implements Comparator<Course> {

    /**
    * Makes a comparision between two course objects based on the courseID
    *
    * @param o1 the first course
    * @param o2 the second course
    * @return the higher course value after the compare
    */
    @Override
    public int compare(Course o1, Course o2) {
        String code1 = o1.getCourseID();
        String code2 = o2.getCourseID();

        String[] part1 = code1.split("(?<=\\D)(?=\\d)");
        String[] part2 = code2.split("(?<=\\D)(?=\\d)");

        String prefix1 = part1[0];
        String prefix2 = part2[0];

        int number1 = Integer.parseInt(part1[1]);
        int number2 = Integer.parseInt(part2[1]);

        if (prefix1.compareToIgnoreCase(prefix2) == 0){
            return number1 - number2;
        } else {
            return prefix1.compareToIgnoreCase(prefix2);
        }

    }

}
