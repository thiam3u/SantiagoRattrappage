package ReseauSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import joueur.Joueur;
import plateau.Parcelle;
import plateau.PileParcelle;

public class Clients {
	
	 private int codemess;
	    private String pseudo = "unknow";
	    private Boolean stopclient = false;

	    Socket clientSocket = null;
	    ObjectOutputStream os = null;
	    //BufferedReader is = null;
	    ObjectInputStream ois = null;

	    Thread att;

	    ArrayList<Joueur> listeJoueur = null;
	    private ArrayList<PileParcelle> pileParcelles = null;
	    private int[] montantEnchere;
	    private Parcelle ParcelleMain = null;
	    private boolean prendParcelle= false;
	    private Joueur j_actif = null;
	    private Parcelle pChoisie = null;

	    public void lancer() {

	        final String hostname = "localhost";
	        final int port = 6789;


	        try {
	            clientSocket = new Socket(hostname, port);
	            os = new ObjectOutputStream(clientSocket.getOutputStream());
	            //is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	            ois = new ObjectInputStream(clientSocket.getInputStream());
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host: " + hostname);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to: " + hostname);
	        }


}
