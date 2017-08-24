package gui;

import gameMaster.MaitreDuJeu;
import joueur.Joueur;
import reseau.Client;
import reseau.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.IOException;
import java.util.ArrayList;


public class LauncherGUI {

    public JPanel getPanel() {
        return panel;
    }

    private JPanel panel = new JPanel();
    private JButton boutonRejoindre = new JButton("Rejoindre partie");
    private JButton boutonCreer = new JButton("Creer partie");
    private JButton boutonSolo = new JButton("Creer partie en local");
    private JLabel labelPseudo = new JLabel("Entrez votre pseudo");
    private JLabel info = new JLabel(" ");
    public JTextField jtf = new JTextField("Pseudonyme");
    private Boolean server = false;

    public Boolean getServer() {
        return server;
    }

    public Boolean getClient() {
        return client;
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }

    private Boolean client = false;


    public String pseudo2String;
    public String pseudo3String;
    public String pseudo4String;

    public LauncherGUI() {
        String cheminparcelle = "/ressource/images/santiago.jpg";
        URL url_parcelle = this.getClass().getResource(cheminparcelle);
        final ImageIcon fondLauncher = new ImageIcon(url_parcelle);

        //L'objet servant à positionner les composants
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LAST_LINE_END ;
        //On positionne la case de départ du composant
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        panel.add(info, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(labelPseudo, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(jtf, gbc);
        //gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(boutonCreer, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(boutonRejoindre, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(boutonSolo, gbc);

        boutonCreer.addActionListener(new BoutonCreerListener());
        boutonRejoindre.addActionListener(new BoutonRejoindreListener());
        boutonSolo.addActionListener(new BoutonSoloListener());

    }

    class BoutonCreerListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {

            info.setText("En attente de joueurs SVP !!");
            server = true;

        }

    }

    class BoutonRejoindreListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            client = true;

        }
    }

    class BoutonSoloListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            JOptionPane pseudo2 = new JOptionPane();
            JOptionPane pseudo3 = new JOptionPane();
            JOptionPane pseudo4 = new JOptionPane();
            pseudo2String = pseudo2.showInputDialog(null, "Entrez le pseudo joueur 2", "Pseudo joueur 2! ", JOptionPane.QUESTION_MESSAGE);
            pseudo3String = pseudo3.showInputDialog(null, "Entrez le pseudo joueur 3", "Pseudo joueur 3! ", JOptionPane.QUESTION_MESSAGE);
            pseudo4String = pseudo4.showInputDialog(null, "Entrez le pseudo joueur 4", "Pseudo joueur 4! ", JOptionPane.QUESTION_MESSAGE);

        }

    }
}