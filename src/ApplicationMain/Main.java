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
	  private static int montantRevenu = 3; //non imposable
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
	    
	    
	    
	    
	    

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
