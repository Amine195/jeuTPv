package MoteursSpecifiques.JeuSerpent;

import MoteurGenerique.Moteur;
import MoteurGenerique.IMoteurSpecifique;
import Utils.Son.EffetSonore;

public class MoteurSerpent implements IMoteurSpecifique {
   private Serpent serpent;
   private Nourriture nourriture;
   private Mur[] murs;
   private Moteur moteur;
   private int frequenceMaj;
   private FabriqueSerpent fab;

   public MoteurSerpent() {
     fab = new FabriqueSerpent();
     moteur = Moteur.getInstance();
   }

   public void initialiserJeu() {
     murs = new Mur[ParamSerpent.NB_MURS];
     for (int i=0; i<ParamSerpent.NB_MURS; i++) {
       murs[i] = (Mur) fab.creerObjet(ParamSerpent.MUR);
     }
     serpent = (Serpent) fab.creerObjet(ParamSerpent.SERPENT);
     nourriture = (Nourriture) fab.creerObjet(ParamSerpent.NOURRITURE);
   }

   public void genererJeu() {
     moteur.setScore(0);
     moteur.setNiveau(1);
     frequenceMaj = ParamSerpent.FREQUENCE_MAJ;
     moteur.notification();
     serpent.generer();
     do {
       nourriture.generer();
     } while (serpent.contains(nourriture.getX(), nourriture.getY())); 
     Mur m;
     for (int i=0; i<ParamSerpent.NB_MURS; i++) {
       m = murs[i];
       do {
         m.generer();
       } while (serpent.intersects(m.getX(), m.getY()) || (nourriture.getX()==m.getX() && nourriture.getY()==m.getY())); 
     }
     moteur.setEtat(Moteur.Etat.INITIALISE);
   }

   public void mettreAJourJeu() {
     traiterCollision(); 
     serpent.mettreAJour();
   }

   public void traiterCollision() {
     int teteX = serpent.getTeteX();
     int teteY = serpent.getTeteY();
     if (!moteur.getIU().containsPanneauJeu(teteX, teteY)) {
       moteur.terminerJeu();
       return;
     }
     for (int i=0; i<ParamSerpent.NB_MURS; i++) {
       if (teteX == murs[i].getX() && teteY == murs[i].getY()) {
         moteur.terminerJeu();
         return;
       }
     }
     if (serpent.mangeLuiMeme()) {
       moteur.terminerJeu();
       return;
     }
     if (teteX == nourriture.getX() && teteY == nourriture.getY()) {
       int x, y;
       boolean meme;
       EffetSonore.TOC.play();
       moteur.setScore(moteur.getScore()+1);
       if (moteur.getScore() % 10 == 0) changerNiveau();
       moteur.notification();
       do {
         meme = false;
         nourriture.generer();
         x = nourriture.getX();
         y = nourriture.getY();
         for (int i=0; i<ParamSerpent.NB_MURS; i++) {
           if (x == murs[i].getX() && y == murs[i].getY()) {
             meme=true;
             break;
           }
         }
       } while (serpent.contains(x, y) || meme);
     } else {
       serpent.diminuer();
     }   
   }

   public void changerNiveau() {
     moteur.setNiveau(moteur.getNiveau()+1);
     frequenceMaj += 5;
   }

   public Serpent getSerpent() { return serpent;}

   public Nourriture getNourriture() { return nourriture;}

   public Mur[] getMurs() { return murs;}

   public long getPeriodeMaj() { return 1000000000L/frequenceMaj;}
}


   