package gui;

import gameMaster.MaitreDuJeu;
import joueur.Joueur;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class MainJoueurGUI {

    private JPanel panel = new JPanel();
    private Joueur joueur;
    private JLabel canalLabel;


    public MainJoueurGUI(Joueur j){
        this.joueur = j;

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(j.getCouleur());
        GridBagConstraints gbc = new GridBagConstraints();
        //On positionne la case de départ du composant
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
        if(joueur.isCanalComplementaire()){
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(canalLabel, gbc);
        }
        String chemin = "/ressource/images/";
        URL url = null;
        ImageIcon icon;
        if(joueur.getParcelleMain() != null){
            chemin += joueur.getParcelleMain().getChamps();
            chemin += joueur.getParcelleMain().getNbouvrier();
            chemin += ".png";
            url = this.getClass().getResource(chemin);
            icon = new ImageIcon(url);
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel(icon), gbc);
        }
    }


    public JPanel getPanel() {
        return panel;
    }
}
