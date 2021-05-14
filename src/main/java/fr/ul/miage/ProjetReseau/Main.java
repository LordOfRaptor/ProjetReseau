package fr.ul.miage.ProjetReseau;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static String IPV4,LINKRESOUCES;
    public static int PORT;
    public static boolean ACCES_DIR;

    public static void main(String[] args) throws UnknownHostException, IOException {
        intializeProperties();

        ServerSocket Server = new ServerSocket(PORT, 10, InetAddress.getByName(IPV4));
        System.out.println("TCPServer Waiting for client on port " + PORT);
        while (true) {
            Socket connected = Server.accept();
            (new HTTPServer(connected)).start();
        }
    }

    public static void intializeProperties(){


        File f = new File("config.properties");
        try (InputStream input = new FileInputStream(f)) {

            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            IPV4 = prop.getProperty("ipV4");
            PORT = Integer.parseInt(prop.getProperty("port")) ;
            LINKRESOUCES = prop.getProperty("pathToSites");
            ACCES_DIR = prop.getProperty("acces_dir").equals("true");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
