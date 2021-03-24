package fr.ul.miage.ProjetReseau.Page;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import fr.ul.miage.ProjetReseau.MyHTTPServer;

public class WebViewForbidden extends WebView{

	public WebViewForbidden(BufferedReader in, DataOutputStream out) {
		super(in, out);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void sendResponse(String responseString) throws IOException {
		
		String statusLine = "HTTP/1.1 401 Forbidden" + "\r\n";
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

	@Override
	public void sendResponse(String file, String query) throws IOException {
		InputStream iStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(RESOURCES_LINK + file + "/.htpasswd");
		if(iStream != null && query != null) {
			HashMap<String, String> querys = new HashMap<>();
			for (String string : query.split("&")) {
				String[] part = string.split("=");
				querys.put(part[0], part[1]);
			}
			System.out.println(query);
			var found = new WebViewSucces(inFromClient, outToClient);
			found.sendResponse(file);
			return;
		}
		sendResponse("<p> Forbidden </p>");
		
	}

}
