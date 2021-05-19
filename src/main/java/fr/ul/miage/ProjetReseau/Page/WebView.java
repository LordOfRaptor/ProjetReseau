package fr.ul.miage.ProjetReseau.Page;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class WebView {

    //Stock les différents site
    private static final String VERTI = "www.verti.miage";
    private static final String DOPETROPE = "www.dopetrope.miage";
    private static final String MAIN = "www.test.miage";
    protected String site = "test";

    protected BufferedReader inFromClient;
    protected DataOutputStream outToClient;


    public WebView(BufferedReader in, DataOutputStream out, String host) {
        site = host;
        this.inFromClient = in;
        this.outToClient = out;
    }

    /**
     * Renvoie le directory qui correpsond à l'host
     *
     * @param host le host
     * @return
     */
    public static String chooseDirectory(String host) {
        switch (host) {
            case VERTI:
                return "verti";
            case DOPETROPE:
                return "dopetrope";
            default:
                return "test";
        }


    }

    /**
     * renvoie l'url qui correspond au directory
     *
     * @param directory le directory
     * @return
     */
    public static String findURL(String directory) {
        switch (directory) {
            case "verti":
                return VERTI;
            case "dopetrope":
                return DOPETROPE;
            default:
                return MAIN;
        }
    }

    /**
     * S'occupe de renvoyer le bonne affichage
     *
     * @param responseString le lien
     * @throws IOException
     */
    protected abstract void sendResponse(String responseString) throws IOException;

    /**
     * S'occupe de renvoyer le bonne affichage
     *
     * @param responseString le lien
     * @param query          permet d'ajouter des options supplémentaires
     * @throws IOException
     */
    public void sendResponse(String responseString, String query) throws IOException {
        sendResponse(responseString);
    }

    /**
     * Affiche ce que contient un fichier
     *
     * @param fin le fichier
     * @param out la sortie
     * @throws IOException
     */
    protected void sendFile(FileInputStream fin, DataOutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
    }

    /**
     * renvoie le bon content type en focntion du type de fichier
     *
     * @param responseString le fichier
     * @return
     */
    protected String content_type(String responseString) {
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
