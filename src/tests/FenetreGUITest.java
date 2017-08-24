package tests;

import gameMaster.MaitreDuJeu;
import joueur.Joueur;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FenetreGUITest {


    @Test
    // Cas de base: quand une enchere est valide si tous les conditions sont réunis
    // c'est à dire montantEnchere >0 , montantEnchere non Déjà pris et montantEnchere < à la somme possedé
    public void testOffreJoueur(){
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(4);
        listeJoueurs.add(new Joueur("Matthieu", 10, Color.red));
        listeJoueurs.add(new Joueur("thiam", 10, Color.blue));
        listeJoueurs.add(new Joueur("nadir", 10, Color.pink));
        listeJoueurs.add(new Joueur("Thomas", 10, Color.green));
        MaitreDuJeu mj = new MaitreDuJeu(listeJoueurs);
        int[] montantEnchere = new int[listeJoueurs.size()];

        boolean res=mj.getFenetre().enchereOk(listeJoueurs.get(3), 5, montantEnchere);
        assertFalse(res);
    }

    @Test
    // permet de verifier que  l'enchere n'est pas valide si le montant  misé est négatif
    public void testOffreJoueursNegative(){
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(4);
        listeJoueurs.add(new Joueur("java", 10, Color.red));
        listeJoueurs.add(new Joueur("GL", 10, Color.blue));
        listeJoueurs.add(new Joueur("reseau", 10, Color.pink));
        listeJoueurs.add(new Joueur("Thomas", 10, Color.green));
        MaitreDuJeu mj = new MaitreDuJeu(listeJoueurs);
        int[] montantEnchere = new int[listeJoueurs.size()];

        boolean res=mj.getFenetre().enchereOk(listeJoueurs.get(0),-5, montantEnchere);
        assertTrue(res);
    }

    @Test
    // permet de verifier que  l'enchere n'est pas valide si le montant  misé > a la somme posseder
    public void testOffreJoueursSuperieurMontantJoueur(){
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(4);
        listeJoueurs.add(new Joueur("Matthieu", 10, Color.red));
        listeJoueurs.add(new Joueur("Yannis", 10, Color.blue));
        listeJoueurs.add(new Joueur("Soraya", 10, Color.pink));
        listeJoueurs.add(new Joueur("Thomas", 10, Color.green));
        MaitreDuJeu mj = new MaitreDuJeu(listeJoueurs);
        int[] montantEnchere = new int[listeJoueurs.size()];

        boolean res=mj.getFenetre().enchereOk(listeJoueurs.get(0),15, montantEnchere);
        assertTrue(res);
    }

    @Test
    // permet de verifier que  l'enchere n'est pas valide si le montant  misé est déjà pris
    public void testOffreJoueursDejaExistante(){
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(4);
        listeJoueurs.add(new Joueur("Matthieu", 10, Color.red));
        listeJoueurs.add(new Joueur("Yannis", 10, Color.blue));
        listeJoueurs.add(new Joueur("Soraya", 10, Color.pink));
        listeJoueurs.add(new Joueur("Thomas", 10, Color.green));
        MaitreDuJeu mj = new MaitreDuJeu(listeJoueurs);
        int[] montantEnchere = new int[listeJoueurs.size()];
        montantEnchere[0] = 4;
        boolean res=mj.getFenetre().enchereOk(listeJoueurs.get(0),4, montantEnchere);
        assertTrue(res);
    }
}