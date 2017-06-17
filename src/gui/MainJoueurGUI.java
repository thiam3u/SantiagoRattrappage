package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import gameMaster.MaitreDuJeu;

public class MainJoueurGUI {
	
    private JPanel panel = new JPanel();
    private Joueur joueur;
    private JLabel canalLabel;
    
    public MainJoueurGUI(Joueur j){
        this.joueur = j;
        
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(j.getCouleur());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // On positionne la case de départ du composant
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel(joueur.getPseudo()), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel(Integer.toString(joueur.getArgent())), gbc);
        
        panel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        
        String cheminparcelle = "/ressource/images/canalhorirrigue.png";
        URL url_parcelle = this.getClass().getResource(cheminparcelle);
        final ImageIcon canal = new ImageIcon(url_parcelle);
        canalLabel = new JLabel(canal);
        
    }

}
