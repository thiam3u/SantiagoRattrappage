package gui;


import joueur.Joueur;
import plateau.Parcelle;
import plateau.PileParcelle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;


public class PileParcelleGUI {

    Thread threadAttenteChoixPile;
    ArrayList<JLabel> pileParcellesGUI = new ArrayList<JLabel>();
    private ArrayList<PileParcelle> pileParcelles;
    private JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
    private boolean encherencours;
    private Parcelle parcelleChoisie = null;
    String cheminvide = "/ressource/images/vide.png";
    URL url_vide = this.getClass().getResource(cheminvide);
    final ImageIcon iconvide = new ImageIcon(url_vide);

    public PileParcelleGUI(ArrayList<PileParcelle> pileParcelles) {
        this.pileParcelles = pileParcelles;

    }

    public void initialisation() {
        String cheminparcelle = "/ressource/images/parcelle.jpg";
        URL url_parcelle = this.getClass().getResource(cheminparcelle);
        final ImageIcon iconparcelle = new ImageIcon(url_parcelle);


        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        //Possibilité 2 : instanciation puis définition du libellé
        for (int i = 0; i < 4; i++) {
            final int indice = i;
            final JLabel thumb = new JLabel();
            thumb.setPreferredSize(new Dimension(100, 100));
            thumb.setIcon(iconparcelle);
            pileParcellesGUI.add(thumb);
            thumb.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //on remplit la liste des Jlabel des 4 piles pour travailelr dessus plus tard
                    if (encherencours) {
                        if (thumb.getIcon() != iconvide) {
                            //recuperer la parcelle
                            parcelleChoisie = pileParcelles.get(indice).getParcelle();
                            if (pileParcelles.get(indice).getPileParcelle().size() > 1) {
                                //retirer limage
                                pileParcelles.get(indice).popParcelle();
                                //afficher  la parcelle suivante
                                // retournerParcelles(thumb, indice);
                                thumb.setIcon(iconvide);
                            } else {
                                //retirer limage
                                pileParcelles.get(indice).popParcelle();
                                thumb.setIcon(iconvide);
                            }
                        }
                        synchronized (threadAttenteChoixPile) {
                            threadAttenteChoixPile.notify();
                            //stocker la parcelle dans la main du joueur
                        }
                    }

                }

            });

            panel.add(thumb);
        }
    }

    public void retournerParcelles(JLabel thumb, int compt) {
        String chemin = null;
        URL url = null;
        switch (pileParcelles.get(compt).recupTypeDessus()) {
            case patate:
                if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                    chemin = "/ressource/images/TuPDT1.png";
                } else {
                    chemin = "/ressource/images/TuPDT2.png";
                }
                break;

            case piment:
                if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                    chemin = "/ressource/images/TuPiment1.png";
                } else {
                    chemin = "/ressource/images/TuPiment2.png";
                }
                break;

            case banane:
                if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                    chemin = "/ressource/images/TuBanane1.png";
                } else {
                    chemin = "/ressource/images/TuBanane2.png";
                }
                break;

            case bambou:
                if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                    chemin = "/ressource/images/TuCanne1.png";
                } else {
                    chemin = "/ressource/images/TuCanne2.png";
                }
                break;

            case haricot:
                if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
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

    //retourneretourner la parcelle au sommet de chacunes des 4 piles
    public void retournerLesPilesParcelles() {
        int compteur = 0;
        //pour chacune des 4 piles , retourner la parcelle au sommet de la pile
        for (JLabel jLabel : pileParcellesGUI) {
            JLabel thumb = jLabel;
            retournerParcelles(thumb, compteur);
            compteur++;
        }
    }

    public JPanel affichagePileParcelle() {
        initialisation();
        return panel;
    }

    public Parcelle choixParcelle(Joueur j_actif) {
        setEnchereEnCours(true);
        parcelleChoisie = null;
        Thread t = new Thread();
        threadAttenteChoixPile = t;
        threadAttenteChoixPile.start();
        synchronized (threadAttenteChoixPile) {
            while (parcelleChoisie == null) {
                try {
                    threadAttenteChoixPile.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //quand le thread est fini, on return parcelleChoisie
            setEnchereEnCours(false);
            return parcelleChoisie;
        }
    }

    private void setEnchereEnCours(boolean b) {
        encherencours = b;
    }

    public void retirerParcelle(Parcelle pChoisie) {
        for(int i =0; i<4; i++){
            if(pileParcelles.get(i).getParcelle().equals(pChoisie)){
                pileParcelles.get(i).popParcelle();
                pileParcellesGUI.get(i).setIcon(iconvide);
            }
        }
    }
}