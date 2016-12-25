/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

//TODO haven't do yet :/

import com.bios.model.Bid;
import java.util.Comparator;

/**
 * Represents a BidJSON Comparator object
 * @author Teh Ming Yi
 */
public class BidJSONComparator implements Comparator<Bid> {

    /**
    * Retrieves the higher bid amount of the two bids json
    *
    * @param o1 the first bid
    * @param o2 the second bid
    * @return the higher bid value after the compare
    */
    @Override
    public int compare(Bid o1, Bid o2) {

        String prefix1 = o1.getCourseID();
        String prefix2 = o2.getCourseID();

        String code1 = o1.getSectionID();
        String code2 = o2.getSectionID();

        double amt1 = o1.getAmount();
        double amt2 = o2.getAmount();

        String user1 = o1.getUserID();
        String user2 = o2.getUserID();

        if (prefix1.compareToIgnoreCase(prefix2) == 0){
            if (code1.compareToIgnoreCase(code2) == 0){
                if (amt1 == amt2){
                    return user1.compareToIgnoreCase(user2);
                } else {
                    return Double.compare(amt2, amt1);
                }
            } else {
                return code1.compareToIgnoreCase(code2);
            }
        } else {
            return prefix1.compareToIgnoreCase(prefix2);
        }

    }

}
