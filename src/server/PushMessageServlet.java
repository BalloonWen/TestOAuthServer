package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.PrintJob;
import server.Filenames2Push;

public class PushMessageServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String fileName =  request.getHeader("filename");
		String driveId = request.getHeader("driveId");
		String resourceId = request.getHeader("resourceId");
		PrintJob printJob = new PrintJob();
		PrintWriter out = response.getWriter();
		System.out.println(fileName);
		if (fileName != null&&driveId !=null) {
			printJob.setFileName(fileName);
			printJob.setDriveId(driveId);
			printJob.setResourceId(resourceId);
			Filenames2Push.filenameQueue.add(printJob);

			for (PrintJob element : Filenames2Push.filenameQueue) {
				System.out.println(element.getFileName()+"    "+element.getResourceId());
			}
			out.write("Server has recevied the file : "+fileName+", now sending to printer...");
		}else{
			out.write("something's wrong");
		}
		

	}

}
