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
			//Affiche l'adresse de la personne et le port du client
			System.out.println("The Client " + connectedClient.getInetAddress() + ":" + connectedClient.getPort()
					+ " is connected");

			inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			outToClient = new DataOutputStream(connectedClient.getOutputStream());

			//Récuper la premiere du header contenant la méthode GET/POST etc...
			//Contient aussi l'URL que l'utilisateur cherche à joindre
			String requestString = inFromClient.readLine();
			String headerLine = requestString;
			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			String httpQueryString = tokenizer.nextToken();

			//Récupere toutes le header et le stock dans une map si on en a besoin
			while (inFromClient.ready()) {
				requestString = inFromClient.readLine();
				if(requestString.contains(":")) {
					String [] headerSeparator = requestString.split(":");
					header.put(headerSeparator[0], headerSeparator[1].trim());
				}
			}

			//Affiche la méthode et l'URL que l'utilisateur cherche à joindre
			System.out.println("The HTTP request string is " + httpMethod + " " + httpQueryString);
			//Stock l'URL principale du site
			String host = header.get("Host");
			//Ne marche qu'avec GET
			if (httpMethod.equals("GET")) {

				//Crée une URL plus lisible pour java
				String fileName = httpQueryString.replaceFirst("/", "");
				fileName = URLDecoder.decode(fileName,StandardCharsets.UTF_8);

				//sépare l'URL en 2 si on rajoute un ? dans l'URL
				String[] seperatorQuestionMark = fileName.split("\\?");
				file = seperatorQuestionMark[0] + File.separator;
				//Vérifie le path du fichier
				file = toPath(host,file);
				if (file != null) {
					//Vérifie si un fichier .htpasswd est présent dans le path
					File f = new File(Main.LINKRESOUCES  + File.separator + WebView.chooseDirectory(host)+ File.separator + file);
					String path = f.getAbsolutePath();
					while (path != null) {
						f = new File(path);
						if (new File(path + File.separator + ".htpasswd").exists()) {
							//Vérifie si le header contient Authorization pour l'accés au forbidden
							if (header.containsKey("Authorization")) {
								query = header.get("Authorization");
							}
							//Stocke la page 401
							wv = new WebViewUnauthorized(inFromClient, outToClient, WebView.chooseDirectory(host),path + File.separator + ".htpasswd");

						}
						path = f.getParent();
					}
					if (wv == null)
						//Stocke la page 200
						wv = new WebViewSucces(inFromClient, outToClient,WebView.chooseDirectory(host));

					
				} else {
					//Stocke la page 404
					wv = new WebViewNotFound(inFromClient, outToClient,WebView.chooseDirectory(host));
				}
				//Affiche la bonne page
				wv.sendResponse(file, query);
			} else {
				//Affiche la page 500
				wv =  new WebViewBadRequest(inFromClient, outToClient,WebView.chooseDirectory(host));
				wv.sendResponse(file, query);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Vérifie si il existe un index.html
	 * @param host le site principale exemple www.verti.miage
	 * @param file la suite de l'URL
	 * @return le chemin vers index.html si il existe sinon si le File existe le retourne et si il n'existe pas retourne null
	 */
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
