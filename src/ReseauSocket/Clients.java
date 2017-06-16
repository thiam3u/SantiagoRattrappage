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
	 
	    ObjectInputStream ois = null;

	    Thread att;
	    // liste des joueurs 
	    ArrayList<Joueur> listeJoueur = null;
	    // listes des tuiles de plantations
	    private ArrayList<PileParcelle> pileParcelles = null;
	    // montant des enchere dans 1 tableau à un dimension 
	    private int[] montantEnchere;
	    //piment choisi par les joueurs 
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
	     Runnable clientSend = new Runnable(){
	            @Override
	            public void run() {

	            	// Si tout a été initialisé, nous voulons écrire des données
	            	 // à la socket sur laquelle nous avons ouvert une connexion sur le port donné

	                if (clientSocket == null || os == null) {
	                    System.out.println("Send");
	                    System.out.println(os);
	                    System.out.println(clientSocket);
	                    System.err.println("quelques chose ne va pa une variable null !!!!.");
	                    return;
	                }
	                synchronized (this) {
	                    try {
	                        while (true) {

	                            int responseLine = ois.readInt();
	                            System.out.println("recu : " + responseLine);

	                            if (responseLine == 1) {
	                                while (pseudo.equals("unknow")) {

	                                }
	                                os.writeInt(1);
	                                os.flush();
	                                os.writeObject(pseudo);
	                                os.flush();
	                                System.out.println("pseudo send");
	                            }

	                            if (responseLine == 2) {
	                                System.out.println("récupération de la liste des joueurs");
	                                listeJoueur = new ArrayList<Joueur>();
	                                for (int i = 0; i < 4; i++) {
	                                    listeJoueur.add((Joueur) ois.readObject());
	                                }
	                                System.out.println(listeJoueur);
	                                os.writeInt(2);
	                                os.flush();
	                                synchronized (att) {
	                                    att.notify();
	                                }

	                            }

	                            if (responseLine == 3) {
	                                System.out.println("récupération des piles parcelles");
	                                pileParcelles = new ArrayList<PileParcelle>();
	                                for (int i = 0; i < 4; i++) {
	                                    System.out.println("recup parti : " + i);
	                                    pileParcelles.add((PileParcelle) ois.readObject());
	                                }
	                                System.out.println(pileParcelles);
	                                os.writeInt(2);
	                                os.flush();
	                                synchronized (att) {
	                                    att.notify();
	                                }
	                            }

	                            if (responseLine == 4) {
	                                System.out.println("récupération des enchéres");

	                                montantEnchere = (int[]) ois.readObject();
	                                System.out.println(montantEnchere);
	                                os.writeInt(2);
	                                os.flush();
	                                synchronized (att) {
	                                    att.notify();
	                                }
	                            }

	                            if (responseLine == 5) {
	                                System.out.println("Demande de plantation");
	                                os.writeInt(2);
	                                os.flush();
	                                prendParcelle = true;
	                                synchronized (att) {
	                                    att.notify();
	                                }
	                            }

	                            if (responseLine == 6) {
	                                System.out.println("recupération  choix autre joueur");

	                                j_actif = (Joueur) ois.readObject();
	                                pChoisie = (Parcelle) ois.readObject();
	                                os.writeInt(2);
	                                os.flush();
	                                synchronized (att) {
	                                    att.notify();
	                                }
	                            }


	                            if (stopclient) {
	                                break;
	                            }

	                          ;
	                        }

	                      

	                        os.close();
	                        clientSocket.close();
	                    } catch (UnknownHostException e) {
	                        System.err.println("Trying to connect to unknown host: " + e);
	                    } catch (IOException e) {
	                        System.err.println("IOException:  " + e);
	                    } catch (ClassNotFoundException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        };
	        Thread serverThreadSend = new Thread(clientSend);
	        serverThreadSend.start();
	        
	        
	        public void sendPseudo(String pseudo){
	            this.pseudo = pseudo;
	        }
	        
	        public ArrayList<Joueur> getJoueurs() {
	            att = new Thread();
	            att.start();
	            synchronized (att) {
	                while (listeJoueur == null) {
	                    System.out.println("att");
	                    try {
	                        att.wait();
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	                ArrayList<Joueur> ret = new ArrayList<Joueur>(listeJoueur);
	                listeJoueur =null;
	                return ret;
	            }
	        }
	        
	        public ArrayList<PileParcelle> getPileParcelles () {
	            att = new Thread();
	            att.start();
	            synchronized (att) {
	                while (pileParcelles == null) {
	                    System.out.println("att");
	                    try {
	                        att.wait();
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	                return pileParcelles;
	            }

	        }
	        public int[] getMontantEnchere() {
	            att = new Thread();
	            att.start();
	            synchronized (att) {
	                while (montantEnchere == null) {
	                    System.out.println("att");
	                    try {
	                        att.wait();
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	                int [] res = (int[]) montantEnchere.clone();
	                montantEnchere = null;
	                return res;
	            }
	        }
	        
	        
	        
	        
	        	

	    }
        
	        
	        


