package fr.ul.miage.ProjetReseau.Page;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;


import fr.ul.miage.ProjetReseau.HTTPServer;
import fr.ul.miage.ProjetReseau.Main;

public class WebViewUnauthorized extends WebView{

	private String htpasswd;
	public WebViewUnauthorized(BufferedReader in, DataOutputStream out, String host, String htpasswd) {
		super(in, out, host);
		this.htpasswd = htpasswd;
	}

	@Override
	protected void sendResponse(String responseString) throws IOException {
		
		String statusLine = "HTTP/1.1 401 Unauthorized" + "\r\n";
		System.out.println(statusLine);
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
		FileInputStream iStream = new FileInputStream(htpasswd);
		//Vérifie que le fichier soit pas null et que la query n'est pas vide
		if(iStream != null && query != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            
            String line = null;
            line = br.readLine();
            br.close();
            
            //Vérifie si l'autorisation est bonne et renvoie une page de succés si oui
            if(checkAuthorization(query, line)) {
				var found = new WebViewSucces(inFromClient, outToClient,site);
				found.sendResponse(file);
				return;
            }
		}
		//Si la query n'est pas la renvoie une 401
		if(query == null) {
			this.sendResponse(file);
			return;
		}
		//Sinon une page 403
		var notfound = new WebViewForbidden(inFromClient, outToClient,site);
		notfound.sendResponse(file);
		
	}

	//Vérifie l'authorization décryptant le htpasswd
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
