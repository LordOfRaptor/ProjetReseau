package fr.ul.miage.ProjetReseau;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Main {

    public static String IPV4, LINKRESOUCES;
    public static int PORT;
    public static boolean ACCES_DIR;

    public static void main(String[] args) throws IOException {
        //Initialise les propriétés
        intializeProperties();
        //Crée un server socket avec le port de config.properties et l'adresse IP présent dand config.properties
        ServerSocket Server = new ServerSocket(PORT, 10, InetAddress.getByName(IPV4));
        System.out.println("TCPServer Waiting for client on port " + PORT);
        //Boucle à l'infinie pour vérifier les connexion entrantes et les lance en thread
        while (true) {
            Socket connected = Server.accept();
            (new HTTPServer(connected)).start();
        }
    }

    public static void intializeProperties() {


        File f = new File("config.properties");
        try (InputStream input = new FileInputStream(f)) {

            Properties prop = new Properties();
            prop.load(input);
            // Stock les variables présente dans config.properties
            IPV4 = prop.getProperty("ipV4");
            PORT = Integer.parseInt(prop.getProperty("port"));
            LINKRESOUCES = prop.getProperty("pathToSites");
            ACCES_DIR = prop.getProperty("acces_dir").equals("true");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
