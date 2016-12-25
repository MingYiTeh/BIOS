/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.exceptions;

/**
 * Represents a Data Formatting Exception 
 * @author kongyujian
 */

public class DataFormattingException extends Exception {
  

    /**
     * Constructs a new DataFormattingException with the specified detail message.
     * @param message the detail message
     */
    public DataFormattingException(String message){
        super(message);
    }
    /**
     * Constructs a new DataFormattingException with the specified detail message and cause.
     * @param message the detail message
     * @param throwable the cause(A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DataFormattingException(String message, Throwable throwable){
        super(message, throwable);
    }
    
}
