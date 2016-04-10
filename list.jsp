
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.google.api.services.drive.model.File"%>
<%@page import="java.util.List"%>
<html>


<body>
	<%
		if (request.getAttribute("fileList") != null) {
			List<File> fileList = (List) request.getAttribute("fileList");
			for (File file : fileList) {

				out.println(file.getTitle() + "   (" + file.getId()+")<br/>");
			}
		}
	%>
</body>
</html>
