/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bios.utilities;

import com.bios.model.Bid;
import com.bios.model.BidDAO;
import com.bios.model.CompletedCourse;
import com.bios.model.CompletedCourseDAO;
import com.bios.model.Course;
import com.bios.model.CourseDAO;
import com.bios.model.DataError;
import com.bios.model.Prerequisite;
import com.bios.model.PrerequisiteDAO;
import com.bios.model.Section;
import com.bios.model.SectionDAO;
import com.bios.model.Student;
import com.bios.model.StudentDAO;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;

/**
 * Represents a BootStrapCSV Reader object
 *
 * @author Kong Yu Jian
 */
public class BootstrapCSVReader {

    ConnectionManager manager = null;
    private Connection connection = null;
    private BootstrapValidator validator;
    private String[] tableList = {"student", "course", "section", "coursecompleted", "bid", "prerequisite"};
    private String[] tableHeaderList = {
        "(UserID, UserPassword, NameOfUser, School, EDollar)",
        "(CourseID, School, Title, Description, ExamDate, ExamStart, ExamEnd)",
        "(CourseID, SectionID, DayOfWeek, StartTime, EndTime, Instructor, Venue, Size)",
        "(UserID, CourseID)",
        "(UserID, Amount, CourseID, SectionID, Status)",
        "(CourseID, PrerequisiteID)"
    };
    private int[] totalLines = new int[6];
    private int[] totalErrorLines = {0, 0, 0, 0, 0, 0};

    /**
     * Constructs a BootstrapCSVReader
     */
    public BootstrapCSVReader() {
        validator = new BootstrapValidator();
        manager = new ConnectionManager();
        connection = manager.getConnection();

        try {
            String sql = "insert ignore into BiddingRound(BidRoundID, Round, Status) values('bid_round', 1, 'started');";
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
    * Retrieves data error courses list
    *
    * @param context the context to retrieve from
    * @return the courses data error list with the specified context
    */
    public ArrayList<DataError> retrieveCourses(ServletContext context) {
        ArrayList<DataError> allErrors = new ArrayList<>();
        //This clears the list
        CourseDAO.getInstance().setCourseList(new ArrayList<Course>());

        try {
            CSVReader csvReader = new CSVReader(new FileReader(context.getRealPath("/WEB-INF") + "/unzipped/course.csv"), ',', '"', 1);
            String sqlStmt = "insert ignore into " + tableList[1] + " " + tableHeaderList[1] + " values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlStmt);

            int lineNum = 1;
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine != null) {
                    lineNum++;
                    //Verifying the read data here
                    String courseID = nextLine[0].trim();
                    String school = nextLine[1].trim();
                    String title = nextLine[2].trim();
                    String description = nextLine[3].trim();
                    String examDate = nextLine[4].trim();
                    String examStart = nextLine[5].trim();
                    String examEnd = nextLine[6].trim();

                    ArrayList<String> errors = validator.validateCourses(courseID, school, title, description, examDate, examStart, examEnd);

                    if (errors.size() == 0) {
                        stmt.setString(1, courseID);
                        stmt.setString(2, school);
                        stmt.setString(3, title);
                        stmt.setString(4, description);
                        stmt.setString(5, examDate);
                        stmt.setString(6, examStart);
                        stmt.setString(7, examEnd);

                        stmt.execute();
                        Course course = new Course(courseID, school, title, description, examDate, examStart, examEnd);
                        CourseDAO.getInstance().addCourse(course);
                    } else {
                        totalErrorLines[0] += 1;
                        for (String str : errors) {
                            DataError error = new DataError(str, lineNum);
                            allErrors.add(error);
                        }
                    }
                }
            }
            totalLines[0] = lineNum - 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //TODO this means that the amount given is not a number
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allErrors;
    }

    /**
    * Retrieves data error section list
    *
    * @param context the context to retrieve from
    * @return the section data error list with the specified context
    */
    public ArrayList<DataError> retrieveSections(ServletContext context) {
        ArrayList<DataError> allErrors = new ArrayList<>();
        //This clears the list
        SectionDAO.getInstance().setSectionList(new ArrayList<Section>());

        try {
            CSVReader csvReader = new CSVReader(new FileReader(context.getRealPath("/WEB-INF") + "/unzipped/section.csv"), ',', '"', 1);
            String sqlStmt = "insert ignore into " + tableList[2] + " " + tableHeaderList[2] + " values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlStmt);

            int lineNum = 1;
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine != null) {
                    lineNum++;
                    //Verifying the read data here
                    String courseID = nextLine[0].trim();
                    String sectionID = nextLine[1].trim();
                    String day = nextLine[2].trim();
                    String start = nextLine[3].trim();
                    String end = nextLine[4].trim();
                    String instructor = nextLine[5].trim();
                    String venue = nextLine[6].trim();
                    String size = nextLine[7].trim();

                    ArrayList<String> errors = validator.validateSection(courseID, sectionID, day, start, end, instructor, venue, size);

                    if (errors.size() == 0) {
                        stmt.setString(1, courseID);
                        stmt.setString(2, sectionID);
                        stmt.setInt(3, Integer.parseInt(day));
                        stmt.setString(4, start);
                        stmt.setString(5, end);
                        stmt.setString(6, instructor);
                        stmt.setString(7, venue);
                        stmt.setInt(8, Integer.parseInt(size));

                        stmt.execute();
                        Section section = new Section(courseID, sectionID, Integer.parseInt(day), start, end, instructor, venue, Integer.parseInt(size));
                        SectionDAO.getInstance().addSection(section);
                    } else {
                        totalErrorLines[1] += 1;
                        for (String str : errors) {
                            DataError error = new DataError(str, lineNum);
                            allErrors.add(error);
                        }
                    }
                }
            }
            totalLines[1] = lineNum - 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //TODO this means that the amount given is not a number
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allErrors;
    }

