
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
        String cheminparcelle = "/ressource/images/parcelle.png";
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

                   X      ҆    ��     �U
    ⒤w��                < P l a t e a u . j a v a     X      ҆    ��    X�U
    �e�w��                < P l a t e a u . j a v a     X      ҆    ��    ��U
    �e�w���               < P l a t e a u . j a v a     X      ҆    ��    �U
    Č�w��� �             < P l a t e a u . j a v a     h      ӆ          `�U
    ?Uw��              & < N o u v e a u   d o s s i e r   ( 3 )     �Ah      ӆ          ��U
    a9Uw��  �           & < N o u v e a u   d o s s i e r   ( 3 )     �Ah      ӆ          0�U
    
1�"w��              & < N o u v e a u   d o s s i e r   ( 3 )       X      ӆ          ��U
    
1�"w��                < p l a t e a u A O U T       X      ӆ          ��U
    
1�"w��   �            < p l a t e a u A O U T       X      ӆ          H�U
    ��"w��               < p l a t e a u A O U T       X      ӆ          ��U
    8)+w��  �            < p l a t e a u A O U T       P      Ԇ    ӆ    ��U
    ��<4w��               < p l a t e a u       P      Ԇ    ӆ    H�U
    ��<4w�� �              < p l a t e a u       P      Ԇ    ӆ    ��U
    ��<4w�� � �            < p l a t e a u       P      Ն    Ԇ    ��U
    HP=4w��                < C a n a l . j a v a P      Ն    Ԇ    8�U
    HP=4w��               < C a n a l . j a v a P      Ն    Ԇ    ��U
    HP=4w��               < C a n a l . j a v a P      Ն    Ԇ    ��U
    HP=4w���               < C a n a l . j a v a P      Ն    Ԇ    (�U
    ^w=4w��� �             < C a n a l . j a v a `      ֆ    Ԇ    x�U
    ��=4w��               " < I n t e r s e c t i o n . j a v a   `      ֆ    Ԇ    ��U
    ��=4w��              " < I n t e r s e c t i o n . j a v a   `      ֆ    Ԇ    8�U
    ��=4w��              " < I n t e r s e c t i o n . j a v a   `      ֆ    Ԇ    ��U
    ��=4w���              " < I n t e r s e c t i o n . j a v a   `      ֆ    Ԇ    ��U
    ��=4w��� �            " < I n t e r s e c t i o n . j a v a   X      ׆    Ԇ    X�U
    <O>4w��                < P a r c e l l e . j a v a   X      ׆    Ԇ    ��U
    <O>4w��               < P a r c e l l e . j a v a   X      ׆    Ԇ    �U
    <O>4w��               < P a r c e l l e . j a v a   X      ׆    Ԇ    `�U
    <O>4w���               < P a r c e l l e . j a v a   X      ׆    Ԇ    ��U
    <O>4w��� �             < P a r c e l l e . j a v a   `      ؆    Ԇ    �U
    ��>4w��               " < P i l e P a r c e l l e . j a v a   `      ؆    Ԇ    p�U
    ��>4w��              " < P i l e P a r c e l l e . j a v a   `      ؆    Ԇ    ��U
    ��>4w��              " < P i l e P a r c e l l e . j a v a   `      ؆    Ԇ    0�U
    ��>4w���              " < P i l e P a r c e l l e . j a v a   `      ؆    Ԇ    ��U
    ��>4w��� �            " < P i l e P a r c e l l e . j a v a   X      ن    Ԇ    ��U
    �Y?4w��                < P l a t e a u . j a v a     X      ن    Ԇ    H�U
    �Y?4w��               < P l a t e a u . j a v a     X      ن    Ԇ    ��U
    �Y?4w��               < P l a t e a u . j a v a     X      ن    Ԇ    ��U
    �Y?4w���               < P l a t e a u . j a v a     X      ن    Ԇ    P�U
    �Y?4w��� �             < P l a t e a u . j a v a     P      Ԇ    ӆ    ��U
    /E4w��               < p l a t e a u       P      Ԇ    ӆ    ��U
    /E4w��  �            < p l a t e a u       h      چ          H�U
    �Z�;w��              & < N o u v e a u   d o s s i e r   ( 3 )     k h      چ          ��U
    �Z�;w��  �           & < N o u v e a u   d o s s i e r   ( 3 )     k h      چ          �U
    vq3?w��              & < N o u v e a u   d o s s i e r   ( 3 )       P      چ          ��U
    vq3?w��                < J o u e u r A O U T                                                 P      چ           �U
    vq3?w��   �            < J o u e u r A O U T P      چ          P�U
    �>M?w��               < J o u e u r A O U T P      چ          ��U
    ��,`w��  �            < J o u e u r A O U T H      ۆ    چ    ��U
    g-`w��               < j o u e u r H      ۆ    چ    8�U
    g-`w�� �              < j o u e u r H      ۆ    چ    ��U
    g-`w�� � �            < j o u e u r X      ܆    ۆ    ��U
    �``w��                < J o u e u r . j a v a     G X      ܆    ۆ     �U
    �``w��               < J o u e u r . j a v a     G X      ܆    ۆ    x�U
    �``w��               < J o u e u r . j a v a     G X      ܆    ۆ    ��U
    �``w���               < J o u e u r . j a v a     G X      ܆    ۆ    (�U
    )``w��� �             < J o u e u r . j a v a     G `      ݆    ۆ    ��U
    ��``w��                 < P r o p o s i t i o n . j a v a     `      ݆    ۆ    ��U
    G�``w��                < P r o p o s i t i o n . j a v a     `      ݆    ۆ    @�U
    G�``w��                < P r o p o s i t i o n . j a v a     `      ݆    ۆ    ��U
    ��``w���                < P r o p o s i t i o n . j a v a     `      ݆    ۆ     �U
    ��``w��� �              < P r o p o s i t i o n . j a v a     H      ۆ    چ    `�U
    ��g`w��               < j o u e u r H      ۆ    چ    ��U
    ��g`w��  �            < j o u e u r                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 du champs " + i + "est de " + champ.size());
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

















