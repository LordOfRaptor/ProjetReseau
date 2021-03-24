package fr.ul.miage.ProjetReseau;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Main {
	
	
	 static final int port=8080;
	 static final String newLine="\r\n";

	 public static void main(String[] args) throws UnknownHostException, IOException
	 {
		 ServerSocket Server = new ServerSocket(port, 10, InetAddress.getByName("192.168.1.21"));
			System.out.println("TCPServer Waiting for client on port " + port);

			while (true) {
				Socket connected = Server.accept();
				(new MyHTTPServer(connected)).start();
			}
	 }

}
