/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import com.bios.utilities.BidComparator;
import com.bios.utilities.ConnectionManager;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a Bid Data Access Object that contains all the bids
 *
 * @author kongyujian
 */
public class RoundOneBidDAO {

    private static RoundOneBidDAO instance = null;
    private static ArrayList<Bid> bidList;

    /**
     * @return the instance of a BidDAO, if there isn't one, a new bidList and
     * will be created and returned
     */
    public static RoundOneBidDAO getInstance() {
        if (instance == null) {
            bidList = new ArrayList<>();
            instance = new RoundOneBidDAO();
        }
        return instance;
    }

    /**
     * Retrieves a list of all the bids
     *
     * @return the BidList
     */
    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    /**
     * Sets bidList as the list which contains all the bids
     *
     * @param bidList the bidList to set
     */
    public void setBidList(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }

    /**
     * Adds the bid to the list of bids
     *
     * @param bid the bid to be added
     */
    public void addBid(Bid bid) {
        bidList.add(bid);
        bidList.sort(new BidComparator());
    }

    public void addBidToDB(Bid bid) {
        ConnectionManager manager = new ConnectionManager();
        manager.addBid(bid);
    }

    /**
     * Retrieves a Bid with specified user ID, course ID and section ID from the BidDAO
     *
     * @param userID User ID of the Student
     * @param courseID Course ID of the bid
     * @param sectionID Section ID of the bid
     * @return the Bid with the specified User ID, course ID and
     * section ID, null otherwise
     */
    public Bid getBid(String userID, String courseID, String sectionID) {
        ArrayList<Bid> bList = getBidList();
        for (Bid bid : bList) {
            String refUserID = bid.getUserID();
            String refCourseID = bid.getCourseID();
            String refSectionID = bid.getSectionID();
            if (refUserID.equals(userID) && refCourseID.equals(courseID)
                    && refSectionID.equals(sectionID)) {
                return bid;
            }
        }
        return null;
    }

    /**
     * Retrieves the pending bid with specified user ID, course ID and section ID from the BidDAO
     * 
     * @param userID User ID of the Student
     * @param courseID Course ID of the bid
     * @param sectionID Section ID of the bid
     * @return the Pending Bid with specified User ID, course ID and section ID, null otherwise
     */
    public Bid getPendingBid(String userID, String courseID, String sectionID) {
        ArrayList<Bid> bList = getBidList();
        Bid bid = null;
        for (int i = 0; i < bList.size(); i++) {
            bid = bList.get(i);
            String refUserID = bid.getUserID();
            String refCourseID = bid.getCourseID();
            String refSectionID = bid.getSectionID();
            if (bid.getStatus().equals("pending") && refUserID.equals(userID) && refCourseID.equals(courseID)
                    && refSectionID.equals(sectionID)) {
                return bid;
            }
        }
        return null;
    }

    /**
     * Retrieves an existing bid with specified user ID, course ID and section ID from the BidDAO
     * 
     * @param userID User ID of the Student
     * @param courseID Course ID of the bid
     * @param sectionID Section ID of the bid
     * @return the bid with specified User ID, course ID and section ID, null otherwise 
     */
    public Bid getPreviousBid(String userID, String courseID, String sectionID) {
        ArrayList<Bid> bList = getBidList();
        Bid bid = null;
        for (int i = 0; i < bList.size(); i++) {
            bid = bList.get(i);
            String refUserID = bid.getUserID();
            String refCourseID = bid.getCourseID();
            String refSectionID = bid.getSectionID();
            if (bid.getStatus().equals("success") && refUserID.equals(userID) && refCourseID.equals(courseID)
                    && refSectionID.equals(sectionID)) {
                return bid;
            }
        }
        return bid;
    }

    /**
     * Retrieves an ArrayList of pending bids
     * 
     * @return an ArrayList of pending bids
     */
    public ArrayList<Bid> getPendingBids() {
        ArrayList<Bid> bList = new ArrayList<>();
        for (Bid bid : bidList) {
            if (bid.getStatus().equals("pending")) {
                bList.add(bid);
            }
        }
        return bList;
    }

    /**
     * Retrieves an ArrayList of pending bids with specific course ID and section ID
     * 
     * @param courseID Course ID of the bid
     * @param sectionID Section ID of the bid
     * @return an ArrayList of pending bids with specific course ID and section ID
     */
    public ArrayList<Bid> getPendingBidsWithID(String courseID, String sectionID) {
        ArrayList<Bid> bList = new ArrayList<>();
        for (Bid bid : bidList) {
            if (bid.getStatus().equals("pending") && bid.getCourseID().equals(courseID)
                    && bid.getSectionID().equals(sectionID)) {
                bList.add(bid);
            }
        }
        return bList;
    }
    
