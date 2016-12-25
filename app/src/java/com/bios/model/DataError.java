/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.model;

/**
 * Represents a DataError Object
 * @author kongyujian
 */
public class DataError {
    
    private String description;
    private int lineNum;
    
    /**
     * Constructs a DataError object with specified error description and the line number where the error occurs
     * @param description description of the data error 
     * @param lineNum line at which the error occurs
     */
    public DataError(String description, int lineNum){
        this.description = description;
        this.lineNum = lineNum;
    }
    /**
     * Retrieves the description of the Data Error
     * @return description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the description of the Data Error
     * @param description description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the line number where the Data Error occurs
     * @return lineNum 
     */
    public int getLineNum() {
        return lineNum;
    }

    /**
     * Sets the line number where the Data Error occurs
     * @param lineNum line number to set
     */
    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
    
}
