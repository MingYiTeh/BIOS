/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import java.io.File;
import javax.servlet.ServletContext;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Represents a Bootstrap Unzipper object
 * @author Kong Yu Jian
 */
public class BootstrapUnzipper {
    public BootstrapUnzipper(){


    }

    /**
    * Delete bootstrap folder
    *
    * @param context the context to delete from
    */
    public void deleteBootstrapFolder(ServletContext context){
        File bootstrapZip = new File(context.getRealPath("/WEB-INF") + "/bootstrap.zip");
        if (bootstrapZip.exists())
            bootstrapZip.delete();
        recursiveDelete(new File(context.getRealPath("/WEB-INF") + "/unzipped"));
    }

    /**
    * Unzips the folder containing all the CSVs for bootstrapping
    *
    * @param context the context to unzip from
    */
    public void unzip(ServletContext context){
        String source = context.getRealPath("/WEB-INF") + "/bootstrap.zip";
        String destination = context.getRealPath("/WEB-INF") + "/unzipped";
        String password = "password"; // Not needed for our case

        try {
            ZipFile zipFile = new ZipFile(source);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
    * Recursive delete the file, the intent is to remove all the files after they have been uploaded
    *
    * @param file the folder to recursively delete from
    */
    public void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return;

        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        file.delete();
    }

}
