package MoteursSpecifiques.JeuSerpent;

import MoteurGenerique.ObjetJeu;
import java.awt.Image;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class SegmentSerpent extends ObjetJeu {
   private int longueur;       
   private Serpent.Direction direction;  
   private Image image_corps; 

   public SegmentSerpent(int teteX, int teteY, int longueur, Serpent.Direction direction) {
     x = teteX;
     y = teteY;
     this.direction = direction;
     this.longueur = longueur;
     image_corps = new ImageIcon(ParamSerpent.CHEMIN_CORPS).getImage();
   }

   public void generer() {}

   public void mettreAJour() {}

   public void grandir() {
     longueur++;
     switch (direction) {
       case GAUCHE: x--; break;
       case DROITE: x++; break;
       case HAUT: y--; break;
       case BAS: y++; break;
     }
   }

   public void diminuer() {
     longueur--; 
   }

   private int getQueueX() {
     if (direction == Serpent.Direction.GAUCHE) {
       return x + longueur - 1;
     } else if (direction == Serpent.Direction.DROITE) {
       return x - longueur + 1;
     } else {
       return x;
     }
   }

   private int getQueueY() {
     if (direction == Serpent.Direction.BAS) {
       return y - longueur + 1;
     } else if (direction == Serpent.Direction.HAUT) {
       return y + longueur - 1;
     } else {
       return y;
     }
   }

   public boolean contains(int x, int y) {
     switch (direction) {
       case GAUCHE: return ((y == this.y) && ((x >= this.x) && (x <= getQueueX())));
       case DROITE: return ((y == this.y) && ((x <= this.x) && (x >= getQueueX())));
       case HAUT: return ((x == this.x) && ((y >= this.y) && (y <= getQueueY())));
       case BAS: return ((x == this.x) && ((y <= this.y) && (y >= getQueueY())));
     }
     return false;
   }

   public void dessiner(Graphics g) {
     int x = this.x;
     int y = this.y;
     switch (direction) {
       case GAUCHE:
         for (int i = 0; i < longueur; i++) {
           g.drawImage(image_corps, x*ParamSerpent.TAILLE_CELLULE, 
             y*ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, null);
           x++;
         }
         break;
       case DROITE:
         for (int i = 0; i < longueur; i++) {
           g.drawImage(image_corps, x*ParamSerpent.TAILLE_CELLULE, 
             y*ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, null);
           x--;
         }
         break;
       case HAUT:
         for (int i = 0; i < longueur; i++) {
           g.drawImage(image_corps, x*ParamSerpent.TAILLE_CELLULE, 
             y*ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, null);
           y++;
         }
         break;
       case BAS:
         for (int i = 0; i < longueur; i++) {
           g.drawImage(image_corps, x*ParamSerpent.TAILLE_CELLULE, 
             y*ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, null);
           y--;
         }
         break;
     }
   }

   public int getLongueur() { return longueur; }

   public int getTeteX() { return x; }

   public int getTeteY() { return y; }
}