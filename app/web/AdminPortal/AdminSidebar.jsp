<%-- 
    Document   : AdminSidebar
    Created on : Sep 16, 2016, 11:02:58 AM
    Author     : kongyujian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- begin #sidebar -->
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
                    <small>Admin</small>
                </div>
            </li>
        </ul>
        <!-- end sidebar user -->
        <!-- begin sidebar nav -->
        <ul class="nav">
            <li class="nav-header">Navigation</li>
            
            <li class="<%if (request.getParameter("selected").equals("home")){ out.print("active");}%>"><a href="admin_home"><i class="fa fa-home"></i> <span>Home</span></a></li>
            <li class="<%if (request.getParameter("selected").equals("data")){ out.print("active");}%>"><a href="bootstrap"><i class="fa fa-database"></i> <span>Bootstrap data</span></a></li>
            <!--<li class="//if (request.getParameter("selected").equals("clear")){ out.print("active");}"><a href="clear_round_1"><i class="fa fa-bell"></i> <span>Clear round</span></a></li>-->
            
            <!-- begin sidebar minify button -->
            <li><a href="javascript:;" class="sidebar-minify-btn" data-click="sidebar-minify"><i class="fa fa-angle-double-left"></i></a></li>
            <!-- end sidebar minify button -->
        </ul>
        <!-- end sidebar nav -->
    </div>
    <!-- end sidebar scrollbar -->
</div>
<div class="sidebar-bg"></div>
<!-- end #sidebar -->
