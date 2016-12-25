<%@page import="com.bios.model.SectionStudentDAO"%>
<%@page import="com.bios.model.SectionStudent"%>
<%@page import="com.bios.model.BidDAO"%>
<%@page import="com.bios.model.Bid"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<%@ include file="../GlobalPages/protect.jsp"%>
<html lang="en">
    <!--<![endif]-->
    <jsp:include page="/GlobalPages/GlobalHead.jsp" >
        <jsp:param name="pageTitle" value="Bid for Section" />
    </jsp:include>

    <body>

        <jsp:include page="/GlobalPages/GlobalNavbar.jsp" />

        <jsp:include page="/StudentPortal/StudentSidebar.jsp" >
            <jsp:param name="activateDropSection" value="active" />
        </jsp:include>


        <!-- begin #content -->
        <div id="content" class="content">
            <!-- begin breadcrumb -->
            <ol class="breadcrumb pull-right">

                <li><a href="javascript:;">Home</a></li>
            </ol>
            <!-- end breadcrumb -->
            <!-- begin page-header -->
            <h1 class="page-header">Student Portal <small>Drop Section</small></h1>
            <!-- end page-header -->
            <!-- begin row -->

            <div class="panel panel-inverse">
                <div class="panel-heading">
                    <div class="panel-heading-btn">
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand" data-original-title="" title=""><i class="fa fa-expand"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload" data-original-title="" title=""><i class="fa fa-repeat"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove" data-original-title="" title=""><i class="fa fa-times"></i></a>
                    </div>
                    <h4 class="panel-title">View &amp; Drop Section</h4>
                </div>
                <div class="panel-body">
                    <table class="table table-striped">
                        <%
                            String self = (String)session.getAttribute("username");
                            SectionStudent sectionStudent = SectionStudentDAO.getInstance().findSectionByUser(self);
                            if (sectionStudent != null){
                        %>
                        <tr>
                            <th>User ID</th><th>Course ID</th><th>Section ID</th><th>Amount</th><th>Status</th><th>Delete?</th>
                        </tr>
                        
                        <%
                                ArrayList<SectionStudent> sectionStudentList = SectionStudentDAO.getInstance().getSectionStudentList();

                                for (int i = 0; i < sectionStudentList.size(); i++) {
                                    SectionStudent currentSectionStudent = sectionStudentList.get(i);
                                    String userID = currentSectionStudent.getUserID();
                                    String courseID = currentSectionStudent.getCourseID();
                                    String sectionID = currentSectionStudent.getSectionID();
                                    double amount = currentSectionStudent.getAmount();
                                    System.out.println("success: " + userID);
                                    if (userID.equals(self)){

                        %>
                        <tr class="margin-bottom-3">
                        <form action ="student_drop_section" method ="post">
                            <td> <%= userID%> <input type="hidden" name="userID" value="<%= userID %>"/></td>
                            <td> <%= courseID%> <input type ="hidden" name="courseID" value="<%=courseID%>" </td>
                            <td> <%= sectionID%> <input type ="hidden" name="sectionID" value="<%=sectionID%>" </td>
                            <td> <%= amount%> <input type ="hidden" name="amount" value="<%=amount%>" </td>
                            <td><span class="label label-success">SUCCESS</span></td>
                            <td> <input type="submit" class="btn btn-danger" value="DROP"> </td>
                        </tr >
                        <%
                                    }
                                }
                            } else {
                        %>
                        <div class="note note-warning">
                            <h4>No successful bids found!</h4>
                            <p>
                                You've yet to place a successful bid, do head over to <a href="bidSection">Bid For Section</a> to place a bid!
                            </p>
                        </div>
                        <%
                            }
                        %>
                    </table>  
                </div>
            </div>

            <jsp:include page="/StudentPortal/StudentBidCalendar.jsp"></jsp:include>

            <!--This modal is shown when dropping of bid is a success-->
            <div class="modal modal-message fade" id="modal-success">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title"><i class="fa fa-upload fa-2x"></i> Upload was successful!</h4>
                        </div>
                        <div class="modal-body">
                            <p> <% out.println((String)session.getAttribute("sudent_drop_successful")); %></p>
                        </div>
                        <div class="modal-footer">
                            <a href="javascript:;" class="btn btn-sm btn-primary" data-dismiss="modal">Close</a>
                        </div>
                    </div>
                </div>
            </div>

            <%
                String dropSuccess = (String) session.getAttribute("student_drop_successful");
                out.println(dropSuccess);
                // If an upload was never made
                if (dropSuccess == null) {
                    // This means a user has just only log-in, and we show welcome message!
                    out.println("<script>DashboardV2.initNotification();</script>");
                } else {
                    // If an upload was made, and it is successful
                    out.println("<script>$('#modal-success').modal('show')</script>");
                    session.setAttribute("student_drop_successful", null);
                }


            %>

        </div>
        <!-- end page container -->

        <script>
            $(document).ready(function () {
                App.init();
                DashboardV2.init();
                Calendar.init();
                console.log($('select'))
                $('select').select2();
                
            });
        </script>
        

    </body>
</html>
