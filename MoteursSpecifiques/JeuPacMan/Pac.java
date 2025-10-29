package MoteursSpecifiques.JeuPacMan;

import MoteurGenerique.Moteur;
import MoteurGenerique.ObjetJeu;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Pac extends ObjetJeu {
   public enum Direction {HAUT, BAS, DROITE, GAUCHE} 
   private Direction dir;
   private Image pacd;
   private Image pacb;
   private Image pach;
   private Image pacg;

   public Pac() {
     pacd = new ImageIcon(ParamPacMan.CHEMIN_PACD).getImage();
     pacg = new ImageIcon(ParamPacMan.CHEMIN_PACG).getImage();
     pach = new ImageIcon(ParamPacMan.CHEMIN_PACH).getImage();
     pacb = new ImageIcon(ParamPacMan.CHEMIN_PACB).getImage();
   }

   public void generer() {
     dir = Direction.DROITE;
   }

   public void mettreAJour() {}

   public void setLocation(int x, int y) {
     this.x = x;
     this.y = y;
   }

   public void setDirection(Pac.Direction nDir) {
     dir = nDir;
     Labyrinthe.CELLULE[][] etat = ((MoteurPacMan)Moteur.
       getInstance().getMoteurSpecifique()).getLabyrinthe().
       getEtat();
     switch (nDir) {
       case DROITE:
         if (etat[x+1][y]!=Labyrinthe.CELLULE.MUR)
         x = x + 1;
         break;
       case GAUCHE:
         if (etat[x-1][y]!=Labyrinthe.CELLULE.MUR)
         x = x - 1;
         break;
       case HAUT:
         if (etat[x][y-1]!=Labyrinthe.CELLULE.MUR)
         y = y - 1;
         break;
       case BAS:
         if (etat[x][y+1]!=Labyrinthe.CELLULE.MUR &&
            (etat[x][y+1]!=Labyrinthe.CELLULE.PORTE_OUVERTE ||
             etat[x][y+1]!=Labyrinthe.CELLULE.PORTE_OUVERTE))
         y = y + 1;
         break;
      }
   }

   public void dessiner(Graphics g) {
     switch (dir) {
       case DROITE:
         g.drawImage(pacd, getX()*ParamPacMan.TAILLE_CELLULE,
           getY()*ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
         break;
       case GAUCHE:
         g.drawImage(pacg, getX()*ParamPacMan.TAILLE_CELLULE,
           getY()*ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
         break;
       case HAUT:
         g.drawImage(pach, getX()*ParamPacMan.TAILLE_CELLULE,
           getY()*ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
         break;
       case BAS:
         g.drawImage(pacb, getX()*ParamPacMan.TAILLE_CELLULE,
           getY()*ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE, null);
         break;
     }   
   }
}
