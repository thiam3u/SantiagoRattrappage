package TestsSantiago;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import plateau.Canal;
import plateau.Parcelle;
import plateau.Plateau;

public class TestPlateau {
	
	   @Test
	    // ce test permet de v�rifier si une plante poss�de un canal Adjacent verticalement
	    public void testTrouveAdjacentVerti(){
	    // phase initialisation
	        Plateau p = new Plateau();
	        Canal canal = new Canal(false,4,2,4,4);
	        Parcelle elem1 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,2,3);
	        Parcelle elem2 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,2,4);
	        Parcelle elem3 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,3,3);
	        Parcelle elem4 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,3,4);
	        Parcelle elem5 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,0,4);

	      // phase de test
	       //tester avec les 4 plantes :
	        assertTrue(p.trouveAdjacentVerti(canal,elem1));
	        assertTrue(p.trouveAdjacentVerti(canal,elem2));
	        assertTrue(p.trouveAdjacentVerti(canal,elem3));
	        assertTrue(p.trouveAdjacentVerti(canal, elem4));
	        assertFalse(p.trouveAdjacentVerti(canal, elem5));
	    }

}
