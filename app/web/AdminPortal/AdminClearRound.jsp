<%@page import="com.bios.model.BidDAO"%>
<%@page import="com.bios.model.Bid"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file = "AdminProtect.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
    <!--<![endif]-->

    <jsp:include page="/GlobalPages/GlobalHead.jsp" >
        <jsp:param name="pageTitle" value="Admin Dashboard" />
    </jsp:include>

    <body>


        <jsp:include page="/GlobalPages/GlobalNavbar.jsp" />

        <jsp:include page="/AdminPortal/AdminSidebar.jsp" >
            <jsp:param name="selected" value="data" />
        </jsp:include>

        <!-- begin #content -->
        <div id="content" class="content">
            <!-- begin breadcrumb -->
            <ol class="breadcrumb pull-right">
                <li><a href="javascript:;">Home</a></li>
            </ol>
            <!-- end breadcrumb -->

            <h1 class="page-header">Admin Portal <small>Clear Round</small></h1>

            <div class="panel panel-inverse">
                <div class="panel-heading">
                    <div class="panel-heading-btn">
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand" data-original-title="" title=""><i class="fa fa-expand"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload" data-original-title="" title=""><i class="fa fa-repeat"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove" data-original-title="" title=""><i class="fa fa-times"></i></a>
                    </div>
                    <h4 class="panel-title">Manage Data Bootstrap</h4>
                </div>
                <div class="panel-body">
                    
                    <%
                        if(session.getAttribute("clearing1ErrorMsg") !=null){
                            String errorMsg = (String)session.getAttribute("clearing1ErrorMsg");
                            out.println("<font color = 'red'><ul>");
                            out.println("<li>" + errorMsg + "</li>");
                            out.println("</ul></font>");
                        }
                        
                        %>
                    <form action="clear_round_1" method="POST" class="margin-bottom-0">
                        
                        <%
                        ArrayList<String> courseSectionList = new ArrayList<>();
                            if (session.getAttribute("courseSectionList") != null) {
                                courseSectionList = (ArrayList<String>) session.getAttribute("courseSectionList");
                                //out.println(session.getAttribute("courseList"));
                            } else {
                                out.println("hello " + session.getAttribute("courseSectionList"));
                            }
                        
                        %>
                        <div class="form-group ">
                            <label for="courseID-sectionID">Course ID-Section ID</label>
                            <select class="form-control" name="courseID-sectionID" id="courseID-sectionID"  >
                                <%for (String s : courseSectionList) {%>
                                <option value="<%= s%>"><%= s%></option> 
                                <%}%>  
                            </select>
                        </div>

                        <div class="login-buttons">
                            <br>
                            <button type="submit" class="btn btn-success btn-block btn-md" >CLEAR ROUND FOR THIS COURSE-SECTION</button>
                        </div>
                        <br>

                    </form>
                    <% if(session.getAttribute("clearing1ErrorMsg") !=null){ %>
                    <div class="panel-body">
                    <table class="table table-striped">
                        <tr>
                            <th>S/N</th><th>User ID</th><th>Course ID</th><th>Section ID</th><th>Amount</th>
                        </tr>
                        <%
                            ArrayList<Bid> bidList = BidDAO.getInstance().getBidList();
                            for (int i = 0; i < bidList.size(); i++) {
                                Bid currentBid = bidList.get(i);
                                String userID = currentBid.getUserID();
                                String courseID = currentBid.getCourseID();
                                String sectionID = currentBid.getSectionID();
                                double amount = currentBid.getAmount();%>
                        <tr class="margin-bottom-3">
                            <td><span class="glyphicon glyphicon-user"></span> <%= i + 1%> </td>
                            <td> <%= userID%> <input type="hidden" name="userID" value="<%= userID %>"/></td>
                            <td> <%= courseID%> <input type ="hidden" name="courseID" value="<%=courseID%>" </td>
                            <td> <%= sectionID%> <input type ="hidden" name="sectionID" value="<%=sectionID%>" </td>
                            <td> <%= amount%> <input type ="hidden" name="amount" value="<%=amount%>" </td>
                        </tr >
                        <%
                            }
                        %>
                    </table>  
                </div>
                     <%
                            }
                        %>       
                    
                </div>
            </div>



        </div>



    </div>
</div>

<!-- end page container -->

<!-- ================== BEGIN BASE JS ================== -->


<!--[if lt IE 9]>
<script src="assets/crossbrowserjs/html5shiv.js"></script>
<script src="assets/crossbrowserjs/respond.min.js"></script>
<script src="assets/crossbrowserjs/excanvas.min.js"></script>
<![endif]-->
<script src="assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="assets/plugins/jquery-cookie/jquery.cookie.js"></script>
<!-- ================== END BASE JS ================== -->

<!-- ================== BEGIN PAGE LEVEL JS ================== -->
<script src="assets/plugins/morris/raphael.min.js"></script>
<script src="assets/plugins/morris/morris.js"></script>
<script src="assets/plugins/jquery-jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="assets/plugins/jquery-jvectormap/jquery-jvectormap-world-merc-en.js"></script>
<script src="assets/plugins/bootstrap-calendar/js/bootstrap_calendar.min.js"></script>
<script src="assets/plugins/gritter/js/jquery.gritter.js"></script>
<script src="assets/js/dropzone.js"></script>
<!-- ================== END PAGE LEVEL JS ================== -->
<script>
    $(document).ready(function () {
        App.init();
    });
</script>

</body>
</html>
