package ApplicationMain;

import java.awt.Color;
import java.util.ArrayList;

import gameMaster.MaitreDuJeu;
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
	    
	    public MaitreDuJeu(ArrayList<Joueur> joueurs) {
	        this.nbTours = 0;
	        this.plateau = new Plateau();
	        this.plateau.initialisation();
	        this.joueurs = joueurs;
	        //créer autant de pile de parcelle qu'il y a de joueurs
	        this.pileParcelles = new ArrayList<PileParcelle>(joueurs.size());
	        initialisationPileParcelles();
	        this.fenetre = new FenetreGUI();
	    }

	    public MaitreDuJeu() {
	        this.nbTours = 0;
	        this.plateau = new Plateau();
	        this.plateau.initialisation();
	        //créer autant de pile de parcelle qu'il y a de joueurs
	        this.fenetre = new FenetreGUI();
	    }
	    
	    
	   
	    
	public static void main(String[] args) {
		  //preparaton d<une liste de couleur pour eviter les doublons
        Color[] tabCouleur = new Color[4];
        tabCouleur[0] = Color.red;
        tabCouleur[1] = Color.cyan;
        tabCouleur[2] = Color.green;
        tabCouleur[3] = Color.pink;
        //A la place une interface graphique devras permettre de choisir les joueurs
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(4);

        MaitreDuJeu mj = new MaitreDuJeu();
        mj.afficherLauncher();

	}
	

}
