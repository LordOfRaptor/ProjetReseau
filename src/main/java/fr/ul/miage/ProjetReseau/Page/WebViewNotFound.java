package fr.ul.miage.ProjetReseau.Page;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import fr.ul.miage.ProjetReseau.MyHTTPServer;

public class WebViewNotFound extends WebView {
	
	
	public WebViewNotFound(BufferedReader in, DataOutputStream out) {
		super(in, out);
	}
	
	@Override
	public void sendResponse(String responseString, String query) throws IOException {
		// TODO Auto-generated method stub
		sendResponse("<p> 404 not found </p>");
	}
	
	@Override
	protected void sendResponse(String responseString) throws IOException {
		String statusLine = "HTTP/1.1 404 Not Found" + "\r\n";
		String serverdetails = "Server: Java HTTPServer";
		String contentLengthLine = null;
		String fileName = null;
		String contentTypeLine = "Content-Type: text/html" + "\r\n";
		FileInputStream fin = null;

		
		responseString = MyHTTPServer.HTML_START + responseString + MyHTTPServer.HTML_END;
		contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
		
		
		outToClient.writeBytes(statusLine);
		outToClient.writeBytes(serverdetails);
		outToClient.writeBytes(contentTypeLine);
		outToClient.writeBytes(contentLengthLine);
		outToClient.writeBytes("Connection: close\r\n");
		outToClient.writeBytes("\r\n");

		
		outToClient.writeBytes(responseString);

		outToClient.close();
		
	}


}
