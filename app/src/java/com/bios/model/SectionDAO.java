/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

import com.bios.model.Section;
import java.util.ArrayList;

/**
 * Represents a Section Data Access Object that contains all the sections
 *
 * @author kongyujian
 */
public class SectionDAO {

    private static SectionDAO instance = null;
    private static ArrayList<Section> sectionList;

    public SectionDAO() {
        sectionList = new ArrayList<>();
    }

    /**
     * @return the instance of a SectionDAO,
     * if there isn't one, a new sectionList and SectionDAO will be created and returned
     */
    public static SectionDAO getInstance() {
        if (instance == null) {
            sectionList = new ArrayList<>();
            instance = new SectionDAO();
        }
        return instance;
    }

    /**
     * Retrieves the list of all the Section objects
     * @return the sectionList
     */
    public ArrayList<Section> getSectionList() {
        return sectionList;
    }

    /**
     * Sets specified sectionList to a list that contains all the Section Objects
     * @param sectionList the section list to set
     */
    public void setSectionList(ArrayList<Section> sectionList) {
        this.sectionList = sectionList;
    }


    /**
     * Retrieves a section with specified course ID and section ID
     * @param courseID Course ID of Section
     * @param sectionID Section ID of Section
     * @return the section with the specified course ID and section ID, null otherwise
     */

    public Section findSection(String courseID, String sectionID) {
        Section result = null;
        for (Section section : sectionList) {
            if (section.getSectionID().equals(sectionID) && section.getCourseID().equals(courseID)) {
                return section;

            }
        }
        return result;
    }

    /**
     * Retrieves the ArrayList of the Section given the specific courseID and sectionID
     * @param courseID Course ID of Section
     * @param sectionID Section ID of Section
     * @return the ArrayList of the Section given the specific courseID and sectionID
     */
    public ArrayList<Section> getSections(String courseID, String sectionID){
        ArrayList<Section> result = new ArrayList<>();
        for (Section section : sectionList) {
            if (section.getSectionID().equals(sectionID) && section.getCourseID().equals(courseID)) {
                result.add(section);
            }
        }
        return result;
    }

    /**
     * Adds the specified section to the section list in SectionDAO
     * @param section section to be added
     */
    public void addSection(Section section){
        sectionList.add(section);
    }


}
