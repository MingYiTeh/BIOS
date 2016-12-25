<%@page import="java.text.DecimalFormat"%>
<%@page import="com.bios.model.BidDAO"%>
<%@page import="com.bios.model.Bid"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.bios.model.Section"%>
<%@page import="com.bios.model.SectionStudentDAO"%>
<%@page import="com.bios.model.SectionDAO"%>
<%@page import="com.bios.utilities.ConnectionManager"%>
<%@page import="com.bios.model.Student"%>
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
            <jsp:param name="activateBidSection" value="active" />
        </jsp:include>


        <!-- begin #content -->
        <div id="content" class="content">
            <!-- begin breadcrumb -->
            <ol class="breadcrumb pull-right">

                <li><a href="javascript:;">Home</a></li>
            </ol>
            <!-- end breadcrumb -->
            <!-- begin page-header -->
            <h1 class="page-header">Student Portal <small>Bid For Section</small></h1>
            <!-- end page-header -->
            <!-- begin row -->

            <%  
                ConnectionManager manager = new ConnectionManager();
                int round = manager.getBiddingRound();
                String status = manager.getBiddingRoundStatus();
            %>

            <div class="row">
                <div class="col-md-<%=(round == 1) ? 6 : 4%> col-sm-6 ui-sortable">
                    <div class="widget widget-stats bg-green">
                        <div class="stats-icon"><i class="fa fa-gavel"></i></div>
                        <div class="stats-info">
                            <h4>CURRENT ROUND</h4>
                            <p><%= round + " " + status.toUpperCase()%></p>	
                        </div>
                        <div class="stats-link">
                            <a href="javascript:;">&nbsp;</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-<%=(round == 1) ? 6 : 4%> col-sm-6 ui-sortable">
                    <div class="widget widget-stats bg-blue">
                        <div class="stats-icon"><i class="fa fa-user"></i></div>
                        <div class="stats-info">
                            <h4>TOTAL AVAILABLE SEATS</h4>
                            <p id="total-vacancy">
                                <%
                                    if (round == 2) {
//                                        String courseID = request.getAttribute("");
//                                        Section section = sectionDAO.findSection(courseID,sectionID);
//                                        int totalAvailableSeats = section.getSize()-sectionStudentDAO.getSectionStudentList().size();
                                        out.println("Select Course To View");
                                    } else {
                                        out.println("Data Not Available");
                                    }
                                %>
                            </p>	
                        </div>
                        <div class="stats-link">
                            <a href="javascript:;">&nbsp;</a>
                        </div>
                    </div>
                </div>

            <%
                if (round == 2){
            %>               
                <div class="col-md-4 col-sm-12 ui-sortable">
                    <div class="widget widget-stats bg-red">
                        <div class="stats-icon"><i class="fa fa-dollar"></i></div>
                        <div class="stats-info">
                            <h4>MIN BID AMOUNT</h4>
                            <p id="min-bid-amount">
                                <%
                                    if (round == 2) {
                                        out.println("Select Course To View");
                                    } else {
                                        out.println("Data Not Available");
                                    }
                                %>
                            </p>	
                        </div>
                        <div class="stats-link">
                            <a href="javascript:;">&nbsp;</a>
                        </div>
                    </div>
                </div>
            <%
            }
            %>
            </div>
            
            <!-- Form code here -->
            <% if (status.equals("started")) { %>
            <div class="panel panel-inverse">
                <div class="panel-heading">
                    <div class="panel-heading-btn">
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand" data-original-title="" title=""><i class="fa fa-expand"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload" data-original-title="" title=""><i class="fa fa-repeat"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove" data-original-title="" title=""><i class="fa fa-times"></i></a>
                    </div>
                    <h4 class="panel-title">Place Your Bids</h4>
                </div>
                <div class="panel-body">
                    <form action="bidSection" method="POST" class="margin-bottom-0">

                        <%
                            ArrayList<String> courseSectionList = new ArrayList<>();
                            if (session.getAttribute("courseSectionList") != null) {
                                courseSectionList = (ArrayList<String>) session.getAttribute("courseSectionList");
                            }

                        %>

                        <div class="form-group ">
                            <label for="courseID-sectionID">Course ID - Section ID</label>
                            <select class="form-control" name="courseID-sectionID" id="courseID-sectionID"  >
                                <%for (String s : courseSectionList) {%>
                                <option value="<%= s%>"><%= s%></option> 
                                <%}%>  
                            </select>
                        </div>

                        <div class ="form-group ">
                            <label for="amount">Bid Amount(e$)</label>
                            <input type="text" name="amount" class="form-control col-xs-6"  placeholder="Bid Amount (e$)"/>
                        </div>

                        <div class="login-buttons">
                            <br>
                            <br>
                            <button type="submit" class="btn btn-success btn-block btn-md" >PLACE BID</button>
                        </div>
                        <br>

                    </form>
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
                    <h4 class="panel-title">Current Bids</h4>
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

                                if (userID.equals(self) && !currentBid.getStatus().equals("success")) {
                        %>
                        <tr class="margin-bottom-3">
                        <form action ="student_drop_bid" method ="post">
                            <td> <%= userID%> <input type="hidden" name="userID" value="<%= userID%>"/></td>
                            <td> <%= courseID%> <input type ="hidden" name="courseID" value="<%=courseID%>" </td>
                            <td> <%= sectionID%> <input type ="hidden" name="sectionID" value="<%=sectionID%>" </td>
                            <td> <%= amount%> <input type ="hidden" name="amount" value="<%=amount%>" </td>
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
                        </form>
                            </tr >
                            <%
                                    }
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

            <% } 
                if (round == 2 && status.equals("started")) {
            %>
            <div class="panel panel-inverse">
                <div class="panel-heading">
                    <div class="panel-heading-btn">
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand" data-original-title="" title=""><i class="fa fa-expand"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload" data-original-title="" title=""><i class="fa fa-repeat"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                        <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove" data-original-title="" title=""><i class="fa fa-times"></i></a>
                    </div>
                    <h4 class="panel-title">Live Bidding Updates</h4>
                </div>
                <div class="panel-body">
                    <table id="table-data" class="table">
                        <tr>
                            <th>Ranking</th><th>Bid Price</th><th>State</th>
                        </tr>
                    </table>
                </div>
            </div>
            <%
                }
            %>

            <jsp:include page="/StudentPortal/StudentBidCalendar.jsp"></jsp:include>

            <div class="modal modal-message fade" id="modal-success">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title"><i class="fa fa-gavel fa-2x"></i> Placing of bid was successful!</h4>
                        </div>
                        <div class="modal-body">
                            <p>
                                <%
                                    String message = (String) session.getAttribute("bid_success");
                                    out.println(message + "!");
                                %>
                            </p>
                        </div>
                        <div class="modal-footer">
                            <a href="javascript:;" class="btn btn-sm btn-primary" data-dismiss="modal">Close</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal modal-message fade" id="modal-error">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title"><i class="fa fa-exclamation fa-2x" aria-hidden="true""></i> Bid is invalid!</h4>
                        </div>
                        <div class="modal-body">
                            <h5>Errors in bid:</h5>
                            <table>    
                                <% 
                                    if(session.getAttribute("errors")!=null){
                                    for (String s : (ArrayList<String>) session.getAttribute("errors")) {
                                %>
                            <tr>
                                <td><%=s%></td>
                            </tr>
                            <%}}%>
                            </table>
                            
                        </div>
                        <div class="modal-footer">
                            <a href="javascript:;" class="btn btn-sm btn-primary" data-dismiss="modal">Close</a>
                        </div>
                    </div>
                </div>
            </div>

            <%
                String bidSuccess = (String) session.getAttribute("bid_success");
                
                // If an upload was never made
                if (session.getAttribute("errors")!=null) {
                    // This means a user has just only log-in, and we show welcome message!
                    out.println("<script>$('#modal-error').modal('show')</script>");
                    session.setAttribute("errors",null);
                } 
                if(bidSuccess!=null) {
                    // If a bid was made, and it is successful
                    out.println("<script>$('#modal-success').modal('show')</script>");
                    session.setAttribute("bid_success", null);
                }
            %>
            

        </div>
        <!-- end page container -->

        <script>

            $(document).ready(function () {
                App.init();
                DashboardV2.init();
                Calendar.init();
                var $select2 = $('#courseID-sectionID').select2();

<%
    if (session.getAttribute("course-sectionID") != null){
        String courseSectionID = (String) session.getAttribute("course-sectionID");
        out.println("performRequest('" + courseSectionID + "')");
        out.println("$('#courseID-sectionID').select2('val', '" + courseSectionID + "');");
        session.removeAttribute("course-sectionID");
    }
%>
                $('#courseID-sectionID').on("select2-selecting", function (e) {
                    performRequest(e.val);
                });
                
                function performRequest(courseSectionID){
                    var baseURL = window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/"));
                    
                    var postData = {
                        courseSectionID: courseSectionID
                    };
                    
                    $.ajax({
                        method: "GET",
                        url: window.location.origin + baseURL + "/getLiveBiddingTable",
                        data: postData
                    }).done(function (data) {
                        console.log(data);

                        $(".to-remove").remove();
                        $("#min-bid-amount").text(data.minBidValue);
                        $("#total-vacancy").text(data.vacancy);
                        
                        for (var i = 0; i < data.data.length; i++) {
                            $("#table-data").append('<tr class="to-remove"><td>' + (i + 1) + '</td><td>' + data.data[i].bidPrice + '</td><td>' + data.data[i].isSuccess + '</td></tr>');
                        }

                        // If no data exists, show user an indication so they aren't confused
                        if (data.data.length == 0) {
                            $("#table-data").append('<tr class="to-remove"><td>There\'s no bidding data yet!</td></tr>');
                        }
                    });
                }

            });
        </script>


    </body>
</html>
