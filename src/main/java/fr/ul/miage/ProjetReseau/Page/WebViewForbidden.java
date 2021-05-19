package fr.ul.miage.ProjetReseau.Page;

import fr.ul.miage.ProjetReseau.HTTPServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class WebViewForbidden extends WebView {

    public WebViewForbidden(BufferedReader in, DataOutputStream out, String host) {
        super(in, out, host);
    }


    @Override
    public void sendResponse(String responseString, String query) throws IOException {
        sendResponse("<p> 403 Forbidden </p>");
    }

    @Override
    protected void sendResponse(String responseString) throws IOException {
        String statusLine = "HTTP/1.1 403 Forbidden" + "\r\n";
        System.out.println(statusLine);
        String serverdetails = "Server: Java HTTPServer";
        String contentLengthLine = null;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";


        responseString = HTTPServer.HTML_START + responseString + HTTPServer.HTML_END;
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
