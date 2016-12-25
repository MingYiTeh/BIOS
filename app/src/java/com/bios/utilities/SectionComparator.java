/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Section;
import java.util.Comparator;
/**
 * Represents a Section Comparator object
 * @author Teh Ming Yi
 */
public class SectionComparator implements Comparator<Section> {

    /**
    * Makes a comparison between two Section objects based on the courseID
    *
    * @param o1 the first section
    * @param o2 the second section
    * @return the higher section value after the compare
    */
    @Override
    public int compare(Section o1, Section o2) {

        String prefix1 = o1.getCourseID();
        String prefix2 = o2.getCourseID();

        int number1 = Integer.parseInt(o1.getSectionID().split("(?<=\\D)(?=\\d)")[1]);
        int number2 = Integer.parseInt(o2.getSectionID().split("(?<=\\D)(?=\\d)")[1]);

        if (prefix1.compareToIgnoreCase(prefix2) == 0){
            return number1 - number2;
        } else {
            return prefix1.compareToIgnoreCase(prefix2);
        }
    }

}
