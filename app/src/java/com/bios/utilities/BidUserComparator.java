/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Bid;
import java.util.Comparator;

/**
 * Represents a Bid User Comparator object
 * @author kongyujian
 */
public class BidUserComparator implements Comparator<Bid> {

    /**
  * Retrieves the higher bid amount of the two bid user
  *
  * @param o1 the first bid
  * @param o2 the second bid
  * @return the higher bid value after the compare
  */
    @Override
    public int compare(Bid o1, Bid o2){
        String user1 = o1.getUserID();
        String user2 = o2.getUserID();

        return user1.compareToIgnoreCase(user2);
    }

}
