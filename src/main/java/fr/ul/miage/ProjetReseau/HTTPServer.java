package fr.ul.miage.ProjetReseau;
/*
* myHTTPServer.java
* Author: S.Prasanna
* @version 1.00
*/

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


import fr.ul.miage.ProjetReseau.Page.*;


public class HTTPServer extends Thread {

	public static final String HTML_START = "<!DOCTYPE HTML><html><title>HTTP Server in java</title><body>";

	public static final String HTML_END = "</body></html>";

	Socket connectedClient = null;
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;
	private HashMap<String, String> header = new HashMap<>();

	public HTTPServer(Socket client) {
		connectedClient = client;
	}

	@Override
	public void run() {

		try {
			WebView wv = null;
			String file = null;
			String query = null;
			System.out.println("The Client " + connectedClient.getInetAddress() + ":" + connectedClient.getPort()
					+ " is connected");

			inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			outToClient = new DataOutputStream(connectedClient.getOutputStream());

			String requestString = inFromClient.readLine();
			String headerLine = requestString;

			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			String httpQueryString = tokenizer.nextToken();

			StringBuffer responseBuffer = new StringBuffer();
			responseBuffer.append("<b> This is the HTTP Server Home Page.... </b><BR>");
			responseBuffer.append("The HTTP Client request is ....<BR>");

			System.out.println("The HTTP request string is ....");
			while (inFromClient.ready()) {
				responseBuffer.append(requestString + "<BR>");
				requestString = inFromClient.readLine();
				if(requestString.contains(":")) {
					String [] headerSeparator = requestString.split(":");
					header.put(headerSeparator[0], headerSeparator[1].trim());
				}
			}
			System.out.println(httpMethod + " " + httpQueryString);
			String host = header.get("Host");
			if (httpMethod.equals("GET")) {
				
				String fileName = httpQueryString.replaceFirst("/", "");
				fileName = URLDecoder.decode(fileName,StandardCharsets.UTF_8);

				String[] seperatorQuestionMark = fileName.split("\\?");
				
				file = seperatorQuestionMark[0] + File.separator;
				file = toPath(host,file);
				if (file != null) {
					File f = new File(Main.LINKRESOUCES  + File.separator + WebView.chooseDirectory(host)+ File.separator + file);
					String path = f.getAbsolutePath();
					while (path != null) {
						f = new File(path);
						if (new File(path + File.separator + ".htpasswd").exists()) {
							if (header.containsKey("Authorization")) {
								query = header.get("Authorization");
							}
							wv = new WebViewUnauthorized(inFromClient, outToClient, WebView.chooseDirectory(host),path + File.separator + ".htpasswd"); //.sendResponse(file, query);

						}
						path = f.getParent();
					}
					if (wv == null)
						wv = new WebViewSucces(inFromClient, outToClient,WebView.chooseDirectory(host)); //.sendResponse(file,query);

					
				} else {
					wv = new WebViewNotFound(inFromClient, outToClient,WebView.chooseDirectory(host)); //.sendResponse("<p> 404 not found </p>", query);
				}
				wv.sendResponse(file, query);
			} else {
				wv =  new WebViewBadRequest(inFromClient, outToClient,WebView.chooseDirectory(host));//.sendResponse("<p> 404 not found </p>", null);
				wv.sendResponse(file, query);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String toPath(String host,String file) {
		File f = new File(Main.LINKRESOUCES + File.separator + WebView.chooseDirectory(host) + File.separator + file);
		if (f.exists()) {
			if (f.isDirectory()) {
				File f2 = new File(Main.LINKRESOUCES + File.separator + WebView.chooseDirectory(host) + File.separator + file + File.separator + "index.html");
				if (f2.exists())
					return file + "index.html";

			}
			return file;
		}
		return null;
	}
	
	
}