    /**
     * Retrieves an ArrayList of successful bids with specific course ID and section ID
     * 
     * @param courseID Course ID of the bid
     * @param sectionID Section ID of the bid
     * @return an ArrayList of successful bids with specific course ID and section ID
     */
    public ArrayList<Bid> getSuccessfulBidsWithID(String courseID, String sectionID) {
        ArrayList<Bid> bList = new ArrayList<>();
        for (Bid bid : bidList) {
            if (bid.getStatus().equals("success") && bid.getCourseID().equals(courseID)
                    && bid.getSectionID().equals(sectionID)) {
                bList.add(bid);
            }
        }
        return bList;
    }

    /**
     * Retrieves an ArrayList of bids that belongs to a specific student
     * 
     * @param userID User ID of the student
     * @return an ArrayList of bids with specific user ID
     */
    public ArrayList<Bid> getStudentBids(String userID) {
        ArrayList<Bid> bList = new ArrayList<>();
        for (Bid bid : bidList) {
            if (bid.getUserID().equals(userID)) {
                bList.add(bid);
            }
        }
        return bList;
    }

    /**
     * Retrieves an ArrayList of pending bids with specific user ID,course ID and section ID
     * 
     * @param userID User ID of the student
     * @param courseID Course ID of the bid
     * @param sectionID Section ID of the bid
     * @return an ArrayList of pending bids with specific user ID,course ID and section ID
     */
    public ArrayList<Bid> getStudentPendingBidsWithID(String userID, String courseID, String sectionID) {
        ArrayList<Bid> bList = new ArrayList<>();
        for (Bid bid : bidList) {
            if (bid.getStatus().equals("pending") && bid.getCourseID().equals(courseID)
                    && bid.getSectionID().equals(sectionID) && bid.getUserID().equals(userID)) {
                bList.add(bid);
            }
        }
        return bList;
    }

    /**
     * Retrieves the bid with specific user ID and course ID
     *
     * @param userID User ID of the student
     * @param courseID Course ID of the bid
     *
     * @return a bid with specific user ID and course ID, null otherwise
     */
    public Bid getStudentBidWithCourseID(String userID, String courseID) {
        for (Bid bid : bidList) {
            if (bid.getCourseID().equals(courseID)
                    && bid.getUserID().equals(userID)) {
                return bid;
            }
        }
        return null;
    }

    
    /**
     * Retrieves a bid by the student
     * 
     * @param userID User ID of the student
     * @return a bid with specific user ID, null otherwise
     */
    public Bid findBidByUser(String userID) {
        for (Bid bid : bidList) {
            if (bid.getUserID().equals(userID)) {
                return bid;
            }
        }
        return null;
    }

    /**
     * Deletes the Bid with the specified User ID, courseID and
     * sectionID from the BidDAO
     *
     * @param userID User ID of the Student
     * @param courseID Course ID of the Bid
     * @param sectionID Section ID of the Bid
     * 
     * @return true if the bid was successfully deleted, false otherwise
     */
    public boolean deleteBid(String userID, String courseID, String sectionID) {
        ArrayList<Bid> bList = getBidList();
        Iterator iter = bList.iterator();
        while (iter.hasNext()) {
            Bid bid = (Bid) iter.next();
            if (bid.getUserID().equals(userID) && bid.getCourseID().equals(courseID)
                    && bid.getSectionID().equals(sectionID)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes the Bid with the specified User ID, courseID and sectionID from the database
     *
     * @param userID User ID of the Student
     * @param courseID Course ID of the Bid
     * @param sectionID Section ID of the Bid
     * 
     */
    public void deleteBidFromDB(String userID, String courseID, String sectionID) {
        ConnectionManager manager = new ConnectionManager();
        manager.deleteBid(userID, courseID, sectionID);
    }

    /**
     * Retrieves an ArrayList of bids with specific courseID and section ID
     * @param courseID Course ID of the Bid
     * @param sectionID Section ID of the Bid
     * @return an ArrayList of bids with specific courseID and section ID
     */
    public ArrayList<Bid> getBids(String courseID, String sectionID) {
        ArrayList<Bid> returnList = new ArrayList<>();

        for (Bid bid : bidList) {
            if (bid.getCourseID().equals(courseID) && bid.getSectionID().equals(sectionID)) {
                returnList.add(bid);
            }
        }
        return returnList;
    }

    /**
     * Drops the existing bidList in RoundOneBidDAO and creates a new empty bidlist
     */
    public void drop() {
        bidList = new ArrayList<Bid>();
    }

}