    /**
    * Retrieves data error students list
    *
    * @param context the context to retrieve from
    * @return the students data error list with the specified context
    */
    public ArrayList<DataError> retrieveStudents(ServletContext context) {
        ArrayList<DataError> allErrors = new ArrayList<>();
        //This clears the list
        StudentDAO.getInstance().setStudentList(new ArrayList<Student>());

        try {
            CSVReader csvReader = new CSVReader(new FileReader(context.getRealPath("/WEB-INF") + "/unzipped/student.csv"), ',', '"', 1);
            String sqlStmt = "insert ignore into " + tableList[0] + " " + tableHeaderList[0] + " values (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlStmt);

            int lineNum = 1;
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine != null) {
                    lineNum++;

                    String userID = nextLine[0].trim();
                    String password = nextLine[1].trim();
                    String name = nextLine[2].trim();
                    String school = nextLine[3].trim();
                    String eDollar = nextLine[4].trim();

                    ArrayList<String> errors = validator.validateStudents(userID, password, name, school, eDollar);

                    if (errors.size() == 0) {
                        stmt.setString(1, userID);
                        stmt.setString(2, password);
                        stmt.setString(3, name);
                        stmt.setString(4, school);
                        stmt.setDouble(5, Double.parseDouble(eDollar));

                        stmt.execute();
                        Student student = new Student(userID, password, name, school, Double.parseDouble(eDollar));
                        StudentDAO.getInstance().addStudent(student);
                    } else {
                        totalErrorLines[2] += 1;
                        for (String str : errors) {
                            DataError error = new DataError(str, lineNum);
                            allErrors.add(error);
                        }
                    }
                }
            }
            totalLines[2] = lineNum - 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            //TODO this means that the amount given is not a number
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allErrors;
    }

    /**
   * Retrieves data error bids list
   *
   * @param context the context to retrieve from
   * @return the bids data error list with the specified context
   */
    public ArrayList<DataError> retrieveBids(ServletContext context) {
        manager = new ConnectionManager();
        ArrayList<DataError> allErrors = new ArrayList<>();
        //This clears the list
        BidDAO.getInstance().setBidList(new ArrayList<>());

        try {
            CSVReader csvReader = new CSVReader(new FileReader(context.getRealPath("/WEB-INF") + "/unzipped/bid.csv"), ',', '"', 1);
            String sqlStmt = "insert ignore into " + tableList[4] + " " + tableHeaderList[4] + " values (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlStmt);

            int lineNum = 1;
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine != null) {
                    lineNum++;
                    //Verifying the read data here
                    String userID = nextLine[0].trim();
                    String amount = nextLine[1].trim();
                    String courseID = nextLine[2].trim();
                    String sectionID = nextLine[3].trim();

                    Student student = StudentDAO.retrieve(userID);
                    ArrayList<Bid> existingBidList = BidDAO.getInstance().getStudentBids(userID);
                    boolean existingBid = false;
                    ArrayList<String> errors = new ArrayList<>();
                    for (Bid b : existingBidList) {
                        if (b.getCourseID().equals(courseID)) {
                            //System.out.println("existing bid"+lineNum);
                            errors = validator.validateExistingBid(userID, amount, courseID, sectionID);
                            existingBid = true;
                            break;
                        }
                    }
                    if (!existingBid) {
                        errors = validator.validateBids(userID, amount, courseID, sectionID);
                    }

                    if (errors.size() == 0 && !existingBid) {
                        stmt.setString(1, userID);
                        stmt.setDouble(2, Double.parseDouble(amount));
                        stmt.setString(3, courseID);
                        stmt.setString(4, sectionID);
                        stmt.setString(5, "pending");

                        stmt.execute();
                        Bid bid = new Bid(userID, Double.parseDouble(amount), courseID, sectionID, "pending");
                        BidDAO.getInstance().addBid(bid);
                        double remain = student.geteDollar() - Double.parseDouble(amount);

                        student.seteDollar(remain);
                        // Linguistically this doesn't make sense, but we're using this as a quick fix, and as an opposite to
                        // .minusEDollar(userID, lineNum);
                        manager.setEDollar(userID, remain);

                    } else if (errors.size() == 0 && existingBid) {
                        //System.out.println("existing bid"+lineNum);
                        // no error and is an existing bid
                        // drop current bid
                        Bid prevBid = BidDAO.getInstance().getStudentBidWithCourseID(userID, courseID);
                        double amt = prevBid.getAmount();
                        String prevSectionID = prevBid.getSectionID();
                        //refund money
                        student.seteDollar(student.geteDollar() + amt);
                        manager.refundEDollar(userID, amt);

                        BidDAO.getInstance().deleteBid(userID, courseID, prevSectionID);
                        BidDAO.getInstance().deleteBidFromDB(userID, courseID, prevSectionID);
                        //add bid
                        stmt.setString(1, userID);
                        stmt.setDouble(2, Double.parseDouble(amount));
                        stmt.setString(3, courseID);
                        stmt.setString(4, sectionID);
                        stmt.setString(5, "pending");

                        stmt.execute();
                        Bid bid = new Bid(userID, Double.parseDouble(amount), courseID, sectionID, "pending");
                        BidDAO.getInstance().addBid(bid);

                        student.seteDollar(student.geteDollar() - Double.parseDouble(amount));
                        // Linguistically this doesn't make sense, but we're using this as a quick fix, and as an opposite to
                        // .minusEDollar(userID, lineNum);
                        manager.refundEDollar(userID, -Double.parseDouble(amount));
                    } else {
                        totalErrorLines[3] += 1;
                        for (String str : errors) {
                            DataError error = new DataError(str, lineNum);
                            allErrors.add(error);
                        }
                    }
                }
            }
            totalLines[3] = lineNum - 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //TODO this means that the amount given is not a number
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allErrors;
    }

    /**
    * Retrieves prerequisites data error courses list
    *
    * @param context the context to retrieve from
    * @return the prerequisites data error list with the specified context
    */
    public ArrayList<DataError> retrievePrerequisites(ServletContext context) {
        ArrayList<DataError> allErrors = new ArrayList<>();
        //This clears the list
        PrerequisiteDAO.getInstance().setPrerequisiteList(new ArrayList<>());

        try {
            CSVReader csvReader = new CSVReader(new FileReader(context.getRealPath("/WEB-INF") + "/unzipped/prerequisite.csv"), ',', '"', 1);
            String sqlStmt = "insert ignore into " + tableList[5] + " " + tableHeaderList[5] + " values (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlStmt);

            int lineNum = 1;

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                lineNum++;
                //Verifying the read data here
                String courseID = nextLine[0].trim();
                String prerequisiteID = nextLine[1].trim();

                ArrayList<String> errors = validator.validatePrerequisites(courseID, prerequisiteID);
                if (errors.size() == 0) {
                    stmt.setString(1, courseID);
                    stmt.setString(2, prerequisiteID);

                    stmt.execute();
                    Prerequisite prerequisite = new Prerequisite(courseID, prerequisiteID);
                    PrerequisiteDAO.getInstance().addPrerequisite(prerequisite);
                } else {
                    totalErrorLines[4] += 1;
                    for (String str : errors) {
                        DataError error = new DataError(str, lineNum);
                        allErrors.add(error);
                    }
                }
            }
            totalLines[4] = lineNum - 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allErrors;
    }

    /**
    * Retrieves completed courses data error courses list
    *
    * @param context the context to retrieve from
    * @return the completed courses data error list with the specified context
    */
    public ArrayList<DataError> retrieveCompletedCourses(ServletContext context) {
        ArrayList<DataError> allErrors = new ArrayList<>();
        //This clears the list
        CompletedCourseDAO.getInstance().setCompletedCourseList(new ArrayList<>());

        try {
            CSVReader csvReader = new CSVReader(new FileReader(context.getRealPath("/WEB-INF") + "/unzipped/course_completed.csv"), ',', '"', 1);
            String sqlStmt = "insert ignore into " + tableList[3] + " " + tableHeaderList[3] + " values (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlStmt);

            int lineNum = 1;
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine != null) {
                    lineNum++;
                    //Verifying the read data here
                    String userID = nextLine[0].trim();
                    String courseID = nextLine[1].trim();

                    ArrayList<String> errors = validator.validateCourseCompletion(userID, courseID);

                    if (errors.size() == 0) {
                        stmt.setString(1, userID);
                        stmt.setString(2, courseID);

                        stmt.execute();
                        CompletedCourse completedCourse = new CompletedCourse(userID, courseID);
                        CompletedCourseDAO.getInstance().addCompletedCourse(completedCourse);
                    } else {
                        totalErrorLines[5] += 1;
                        for (String str : errors) {
                            DataError error = new DataError(str, lineNum);
                            allErrors.add(error);
                        }
                    }
                }
            }
            totalLines[5] = lineNum - 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allErrors;
    }

    /**
   * Retrieves the total number of lines
   *
   * @return the total number of lines
   */
    public int[] getTotalLines() {
        return totalLines;
    }

    /**
   * Retrieves the total number of lines of error
   *
   * @return the total number of lines of errors
   */
    public int[] getTotalErrorLines() {
        return totalErrorLines;
    }

}
