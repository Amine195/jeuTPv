package MoteursSpecifiques.JeuSerpent;

import MoteurGenerique.ObjetJeu;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Mur extends ObjetJeu {
   private Image image;

   public Mur() {
     image = new ImageIcon(ParamSerpent.CHEMIN_MUR).getImage();
   }
 
   public void generer() {  // place au hasard dans la zone
     x = rand.nextInt(ParamSerpent.COLONNES-2);
     y = rand.nextInt(ParamSerpent.LIGNES- 2);
   }

   public void mettreAJour(){}

   public void dessiner(Graphics g) {
     g.drawImage(image, x*ParamSerpent.TAILLE_CELLULE, y*ParamSerpent.TAILLE_CELLULE, 
       ParamSerpent.TAILLE_CELLULE, ParamSerpent.TAILLE_CELLULE, null); 
   }
}