package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import joueur.Joueur;

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
        String cheminparcelle = "/ressource/images/parcelle.png";
        URL url_parcelle = this.getClass().getResource(cheminparcelle);
        final ImageIcon iconparcelle = new ImageIcon(url_parcelle);
        
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        // Instanciation puis définition du libellé
        for (int i = 0; i < 4; i++) {
            final int indice = i;
            final JLabel thumb = new JLabel();
            thumb.setPreferredSize(new Dimension(100, 100));
            thumb.setIcon(iconparcelle);
            pileParcellesGUI.add(thumb);
            thumb.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //On remplit la liste des Jlabel des 4 piles pour travailler dessus plus tard
                    if (encherencours) {
                        if (thumb.getIcon() != iconvide) {
                            //Recuperer la parcelle
                            parcelleChoisie = pileParcelles.get(indice).getParcelle();
                            if (pileParcelles.get(indice).getPileParcelle().size() > 1) {
                                //Retirer l'image
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
    					chemin = "/ressource/images/patate1.png";
    				} else {
    					chemin = "/ressource/images/patate2.png";
    				}
    				break;
    				
                case piment:
                    if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                        chemin = "/ressource/images/piment1.png";
                    } else {
                        chemin = "/ressource/images/piment2.png";
                    }
                    break;
                    
                case banane:
                    if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                        chemin = "/ressource/images/banane1.png";
                    } else {
                        chemin = "/ressource/images/banane2.png";
                    }
                    break;
                    
                case bambou:
                    if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                        chemin = "/ressource/images/bambou1.png";
                    } else {
                        chemin = "/ressource/images/bambou2.png";
                    }
                    break;
                    
                case haricot:
                    if (pileParcelles.get(compt).recupNbouvrierDessus() == 1) {
                        chemin = "/ressource/images/haricot1.png";
                    } else {
                        chemin = "/ressource/images/haricot2.png";
                    }
                    break;
                    
                case vide:
                    chemin = "/ressource/images/vide.png";
                    break;
                   
                case test:
                    chemin = "/ressource/images/test.png";
                    break;
    		}
        
        
       
        
        
        
        
}
