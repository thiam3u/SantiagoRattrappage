package tests;

import gameMaster.MaitreDuJeu;
import joueur.Joueur;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import plateau.Canal;
import plateau.Parcelle;
import plateau.Plateau;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlateauTest {


    @Test
    // ce test permet de vérifier si une plante posséde un canal Adjacent verticalement
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


    //faire la meme avec trouveAdjacentHori(canal,elem)

    @Test
    // ce test permet de vérifier si une plante posséde un canal Adjacent horizontalement
    public void testTrouveAdjacentHori(){
        Plateau p = new Plateau();
        Canal canal = new Canal(false,2,4,4,4);
        Parcelle elem1 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,3,2);
        Parcelle elem2 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,4,2);
        Parcelle elem3 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,3,3);
        Parcelle elem4 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,4,3);
        Parcelle elem5 = new Parcelle(1,0,false,false, Parcelle.typeChamps.vide,0,3);

        //tester avec les 4 parcelles :
        assertTrue(p.trouveAdjacentHori(canal, elem1));
        assertTrue(p.trouveAdjacentHori(canal, elem2));
        assertTrue(p.trouveAdjacentHori(canal, elem3));
        assertTrue(p.trouveAdjacentHori(canal, elem4));
        assertFalse(p.trouveAdjacentHori(canal, elem5));

    }

    @Test
   // permet de tester la phase5 : permet de vérifier que les plantes Adjacent à un canal est bien irrigué 
    public void testIrrigation(){
        Plateau p = new Plateau();
        Canal canal = new Canal(false,2,4,4,4);
        //p.irrigation(canal);
        ArrayList<Parcelle> listeP;
        listeP = p.listeParcellesAdjacentes(canal);
        for (Parcelle parcelle : listeP) {
            assertTrue(parcelle.isIrrigue());
        }
    }

    @Test
    public void testEstIrriguable(){
        Plateau p = new Plateau();
        Canal canal = new Canal(false,2,4,4,4);
        assertTrue(true);
    }
}