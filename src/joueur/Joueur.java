package joueur;

import plateau.Parcelle;
import java.awt.*;
import java.io.Serializable;

public class Joueur {
	
	private static final long serialversionUID = 1350092881346723535L;
	private String pseudo;
	private int argent;
	private int score;
	private Parcelle parcelleMain;
	private boolean canalComplementaire = true;
	private Color couleur;
	
	public Joueur(String pseudo, int argent, int score, Color couleur){
		this.pseudo = pseudo;
		this.argent = argent;
		this.score = 0;
		this.couleur = couleur;
	}

}
