package ApplicationMain;

import java.util.ArrayList;

import gui.FenetreGUI;
import gui.PileParcelleGUI;
import joueur.Joueur;
import plateau.PileParcelle;
import plateau.Plateau;
import reseau.Client;
import reseau.Server;

public class Main {
	  private static int montantRevenu = 5; //non imposable
	    private Joueur joueursCli;
	    private int nbTours = 0; //nombre de tours écoulé dans la partie
	    private Plateau plateau; //le plateau de jeu
	    private ArrayList<Joueur> joueurs; //la liste des joueurs dans la partie
	    private ArrayList<PileParcelle> pileParcelles;
	    private PileParcelleGUI ppgui;
	    private FenetreGUI fenetre;
	    private Joueur j_actif;
	    private Joueur constructeurCanal;
	    private Server serv = null;
	    private Client cli = null;
	    private Boolean local = false;
	    private String pseudoJoueur;
	    
	    
	    public Boolean getLocal() {

	        return local;
	    }
	    public void setLocal(Boolean local) {
	        this.local = local;
	    }

	    public Server getServ() {
	        return serv;
	    }

	    public void setServ(Server serv) {
	        this.serv = serv;
	    }

	    public Client getCli() {
	        return cli;
	    }

	    public void setCli(Client cli) {
	        this.cli = cli;
	    }

	    public Plateau getPlateau() {
	        return plateau;
	    }

	    public FenetreGUI getFenetre() {
	        return fenetre;
	    }

	    public ArrayList<PileParcelle> getPileParcelles() {
	        return pileParcelles;
	    }

	    public void setJ_actif(Joueur joueur) {
	        this.j_actif = joueur;
	    }

	    private void setJoueur(ArrayList<Joueur> listeJoueurs) {
	        joueurs = listeJoueurs;
	        this.pileParcelles = new ArrayList<PileParcelle>(joueurs.size());
	        initialisationPileParcelles();
	    }

	    public void setConstructeurCanal(Joueur j) {
	        constructeurCanal = j;
	    }
	    
	    
	    private void setJoueur(ArrayList<Joueur> listeJoueurs) {
	        joueurs = listeJoueurs;
	        this.pileParcelles = new ArrayList<PileParcelle>(joueurs.size());
	        initialisationPileParcelles();
	    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
