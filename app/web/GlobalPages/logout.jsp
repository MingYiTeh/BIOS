<%-- 
    Document   : logout
    Created on : Oct 24, 2016, 11:37:22 AM
    Author     : Has Nilofar
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
        System.out.println("logout.jsp");
        if(session != null){
        session.invalidate();
        }
        response.sendRedirect("/app/");
        %>
    </body>
</html>
