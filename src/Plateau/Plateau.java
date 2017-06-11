
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

















