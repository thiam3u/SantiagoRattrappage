/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package plateau;


import joueur.Joueur;
import joueur.Proposition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class Plateau extends JApplet {

    //Tableaux relatifs aux Parcelles
    ArrayList<JLabel> ListParcelleGUI = new ArrayList<JLabel>();
    ArrayList<Parcelle> ListParcelleModele = new ArrayList<Parcelle>();
    //Tableaux relatifs aux Canaux
    ArrayList<JLabel> ListCanauxGUI = new ArrayList<JLabel>();
    ArrayList<Canal> ListCanauxModele = new ArrayList<Canal>();
    //Tableaux relatifs aux Intersections
    ArrayList<JLabel> ListIntersectGUI = new ArrayList<JLabel>();
    ArrayList<Intersection> ListIntersect = new ArrayList<Intersection>();

    JLabel parcelleChoisie = null;
    JLabel canalChoisi = null;
    boolean depotencours = false;
    boolean propositionCanalEncours = false;

    //les threads
    Thread threadAttenteDepotParcelle;
    Thread threadAttenteChoixCanalProposition;

    private JPanel panelRenvoi = new JPanel();
    private JPanel glassOuvrier = new JPanel();
    private JPanel panel = new JPanel(new GridBagLayout());
    private JLayeredPane panelCalque = new JLayeredPane();

    public Plateau() {

    }

    public JLayeredPane getPanelCalque() {
        return panelCalque;
    }

    public JPanel getGlassOuvrier() {
        return glassOuvrier;
    }

    public ArrayList<Canal> getListCanauxModele() {
        return ListCanauxModele;
    }

    public ArrayList<JLabel> getListCanauxGUI() {
        return ListCanauxGUI;
    }

    public ArrayList<Parcelle> getListParcelleModele() {
        return ListParcelleModele;
    }

    public void initialisation() {
        //mis en places des JPanels gérant le plateau


        //panel plateau
        this.panel.setBounds(0, 0, 500, 500);
        this.glassOuvrier.setBounds(this.panel.getBounds());
        this.glassOuvrier.setLayout(null);
        this.glassOuvrier.setOpaque(false);

        //on ajoute au couche de calque
        this.panelCalque.setPreferredSize(new Dimension(500, 500));
        this.panelCalque.add(this.glassOuvrier, Integer.valueOf(30), 0);
        this.panelCalque.add(this.panel, Integer.valueOf(1), 1);

        //Traitement des ressources
        //pour la performance , on instancie seulement  au début , pour ne pas devoir le faire plus tard dans le listener, gain
        String cheminparcelle = "/ressource/images/parcelle.jpg";
        URL url_parcelle = this.getClass().getResource(cheminparcelle);
        //Les canals
        String chemincanalhori = "/ressource/images/canalhori.png";
        URL url_canalhori = this.getClass().getResource(chemincanalhori);
        String chemincanalverti = "/ressource/images/canalverti.png";
        URL url_canalverti = this.getClass().getResource(chemincanalverti);
        //intersection
        String cheminintersection = "/ressource/images/intersection.png";
        URL url_intersection = this.getClass().getResource(cheminintersection);

        final ImageIcon iconparcelle = new ImageIcon(url_parcelle),
                iconcanalhori = new ImageIcon(url_canalhori),
                iconcanalverti = new ImageIcon(url_canalverti),
                iconintersection = new ImageIcon(url_intersection);


        //compteur pour enregistrer les coordonées des Parcelles
        int compteurIparcelle = 0;
        int compteurJparcelle = 0;
        //compteur pour enregistrer les coordonées des canaux HOrizontaux
        int xdebH = 0;
        int ydebH = 0;
        int xfinH = 0;
        int yfinH = 0;
        //compteur pour enregistrer les coordonées des canaux Verticaux
        int xdebV = 0;
        int ydebV = 0;
        int xfinV = 0;
        int yfinV = 2;
        //compteur pour enregistrer les intersections
        int xIntersec = 0;
        int yIntersec = 0;

        //Construction du Plateau
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 13; j++) {
                GridBagConstraints gc = new GridBagConstraints();
                final JLabel thumb = new JLabel();
                //si intersection
                if (testIntersection(i, j)) {

                    thumb.setIcon(iconintersection);
                    gc.gridx = j;
                    gc.gridy = i;
                    //creation de l'objet  Intersection
                    if (xIntersec > 8) {
                        xIntersec = 0;
                        yIntersec = yIntersec + 2;

                    }
                    Intersection inter = new Intersection(xIntersec, yIntersec);
                    xIntersec = xIntersec + 2;
                    //ajout au tableau d'intersection
                    ListIntersect.add(inter);
                    ListIntersectGUI.add(thumb);
                } else {
                    //si canal
                    if (testCanal(i) || testCanaldeux(j)) {
                        //si canal horizontal
                        if (testCanalHori(j)) {
                            //on ajoute 2 a xfin
                            xfinH = xfinH + 2;
                            if (xfinH > 8) {
                                xdebH = 0;
                                xfinH = 2;
                                ydebH = ydebH + 2;
                                yfinH = yfinH + 2;
                            }
                            //on creer le canal
                            final Canal canal = new Canal(false, xdebH, ydebH, xfinH, yfinH);
                            //on enregistre le canal dans la liste modele
                            ListCanauxModele.add(canal);
                            //on modifie les compteurs
                            xdebH = xfinH;
                            //on travaille dessus
                            thumb.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (propositionCanalEncours) {
                                        if (estIrriguable(canal)) {
                                            canalChoisi = thumb;
                                            synchronized (threadAttenteChoixCanalProposition) {
                                                threadAttenteChoixCanalProposition.notify();
                                            }
                                        }
                                    }
                                    //test
                                    canal.toString();
                                }
                            });
                            thumb.setIcon(iconcanalhori);
                            //on enregistre l'image du canal dans la liste GUI
                            ListCanauxGUI.add(thumb);
                            gc.gridwidth = 2;
                            gc.fill = GridBagConstraints.HORIZONTAL;
                            gc.gridx = j;
                            gc.gridy = i;
                        }
                        //si canal vertical
                        if (testCanalVerti(i)) {
                            //on creer le canal
                            final Canal canal = new Canal(false, xdebV, ydebV, xfinV, yfinV);
                            //on enregistre le canal dans la liste modele
                            ListCanauxModele.add(canal);
                            //on modifie les compteurs
                            if (xdebV > 7) {
                                xdebV = 0;
                                xfinV = 0;
                                ydebV = yfinV;
                                yfinV = ydebV + 2;
                            } else {//a chaque tour
                                xdebV = xdebV + 2;
                                xfinV = xdebV;
                                yfinV = ydebV + 2;
                            }
                            //on travaille dessus
                            thumb.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (propositionCanalEncours) {
                                        if (estIrriguable(canal)) {
                                            canalChoisi = thumb;
                                            synchronized (threadAttenteChoixCanalProposition) {
                                                threadAttenteChoixCanalProposition.notify();
                                            }
                                        }
                                    }
                                    //test
                                    canal.toString();
                                }

                            });
                            thumb.setIcon(iconcanalverti);
                            //on enregistre l'image du canal dans la liste GUI
                            ListCanauxGUI.add(thumb);
                            gc.gridheight = 2;
                            gc.fill = GridBagConstraints.VERTICAL;
                            gc.gridx = j;
                            gc.gridy = i;
                        }
                    } else {//si Parcelle
                        thumb.setPreferredSize(new Dimension(50, 50));
                        thumb.setIcon(iconparcelle);
                        //ajout au panel
                        gc.gridx = j;
                        gc.gridy = i;
                        //creation de l'objet  Parcelle
                        final Parcelle parcelle = new Parcelle(0, 0, false, false, Parcelle.typeChamps.vide, compteurIparcelle, compteurJparcelle);
                        //ajout aux tableaux
                        ListParcelleGUI.add(thumb);
                        ListParcelleModele.add(parcelle);
                        //Calcul pour remplir correctement le tableau 8*6 de Parcelle
                        if (compteurJparcelle < 7) {
                            compteurJparcelle++;
                        } else {
                            compteurJparcelle = 0;
                            compteurIparcelle++;
                        }
                        //gestion listener pour le label
                        thumb.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                if (depotencours) {
                                    if (thumb.getIcon() == iconparcelle && (!parcelle.isSecheresse())) {
                                        parcelleChoisie = thumb;
                                        synchronized (threadAttenteDepotParcelle) {
                                            threadAttenteDepotParcelle.notify();
                                        }


                                    }
                                }
                            }

                        });
                    }
                }
                panel.add(thumb, gc);
            }
        }
        initialisationSource();
    }


    ///////////////////
    ///////TESTS///////
    ///////////////////
    public boolean testIntersection(int i, int j) {
        return ((i == 0 || i == 3 || i == 6 || i == 9) && (j == 0 || j == 3 || j == 6 || j == 9 || j == 12));
    }

    public boolean testCanal(int i) {
        return (i == 0 || i == 3 || i == 6 || i == 9);
    }

    public boolean testCanaldeux(int j) {
        return (j == 0 || j == 3 || j == 6 || j == 9 || j == 12);
    }

    public boolean testCanalHori(int j) {
        return (j == 1 || j == 4 || j == 7 || j == 10 || j == 13);
    }

    public boolean testCanalVerti(int i) {
        return (i == 1 || i == 4 || i == 7 || i == 10);
    }

    //Test pour savoir si une parcelle est adjacente  a un canal vertical
    public boolean trouveAdjacentVerti(Canal canal, Parcelle elem) {
        return ((elem.getNumligne() < canal.yfin) && (elem.getNumligne() >= canal.ydeb) && ((elem.getNumcolonne() == canal.xdeb) || (elem.getNumcolonne() == canal.xdeb - 1)));
    }

    //Test pour savoir si une parcelle est adjacente  a un canal horizontal
    public boolean trouveAdjacentHori(Canal canal, Parcelle elem) {
        return ((elem.getNumcolonne() < canal.xfin) && (elem.getNumcolonne() >= canal.xdeb) && ((elem.getNumligne() == canal.ydeb) || (elem.getNumligne() == canal.ydeb - 1)));
    }

    //Test un canal si il peut etre irrigue (touche la source ou un autre canal)
    public boolean estIrriguable(Canal canal) {
        int xdeb = canal.getXdeb();
        int ydeb = canal.getYdeb();
        int xfin = canal.getXfin();
        int yfin = canal.getYfin();
        boolean ok = false;
        //si le canal n est pas irrigue a la base
        if (!canal.isIrrigue()) {
            for (Intersection elem : ListIntersect) {
                //si l'intersection est irrigue
                if (elem.isirrigue()) {
                    //si les coordonnées correspondent au debut du canal
                    if ((xdeb == elem.getI()) && (ydeb == elem.getJ())) {
                        //alors on renvoie vrai
                        ok = true;
                        //on passe l'autre intersection (fin) a irrigue
                        //   irrigueIntersection(xfin, yfin);
                    } else if ((xfin == elem.getI()) && (yfin == elem.getJ())) {  //si les coordonnées correspondent a la fin du canal
                        ok = true;
                        //on passe l'autre intersection (debut) a irrigue
                        //  irrigueIntersection(xdeb, ydeb);
                    }
                }
            }
        }
        return ok;

    }

    ///////////////////
    /////FONCTION//////
    ///////////////////

    // utiliser lors du depot et lors de secheresse
    public void colorierOuvrier(Parcelle parcelle,Joueur joueur) {
        JLabel thumb = modeleToGuiIParcelle(parcelle);
        int nbouv = parcelle.getNbouvrieractif();
        int posx = thumb.getX();
        int posy = thumb.getY();

        //calcul de la position du premier carré de couleur
        final int posxcarre = posx + 5;
        final int posycarre = posy + 5;

     //   System.out.println("posxcarre " + posxcarre + " posycarre " + posycarre);
        //coloriage du premier ouvrier
        if (nbouv > 0) {
            Ouvrier ouvrier = new Ouvrier(joueur.getCouleur());
            ouvrier.drawGraphic();
            ouvrier.setBorder(BorderFactory.createLineBorder(Color.black));
            glassOuvrier.add(ouvrier);
            ouvrier.setBounds(posxcarre, posycarre, 10, 10);

            //coloriage du second ouvrier
            if (nbouv > 1) {
                //calcul de la position du premier carré de couleur
                final int posxcarre2 = posxcarre + 20;
                final int posycarre2 = posycarre;

                Ouvrier ouvrier2 = new Ouvrier(joueur.getCouleur());
                ouvrier2.drawGraphic();
                ouvrier2.setBorder(BorderFactory.createLineBorder(Color.black));
                glassOuvrier.add(ouvrier2);
                ouvrier2.setBounds(posxcarre2, posycarre2, 10, 10);
            }
        }

    }

    public JLabel modeleToGuiIParcelle(Parcelle parcelle) {
        int index = ListParcelleModele.indexOf(parcelle);
        return ListParcelleGUI.get(index);
    }

    public void irrigueIntersection(int i, int j) {
        for (Intersection elem : ListIntersect) {
            if (elem.getI() == i && elem.getJ() == j) {
                elem.setirrigue(true);
            }
        }
    }

    //Renvoie la liste des Parcelles Adjacentes au canal
    public ArrayList<Parcelle> listeParcellesAdjacentes(Canal canal) {
        ArrayList<Parcelle> listeP = new ArrayList<Parcelle>();
        for (Parcelle elem : ListParcelleModele) {
            if (canal.estHorizontale()) {

                if (trouveAdjacentHori(canal, elem)) {
                    listeP.add(elem);
                }
            } else if (canal.estVerticale()) {
                if (trouveAdjacentVerti(canal, elem)) {
                    listeP.add(elem);
                }
            }
        }
        return listeP;
    }

    //irrigue le canal et les parcelles adjacentes au canal
    public void irrigation(Canal canal) {
        //on irrigue
        canal.setIrrigue(true);

        //on recupere son JLabel
        int index = this.getListCanauxModele().indexOf(canal);
        JLabel canalGUI = this.getListCanauxGUI().get(index);
        //on modifie son apparence (JLabel)
        String chemincanalhorirrigue = "/ressource/images/canalhorirrigue.png";
        URL url_canalhorirrigue = this.getClass().getResource(chemincanalhorirrigue);
        String chemincanalvertirrigue = "/ressource/images/canalvertirrigue.png";
        URL url_canalvertirrigue = this.getClass().getResource(chemincanalvertirrigue);
        ImageIcon iconcanalhorirrigue = new ImageIcon(url_canalhorirrigue),
                iconcanalvertirrigue = new ImageIcon(url_canalvertirrigue);
        if (canal.estHorizontale()) {
            canalGUI.setIcon(iconcanalhorirrigue);
        } else {
            canalGUI.setIcon(iconcanalvertirrigue);
        }


        ArrayList<Parcelle> listeP;
        listeP = listeParcellesAdjacentes(canal);
        for (Parcelle parcelle : listeP) {
            //on irrigue la parcelle seulement si elle n<est pas en secheresse
            if (!parcelle.isSecheresse()) {
                parcelle.setIrrigue(true);
            }
        }

        //  this.getListParcelleModele().toString();
        //on irrigue mtn l<intersection au bout du canal
        int xdeb = canal.getXdeb();
        int ydeb = canal.getYdeb();
        int xfin = canal.getXfin();
        int yfin = canal.getYfin();

        for (Intersection elem : ListIntersect) {
            //si l'intersection est irrigue
            if (elem.isirrigue()) {
                //si les coordonnées correspondent au debut du canal ou sa la fin
                if ((xdeb == elem.getI()) && (ydeb == elem.getJ())) {
                    //on passe l'autre intersection (fin) a irrigue
                    irrigueIntersection(xfin, yfin);
                } else if ((xfin == elem.getI()) && (yfin == elem.getJ())) {
                    //on passe l'autre intersection (debut) a irrigue
                    irrigueIntersection(xdeb, ydeb);
                }
            }
        }
    }

    public void decolorationProposition(ArrayList<Proposition> listProposition) {

        for (Proposition proposition : listProposition) {

            Canal canal = proposition.getCanal();
            //on recupere son JLabel
            int index = this.getListCanauxModele().indexOf(canal);
            JLabel canalGUI = this.getListCanauxGUI().get(index);


            //on modifie son apparence (JLabel)
            //les propositions
            String chemincanalhoriprop = "/ressource/images/canalpropositionhori.png";
            URL url_canalhoriprop = this.getClass().getResource(chemincanalhoriprop);
            String chemincanalvertiprop = "/ressource/images/canalpropositionverti.png";
            URL url_canalvertiprop = this.getClass().getResource(chemincanalvertiprop);
            //les normaux
            String chemincanalverti = "/ressource/images/canalverti.png";
            URL url_canalverti = this.getClass().getResource(chemincanalverti);
            String chemincanalhori = "/ressource/images/canalhori.png";
            URL url_canalhori = this.getClass().getResource(chemincanalhori);


            ImageIcon iconcanalhoriprop = new ImageIcon(url_canalhoriprop),
                    iconcanalvertiprop = new ImageIcon(url_canalvertiprop),
                    iconcanalhori = new ImageIcon(url_canalhori),
                    iconcanalverti = new ImageIcon(url_canalverti);


            //si canal vert, alors c est une proposition non retenue => on decolore
            if (canal.estHorizontale()) {

                if (canalGUI.getIcon().toString().equals(iconcanalhoriprop.toString())) {

                    canalGUI.setIcon(iconcanalhori);
                }
            } else if (canal.estVerticale()) {

                if (canalGUI.getIcon().toString().equals(iconcanalvertiprop.toString())) {

                    canalGUI.setIcon(iconcanalverti);
                }
            }
        }


    }

    //colorie le canal ayant eu une proposition en vert , seulement pour les test
    public void colorieCanalPropVert(Canal canal) {


        //on recupere son JLabel
        int index = this.getListCanauxModele().indexOf(canal);
        JLabel canalGUI = this.getListCanauxGUI().get(index);
        //on modifie son apparence (JLabel)
        String chemincanalhori = "/ressource/images/canalpropositionhori.png";
        URL url_canalhori = this.getClass().getResource(chemincanalhori);
        String chemincanalverti = "/ressource/images/canalpropositionverti.png";
        URL url_canalverti = this.getClass().getResource(chemincanalverti);


        ImageIcon iconcanalhori = new ImageIcon(url_canalhori),
                iconcanalverti = new ImageIcon(url_canalverti);

        if (canal.estHorizontale()) {
            canalGUI.setIcon(iconcanalhori);
        } else {
            canalGUI.setIcon(iconcanalverti);
        }
    }

    public void initialisationSource() {
        String cheminsource = "/ressource/images/pionSource.png";
        URL url_source = this.getClass().getResource(cheminsource);
        ImageIcon iconsource = new ImageIcon(url_source);

        Random randomGenerator;
        randomGenerator = new Random();
        int index = randomGenerator.nextInt(ListIntersect.size());
        ListIntersect.get(index).setirrigue(true);
        ListIntersectGUI.get(index).setIcon(iconsource);
    }

    //quand une parcelle devient vide
    public void secheresse(Parcelle parcelle) {
        parcelle.setSecheresse(true);
        parcelle.setProprio(null);
        //et on modifie l image
        String cheminvide = "/ressource/images/vide.png";
        URL url_vide = this.getClass().getResource(cheminvide);
        final ImageIcon iconvide = new ImageIcon(url_vide);

        int index = ListParcelleModele.indexOf(parcelle);
        JLabel parcelleGUI = ListParcelleGUI.get(index);
        parcelleGUI.setIcon(iconvide);


    }

    //permet de choisir sur le plateau le canal que l'on veut encherir
    public Canal choixCanal(Joueur joueur) {

        propositionCanalEncours = true;
        canalChoisi = null;
        Thread t = new Thread();
        threadAttenteChoixCanalProposition = t;
        threadAttenteChoixCanalProposition.start();
        synchronized (threadAttenteChoixCanalProposition) {
            while (canalChoisi == null) {
                try {
                    threadAttenteChoixCanalProposition.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            propositionCanalEncours = false;


            //on recupere le canal correspondant au JLabel et on le return
            int index = ListCanauxGUI.indexOf(canalChoisi);
            Canal canal = ListCanauxModele.get(index);


            return canal;
        }

    }

    //permet de deposer sur le plateau la parcelle que l'on a en main
    public void depotParcelle(Joueur joueur) {

        depotencours = true;
        parcelleChoisie = null;
        Thread t = new Thread();
        threadAttenteDepotParcelle = t;
        threadAttenteDepotParcelle.start();
        synchronized (threadAttenteDepotParcelle) {
            while (parcelleChoisie == null) {
                try {
                    threadAttenteDepotParcelle.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            depotencours = false;
            deposerParcelle(joueur);
        }
    }

    public void deposerParcelle(Joueur joueur) {

        //on recupere la position de la parcelle sur laquel le joueur a cliquer
        int indexParcelle = ListParcelleGUI.indexOf(parcelleChoisie);
        //on recupere la position i et j du label ainsi que le boolean irrigue
        int ligne = ListParcelleModele.get(indexParcelle).getNumligne();
        int col = ListParcelleModele.get(indexParcelle).getNumcolonne();
        boolean estIrrigue = ListParcelleModele.get(indexParcelle).isIrrigue();
        //on recupere la parcelle que le joueur pose (a obtenu dans la phase d'enchere)
        Parcelle parcelleMain = joueur.getParcelleMain();
        parcelleMain.setChamps(parcelleMain.getChamps());
        //on set la position a la parcelle
        parcelleMain.setNumligne(ligne);
        parcelleMain.setNumcolonne(col);
        //on set l'irrigation ou non de la parcelle
        parcelleMain.setIrrigue(estIrrigue);
        //on remplie les ouvriers
        parcelleMain.setNbouvrieractif(parcelleMain.getNbouvrier());
        //on attribue le proprio
        parcelleMain.setProprio(joueur);
        //on met a jour la liste des parcelles du plateau
        ListParcelleModele.set(indexParcelle, parcelleMain);
        //on modifie l'Affichage de la parcelle sur le plateau
        retournerParcelle(parcelleChoisie, parcelleMain);
        ListParcelleGUI.set(indexParcelle, parcelleChoisie);
        //on colorie les ouvriers
        colorierOuvrier(parcelleMain,joueur);


        //on vide la main du joueur
        joueur.setParcelleMain(null);

    }


    //TODO affiche la parcelle nouvellemtn plac�e
    public void retournerParcelle(JLabel thumb, Parcelle parcelle) {
        String chemin = null;
        URL url = null;
        switch (parcelle.getChamps()) {
            case patate:
                if (parcelle.getNbouvrier() == 1) {
                    chemin = "/ressource/images/TuPDT1.png";
                } else {
                    chemin = "/ressource/images/TuPDT2.png";
                }
                break;

            case piment:
                if (parcelle.getNbouvrier() == 1) {
                    chemin = "/ressource/images/TuPiment1.png";
                } else {
                    chemin = "/ressource/images/TuPiment2.png";
                }
                break;

            case banane:
                if (parcelle.getNbouvrier() == 1) {
                    chemin = "/ressource/images/TuBanane1.png";
                } else {
                    chemin = "/ressource/images/TuBanane2.png";
                }
                break;

            case bambou:
                if (parcelle.getNbouvrier() == 1) {
                    chemin = "/ressource/images/TuCanne1.png";
                } else {
                    chemin = "/ressource/images/TuCanne2.png";
                }
                break;

            case haricot:
                if (parcelle.getNbouvrier() == 1) {
                    chemin = "/ressource/images/TuHaricots1.png";
                } else {
                    chemin = "/ressource/images/TuHaricots2.png";
                }
                break;

            case vide:
                chemin = "/ressource/images/DosTuiles.png";
                break;

            case test:
                chemin = "/ressource/images/test.png";
                break;

            default:
                chemin = "/ressource/images/DosTuiles.png";
        }
        url = this.getClass().getResource(chemin);
        ImageIcon icon = new ImageIcon(url);
        thumb.setIcon(icon);

    }

    //renvoie la parcelle etant au coordonnées demandées
    public Parcelle recupParcelleCoordonnee(int i, int j) {
        Parcelle labonne = null;
        for (Parcelle parcelle : ListParcelleModele) {
            if ((parcelle.getNumligne() == j) && (parcelle.getNumcolonne() == i)) {
                labonne = parcelle;
            }
        }
        return labonne;
    }


    //fonctions de calcul du resultat final
    public int[] calculResultatFinal(ArrayList<Joueur> listeJoueurs) {
        ArrayList<Parcelle> liste = getListParcelleModele();


        //on cree les champs
        ArrayList<ArrayList<Parcelle>> listChamps = creationListeChamps();
        int i = 0;
        for (ArrayList<Parcelle> champ : listChamps) {
            System.out.println("taille liste du champs " + i + "est de " + champ.size());
            System.out.println("affichage du champs");
            System.out.println(champ.toString());
            i++;
        }
        //on calcul les points
        int[] res = calculPoint(listeJoueurs, listChamps);
        return res;
    }

    public ArrayList<ArrayList<Parcelle>> creationListeChamps() {
        ArrayList<ArrayList<Parcelle>> listChamps = new ArrayList<ArrayList<Parcelle>>();
        int k = 0;
        //parcours de toutes les parcelles
        for (Parcelle parcelle : this.getListParcelleModele()) {
            k++;
            if ((!parcelle.isMarquer()) && (!parcelle.isSecheresse()) && (parcelle.getChamps() != Parcelle.typeChamps.vide)) {
                //creation du champs
                ArrayList<Parcelle> champs = new ArrayList<Parcelle>();
                //incoporation du champs dans la liste
                listChamps.add(champs);
                //incorporation de la premiere parcelle dans le nouveau champs
                champs.add(parcelle);
                //on ajoute les parcelles voisines de meme type dans les 4 directions :
                //lancement de la racherche par recursivite
                chercheProximite(champs, parcelle.getNumcolonne(), parcelle.getNumligne());
            }
        }

        return listChamps;
    }

    //test si outofbounds
    public boolean outofbounds(int i, int j) {
        return (i < 0 || i > 7 || j < 0 || j > 5);
    }

    //permet de chercher les parcelles de memes types voisines au parcelle du champs
    public void chercheProximite(ArrayList<Parcelle> champs, int i, int j) {

        //type champs de la parcelle recherché sur le board
        Parcelle.typeChamps parcelleTypeChamps;
        //type de la premiere parcelle du champs en question (donc de toutes les parcelles de ce champ)
        Parcelle.typeChamps parcelleChampsTypeChamps = champs.get(0).getChamps();

        //on gere el outofbounds
        if (!outofbounds(i, j)) {
            // on retrouve la parcelle au coordonne recherché
            Parcelle parcelle = recupParcelleCoordonnee(i, j);
            if (!parcelle.isMarquer() && (!parcelle.isSecheresse())) {
                parcelleTypeChamps = parcelle.getChamps();

                if (parcelleTypeChamps == parcelleChampsTypeChamps) {
                    //si la parcelle est differente de la premiere du champs
                    parcelle.setMarquer(true);
                    if (parcelle != champs.get(0)) {
                        champs.add(parcelle);
                    }
                    //on recherche dans les 4 directions par recursivite
                    chercheProximite(champs, parcelle.getNumcolonne() + 1, parcelle.getNumligne());//droit
                    chercheProximite(champs, parcelle.getNumcolonne() - 1, parcelle.getNumligne());//gauche
                    chercheProximite(champs, parcelle.getNumcolonne(), parcelle.getNumligne() - 1);//haut
                    chercheProximite(champs, parcelle.getNumcolonne(), parcelle.getNumligne() + 1);//bas
                }
            }
        }
    }

    public int[] calculPoint(ArrayList<Joueur> joueurs, ArrayList<ArrayList<Parcelle>> listeChamps) {

        int[] totalJoueurChampsTab = new int[joueurs.size()];
        int i = 0;
        for (Joueur joueur : joueurs) {
            int totalJoueurListeChamps = 0;
            for (ArrayList<Parcelle> champs : listeChamps) {
                int taille = champs.size();
                int nbOuvrierActifJoueurChamps = 0;
                for (Parcelle parcelle : champs) {
                    //si la parcelle appartient au joueur en cours on augment son total d ouvrier sur le champs
                    if (parcelle.getProprio() == joueur) {
                        nbOuvrierActifJoueurChamps += parcelle.getNbouvrieractif();
                    }
                }
                //on a fini de parcourir le champs, on calcule les points du joueur sur le champs
                int totalJoueurChamps = nbOuvrierActifJoueurChamps * taille;
                totalJoueurListeChamps += totalJoueurChamps;

            }
            totalJoueurChampsTab[i] = totalJoueurListeChamps;
            i++;
        }
        return totalJoueurChampsTab;


    }


    class Ouvrier extends JPanel {
        Color couleur;
        public Ouvrier(Color couleur){
            this.couleur=couleur;
        }
        public void drawGraphic() {
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(couleur);
            g.fillRect(0, 0, 10, 10);
        }
    }


}

















