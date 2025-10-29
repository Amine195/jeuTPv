package MoteursSpecifiques.JeuSerpent;

import MoteurGenerique.ObjetJeu;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;

public class Serpent extends ObjetJeu {
   public static enum Direction {HAUT, BAS, GAUCHE, DROITE}
   private Direction direction;    
   private List<SegmentSerpent> segments;
   private boolean majDirAttente;
   private Image image_tete;
    
   public Serpent() {
     image_tete = new ImageIcon(ParamSerpent.CHEMIN_TETE).getImage();
     segments = new ArrayList<SegmentSerpent>();
   }

   public void generer() {
      segments.clear();
      int longueur = ParamSerpent.LONGUEUR_INITIALE;
      direction = Direction.values()[rand.nextInt(Direction.values().length)];
      int teteX = longueur + rand.nextInt(ParamSerpent.COLONNES-2*longueur);
      int teteY = longueur + rand.nextInt(ParamSerpent.LIGNES-2*longueur);
      if (direction == Direction.HAUT) {
        teteY = ParamSerpent.LIGNES/2 + rand.nextInt(ParamSerpent.LIGNES/2) - longueur;
      } else if (direction == Direction.BAS) {
        teteY = rand.nextInt(ParamSerpent.LIGNES/2) + longueur;
      } else if (direction == Direction.GAUCHE) {
        teteX = ParamSerpent.COLONNES/2+rand.nextInt(ParamSerpent.COLONNES/2) - longueur;
      } else {
        teteX = rand.nextInt(ParamSerpent.COLONNES/2) + longueur;
      }
      segments.add(new SegmentSerpent(teteX, teteY, longueur, direction));
      majDirAttente = false;
   }

   public void setDirection(Direction nDir) {
      // pas de majDirAttente et pas modif inutile et pas de 180°
      if (!majDirAttente && (nDir != direction)
            && ((nDir == Direction.HAUT && direction != Direction.BAS)
             || (nDir == Direction.BAS && direction != Direction.HAUT)
             || (nDir == Direction.GAUCHE && direction != Direction.DROITE) 
             || (nDir == Direction.DROITE && direction != Direction.GAUCHE))) {
         SegmentSerpent segmentTete = segments.get(0); 
         int x = segmentTete.getTeteX();
         int y = segmentTete.getTeteY();
         segments.add(0, new SegmentSerpent(x, y, 0, nDir));
         direction = nDir;
         majDirAttente = true; // sera effece apres maj
      }
   }

   public void mettreAJour() {
      SegmentSerpent segmentTete;
      segmentTete = segments.get(0);   
      segmentTete.grandir();
      majDirAttente = false;   // peut de nouveau traiter une touche
   }

   public void diminuer() {
      SegmentSerpent segmentQueue;
      segmentQueue = segments.get(segments.size() - 1);
      segmentQueue.diminuer();
      if (segmentQueue.getLongueur() == 0) {
         segments.remove(segmentQueue);
      }
   }

   public int getTeteX() { return segments.get(0).getTeteX();}

   public int getTeteY() { return segments.get(0).getTeteY();}

   public int getLongueur() {
      int longueur = 0;
      for (SegmentSerpent segment : segments) {
         longueur += segment.getLongueur();
      }
      return longueur;
   }

   public boolean contains(int x, int y) {
      for (int i = 0; i < segments.size(); i++) {
         SegmentSerpent segment = segments.get(i);
         if (segment.contains(x, y)) {
            return true;
         }
      }
      return false;
   }

   public boolean intersects(int x, int y) {
     if ((getTeteX() == x) || (getTeteY() == y)) return true;
     else return false;
   }

   public boolean mangeLuiMeme() {
      int teteX = getTeteX();
      int teteY = getTeteY();
      // si (teteX, teteY) touche un segment (4e ou plus) 
      for (int i = 3; i < segments.size(); i++) {
         SegmentSerpent segment = segments.get(i);
         if (segment.contains(teteX, teteY)) {
            return true;
         }
      }
      return false;
   }

   public void dessiner(Graphics g) {
      for (int i = 0; i < segments.size(); i++) {
         segments.get(i).dessiner(g);   
      }
      if (segments.size() > 0) {
         g.drawImage(image_tete, getTeteX()*ParamSerpent.TAILLE_CELLULE, 
            getTeteY()*ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, 
            ParamSerpent.TAILLE_CELLULE, null); 
      }
   }
}


   