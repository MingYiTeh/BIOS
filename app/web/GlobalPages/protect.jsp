<%-- 
    Document   : Protect.jsp
    Created on : 18 Sep, 2016, 7:55:21 PM
    Author     : Teh Ming Yi
--%>

<%@page import="is203.JWTUtility"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            if(session==null || session.getAttribute("token")==null){
                //session.invalidate();
                request.getRequestDispatcher("../GlobalPages/unauthorisedAccessPage.jsp").forward(request,response);
                //response.sendRedirect("../GlobalPages/unauthorisedAccessPage.jsp");
            } 
            String result = JWTUtility.verify((String)session.getAttribute("token"), "abcdefgh12345678");
            if(result.equals("admin") || !result.equals(session.getAttribute("username"))){
                request.getRequestDispatcher("../GlobalPages/unauthorisedAccessPage.jsp").forward(request,response);
            }
        %>
    </body>
</html>
