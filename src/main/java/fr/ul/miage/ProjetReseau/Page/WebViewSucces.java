package fr.ul.miage.ProjetReseau.Page;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import fr.ul.miage.ProjetReseau.MyHTTPServer;

public class WebViewSucces extends WebView {
	
	
	public WebViewSucces(BufferedReader in, DataOutputStream out) {
		super(in, out);
	}
	
	
	
	
	@Override
	protected void sendResponse(String responseString) throws IOException {
		
		String statusLine = "HTTP/1.1 200 OK" + "\r\n";
		String serverdetails = "Server: Java HTTPServer";
		String contentLengthLine = null;
		String contentTypeLine = "Content-Type: text/html" + "\r\n";
		FileInputStream fin = null;

		Path temp = Files.createTempFile("resource-", ".ext");
		Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCES_LINK+responseString), temp, StandardCopyOption.REPLACE_EXISTING);
		fin = new FileInputStream(temp.toString());
		contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
		if (!responseString.endsWith(".htm") && !responseString.endsWith(".html"))
			contentTypeLine = "Content-Type: \r\n";
		
		
		outToClient.writeBytes(statusLine);
		outToClient.writeBytes(serverdetails);
		outToClient.writeBytes(contentTypeLine);
		outToClient.writeBytes(contentLengthLine);
		outToClient.writeBytes("Connection: close\r\n");
		outToClient.writeBytes("\r\n");

		
		sendFile(fin, outToClient);

		outToClient.close();
		
	}

}
