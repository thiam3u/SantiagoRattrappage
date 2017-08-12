package ApplicationMain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import gameMaster.MaitreDuJeu;
import gui.FenetreGUI;
import gui.PileParcelleGUI;
import joueur.Joueur;
import joueur.Proposition;
import plateau.Parcelle;
import plateau.PileParcelle;
import plateau.Plateau;
import reseau.Client;
import reseau.Server;

public class Main {
	  private static int montantRevenu = 5; //non imposable
	    private Joueur joueursCli;
	    private int nbTours = 0; //nombre de tours √©coul√© dans la partie
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
	        //cr√©er autant de pile de parcelle qu'il y a de joueurs
	        this.pileParcelles = new ArrayList<PileParcelle>(joueurs.size());
	        initialisationPileParcelles();
	        this.fenetre = new FenetreGUI();
	    }

	    public MaitreDuJeu() {
	        this.nbTours = 0;
	        this.plateau = new Plateau();
	        this.plateau.initialisation();
	        //cr√©er autant de pile de parcelle qu'il y a de joueurs
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
                mj.fenetre.getLauncher().setInfo("Tout les joueurs sont connect√© d√©but de la partie");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 3; i++) {
                    while (mj.getServ().getPseudo(i) == "unknow") {
                        mj.fenetre.getLauncher().setInfo("Tout les joueurs sont connect√© d√©but de la partie");
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
	
          // les fonctionnalitÈ du jeu 
	
        public void refreshInfo(String mess) {
            fenetre.refreshInfo("Tours : " + Integer.toString(nbTours) + "Phase : " + mess + " Joueur : " + j_actif.getPseudo());
        }
        //cette methode gere la premiÈre partie du jeu: EnchÈre pour les plantes
        public void enchereParcelle() {
            int[] montantEnchere = new int[joueurs.size()];
            for (int i = 0; i < joueurs.size(); i++) {
                montantEnchere[i] = -1;
            }
            this.retournerPlantation();
            for (int i = 0; i < joueurs.size(); i++) {
                j_actif = joueurs.get(i);
                montantEnchere[i] = fenetre.offreJoueur(j_actif, montantEnchere);
            }
            majConstructeurCanal(montantEnchere);
            ArrayList<Joueur> toursJoueurs = triJoueurTour(montantEnchere);
            joueurs = toursJoueurs;
            for (int i = 0; i < joueurs.size(); i++) {
                j_actif = toursJoueurs.get(i);
                refreshInfo("Ench√®re Parcelle");
                Parcelle pChoisie = fenetre.choixParcelle(j_actif);
                j_actif.setParcelleMain(pChoisie);
                fenetre.RefreshMainJoueur(joueurs);
            }    
	}
        public void enchereParcelleClient() {
            Parcelle pChoisie = null;
            this.retournerPlantation();
            int[] montantEnchere = cli.getMontantEnchere();
            int[] montant = {-1};
            j_actif = joueursCli;
            montant[0] = fenetre.offreJoueur(j_actif, montantEnchere);
            cli.sendEnchere(montant[0]);
            //reucpere le tri des joueurs
            joueurs = cli.getJoueurs();
            for (int i = 0; i < joueurs.size(); i++) {
                j_actif = joueurs.get(i);
                //si c'est √† notre tour de jouer
                if (joueurs.get(i).getPseudo().equals(joueursCli.getPseudo())) {
                    //authorization du serveur
                    System.out.println("A moi !");
                    cli.getParcelleMain();
                    pChoisie = fenetre.choixParcelle(j_actif);
                    j_actif.setParcelleMain(pChoisie);
                    //envoie r√©sultat
                    cli.sendParcelle(pChoisie);
                }
                //si quelqu'un d'autre √† jouer rÈcupÈre son choix
                else {
                    System.out.println(" A lui !" + j_actif.getPseudo());
                    pChoisie = cli.getParcellePrise();
                    fenetre.retirerParcelle(pChoisie);
                    joueurs.set(i, cli.getJoueur());
                }
            }

        }
        //gÈre la deuxieme phase du jeu le changement du constructeur de canal
        public void majConstructeurCanal(int[] montantEnchere) {
            int min = 10000;
            int pos = -1;
            for (int i = 0; i < joueurs.size(); i++) {
                if (montantEnchere[i] < min) {
                    min = montantEnchere[i];
                    pos = i;
                }
            }
            setConstructeurCanal(joueurs.get(pos));
        }
        
        //gÈre la troisieme phase du jeu le depot de la parcelle en main des joueurs
        public void depotParcelle() {
            for (Joueur joueur : joueurs) {
                j_actif = joueur;
                refreshInfo("DÈpot parcelle");
                fenetre.depotParcelle(j_actif);
                fenetre.RefreshMainJoueur(joueurs);
            }
        }
        
        //gÈre la  quatrieme phase du jeu soudoiement aupres du constructeur et construction du canal par ce dernier
        private void soudoiementConstructeur() {
            ArrayList<Proposition> listProposition = new ArrayList<Proposition>();

            //On construit la liste des differentes proposition
            for (int i = 0; i < joueurs.size(); i++) {

                j_actif = joueurs.get(i);
                refreshInfo("Enchere canal");
                //seul les non constructeurs emettent des propositions
                if (j_actif != constructeurCanal) {
                    fenetre.propositionCanalJoueur(j_actif, listProposition);
                }
            }

            //on affiche la liste au constructeur (qui contient aussi sa proposition) il choisit une proposition pour construire
            constructionCanal(listProposition);
        }
        //gÈre la  quatrieme phase du jeu soudoiement aupres du constructeur et construction du canal par ce dernier
        private void soudoiementConstructeur() {
            ArrayList<Proposition> listProposition = new ArrayList<Proposition>();

            //On construit la liste des differentes proposition
            for (int i = 0; i < joueurs.size(); i++) {

                j_actif = joueurs.get(i);
                refreshInfo("Enchere canal");
                //seul les non constructeurs emettent des propositions
                if (j_actif != constructeurCanal) {
                    fenetre.propositionCanalJoueur(j_actif, listProposition);
                }
            }

            //on affiche la liste au constructeur (qui contient aussi sa proposition) il choisit une proposition pour construire
            constructionCanal(listProposition);
        }

        //gere la  cinquieme phase du jeu secheresse 
        private void irriguationComplementaire() {
            refreshInfo("Irriguation");
            boolean adeposer = false;
            for (int i = 0; i < joueurs.size(); i++) {
                j_actif = joueurs.get(i);
                refreshInfo("Irriguation");
                if (j_actif.isCanalComplementaire()) { //si la personne actif a un canal compl√©mentaire
                    if (!adeposer) { //si personne n'a d√©ja pos√© son canal durant ce tour de jeu
                        adeposer = fenetre.depotCanalComplementaire(j_actif);
                    }
                }
            }
        }
        //gÈre la  sixieme phase du jeu
        private void secheresse() {
            refreshInfo("Secheresse");
            System.out.println("MJ.secheresse");
            fenetre.secheresse();

        }
          

        //gÈre la  septieme phase du jeu le paiement de tous les joueurs en fin de tours
        private void paiementJoueur() {
            for (Joueur joueur : joueurs) {
                joueur.setArgent(joueur.getArgent() + montantRevenu);
            }
            fenetre.RefreshMainJoueur(joueurs);
        }
        
        // les fonction du jeu 
        
        public void resultatFinal(ArrayList<Joueur> listeJoueurs) {
            fenetre.calculResultatFinal(listeJoueurs);
      
        }
        
        public void afficherJeu() {
            this.fenetre.creationPlateau(pileParcelles, joueurs);
        }
      //CrÈer les diffÈrentes pile de parcelles pour la partie
        private void initialisationPileParcelles() {

            //on init les piles parcelles
            for (int i = 0; i < joueurs.size(); i++) {
                pileParcelles.add(new PileParcelle());
            }

            int nbPile = pileParcelles.size();
            long seed = System.nanoTime(); //Pour notre randoum
            ArrayList<Parcelle> parcelles = new ArrayList<Parcelle>(45);

            //On crÈer les 45 tuiles
            creationParcelles(parcelles);

            Collections.shuffle(parcelles, new Random(seed));

            //Si on est ‡† 4 joueurs on retire une parcelle du jeu
            if (nbPile == 4) {
                parcelles.remove(0);
            }
            while (!parcelles.isEmpty()) {
                for (int i = 0; i < nbPile; i++) {
                    pileParcelles.get(i).AjoutParcelle(parcelles.get(0));
                    parcelles.remove(0);
                }
            }
        }
      //Retourne une liste de joueurs triÈ pour la phase de tour 1
        private ArrayList<Joueur> triJoueurTour(int[] montantEnchere) {
            ArrayList<Joueur> res = new ArrayList<Joueur>(joueurs.size());
            int max = -1;
            int maxExclu = 10000;
            int pos = -1;
            //tries les joueurs qui ont misÈs
            for (int j = 0; j < joueurs.size(); j++) {
                for (int i = 0; i < joueurs.size(); i++) {
                    if (montantEnchere[i] != 0 && max < montantEnchere[i] && montantEnchere[i] < maxExclu) {
                        max = montantEnchere[i];
                        pos = i;
                    }
                }
                maxExclu = max;
                if (pos != -1 && montantEnchere[pos] != 0) {
                    res.add(joueurs.get(pos));
                }
                max = -1;
                pos = -1;
            }
            //tri les joueurs qui ont passÈ
            for (int i = joueurs.size() - 1; i > -1; i--) {
                if (montantEnchere[i] == 0) {
                    res.add(joueurs.get(i));
                }
            }
            return res;
        }
        
        private void constructionCanal(ArrayList<Proposition> listProposition) {
            System.out.println("Construction canal");
            fenetre.choixCanalConstructeur(constructeurCanal, listProposition);
        }
        
        private void retournerPlantation() {
            fenetre.retournerLesPilesParcelles();
        }
        
        private void creationParcelles(ArrayList<Parcelle> parcelles) {
            //les 6 patates avec 1 travailleur
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.patate));
            //les 3 patates avec 2 travailleurs
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.patate));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.patate));

            //les 6 piments avec 1 travailleur
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.piment));
            //les 3 piments avec 2 travailleurs
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.piment));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.piment));
            
            //les 6 bananes avec 1 travailleur
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.banane));
            //les 3 bananes avec 2 travailleurs
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.banane));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.banane));
            
            //les 6 bambous avec 1 travailleur
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.bambou));
            //les 3 bambous avec 2 travailleurs
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.bambou));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.bambou));
           
            //les 6 haricots avec 1 travailleur
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(1, false, false, Parcelle.typeChamps.haricot));
            //les 3 haricots avec 2 travailleurs
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.haricot));
            parcelles.add(new Parcelle(2, false, false, Parcelle.typeChamps.haricot));

        }  
        
        
}
