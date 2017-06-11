package joueur;

import java.util.ArrayList;
import plateau.Canal;

public class Proposition {
	
    ArrayList<Joueur> listJoueur = new ArrayList<Joueur>();
    ArrayList<Integer> listMontant = new ArrayList<Integer>();
    Canal canal;
    
	public Proposition(Canal canal) {
		this.canal = canal;
	}
	
    public Canal getCanal() {
        return canal;
    }
    
    public void setCanal(Canal canal) {
        this.canal = canal;
    }
    
    // Calcul du montant total des propositions
    public int total() {
        int total = 0;      
        for (Integer elem : listMontant) {
            total += elem;
        }
        return total;
    }
    
    // On ajoute le montant de l'enchère du joueur qui soutient la nouvelle proposition ou non
    public void soutenirProposition(Joueur joueur, int montant) {
        listJoueur.add(joueur);
        listMontant.add(montant);
    }
    
    public void paiementProposition() {
        int nbj = 0;
        // Pour chaque joueur, on lui débite le montant qu'il s'est engagé à payer
        for (Joueur joueur : listJoueur) {
        	joueur.setArgent(joueur.getArgent() - listMontant.get(nbj));
        	nbj++;
        }
    }
    
    @Override
    public String toString() {
        return "Proposition{" +
                "listJoueur=" + listJoueur.toString() +
                ", listmontant=" + listMontant +
                ", total=" + total() +
                '}';
    }

    // Pour chaque joueur, on affiche le montant de sa proposition
    public String affichageProposition() {
        String mess = "";
        for (Joueur joueur : listJoueur) {
            int montant = listMontant.get(listJoueur.indexOf(joueur));
            mess = mess + joueur.getPseudo() + " donne " + montant + " escudos " + '\n';
        }
        return mess;
    }
    
}
