package reseau;

import gui.LauncherGUI;
import joueur.Joueur;
import plateau.Parcelle;
import plateau.PileParcelle;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

	// d�clare un socket serveur et un socket client pour le serveur
    // d�clare le nombre de connexions

    ServerSocket echoServer = null;
    Socket clientSocket = null;
    int numConnections = 0;
    int port;
    ArrayList<Server2Connection> clientCo = new ArrayList<Server2Connection>();


    public Server(int port) {
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
                            Server2Connection oneconnection = new Server2Connection(clientSocket, numConnections, Server.this);
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


        System.out.println("Le serveur est d�marr� et attend des conn�xions.");
        System.out.println("avec les multi-threds les connexion multiples sont autoris�.");
        System.out.println("tout client peux envoy�er -1 pour arr�ter le serveur .");

        Thread serverThread = new Thread(serverTask);
        serverThread.start();


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

    public void sendPileParcelle(ArrayList<PileParcelle> pileParcelles, int i){
        System.out.println("sendPile");
        try {
            clientCo.get(i).oos.writeInt(3);
            clientCo.get(i).oos.flush();
            System.out.println("sendJoueur to " + i);
            for(int j =0; j<pileParcelles.size(); j++){
                System.out.println("Send " + j + " Part of the pile");
                clientCo.get(i).oos.writeObject(pileParcelles.get(j));
                clientCo.get(i).oos.flush();
            }
        } catch (IOException e) {
            System.out.println("fail envoie");
            e.printStackTrace();
        }
    }

    public Boolean reponseClient(int i){
        if(clientCo.get(i).recu){
            clientCo.get(i).recu = false;
            return true;
        }
        return false;
    }

    public int getOffreJoueur(int[] montantEnchere, Joueur j_actif) {
        Server2Connection j = getJoueur(j_actif.getPseudo());
        j.att = new Thread();
        j.att.start();
        j.montantEnchereInt = -1;
        synchronized (j.att) {
            try {
                j.oos.writeInt(4);
                j.oos.flush();
                j.oos.writeObject(montantEnchere);
                while (j.montantEnchereInt == -1) {
                    System.out.println("att");
                    j.att.wait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return j.montantEnchereInt;
        }
    }

    public Parcelle parcelleChoisi(Joueur j_actif) {
        Server2Connection j = getJoueur(j_actif.getPseudo());
        j.att = new Thread();
        j.att.start();
        j.parcelleMain =null;
        synchronized (j.att) {
            try {
                j.oos.writeInt(5);
                j.oos.flush();
                while (j.parcelleMain == null) {
                    System.out.println("att parcelle du client");
                    j.att.wait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return j.parcelleMain;
        }
    }

    private Server2Connection getJoueur(String pseudo) {
        for(int i = 0; i<3; i++){
            if(clientCo.get(i).pseudoJoueur == pseudo){
                return clientCo.get(i);
            }
        }
        return null;
    }

    public void sendParcelleChoisi(Joueur j_actif, Parcelle pChoisie) {
        System.out.println("sendParcelleChoisi");
        try {
            for (int i = 0; i < 3; i++) {
                if(!clientCo.get(i).pseudoJoueur.equals(j_actif.getPseudo())){
                    clientCo.get(i).oos.writeInt(6);
                    clientCo.get(i).oos.flush();
                    clientCo.get(i).oos.writeObject(j_actif);
                    clientCo.get(i).oos.flush();
                    clientCo.get(i).oos.writeObject(pChoisie);
                    clientCo.get(i).oos.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("fail envoie");
            e.printStackTrace();
        }
    }
}

class Server2Connection implements Runnable {
    ObjectInputStream is;
    //PrintStream os;
    ObjectOutputStream oos = null;
    Socket clientSocket;
    int id;
    Server server;
    Boolean recu = false;
    String pseudoJoueur = "unknow";
    int montantEnchereInt = -1;
    Thread att;
    public Parcelle parcelleMain;

    public Server2Connection(Socket clientSocket, int id, Server server) {
        this.clientSocket = clientSocket;
        this.id = id;
        this.server = server;
        System.out.println( "Connection " + id + " �tablis avec : " + clientSocket );
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
        System.out.println("client thread cr�er");
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
