<%@page import="java.util.ArrayList"%>
<%@page import="com.bios.model.Bid"%>
<%@page import="com.bios.model.BidDAO"%>
<%@page import="com.bios.utilities.ConnectionManager"%>
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
            <jsp:param name="selected" value="home" />
        </jsp:include>

        <!-- begin #content -->
        <div id="content" class="content">
            <!-- begin breadcrumb -->
            <ol class="breadcrumb pull-right">
                <li><a href="javascript:;">Home</a></li>
            </ol>
            <!-- end breadcrumb -->

            <h1 class="page-header">Admin Portal <small>Home</small></h1>

            <%
                ConnectionManager manager = new ConnectionManager();
                int round = manager.getBiddingRound();
                String status = manager.getBiddingRoundStatus();
            %>

            <div class="row">
                <div class="col-md-6 col-sm-6 ui-sortable">
                    <div class="widget widget-stats bg-green">
                        <div class="stats-icon"><i class="fa fa-gavel"></i></div>
                        <div class="stats-info">
                            <h4>CURRENT ROUND</h4>
                            <p>
                                
                                <% 
out.println("ROUND " + round + " " + status.toUpperCase());
                                %>
                            </p>
                        </div>
                        <div class="stats-link">
                            <a href="javascript:;">&nbsp;</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-sm-6 ui-sortable">
                    <div class="widget widget-stats bg-red">
                        <div class="stats-icon"><i class="fa fa-clock-o"></i></div>
                        <div class="stats-info">
                            <h4>ROUND ENDS ON</h4>
                            <p>12 DEC 2016</p>	
                        </div>
                        <div class="stats-link">
                            <a href="javascript:;">&nbsp;</a>
                        </div>
                    </div>
                </div>

            </div>


            <div class="panel panel-inverse">
                <div class="panel-heading">
                    <div class="panel-heading-btn">
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand" data-original-title="" title=""><i class="fa fa-expand"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload" data-original-title="" title=""><i class="fa fa-repeat"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove" data-original-title="" title=""><i class="fa fa-times"></i></a>
                    </div>
                    <h4 class="panel-title">Manage Bidding Round</h4>
                </div>
                <div class="panel-body">
                    <p>Are you sure you wish to change the bidding round?</p>
                    <a href="BiddingRoundServlet?advance=true" class="btn btn-md btn-success btn-inverse <%=(round==2 && status.equals("stopped")) ? "disabled" : ""%>">
                        <i class="fa fa-arrow-up pull-left"></i>
                        <%
                        if (status.equals("started")){
                            out.println("Stop round");
                        } else {
                            out.println("Start round");
                        }
                        %>
                        <br>
                    </a>
                </div>
            </div>
            <div class="panel panel-inverse">
                <div class="panel-heading">
                    <div class="panel-heading-btn">
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand" data-original-title="" title=""><i class="fa fa-expand"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload" data-original-title="" title=""><i class="fa fa-repeat"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove" data-original-title="" title=""><i class="fa fa-times"></i></a>
                    </div>
                    <h4 class="panel-title">View all bids</h4>
                </div>
                <div class="panel-body">
                    
                    <table class="table table-striped">
                        <tr>
                            <th>S/N</th><th>User ID</th><th>Course ID</th><th>Section ID</th><th>Amount</th><th>Status</th><th>Delete?</th>
                        </tr>
                        
                        
                        
                        <%
                            ArrayList<Bid> bidList = BidDAO.getInstance().getBidList();
                            for (int i = 0; i < bidList.size(); i++) {
                                Bid currentBid = bidList.get(i);
                                String userID = currentBid.getUserID();
                                String courseID = currentBid.getCourseID();
                                String sectionID = currentBid.getSectionID();
                                double amount = currentBid.getAmount();
                        %>
                                        
                        
                        <tr class="margin-bottom-3">
                            
                            
                            <td><span class="glyphicon glyphicon-user"></span> <%= i + 1%> </td>
                            <td> <form method="post" action="deleteBid">
                              <%= userID%><input type="hidden" name="userID" value="<%= userID %>"/></td>
                            <td> <%= courseID%> <input type ="hidden" name="courseID" value="<%=courseID%>"/></td>
                            <td> <%= sectionID%> <input type ="hidden" name="sectionID" value="<%=sectionID%>"/></td>
                            <td> <%= amount%> <input type ="hidden" name="amount" value="<%=amount%>"/></td>
                            <td> <% 
                                    String labelType = currentBid.getStatus().toUpperCase();
                                    if (labelType.equals("PENDING")){
                                        labelType = "info";
                                    } else if (labelType.equals("FAILED")){
                                        labelType = "danger";
                                    } else {
                                        labelType = "success";
                                    }
                                    out.println("<span class=\"label label-" + labelType +"\">" + currentBid.getStatus().toUpperCase() + "</span>");

                                %>
                            </td>
                            <td> <input type="submit" class="btn btn-danger" value="DELETE"/></td>
                            </form>
                        </tr>
                        <%
                            }
                        %>

                    </table>  
                        
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
