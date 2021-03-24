package fr.ul.miage.ProjetReseau;
/*
* myHTTPServer.java
* Author: S.Prasanna
* @version 1.00
*/

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;


import fr.ul.miage.ProjetReseau.Page.WebView;
import fr.ul.miage.ProjetReseau.Page.WebViewForbidden;
import fr.ul.miage.ProjetReseau.Page.WebViewNotFound;
import fr.ul.miage.ProjetReseau.Page.WebViewSucces;


public class MyHTTPServer extends Thread {

	public static final String HTML_START = "<!DOCTYPE HTML><html>" + "<title>HTTP Server in java</title>" + "<body>";

	public static final String HTML_END = "</body>" + "</html>";

	Socket connectedClient = null;
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;

	public MyHTTPServer(Socket client) {
		connectedClient = client;
	}

	public void run() {

		try {

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
				System.out.println(requestString);
				requestString = inFromClient.readLine();
			}
			
			if (httpMethod.equals("GET")) {
				
				String fileName = httpQueryString.replaceFirst("/", "");
				fileName = URLDecoder.decode(fileName,StandardCharsets.UTF_8);
				// get the question mark

				String[] seperatorQuestionMark = fileName.split("\\?");
				
				var file = seperatorQuestionMark[0];
				var query = (seperatorQuestionMark.length == 1) ? null : seperatorQuestionMark[1];
				
				InputStream iStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("fr/ul/miage/ProjetReseau/" + file);
				
				if (iStream != null) {
				
					if(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("fr/ul/miage/ProjetReseau/" + file + "/.htpasswd") != null) {
						
						new WebViewForbidden(inFromClient, outToClient).sendResponse(file, query);
						
					}
					else {
						new WebViewSucces(inFromClient, outToClient).sendResponse(file,query);
					}
					
				} else {
					new WebViewNotFound(inFromClient, outToClient).sendResponse("<p> 404 not found </p>", query);
				}

			} else
				new WebViewNotFound(inFromClient, outToClient).sendResponse("<p> 404 not found </p>", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
