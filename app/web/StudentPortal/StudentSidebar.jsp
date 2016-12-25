<%-- 
    Document   : StudentSidebar
    Created on : 18 Sep, 2016, 7:12:01 PM
    Author     : Teh Ming Yi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div id="sidebar" class="sidebar">
    <!-- begin sidebar scrollbar -->
    <div data-scrollbar="true" data-height="100%">
        <!-- begin sidebar user -->
        <ul class="nav">
            <li class="nav-profile">
                <div class="image">
                    <a href="javascript:;"><img src="assets/img/user-13.jpg" alt="" /></a>
                </div>
                <div class="info">
                    <%
                        if (session.getAttribute("username") == null){
                            out.print("Login/Signup");
                        }
                        else {
                            out.print(session.getAttribute("username"));
                        }
                    %>
                    <small>Student</small>
                </div>
            </li>
        </ul>
        <!-- end sidebar user -->
        <!-- begin sidebar nav -->
        <ul class="nav">
            <li class="nav-header">Navigation</li>
            <li class="<%= request.getParameter("activateHome") %>"><a href="student_home"><i class="fa fa-home"></i> <span>Home</span></a></li>
            <% 
                String str=null;
                if(request.getParameter("activateBidSection")!=null || request.getParameter("activateDropBid")!=null ||
                        request.getParameter("activateDropSection")!=null){
                    str="active";
                }
            %>
            <li class="<%= str%> has-sub">
                <a href="javascript:;">
                    <b class="caret pull-right"></b>
                    <i class="fa fa-laptop"></i>
                    <span>
                        Bidding
                    </span>
                </a>
                <ul class="sub-menu">
                    <li class="<%= request.getParameter("activateBidSection")%>"><a href="bidSection">Bid For Section</a></li>
                    <li class="<%= request.getParameter("activateDropBid")%>"><a href="student_drop_bid">Drop Bid</a></li>
                    <li class="<%= request.getParameter("activateDropSection")%>"><a href="student_drop_section">Drop Section</a></li>
                </ul>
            </li>
            <!-- begin sidebar minify button -->
            <li><a href="javascript:;" class="sidebar-minify-btn" data-click="sidebar-minify"><i class="fa fa-angle-double-left"></i></a></li>
            <!-- end sidebar minify button -->
        </ul>
        <!-- end sidebar nav -->
    </div>
    <!-- end sidebar scrollbar -->
</div>

<div class="sidebar-bg"></div>