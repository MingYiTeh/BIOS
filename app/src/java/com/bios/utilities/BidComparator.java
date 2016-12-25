/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Bid;
import java.util.Comparator;

/**
 * Represents a Bid Comparator object
 * @author Teh Ming Yi
 */
public class BidComparator implements Comparator<Bid> {

    /**
    * Retrieves the higher bid amount of the two bids
    *
    * @param o1 the first bid
    * @param o2 the second bid
    * @return the higher bid value after the compare
    */
    @Override
    public int compare(Bid o1, Bid o2) {
        double a = o1.getAmount();
        double b = o2.getAmount();
        if (a < b){
            return 1;
        } else if(a == b){
            return 0;
        }
        else {
            return -1;
        }

    }

}
