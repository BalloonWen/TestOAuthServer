package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class UploadToServer extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Drive drive;
	private String filePath;
	private java.io.File file;
	private boolean isMultipart;
	private static HttpTransport httpTransport;
	String fileName;
	public void init() {
		// get the file location where it would be stored
		filePath = getServletContext().getInitParameter("file-upload");
		// filePath = "E:\\";
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new ServletException("GET method used with "
				+ getClass().getName() + ": POST method required.");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			// Check that we have a file upload request
			isMultipart = ServletFileUpload.isMultipartContent(request);
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			if (!isMultipart) {
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Servlet upload</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<p>No file uploaded</p>");
				out.println("</body>");
				out.println("</html>");
				return;
			}

			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// parse the request to get file items
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					 fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();

					// Write the file
					if (fileName.lastIndexOf("\\") >= 0) {
						file = new java.io.File(
								filePath
										+ fileName.substring(fileName
												.lastIndexOf("\\")));
					} else {
						file = new java.io.File(
								filePath
										+ fileName.substring(fileName
												.lastIndexOf("\\") + 1));
					}

					fi.write(file);
					out.println("Uploaded Filename: " + fileName + "<br>");
				}

			}

			out.println("</body>");
			out.println("</html>");
			java.io.File UPLOAD_FILE = new java.io.File(filePath+fileName);
			 InputStream in = request.getInputStream();

			 
			 drive = DriveAuthUtil.getDriveService();

			 File fileMetadata = new File();
			 fileMetadata.setTitle(UPLOAD_FILE.getName());

			 FileContent mediaContent = new FileContent("image/jpeg",
			 UPLOAD_FILE);
			
			 Drive.Files.Insert insert = drive.files().insert(fileMetadata,
			 mediaContent);
			 MediaHttpUploader uploader = insert.getMediaHttpUploader();
			 uploader.setDirectUploadEnabled(true);
			
			 insert.execute();

			// obtain the Drive object in the DriveAuthUtil class
			Drive drive = DriveAuthUtil.getDriveService();
			FileList result = drive.files().list().setMaxResults(10).execute();
			List<File> files = result.getItems();
			if (files == null || files.size() == 0) {
				System.out.println("No files found.");
			} else {
				request.setAttribute("fileList", files);

				System.out.println("Files:");
				for (File file : files) {
					System.out.printf("%s (%s)\n", file.getTitle(),
							file.getId());
				}
				request.getRequestDispatcher("/list.jsp").forward(request,
						response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("something error");
		}
	}
}
