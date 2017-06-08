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
	
    public int total() {
        int total = 0;      
        for (Integer elem : listMontant) {
            total += elem;
        }
        return total;
    }
    
    

}
