package fr.ul.miage.ProjetReseau.Page;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import fr.ul.miage.ProjetReseau.HTTPServer;
import fr.ul.miage.ProjetReseau.Main;

public class WebViewSucces extends WebView {
	
	
	public WebViewSucces(BufferedReader in, DataOutputStream out,String host) {
		super(in, out,host);
	}

	@Override
	protected void sendResponse(String responseString) throws IOException {
		
		String statusLine = "HTTP/1.1 200 OK" + System.getProperty("line.separator");
		String serverdetails = "Server: Java HTTPServer";
		String contentLengthLine = null;
		String contentTypeLine = null;
		FileInputStream fin = null;
		//Récupere le fichier que l'utilsiateur cherche à avoir
		File f = new File(Main.LINKRESOUCES + File.separator + site + File.separator + responseString);
		String fileName = responseString;
		//Si le fichier n'est pas un directory le récupere et stock les différentes info
		if(!f.isDirectory()) {
			fin = new FileInputStream(Main.LINKRESOUCES + File.separator + site + File.separator + responseString);
			contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + System.getProperty("line.separator");

			contentTypeLine = content_type(responseString);
		}
		//Si c'est un directory on vérifie qu'on a le droit de lister les fichier
		else if(Main.ACCES_DIR){
			StringBuilder sb = new StringBuilder(HTTPServer.HTML_START);
			sb.append("<ul>");
			String format = "<li><a href=%s> %s</li> %n";
			File[] files = f.listFiles();
			for(File file : files ){
				sb.append(String.format(format,
						"http://"+findURL( site )+ "/" + responseString.replace("\\","/") + file.getName(),
						file.getName()));
			}
			sb.append("</ul>");
			sb.append(HTTPServer.HTML_END);
			responseString = sb.toString();
			contentLengthLine = "Content-Length: " + responseString.length() + System.getProperty("line.separator");
			contentTypeLine = "Content-Type: text/html" + "\r\n";
		}
		//sinon affiche un forbidden
		else {
			new WebViewForbidden(inFromClient, outToClient, site).sendResponse("", "");
			return;
		}
		System.out.println(statusLine + " " + fileName);
		
		outToClient.writeBytes(statusLine);
		outToClient.writeBytes(serverdetails);
		outToClient.writeBytes(contentTypeLine);
		outToClient.writeBytes(contentLengthLine);
		outToClient.writeBytes("Connection: close"+System.getProperty("line.separator"));
		outToClient.writeBytes(System.getProperty("line.separator"));

		if(fin != null)
			sendFile(fin, outToClient);
		else
			outToClient.writeBytes(responseString);

		outToClient.close();
		
	}

}
