package tests;

import gameMaster.MaitreDuJeu;
import joueur.Joueur;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MaitreDuJeuTest {

    @Test
    // ce test permet de vérifier la nombre de plante dans la pile de chaque joueur
       public void initPileParcellesTestTaillePile(){
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(4);
        listeJoueurs.add(new Joueur("Matthieu", 10, Color.red));
        listeJoueurs.add(new Joueur("Yannis", 10, Color.blue));
        listeJoueurs.add(new Joueur("Soraya", 10, Color.pink));
        listeJoueurs.add(new Joueur("Thomas", 10, Color.green));
        MaitreDuJeu mj = new MaitreDuJeu(listeJoueurs);

        assertEquals(11, mj.getPileParcelles().get(0).getNbParcelle());

    }
}