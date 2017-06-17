package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
        
    }

}
