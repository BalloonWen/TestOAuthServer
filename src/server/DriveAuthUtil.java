package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class DriveAuthUtil {
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
//	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static final List<String> SCOPES = Arrays
			.asList(DriveScopes.DRIVE);
	private static final String APPLICATION_NAME = "SamsungPrinterServer";
	private static final String CALLBACK_URI = "http://localhost:8080/TestOAuthServer/admin.jsp";
	private static GoogleAuthorizationCodeFlow flow;
	private static String authCode = "";
	private static Credential credential = null;
	public static String REFRESH_TOKEN="";
	

	// private static final java.io.File DATA_STORE_DIR = new java.io.File(
	// System.getProperty("user.home"),
	// ".credentials/drive-java-quickstart.json");

	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			"C:\\Users\\Balloon\\Desktop\\client_secret1.json");

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
			// Load client secrets.
			InputStream in = new FileInputStream(
					"C:\\Users\\Balloon\\Desktop\\client_secret.json");
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
					JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
					JSON_FACTORY, clientSecrets, SCOPES)
//					.setDataStoreFactory(DATA_STORE_FACTORY)
					.setApprovalPrompt("force").setAccessType("offline")
					.build();

		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	// build request uri
	public String buildPermissionUrl() {

		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();

		return url.setRedirectUri(CALLBACK_URI).build();
	}

	public static void authorize() throws IOException {

		if (credential == null) {
			final GoogleTokenResponse response = flow.newTokenRequest(authCode)
					.setRedirectUri(CALLBACK_URI).execute();

			credential = flow.createAndStoreCredential(response,
					generateUserId());
			REFRESH_TOKEN = credential.getRefreshToken();
		}
		System.out.println(REFRESH_TOKEN);
		System.out.println("Credentials saved to "
				+ DATA_STORE_DIR.getAbsolutePath());

	}

	public static Credential getRefreshToken() {

		try {
			if(REFRESH_TOKEN.equals("")||credential.getExpiresInSeconds()<3500||credential!=null){
				System.out.println("get permission first");
				return credential;
			}
			GoogleCredential refreshCredential = new GoogleCredential.Builder()
					.setClientSecrets(
							"830410059030-mq65m3dr2p7vs24to2sm3r1h29o0p8or.apps.googleusercontent.com",
							"v3I3GOI-Z6dMu68EzkdCeLna")
					.setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT)
					.build().setRefreshToken(REFRESH_TOKEN);
			// print access token
			String accessToken = "";
			accessToken = refreshCredential.getAccessToken();
			System.out.println("Access token before: " + accessToken);
			refreshCredential.refreshToken();
			accessToken = refreshCredential.getAccessToken();
			return refreshCredential;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return credential;

	}

	/**
	 * because flow.createAndStoreCredential needs a String for userId, I
	 * generated one
	 * 
	 */
	private static String generateUserId() {

		SecureRandom sr1 = new SecureRandom();

		String userId = "google;" + sr1.nextInt();
		return userId;

	}

	// obtain Drive type object
	public static Drive getDriveService() throws IOException {
		 credential = getRefreshToken();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	// after allow the request, the google will send back a code
	public static void setCode(String code) {
		authCode = code;
	}

}
