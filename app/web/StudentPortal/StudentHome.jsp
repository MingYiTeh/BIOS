<%@page import="java.text.DecimalFormat"%>
<%@page import="com.bios.model.BidDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bios.model.Bid"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<%@ include file="../GlobalPages/protect.jsp"%>
<html lang="en">
    <!--<![endif]-->
    <jsp:include page="/GlobalPages/GlobalHead.jsp" >
        <jsp:param name="pageTitle" value="Student Portal" />
    </jsp:include>

    <body>

        <jsp:include page="/GlobalPages/GlobalNavbar.jsp" />

        <jsp:include page="/StudentPortal/StudentSidebar.jsp" >
            <jsp:param name="activateHome" value="active" />
        </jsp:include>

        <!-- begin #content -->
        <div id="content" class="content">
            <!-- begin breadcrumb -->
            <ol class="breadcrumb pull-right">
                <li><a href="javascript:;">Home</a></li>
            </ol>
            <!-- end breadcrumb -->
            <!-- begin page-header -->
            <h1 class="page-header">Student Portal <small>Home</small></h1>
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
                    <h4 class="panel-title">All Bids</h4>
                </div>
                <div class="panel-body">
                    <table class="table table-striped">
                        <%                            
                            String self = (String) session.getAttribute("username");
                            Bid bid = BidDAO.getInstance().findBidByUser(self);
                            if (bid != null) {
                        %>
                        <tr>
                            <th>User ID</th><th>Course ID</th><th>Section ID</th><th>Amount</th><th>Status</th>
                        </tr>

                        <%
                            ArrayList<Bid> bidList = BidDAO.getInstance().getStudentBids(self);
                            DecimalFormat df = new DecimalFormat("0.00");
                            
                            for (int i = 0; i < bidList.size(); i++) {
                                Bid currentBid = bidList.get(i);
                                String userID = currentBid.getUserID();
                                String courseID = currentBid.getCourseID();
                                String sectionID = currentBid.getSectionID();
                                double amount = currentBid.getAmount();
                                String bidAmount = df.format(amount);
                                
                                //if (userID.equals(self) && !currentBid.getStatus().equals("success")) {
                        %>
                        <tr class="margin-bottom-3">
                            <td> <%= userID%> </td>
                            <td> <%= courseID%> </td>
                            <td> <%= sectionID%> </td>
                            <td> <%= bidAmount%> </td>
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
                        </tr >
                            <%
                                    //}
                                }
                            } else {
                            %>
                            <div class="note note-warning">
                                <h4>No bids found!</h4>
                                <p>
                                    You've yet to place a bid for any of the sections, do head over to <a href="bidSection">Bid For Section</a> to place a bid!
                                </p>
                            </div>
                            <%
                                }
                            %>
                    </table>  
                </div>
            </div>
            
            <jsp:include page="/StudentPortal/StudentBidCalendar.jsp"></jsp:include>
            
        </div>
    </div>
    <!-- end page container -->

    <script>
        $(document).ready(function () {
            App.init();
            DashboardV2.init();
        });
        
    </script>    

</body>
</html>
