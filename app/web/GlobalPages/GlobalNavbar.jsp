<%-- 
    Document   : navbar
    Created on : Sep 16, 2016, 10:59:17 AM
    Author     : kongyujian
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="com.bios.model.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- begin #page-loader -->
<div id="page-loader" class="fade in"><span class="spinner"></span></div>
<!-- end #page-loader -->

<!-- begin #page-container -->
<div id="page-container" class="fade page-sidebar-fixed page-header-fixed">
    <!-- begin #header -->
    <div id="header" class="header navbar navbar-inverse navbar-fixed-top">
        <!-- begin container-fluid -->
        <div class="container-fluid">
            <!-- begin mobile sidebar expand / collapse button -->
            <div class="navbar-header">
                <a href="index.html" class="navbar-brand"><span class="navbar-logo"></span> BIOS</a>
                <button type="button" class="navbar-toggle" data-click="sidebar-toggled">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>
            <!-- end mobile sidebar expand / collapse button -->

            <!-- begin header navigation right -->
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form class="navbar-form full-width">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Enter keyword" />
                            <button type="submit" class="btn btn-search"><i class="fa fa-search"></i></button>
                        </div>
                    </form>
                </li>

                <li class="dropdown navbar-user">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="assets/img/user-13.jpg" alt="" />
                        <span id="userID" class="hidden-xs">
                            <%
                                DecimalFormat df = new DecimalFormat("0.00");
                                if (session.getAttribute("username") == null) {
                                    out.print("LogOut/Signup");
                                } else {
                                    Student student = (Student) session.getAttribute("authenticated.student");
                                    out.print(session.getAttribute("username") + " | ");
                                    if (student != null) {
                                        //System.out.println(student.geteDollar());
                                        double amt = student.geteDollar();
                                        String result = df.format(amt);
                                        if (result.equals("-0.00")) {
                                            out.print("$0.00");
                                        } else {
                                            out.print("$" + result);
                                        }
                                    }
                                }
                            %>
                        </span> <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu animated fadeInLeft">
                        <li class="arrow"></li>
                        <li><a href="javascript:;">Edit Profile</a></li>
                        <li class="divider"></li>
                        <li><a href="GlobalPages/logout.jsp">Log Out</a></li>
                    </ul>
                </li>
            </ul>
            <!-- end header navigation right -->
        </div>
        <!-- end container-fluid -->
    </div>
</div>