<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
    <!--<![endif]-->
    <jsp:include page="/GlobalPages/GlobalHead.jsp" >
        <jsp:param name="pageTitle" value="Unauthorised Access" />
    </jsp:include>
    <body>
        
        <div id="page-loader" class="fade in"><span class="spinner"></span></div>
        
        <div id="page-container" class="fade">
            <div class="error">
                <div class="error-code m-b-10">404 <i class="fa fa-warning"></i></div>
                <div class="error-content">
                    <div class="error-message">We couldn't find it...</div>
                    <div class="error-desc m-b-20">
                        The page you're looking for doesn't exist. <br />
                        Perhaps, you might be lost? Want to try heading back <a href="login">home</a>?
                    </div>
                    <div>
                        <a href="login" class="btn btn-success">Go Back to Login Page</a>
                    </div>
                </div>
            </div>            
        </div>
        
        <script>
            $(document).ready(function () {
                App.init();
            });
        </script>
    </body>
</html>

