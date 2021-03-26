package fr.ul.miage.ProjetReseau.Page;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;


import fr.ul.miage.ProjetReseau.HTTPServer;

public class WebViewForbidden extends WebView{

	public WebViewForbidden(BufferedReader in, DataOutputStream out) {
		super(in, out);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void sendResponse(String responseString) throws IOException {
		
		String statusLine = "HTTP/1.1 401 Unauthorized" + "\r\n";
		String serverdetails = "Server: Java HTTPServer";
		String unauthorizedString = "WWW-Authenticate: Basic realm=\"Access to staging site\"";
		String contentLengthLine = null;
		String contentTypeLine = "Content-Type: text/html" + "\r\n";
		FileInputStream fin = null;

		
		responseString = HTTPServer.HTML_START + responseString + HTTPServer.HTML_END;
		contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
		
		
		outToClient.writeBytes(statusLine);
		outToClient.writeBytes(serverdetails);
		outToClient.writeBytes(contentTypeLine);
		outToClient.writeBytes(contentLengthLine);
		outToClient.writeBytes(unauthorizedString);
		outToClient.writeBytes("Connection: close\r\n");
		outToClient.writeBytes("\r\n");

		
		outToClient.writeBytes(responseString);

		outToClient.close();
	}

	@Override
	public void sendResponse(String file, String query) throws IOException {
		
		InputStream iStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(RESOURCES_LINK + file + "/.htpasswd");
		if(iStream != null && query != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            
            String line = null;
            line = br.readLine();
            br.close();
            
            
            if(checkAuthorization(query, line)) {
				System.out.println(query);
				var found = new WebViewSucces(inFromClient, outToClient);
				found.sendResponse(file);
				return;
            }
		}
		sendResponse("<p> Forbidden </p>");
		
	}
	
	public boolean checkAuthorization(String encodedString, String username_mdp){
		try {
			encodedString = encodedString.substring(encodedString.lastIndexOf(" ")+1);
			byte [] base64EncodedString = Base64.getDecoder().decode(encodedString);
			encodedString = new String(base64EncodedString);
			
			String [] query = encodedString.split(":");
			String [] htpsswrd = username_mdp.split(":");
			
			
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(StandardCharsets.UTF_8.encode(query[1]));
			query[1] = String.format("%032x", new BigInteger(1, md5.digest()));
			
			return query[0].equals(htpsswrd[0]) && query[1].equals(htpsswrd[1]);
			
		} catch (Exception e ) {
			e.printStackTrace();
			return false;
		}
		
	}

}
