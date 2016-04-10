package server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class UploadServlet extends HttpServlet {
	
//	private static final String SERVICE_ACCOUNT_EMAIL = "tomcat@samsungprinterserver-1251.iam.gserviceaccount.com";
//	private static final String UPLOAD_FILE_PATH = "C:\\Users\\Balloon\\Desktop\\ccc.jpg";
//	private static final String SCOPE = "https://www.googleapis.com/auth/drive";

//	private static HttpTransport httpTransport;
//	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static Drive drive;
//	private static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		String fileName = request.getHeader("fileName");
//		System.out.println(fileName);
//		response.setContentType("text/plain;charset=utf-8");
		
		try {
//			InputStream in = request.getInputStream();
//			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//			Credential credential = DriveAuthServlet.authorize();
//			drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
//			          APPLICATION_NAME).build();
			
//			File fileMetadata = new File();
//		    fileMetadata.setTitle(UPLOAD_FILE.getName());

//		    FileContent mediaContent = new FileContent("image/jpeg", UPLOAD_FILE);
//
//		    Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
//		    MediaHttpUploader uploader = insert.getMediaHttpUploader();
//		    uploader.setDirectUploadEnabled(true);
//		    
//		    insert.execute();
			
			
			//obtain the Drive object in the DriveAuthUtil class
			Drive drive = DriveAuthUtil.getDriveService();
		    FileList result = drive.files().list()
		             .setMaxResults(10)
		             .execute();
		        List<File> files = result.getItems();
		        if (files == null || files.size() == 0) {
		            System.out.println("No files found.");
		        } else {
		        	request.setAttribute("fileList", files);
		        	
		            System.out.println("Files:");
		            for (File file : files) {
		                System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
		            }	
		            request.getRequestDispatcher("/list.jsp").forward(request, response);
		        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
