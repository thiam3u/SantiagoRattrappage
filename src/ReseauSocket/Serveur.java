package ReseauSocket;


//import gui.LauncherGUI;
//import joueur.Joueur;
//import plateau.Parcelle;
//import plateau.PileParcelle;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

//import reseau.Server;
//import reseau.Server2Connection;

public class Serveur {
	// déclare un socket serveur et un socket client pour le serveur;
		// déclare le nombre de connexions

	    ServerSocket echoServer = null;
	    Socket clientSocket = null;
	    int numConnections = 0;
	    int port;
	   // ArrayList<Server2Connection> clientCo = new ArrayList<Server2Connection>();


	    public Serveur(int port) {
	        this.port = port;
	    }

	    public void stopServer() {
	        System.out.println("Server cleaning up.");
	        System.exit(0);
	    }

	    public void startServer() {
	        
	        Runnable serverTask = new Runnable() {
	            @Override
	            public void run() {
	                try {
	                    echoServer = new ServerSocket(port);
	                    while (true) {
	                        try {
	                            clientSocket = echoServer.accept();
	                            numConnections++;
	                            //Server2Connection oneconnection = new Server2Connection(clientSocket, numConnections, Server.this);
	                           // clientCo.add(oneconnection);
	                           // new Thread(oneconnection).start();
	                        } catch (IOException e) {
	                            System.out.println(e);
	                        }
	                    }
	                } catch (IOException e) {
	                    System.out.println(e);
	                }
	            }
	        };
}
}
