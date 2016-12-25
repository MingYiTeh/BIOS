<%@page import="java.util.ArrayList"%>
<%@page import="com.bios.model.DataError"%>
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

            <h1 class="page-header">Admin Portal <small>Bootstrap</small></h1>

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
                    <form action="bootstrap" method="post" enctype="multipart/form-data">
                        <fieldset>
                            <legend>Choose zip file to upload:</legend>
                            <input type="file" name="file" size="50" />
                            <br />
                            <input class="btn btn-sm btn-success" type="submit" value="Upload File" />
                        </fieldset>
                    </form>

                </div>
            </div>

        </div>

<!--        <form action="bootstrap" method="post" enctype="multipart/form-data">
            <input type="file" name="file" size="50" />
            <br />
            <input type="submit" value="Upload File" />
        </form>-->

        <!--This modal is shown when the bootstrapping process was a success-->
        <div class="modal modal-message fade" id="modal-success">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title"><i class="fa fa-upload fa-2x"></i> Upload was successful!</h4>
                    </div>
                    <div class="modal-body">
                        <p>Hey, your zip file was uploaded and all seems good!</p>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:;" class="btn btn-sm btn-primary" data-dismiss="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!--This modal is shown when bootstrapping process has failed, and will list the reasons-->
        <div class="modal modal-message fade" id="modal-failed">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title"><i class="fa fa-warning fa-2x"></i> Upload completed with warnings!</h4>
                    </div>
                    <div class="modal-body">
                        <p>Your zip file contained the following errors: </p>

                        <%
                            ArrayList<DataError> allCourses = (ArrayList<DataError>) session.getAttribute("courseErrors");
                            ArrayList<DataError> allSections = (ArrayList<DataError>) session.getAttribute("sectionErrors");
                            ArrayList<DataError> allStudents = (ArrayList<DataError>) session.getAttribute("studentErrors");
                            ArrayList<DataError> allBids = (ArrayList<DataError>) session.getAttribute("bidErrors");
                            ArrayList<DataError> allPrerequisites = (ArrayList<DataError>) session.getAttribute("prerequisiteErrors");
                            ArrayList<DataError> allCompletedCourses = (ArrayList<DataError>) session.getAttribute("completedCourseErrors");

                            int[] totalLines = (int[]) session.getAttribute("totalLines");
                            int[] totalErrorLines = (int[]) session.getAttribute("totalErrorLines");
                            String[] tableHeaders = (String[]) session.getAttribute("tableHeaders");

                            ArrayList<ArrayList<DataError>> everything = new ArrayList<>();
                            everything.add(allCourses);
                            everything.add(allSections);
                            everything.add(allStudents);
                            everything.add(allBids);
                            everything.add(allPrerequisites);
                            everything.add(allCompletedCourses);

                            for (int i = 0; i < everything.size(); i++) {
                                ArrayList<DataError> allErr = everything.get(i);
                                if (allErr != null) {
                        %>
                        <div class="panel-group" id="accordion">
                            <div class="panel panel-inverse overflow-hidden">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <a class="accordion-toggle accordion-toggle-styled collapsed" data-toggle="collapse" data-parent="#accordion" href="#<%=tableHeaders[i].replace(".csv","")%>">
                                            <i class="fa fa-plus-circle pull-right"></i> 
                                            <%out.println(tableHeaders[i] + " (" + (totalLines[i] - totalErrorLines[i]) + "/" + totalLines[i] + "):");%>
                                        </a>
                                    </h3>
                                </div>
                                <div id="<%=tableHeaders[i].replace(".csv","")%>" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <table class="table">
                                            <h5><strong><%out.println(tableHeaders[i] + " (" + (totalLines[i] - totalErrorLines[i]) + "/" + totalLines[i] + "):");%></strong></h5>
                                            <thead>
                                                <tr>
                                                    <th>Line Number</th>
                                                    <th>Description</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    for (DataError err : allErr) {

                                                        if (err != null) {
                                                            String desc = err.getDescription();
                                                            int line = err.getLineNum();
                                                %>
                                                <tr>
                                                    <td><%=line%></td>
                                                    <td><%=desc%></td>
                                                </tr>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%
                                }
                            }
                        %>

                    </div>
                    <div class="modal-footer">
                        <a href="javascript:;" class="btn btn-sm btn-primary" data-dismiss="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <%
            String uploadSuccess = (String) session.getAttribute("uploadSuccess");
            // If an upload was never made
            if (uploadSuccess == null) {
                // This means a user has just only log-in, and we show welcome message!
//                out.println("<script>DashboardV2.initNotification();</script>");
            } else {
                // If an upload was made, and it is successful
                if (uploadSuccess.equals("sucess")) {
                    out.println("<script>$('#modal-success').modal('show')</script>");
                } else {
                    out.println("<script>$('#modal-failed').modal('show')</script>");
                }

                session.setAttribute("uploadSuccess", null);
            }


        %>



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

<!-- ================== END PAGE LEVEL JS ================== -->
<script>
    $(document).ready(function () {
        App.init();


    });
</script>

</body>
</html>
