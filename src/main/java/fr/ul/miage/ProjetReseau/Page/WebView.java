package fr.ul.miage.ProjetReseau.Page;

import fr.ul.miage.ProjetReseau.Main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class WebView {
	
	
	private static final String VERTI = "www.verti.miage";
	private static final String DOPETROPE = "www.dopetrope.miage";
	private static final String MAIN = "www.test.miage";
	protected String site = "test";
	
	protected BufferedReader inFromClient;
	protected DataOutputStream outToClient;
	
	
	public WebView(BufferedReader in, DataOutputStream out,String host) {
		site = host;
		this.inFromClient = in;
		this.outToClient = out;
	}
	
	protected abstract void sendResponse(String responseString) throws IOException;
	
	public void sendResponse(String responseString,String query) throws IOException{
		sendResponse(responseString);
	}
	
	protected void sendFile(FileInputStream fin, DataOutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = fin.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
		}
		fin.close();
	}

	public static String chooseDirectory(String host){
		switch (host) {
			case VERTI:
				return"verti";
			case DOPETROPE:
				return "dopetrope";
			default:
				return "test";
		}


	}

	public static String findURL(String host){
		switch (host) {
			case "verti":
				return VERTI;
			case "dopetrope":
				return DOPETROPE;
			default:
				return MAIN;
		} }

	protected String content_type(String responseString){
		String contentTypeLine = "Content-Type: " + System.getProperty("line.separator");
		if (responseString.endsWith(".htm") || responseString.endsWith(".html"))
			contentTypeLine = "Content-Type: text/html" + System.getProperty("line.separator");
		if (responseString.endsWith(".css"))
			contentTypeLine = "Content-Type: text/css" + System.getProperty("line.separator");
		if (responseString.endsWith(".jpg") || responseString.endsWith(".jpeg"))
			contentTypeLine = "Content-Type: image/jpeg" + System.getProperty("line.separator");
		if (responseString.endsWith(".png"))
			contentTypeLine = "Content-Type: image/png" + System.getProperty("line.separator");
		if (responseString.endsWith(".ico"))
			contentTypeLine = "Content-Type: image/x-icon" + System.getProperty("line.separator");
		if (responseString.endsWith(".js"))
			contentTypeLine = "Content-Type: application/javascript" + System.getProperty("line.separator");
		if (responseString.endsWith(".woff"))
			contentTypeLine = "Content-Type: font/woff" + System.getProperty("line.separator");
		if (responseString.endsWith(".woff2"))
			contentTypeLine = "Content-Type: font/woff2" + System.getProperty("line.separator");
		return contentTypeLine;
	}

}
