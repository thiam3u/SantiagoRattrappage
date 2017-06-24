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
        while (mj.getCli() == null && mj.getServ() == null && !mj.getLocal()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mj.fenetre.getLauncher().getServer()) {
                mj.setServ(new Server(6789));
                mj.getServ().startServer();
            }
            if (mj.fenetre.getLauncher().getClient()) {
                mj.setCli(new Client());
                mj.getCli().lancer();
                mj.getCli().sendPseudo(mj.fenetre.getLauncher().jtf.getText());
            }
            if (mj.fenetre.getLauncher().pseudo4String != null) {
                listeJoueurs.add(new Joueur(mj.fenetre.getLauncher().jtf.getText(), 10, tabCouleur[0]));
                listeJoueurs.add(new Joueur(mj.fenetre.getLauncher().pseudo2String, 10, tabCouleur[1]));
                listeJoueurs.add(new Joueur(mj.fenetre.getLauncher().pseudo3String, 10, tabCouleur[2]));
                listeJoueurs.add(new Joueur(mj.fenetre.getLauncher().pseudo4String, 10, tabCouleur[3]));
                mj.setLocal(true);
            }
            if (mj.getLocal()) {
                mj.jouerPartieLocal(listeJoueurs);
            }
            if (mj.getServ() != null) {
                mj.pseudoJoueur = mj.fenetre.getLauncher().jtf.getText();
                listeJoueurs.add(new Joueur(mj.fenetre.getLauncher().jtf.getText(), 10, Color.red));
                while (mj.getServ().getNbCo() != 3) {
                    int jrestant = 3 - mj.getServ().getNbCo();
                    mj.fenetre.getLauncher().setInfo("Attente de " + jrestant + "joueur");
                }
                mj.fenetre.getLauncher().setInfo("Tout les joueurs sont connecté début de la partie");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 3; i++) {
                    while (mj.getServ().getPseudo(i) == "unknow") {
                        mj.fenetre.getLauncher().setInfo("Tout les joueurs sont connecté début de la partie");
                    }
                    listeJoueurs.add(new Joueur(mj.getServ().getPseudo(i), 10, tabCouleur[i]));

                }

                mj.jouerPartieServeur(listeJoueurs);
            }
            if (mj.getCli() != null) {
                mj.joueursCli = new Joueur(mj.fenetre.getLauncher().jtf.getText(), 10, tabCouleur[0]);
                listeJoueurs = mj.getCli().getJoueurs();
                mj.jouerPartieClient(listeJoueurs);
            }
        }
	}
          // les fonctionnalit� du jeu 
	
        public void refreshInfo(String mess) {
            fenetre.refreshInfo("Tours : " + Integer.toString(nbTours) + "Phase : " + mess + " Joueur : " + j_actif.getPseudo());
        }
        //cette methode gere la premi�re partie du Ench�re pour les plantes
        public void enchereParcelle() {
            int[] montantEnchere = new int[joueurs.size()];
            for (int i = 0; i < joueurs.size(); i++) {
                montantEnchere[i] = -1;
            }
	}

}
