package MoteursSpecifiques.JeuPacMan;

import MoteurGenerique.Moteur;
import MoteurGenerique.IMoteurSpecifique;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class MoteurPacMan implements IMoteurSpecifique {
   private Labyrinthe labyrinthe;
   private Pac pac;
   private ArrayList<Fantome> fantomes;
   private Moteur moteur;
   private int timer;
   private FabriquePacMan fab;
   private int nb_fantomes_caches;

   public MoteurPacMan() {
     fab = new FabriquePacMan();
     moteur = Moteur.getInstance();
   }

   public void initialiserJeu() {
     labyrinthe = (Labyrinthe) fab.creerObjet(
       ParamPacMan.LABYRINTHE);
     pac = (Pac) fab.creerObjet(ParamPacMan.PAC);
     fantomes = new ArrayList<Fantome>();
     for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
       fantomes.add(((Fantome) fab.creerObjet(
         ParamPacMan.FANTOME)));
     }
   }

   public void genererJeu() {
     moteur.setScore(0);
     moteur.setNiveau(1);
     timer = 0;
     nb_fantomes_caches = 0;
     moteur.notification();
     labyrinthe.generer();
     pac.generer();
     pac.setLocation(labyrinthe.getInitialPacLocX(), 
                     labyrinthe.getInitialPacLocY());
     genererFantomes();
     moteur.setEtat(Moteur.Etat.INITIALISE);
   }

   private void genererFantomes() {
     ArrayList<Point> zoneDepart = labyrinthe.getZoneDepart();
     int max = zoneDepart.size();
     Random rand = new Random();
     int selectionne, x, y;
     for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
       selectionne = rand.nextInt(max); 
       x = (int)zoneDepart.get(selectionne).getX();
       y = (int)zoneDepart.get(selectionne).getY();
       zoneDepart.remove(selectionne);
       max--;
       fantomes.get(i).generer();
       fantomes.get(i).setLocation(x, y);
     }
   }

   public void mettreAJourJeu() {
     if (timer >= 0) timer++;
     traiterCollision();
     if (!getPorteOuverte() && 
        (timer == ParamPacMan.DUREE_PORTE_OUVERTE)) {
       Labyrinthe.CELLULE[][] etat = getLabyrinthe().getEtat();
       etat[labyrinthe.getPorteX()][labyrinthe.getPorteY()] = 
         Labyrinthe.CELLULE.PORTE_OUVERTE;
       timer = -1;
     }
     if (getPorteOuverte() && (timer == ParamPacMan.DUREE_EFFRAYE)) {
       for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
         fantomes.get(i).setEffraye(false);
         timer = -1;
       }
     }
     for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
       fantomes.get(i).mettreAJour();
     }
   }

   public void traiterCollision() {
     int x = pac.getX();
     int y = pac.getY();
     Labyrinthe.CELLULE[][] etat = getLabyrinthe().getEtat();
     if (etat[x][y]==Labyrinthe.CELLULE.POINT) {
       moteur.setScore(moteur.getScore()+1);
       etat[x][y]=Labyrinthe.CELLULE.BLANCHE;
       moteur.notification();
     }
     if (etat[x][y]==Labyrinthe.CELLULE.POINT_PUISSANCE) {
       moteur.setScore(moteur.getScore()+1);
       etat[x][y]=Labyrinthe.CELLULE.BLANCHE;
       moteur.notification();
       for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
         fantomes.get(i).setEffraye(true);
       }
       timer = 0;
     }
     for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
       if ((fantomes.get(i).getX()==x) && (fantomes.get(i).
          getY()==y) && fantomes.get(i).estEffraye()){
         moteur.setScore(moteur.getScore()+100);
         moteur.notification();
         // cacher le fantome
         nb_fantomes_caches++;
         fantomes.get(i).setX(-1);
         fantomes.get(i).setY(-1);
       } else if ((fantomes.get(i).getX()==x) && (fantomes.get(i).
         getY()==y) && !fantomes.get(i).estEffraye()){
         moteur.terminerJeu();
         return;
       } 
     }
     if (nb_fantomes_caches==ParamPacMan.NB_FANTOMES) {
       changerNiveau();
       moteur.notification();
     }
   }

   private void changerNiveau() {
     moteur.setNiveau(moteur.getNiveau()+1);
     timer = 0;
     nb_fantomes_caches=0;
     labyrinthe.generer();
     pac.generer();
     pac.setLocation(labyrinthe.getInitialPacLocX(),
                     labyrinthe.getInitialPacLocY());
     genererFantomes();
     for (int i=0; i<ParamPacMan.NB_FANTOMES; i++) {
       fantomes.get(i).accelerer();
     }
   }

   public boolean getPorteOuverte() {
     Labyrinthe.CELLULE[][] etat = getLabyrinthe().getEtat();
     return (etat[labyrinthe.getPorteX()][labyrinthe.getPorteY()]
       ==Labyrinthe.CELLULE.PORTE_OUVERTE);
   }

   public Labyrinthe getLabyrinthe() { return labyrinthe;}

   public Pac getPac() { return pac;}

   public ArrayList<Fantome> getFantomes() { return fantomes;}

   public long getPeriodeMaj() { 
     return 1000000000L/ParamPacMan.FREQUENCE_MAJ;
   }
}
