/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import com.bios.utilities.BidComparator;
import com.bios.utilities.ConnectionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * Represents a MinBidDAO object that helps to calculate minBidValue
 * @author kongyujian
 */
public class MinBidDAO {
    
    private static MinBidDAO instance = null;
    // String will be the "courseID-sectionID" and Integer will be the min bid amt
    private static HashMap<String, Double> bidList;

    /**
     * @return the instance of a MinBidDAO, 
     * if there isn't one, a new bidList and an instance of the MinBidDAO will be created
     */
    public static MinBidDAO getInstance() {
        if (instance == null) {
            bidList = new HashMap<>();
            instance = new MinBidDAO();
        }
        return instance;
    }
    
    public HashMap<String, Double> getMinBidList(){
        return bidList;
    }
    
    // updates the minimum bid, and returns true if it can be updated, if not returns false
    // Criteria is that, the current min bid is lower than the value that is to be inserted
    public boolean updateMinBid(String key, Double value){
        if (!bidList.containsKey(key)){
            bidList.put(key, value);
            return true;
        } else {
            double currMinBid = bidList.get(key);
            if (value > currMinBid){
                bidList.replace(key, value);
                return true;
            } else {
                return false;
            }
        }
    }
    
    public double getValue(String key){
        return bidList.get(key);
    }
    
    public void refresh(){
        
        double minBidValue = 10;
        
        ArrayList<Section> secList = SectionDAO.getInstance().getSectionList();
        
        for (Section sec : secList){
            ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentListWithID(sec.getCourseID(), sec.getSectionID());
            int vacancy = sec.getSize() - sectionStudentList.size();
            ArrayList<Bid> round2Bids = BidDAO.getInstance().getPendingBidsWithID(sec.getCourseID(), sec.getSectionID());
            round2Bids.sort(new BidComparator());
            
            
            // This is to re-compute the minimum bid value
            if (round2Bids.size() >= vacancy && vacancy != 0){ // should this be >=? wha if vacancy==0?
                minBidValue = round2Bids.get(vacancy - 1).getAmount() + 1;
            }
            //System.out.println("SecSize=" + sec.getSize() + " SecStud=" + sectionStudentList.size() + " vacancy=" + vacancy + " Round2Bids=" + round2Bids.size() + " minVal=" +minBidValue);
            
            updateMinBid(sec.getCourseID()+"-"+sec.getSectionID(), minBidValue);
            minBidValue = 10;
        }
    }
    
    public void drop(){
        bidList = new HashMap<String, Double>();
    }
    
}
