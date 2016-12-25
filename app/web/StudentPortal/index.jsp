<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<%@ include file="../GlobalPages/protect.jsp"%>
<html lang="en">
    <!--<![endif]-->
    <head>
        <meta charset="utf-8" />
        <title>BIOS | Student Portal</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport" />
        <meta content="" name="description" />
        <meta content="SE-Vivor" name="author" />
        <meta HTTP=EQUIV="refresh" CONTENT="<%=session.getMaxInactiveInterval()%>"; URL="../GlobalPages/unauthorisedAccessPage.jsp" />

        <!-- ================== BEGIN BASE CSS STYLE ================== -->
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet">
        <link href="../assets/plugins/jquery-ui-1.10.4/themes/base/minified/jquery-ui.min.css" rel="stylesheet" />
        <link href="../assets/plugins/bootstrap-3.2.0/css/bootstrap.min.css" rel="stylesheet" />
        <link href="../assets/plugins/font-awesome-4.2.0/css/font-awesome.min.css" rel="stylesheet" />
        <link href="../assets/css/animate.min.css" rel="stylesheet" />
        <link href="../assets/css/style.min.css" rel="stylesheet" />
        <link href="../assets/css/style-responsive.min.css" rel="stylesheet" />
        <link href="../assets/css/theme/default.css" rel="stylesheet" id="theme" />
        <!-- ================== END BASE CSS STYLE ================== -->

        <!-- ================== BEGIN PAGE LEVEL CSS STYLE ================== -->
        <link href="../assets/plugins/jquery-jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" />
        <link href="../assets/plugins/bootstrap-calendar/css/bootstrap_calendar.css" rel="stylesheet" />
        <link href="../assets/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" />
        <link href="../assets/plugins/morris/morris.css" rel="stylesheet" />
        <link href="../assets/plugins/fullcalendar/fullcalendar/fullcalendar.css" rel="stylesheet" />
        <!-- ================== END PAGE LEVEL CSS STYLE ================== -->
    </head>
    <body>
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
                                <img src="../assets/img/user-13.jpg" alt="" />
                                <span class="hidden-xs">Login/Signup</span> <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu animated fadeInLeft">
                                <li class="arrow"></li>
                                <li><a href="javascript:;">Edit Profile</a></li>
                                <li class="divider"></li>
                                <li><a href="login.html">Log In</a></li>
                            </ul>
                        </li>
                    </ul>
                    <!-- end header navigation right -->
                </div>
                <!-- end container-fluid -->
            </div>
            <!-- end #header -->

            <!-- begin #sidebar -->
            <div id="sidebar" class="sidebar">
                <!-- begin sidebar scrollbar -->
                <div data-scrollbar="true" data-height="100%">
                    <!-- begin sidebar user -->
                    <ul class="nav">
                        <li class="nav-profile">
                            <div class="image">
                                <a href="javascript:;"><img src="../assets/img/user-13.jpg" alt="" /></a>
                            </div>
                            <div class="info">
                                Sean Ngu
                                <small>Front end developer</small>
                            </div>
                        </li>
                    </ul>
                    <!-- end sidebar user -->
                    <!-- begin sidebar nav -->
                    <ul class="nav">
                        <li class="nav-header">Navigation</li>
                        <li class="active"><a href="#"><i class="fa fa-home"></i> <span>Home</span></a></li>
                        <li class="has-sub">
                            <a href="javascript:;">
                                <b class="caret pull-right"></b>
                                <i class="fa fa-laptop"></i>
                                <span>
                                    Bidding
                                </span>
                            </a>
                            <ul class="sub-menu">
                                <li><a href="bidSection.jsp">Bid For Section</a></li>
                                <li><a href="index.html">Drop Bid</a></li>
                                <li><a href="index.html">Drop Section</a></li>
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
            <!-- end #sidebar -->

            <!-- begin #content -->
            <div id="content" class="content">
                <!-- begin breadcrumb -->
                <ol class="breadcrumb pull-right">
                    <li><a href="javascript:;">Home</a></li>
                </ol>
                <!-- end breadcrumb -->
                <!-- begin page-header -->
                <h1 class="page-header">Student Portal <small>Home</small></h1>
                <h1 class="page-header"><% out.print("This is the token:" + session.getAttribute("token"));%></h1>
                <!-- end page-header -->
                <!-- begin row -->


                <!-- Calendar code here -->
                <div class="panel panel-inverse">
                    <div class="panel-heading">
                        <div class="panel-heading-btn">
                            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
                            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload"><i class="fa fa-repeat"></i></a>
                            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
                            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
                        </div>
                        <h4 class="panel-title">Calendar</h4>
                    </div>
                    <div class="panel-body p-0">
                        <div class="vertical-box">
                            <div class="vertical-box-column p-15 bg-silver width-sm">
                                <div id="external-events" class="calendar-event">
                                    <h4 class=" m-b-20">Modules</h4>
                                    <div class="external-event bg-purple" data-bg="bg-purple" data-title="Discussion" data-media="<i class='fa fa-comments'></i>" data-desc="Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
                                        <h5><i class="fa fa-comments fa-lg fa-fw"></i> Discussion</h5>
                                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                                    </div>
                                    <div class="external-event bg-blue" data-bg="bg-blue" data-title="Dinner" data-media="<i class='fa fa-cutlery'></i>" data-desc="Cum sociis natoque penatibus et magnis dis parturient montes.">
                                        <h5><i class="fa fa-cutlery fa-lg fa-fw"></i> Dinner</h5>
                                        <p>Cum sociis natoque penatibus et magnis dis parturient montes.</p>
                                    </div>
                                    <div class="external-event bg-green" data-bg="bg-green" data-title="Brainstorming" data-media="<i class='fa fa-lightbulb-o'></i>" data-desc="Mauris tristique massa eu venenatis semper. Phasellus a nibh nisi.">
                                        <h5><i class="fa fa-lightbulb-o fa-lg fa-fw"></i> Brainstorming</h5>
                                        <p>Mauris tristique massa eu venenatis semper. Phasellus a nibh nisi.</p>
                                    </div>
                                    <div class="external-event bg-orange" data-bg="bg-orange" data-title="Performance Rating" data-media="<i class='fa fa-tasks'></i>" data-desc="Class aptent taciti sociosqu ad litora torquent per conubia nostra.">
                                        <h5><i class="fa fa-tasks fa-lg fa-fw"></i> Performance Rating</h5>
                                        <p>Class aptent taciti sociosqu ad litora torquent per conubia nostra.</p>
                                    </div>
                                    <div class="external-event bg-red" data-bg="bg-red" data-title="Video Shooting" data-media="<i class='fa fa-video-camera'></i>" data-desc="Donec ligula nisi, tempus eu egestas id, auctor sit amet velit.">
                                        <h5><i class="fa fa-video-camera fa-lg fa-fw"></i> Video Shooting</h5>
                                        <p>Donec ligula nisi, tempus eu egestas id, auctor sit amet velit.</p>
                                    </div>

                                </div>
                            </div>
                            <div id="calendar" class="vertical-box-column p-15 calendar"></div>
                        </div>
                    </div>
                </div>
                <!-- Calendar code ends here -->


            </div>
            <!-- end page container -->

            <!-- ================== BEGIN BASE JS ================== -->
            <script src="../assets/plugins/jquery-1.8.2/jquery-1.8.2.min.js"></script>
            <script src="../assets/plugins/jquery-ui-1.10.4/ui/minified/jquery-ui.min.js"></script>
            <script src="../assets/plugins/bootstrap-3.2.0/js/bootstrap.min.js"></script>
            <!--[if lt IE 9]>
                    <script src="../assets/crossbrowserjs/html5shiv.js"></script>
                    <script src="../assets/crossbrowserjs/respond.min.js"></script>
                    <script src="../assets/crossbrowserjs/excanvas.min.js"></script>
            <![endif]-->
            <script src="../assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>
            <script src="../assets/plugins/jquery-cookie/jquery.cookie.js"></script>
            <!-- ================== END BASE JS ================== -->

            <!-- ================== BEGIN PAGE LEVEL JS ================== -->
            <script src="../assets/plugins/morris/raphael.min.js"></script>
            <!--<script src="../assets/plugins/morris/morris.js"></script>-->
            <script src="../assets/plugins/jquery-jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
            <script src="../assets/plugins/jquery-jvectormap/jquery-jvectormap-world-merc-en.js"></script>
            <script src="../assets/plugins/bootstrap-calendar/js/bootstrap_calendar.min.js"></script>
            <script src="../assets/plugins/gritter/js/jquery.gritter.js"></script>
            <script src="../assets/js/dashboard-v2.min.js"></script>
            <script src="../assets/plugins/fullcalendar/fullcalendar/fullcalendar.js"></script>
            <script src="../assets/js/calendar.demo.min.js"></script>
            <script src="../assets/js/apps.min.js"></script>
            <!-- ================== END PAGE LEVEL JS ================== -->
            <script>
                $(document).ready(function () {
                    App.init();
                    DashboardV2.init();
                    Calendar.init();
                });
            </script>

    </body>
</html>
