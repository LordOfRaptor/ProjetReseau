package fr.ul.miage.ProjetReseau.Page;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class WebView {
	
	
	public final String RESOURCES_LINK = "fr/ul/miage/ProjetReseau/";
	
	protected BufferedReader inFromClient;
	protected DataOutputStream outToClient;
	
	
	public WebView(BufferedReader in, DataOutputStream out) {
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

}
