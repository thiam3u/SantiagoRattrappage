package gui;

import java.awt.Color;
import java.awt.GridLayout;
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
        
        
       
        
        
        
        
}
