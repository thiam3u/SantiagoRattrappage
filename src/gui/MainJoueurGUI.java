package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import gameMaster.MaitreDuJeu;

public class MainJoueurGUI {
	
    private JPanel panel = new JPanel();
    private Joueur joueur;
    private JLabel canalLabel;
    
    public MainJoueurGUI(Joueur j){
        this.joueur = j;
        
    }

}
