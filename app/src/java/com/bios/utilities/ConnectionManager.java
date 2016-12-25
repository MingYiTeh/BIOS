/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Bid;
import com.bios.model.Course;
import com.bios.model.SectionStudent;
import com.bios.model.SectionStudentDAO;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a Connection Manager object
 * @author Kong Yu Jian
 */
public class ConnectionManager {

    private static final String CONNECTION_ADDRESS = "localhost";
    private static final int CONNECTION_PORT = 3306;
    private static final String CONNECTION_DATABASE = "bios";
    private static final String CONNECTION_USERNAME = "root";
    private static final String CONNECTION_PASSWORD = "";

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private Connection connection = null;

    /**
     * Constructs a ConnectionManager
     */
    public ConnectionManager(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
            return;
        }

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves the connection object
     * @return the connection
     */
    public Connection getConnection() {
        if (connection == null){
            try {
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("Connection failed, may want to check your login credentials!");
            }
        }
        return connection;
    }

    /**
     * Delete all the tables, and drop them from database
     */
    public void deleteAllTables(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
            return;
        }

        Statement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }
            SectionStudentDAO.getInstance().setSectionStudentList(new ArrayList<SectionStudent>());

            stmt = connection.createStatement();
            String dropStudentTable = "delete from bios.student;";
            String dropBidTable = "delete from bios.bid;";
            String dropBiddingRoundTable = "delete from bios.biddinground;";
            String dropCompletedCourseTable = "delete from bios.coursecompleted;";
            String dropCourseTable = "delete from bios.course;";
            String dropSectionTable = "delete from bios.section;";
            String dropSectionStudentTable = "delete from bios.sectionstudent;";
            String dropPrerequisiteTable = "delete from bios.prerequisite;";

            stmt.execute(dropPrerequisiteTable);
            stmt.execute(dropSectionStudentTable);
            stmt.execute(dropCompletedCourseTable);
            stmt.execute(dropBidTable);
            stmt.execute(dropBiddingRoundTable);
            stmt.execute(dropSectionTable);
            stmt.execute(dropCourseTable);
            stmt.execute(dropStudentTable);

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }

        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection

        }
    }

    /**
     * Retrieves the bidding round
     * @return the bidding round number
     */
    public int getBiddingRound(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
            return 0;
        }

        Statement stmt = null;
        ResultSet rs = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            stmt = connection.createStatement();
            String select = "select * from bios.biddinground where BidRoundID='bid_round';";


            rs = stmt.executeQuery(select);
            while (rs.next()){
                int round = rs.getInt("Round");
                return round;
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection

        }
        // If we return 0, it means round has not yet started, or there was an error
        return 0;
    }

    /**
     * Retrieves the bidding round status
     * @return the string of the bidding round status
     */
    public String getBiddingRoundStatus(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
            return "";
        }

        ResultSet rs = null;
        Statement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            stmt = connection.createStatement();
            String select = "select * from bios.biddinground where BidRoundID='bid_round';";


            rs = stmt.executeQuery(select);
            while (rs.next()){
                String roundStatus = rs.getString("Status");
                return roundStatus;
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection

        }
        // If we return 0, it means round has not yet started, or there was an error
        return "";
    }

    /**
     * Modify the bidding round by increasing the round number, and going from round started, to round stopped
     */
    public void modifyBiddingRound(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        ResultSet rs = null;
        Statement stmt = null;

        try {
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            stmt = connection.createStatement();
            String select = "select * from bios.biddinground where BidRoundID='bid_round';";

            rs = stmt.executeQuery(select);
            while (rs.next()){
                int round = rs.getInt("Round");
                String status = rs.getString("Status");
                System.out.println(round);

                if (status.equals("stopped") && round == 0){
                    round = 1;
                    status = "started";
                } else if (status.equals("started") && round == 1){
                    round = 1;
                    status = "stopped";
                } else if (status.equals("stopped") && round == 1){
                    round = 2;
                    status = "started";
                } else if (status.equals("started") && round == 2){
                    round = 2;
                    status = "stopped";
                }

                String update = "update bios.biddinground set Round=" + round + ", Status='" + status + "' where BidRoundID='bid_round'";
                stmt.execute(update);

                break;
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

    /**
     * Sets the e dollar
     * @param userID the user's ID to set
     * @param amount the amount to set
     */
    public void setEDollar(String userID, double amount){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }


            String update = "update student set EDollar=? where UserID=?;";
            stmt = connection.prepareStatement(update);

            stmt.setDouble(1, amount);
            stmt.setString(2, userID);

            stmt.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

    /**
     * Refund the e dollar
     * @param userID the user's ID to set
     * @param amount the amount to set
     */
    public void refundEDollar(String userID, double amount){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }


            String update = "update student set EDollar = EDollar + ? where UserID=?;";
            stmt = connection.prepareStatement(update);

            stmt.setDouble(1, amount);
            stmt.setString(2, userID);


            stmt.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

    /**
     * Add the bid and capture it
     * @param bid the bid to be added
     */
    public void addBid(Bid bid){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }


            String insert = "insert into bid (UserID, Amount, CourseID, SectionID, Status) VALUES (?,?,?,?,?);";
            stmt = connection.prepareStatement(insert);

            stmt.setString(1, bid.getUserID());
            stmt.setDouble(2, bid.getAmount());
            stmt.setString(3, bid.getCourseID());
            stmt.setString(4, bid.getSectionID());
            stmt.setString(5, bid.getStatus());


            stmt.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

    /**
     * Update a bid in the database which is specified by its userID, courseID, sectionID
     * @param userID the user's ID to update
     * @param amount the amount to update
     * @param courseID the course ID to update
     * @param sectionID the section ID to update
     * @param status the status to update
     */
    public void updateBid(String userID, double amount, String courseID, String sectionID, String status){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            String update = "update bios.bid set UserID=?, Amount=?, CourseID=?, SectionID=?, Status=? where UserID=? and CourseID=?;";
            stmt = connection.prepareStatement(update);

            stmt.setString(1, userID);
            stmt.setDouble(2, amount);
            stmt.setString(3, courseID);
            stmt.setString(4, sectionID);
            stmt.setString(5, status);
            stmt.setString(6, userID);
            stmt.setString(7, courseID);
            //stmt.setString(8, sectionID);

            stmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

    /**
     * Deletes a bid in the database which is specified by its userID, courseID, sectionID
     * @param userID the user's ID to delete
     * @param courseID the course's ID to delete
     * @param sectionID the section's ID to delete
     */
    public void deleteBid(String userID, String courseID, String sectionID){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            String delete = "delete from bios.bid where UserID=? and CourseID=? and SectionID=?;";
            stmt = connection.prepareStatement(delete);

            stmt.setString(1, userID);
            stmt.setString(2, courseID);
            stmt.setString(3, sectionID);

            stmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }


    /**
     * Adds a section student object into the database
     * @param sectionStudent the section student to be added
     */
    public void addSectionStudent(SectionStudent sectionStudent){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }


            String insert = "insert into sectionstudent (CourseID, UserID, SectionID, Amount) VALUES (?,?,?,?);";
            stmt = connection.prepareStatement(insert);

            stmt.setString(1, sectionStudent.getCourseID());
            stmt.setString(2, sectionStudent.getUserID());
            stmt.setString(3, sectionStudent.getSectionID());
            stmt.setDouble(4, sectionStudent.getAmount());

            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

    /**
     * Retrieves all bids in descending order
     * @return the bid list to be returned, and retrieved in descending order
     */
    public ArrayList<Bid> retrieveAllBids(){
        ArrayList<Bid> allBids = new ArrayList<Bid>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            String sql = "select * from bios.bid order by Amount desc;";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()){
                String userID = rs.getString("UserID");
                String courseID = rs.getString("CourseID");
                String sectionID = rs.getString("SectionID");
                double amount = rs.getDouble("Amount");
                String status = rs.getString("Status");

                Bid b = new Bid(userID, amount, courseID, sectionID, status);
                allBids.add(b);
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
        return allBids;
    }

    /**
     * Deletes a SectionStudent object from the database
     * @param userID the user's ID to delete
     * @param amount the amount to delete
     * @param courseID the course ID to delete
     * @param sectionID the section ID to delete
     */
     public void deleteSectionStudent(String userID, double amount, String courseID, String sectionID){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("JDBC driver not found!");
        }

        PreparedStatement stmt = null;

        try {
            // TODO grab all these code and put it into a remove method
            if (connection == null){
                connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                    + CONNECTION_ADDRESS + ":"
                    + CONNECTION_PORT
                    + "/" + CONNECTION_DATABASE,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);
            }

            String delete = "delete from bios.sectionstudent where UserID=? and CourseID=? and SectionID=?;";
            stmt = connection.prepareStatement(delete);

            stmt.setString(1, userID);
            stmt.setString(2, courseID);
            stmt.setString(3, sectionID);


            stmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Connection failed, may want to check your login credentials!");
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

        //why need this when we already have try-catch?
        if (connection != null){
            // This means that we have successfully connected to Database
        } else {
            // We have failed to make database connection
        }
    }

}
