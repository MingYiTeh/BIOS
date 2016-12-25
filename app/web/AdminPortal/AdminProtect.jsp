<%-- 
    Document   : AdminProtect
    Created on : 28 Sep, 2016, 2:21:58 PM
    Author     : Jackson Kwa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            if(session==null || session.getAttribute("token")==null || !session.getAttribute("username").equals("admin")){
                //session.invalidate();
                request.getRequestDispatcher("../GlobalPages/unauthorisedAccessPage.jsp").forward(request,response);
                //response.sendRedirect("../GlobalPages/unauthorisedAccessPage.jsp");
            }
        %>
    </body>
</html>