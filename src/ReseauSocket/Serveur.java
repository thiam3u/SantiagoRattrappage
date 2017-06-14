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
	// d�clare un socket serveur et un socket client pour le serveur;
		// d�clare le nombre de connexions

	    ServerSocket echoServer = null;
	    Socket clientSocket = null;
	    int numConnections = 0;
	    int port;
	    ArrayList<Server2Connection> clientCo = new ArrayList<Server2Connection>();


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
	                            Server2Connection oneconnection = new Server2Connection(clientSocket, numConnections, Serveur.this);
	                            clientCo.add(oneconnection);
	                            new Thread(oneconnection).start();
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
	    
	    
	    public String getPseudo(int id) {
	        return clientCo.get(id).pseudoJoueur;
	    }

	    public int getNbCo() {
	        return numConnections;
	    }

	    public void sendJoueur(ArrayList<Joueur> listeJoueurs, int i) {
	        System.out.println("sendJoueur");
	        try {
	            clientCo.get(i).oos.reset();
	            clientCo.get(i).oos.writeInt(2);
	            clientCo.get(i).oos.flush();
	            for(int j =0; j<4; j++){
	                clientCo.get(i).oos.writeObject(listeJoueurs.get(j));
	                clientCo.get(i).oos.flush();

	            }
	            //clientCo.get(i).oos.reset();
	            //Thread.sleep(800);
	        } catch (IOException e) {
	            System.out.println("fail envoie");
	            e.printStackTrace();
	        }
	    }
	    
	    
	    
	    class Server2Connection implements Runnable {
	        ObjectInputStream is;
	        //PrintStream os;
	        ObjectOutputStream oos = null;
	        Socket clientSocket;
	        int id;
	        Serveur server;
	        Boolean recu = false;
	        String pseudoJoueur = "unknow";
	        int montantEnchereInt = -1;
	        Thread att;
	         public Parcelle parcelleMain;

	        public Server2Connection(Socket clientSocket, int id, Serveur server) {
	            this.clientSocket = clientSocket;
	            this.id = id;
	            this.server = server;
	            System.out.println( "Connection " + id + " established with: " + clientSocket );
	            try {
	                is = new ObjectInputStream(clientSocket.getInputStream());
	                //os = new PrintStream(clientSocket.getOutputStream());
	                oos = new ObjectOutputStream(clientSocket.getOutputStream());
	            } catch (IOException e) {
	                System.out.println(e);
	            }

	            try {
	                System.out.println("demande pseudo");
	                oos.writeInt(1);
	                oos.flush();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        public void run() {
	            int n;
	            System.out.println("client thread crée");
	            try {
	                boolean serverStop = false;

	                while (true) {
	                    n = is.readInt();
	                    System.out.println( "Received " + n + " from Connection " + id + "." );


	                    //1 = pseudo
	                    if(n == 1){
	                        try {
	                            pseudoJoueur = (String) is.readObject();
	                        } catch (ClassNotFoundException e) {
	                            e.printStackTrace();
	                        }
	                        System.out.println(pseudoJoueur);
	                    }
	                    if(n == 2){
	                        recu = true;
	                    }
	                    if(n == 3){
	                        montantEnchereInt = is.readInt();
	                        synchronized (att){
	                            att.notify();
	                        }
	                    }
	                    if(n == 4){
	                        try {
	                            parcelleMain = (Parcelle) is.readObject();
	                            synchronized (att){
	                                att.notify();
	                            }
	                        } catch (ClassNotFoundException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                    if(serverStop){
	                        break;
	                    }

	                }

	                System.out.println( "Connection " + id + " closed." );
	                is.close();
	                //os.close();
	                oos.close();
	                clientSocket.close();

	                if ( serverStop ) server.stopServer();
	            } catch (IOException e) {
	                System.out.println(e);
	            }
	        }  

}
	    
	    
	    
}
