package MoteursSpecifiques.JeuSerpent;

import MoteurGenerique.ObjetJeu;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Nourriture extends ObjetJeu {
   private Image image;

   public Nourriture() {
     image = new ImageIcon(ParamSerpent.CHEMIN_NOURRITURE).getImage();
   }

   public void generer() {  // place au hasard dans la zone (un peu éloigne du bord).
     x = rand.nextInt(ParamSerpent.COLONNES - 4) + 2;
     y = rand.nextInt(ParamSerpent.LIGNES - 4) + 2;
   }

   public void mettreAJour() {}

   public void dessiner(Graphics g) {
     g.drawImage(image, x*ParamSerpent.TAILLE_CELLULE, y*ParamSerpent.TAILLE_CELLULE, 
       ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, null); 
   }
}