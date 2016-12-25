<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <!--<![endif]-->
    <jsp:include page="/GlobalPages/GlobalHead.jsp" >
        <jsp:param name="pageTitle" value="Login Page" />
    </jsp:include>

    <body>
        <!-- begin #page-loader -->
        <div id="page-loader" class="fade in"><span class="spinner"></span></div>
        <!-- end #page-loader -->

        <div class="login-cover">
            <div class="login-cover-image"><img src="assets/img/login-bg/bg-1.jpg" data-id="login-cover-image" alt="" /></div>
            <div class="login-cover-bg"></div>
        </div>
        <!-- begin #page-container -->
        <div id="page-container" class="fade">
            <!-- begin login -->
            <div class="login login-v2" data-pageload-addclass="animated flipInX">
                <!-- begin brand -->
                <div class="login-header">
                    <div class="brand">
                        <span class="logo"></span> BIOS
                        <small>Bidding Online System</small>
                    </div>
                    <div class="icon">
                        <i class="fa fa-sign-in"></i>
                    </div>
                </div>
                <!-- end brand -->
                <div class="login-content">
                    <form action="login" method="POST" class="margin-bottom-0">
                        <%
                            if (session.getAttribute("loginErrorMsg") != null) {
                                String errorMsg = (String) session.getAttribute("loginErrorMsg");
                                out.println("<font color = 'red'><ul>");
                                out.println("<li>" + errorMsg + "</li>");
                                out.println("</ul></font>");
                            }
                        %>
                        <div class="form-group m-b-20">
                            <input type="text" name="userId" class="form-control input-lg" placeholder="Email Address" />
                        </div>
                        <div class="form-group m-b-20">
                            <input type="password" name="password" class="form-control input-lg" placeholder="Password" />
                        </div>
                        <div class="checkbox m-b-20">
                            <label>
                                <input type="checkbox" /> Remember Me
                            </label>
                        </div>
                        <div class="login-buttons">
                            <button type="submit" class="btn btn-success btn-block btn-lg">Sign me in</button>
                        </div>
                        <div class="m-t-20">
                            Not a member yet? Click <a href="register.html">here</a> to register.
                        </div>
                    </form>
                </div>
            </div>
            <!-- end login -->

        </div>
        <!-- end page container -->

        <!-- ================== BEGIN BASE JS ================== -->
        <script src="assets/plugins/jquery-1.8.2/jquery-1.8.2.min.js"></script>
        <script src="assets/plugins/jquery-ui-1.10.4/ui/minified/jquery-ui.min.js"></script>
        <script src="assets/plugins/bootstrap-3.2.0/js/bootstrap.min.js"></script>
        <!--[if lt IE 9]>
                <script src="assets/crossbrowserjs/html5shiv.js"></script>
                <script src="assets/crossbrowserjs/respond.min.js"></script>
                <script src="assets/crossbrowserjs/excanvas.min.js"></script>
        <![endif]-->
        <script src="assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="assets/plugins/jquery-cookie/jquery.cookie.js"></script>
        <!-- ================== END BASE JS ================== -->

        <!-- ================== BEGIN PAGE LEVEL JS ================== -->
        <script src="assets/js/login-v2.demo.min.js"></script>
        <script src="assets/js/apps.min.js"></script>
        <!-- ================== END PAGE LEVEL JS ================== -->

        <script>
            $(document).ready(function () {
                App.init();
                LoginV2.init();
            });
        </script>

    </body>
</html>