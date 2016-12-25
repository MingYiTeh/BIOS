/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import java.util.Comparator;
/**
 * Represents an Error Message Comparator object
 * @author Teh Ming Yi
 */
public class ErrorMsgComparator implements Comparator<String>{

    /**
   * Makse a comparison between two error messages
   *
   * @param s1 the first error message
   * @param s2 the second error message
   * @return the higher error message after comparing
   */
    @Override
    public int compare(String s1, String s2) {
        return s1.substring(s1.indexOf(" ")+1).compareToIgnoreCase(s2.substring(s2.indexOf(" ")+1));
    }

}
