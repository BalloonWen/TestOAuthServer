<%@page import="server.DriveAuthUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>


<title>My JSP 'admin.jsp' starting page</title>



</head>

<body>
	This is my JSP page.
	<br>

	<%
		DriveAuthUtil util = new DriveAuthUtil();
		if (request.getParameter("code") == null) {
			out.println("<a href='" + util.buildPermissionUrl()
					+ "'>get permission</a>");
		} else {
		
			DriveAuthUtil.setCode(request.getParameter("code"));
			DriveAuthUtil.authorize();
			//out.println(DriveAuthUtil.getRefreshToken().getAccessToken()+"<br/>");
			out.print(request.getParameter("code"));
		}
	%>
</body>
</html>
