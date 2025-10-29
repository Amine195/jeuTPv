package MoteursSpecifiques.JeuPacMan;

import MoteurGenerique.Moteur;
import MoteurGenerique.ObjetJeu;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Fantome extends ObjetJeu {
   private enum DIR { HAUT, BAS, DROITE, GAUCHE};
   private DIR direction;
   private int vitesse;
   private boolean effraye;
   private boolean enBoite;
   private Image fantome;
   private Image fantomeEff;
   private int nbCycles; 

   public Fantome() {
     fantome = new ImageIcon(ParamPacMan.CHEMIN_FANTOME).getImage();
     fantomeEff = new ImageIcon(ParamPacMan.CHEMIN_FANTOME_EFFRAYE).
       getImage();
     vitesse = ParamPacMan.VITESSE_FANTOME;
   }

   public void generer() {
     effraye = false;
     enBoite = true;
     nbCycles=0;
   }

   public void setLocation (int x, int y) {
     this.x = x;
     this.y = y;
   }

   public void mettreAJour() {
     // fantome cache
     if(x==-1 && y==-1) return;
     // implantation de la vitesse
     nbCycles++;
     if (nbCycles % vitesse != 0) return;
     else nbCycles = 0;
     Labyrinthe labyrinthe = ((MoteurPacMan)Moteur.getInstance().
       getMoteurSpecifique()).getLabyrinthe();
     Labyrinthe.CELLULE[][] etat = labyrinthe.getEtat();
     boolean porte = ((MoteurPacMan)Moteur.getInstance().
       getMoteurSpecifique()).getPorteOuverte();
     int portex = labyrinthe.getPorteX();
     int portey = labyrinthe.getPorteY();
     // si pret a sortir
     if (enBoite && porte && x==portex && y==portey+1 &&
      (etat[portex][portey-1]==Labyrinthe.CELLULE.BLANCHE || 
       etat[portex][portey-1]==Labyrinthe.CELLULE.POINT)) {
       x = portex;
       y = portey-2;
       enBoite = false;
       direction = choisirDir();
     }
     // si en deplacement dans la boite
     if (enBoite && (x < portex) && etat[x+1][y]==
       Labyrinthe.CELLULE.BLANCHE && labyrinthe.celluleVide(x+1,y))
       x = x+1;
     if (enBoite && (x > portex) && etat[x-1][y]==
       Labyrinthe.CELLULE.BLANCHE && labyrinthe.celluleVide(x-1,y))
       x = x-1;
     if (enBoite && (y > portey) && etat[x][y-1]==
       Labyrinthe.CELLULE.BLANCHE && labyrinthe.celluleVide(x,y-1))
       y = y-1;
     // si en deplacement hors de la boite
     if (!enBoite) {
       // choisir une direction au hasard
       direction = choisirDir();
       if (direction == DIR.HAUT) y = y-1; 
       else if (direction==DIR.BAS) y = y+1; 
       else if (direction==DIR.GAUCHE) x = x-1; 
       else x = x+1; 
     }
   }

   private DIR choisirDir() {
     ArrayList<DIR> possible = new ArrayList<DIR>();
     Labyrinthe labyrinthe = ((MoteurPacMan)Moteur.getInstance().
       getMoteurSpecifique()).getLabyrinthe();
     Labyrinthe.CELLULE[][] etat = labyrinthe.getEtat();
     if ((etat[x][y+1]!=Labyrinthe.CELLULE.MUR) &&
       (etat[x][y+1]!=Labyrinthe.CELLULE.PORTE_OUVERTE) &&
       (direction!=DIR.HAUT)) possible.add(DIR.BAS);
     if ((etat[x][y-1]!=Labyrinthe.CELLULE.MUR) &&
       (direction!=DIR.BAS)) possible.add(DIR.HAUT);
     if ((etat[x-1][y]!=Labyrinthe.CELLULE.MUR) &&
       (direction!=DIR.DROITE)) possible.add(DIR.GAUCHE);
     if ((etat[x+1][y]!=Labyrinthe.CELLULE.MUR) &&
       (direction!=DIR.GAUCHE)) possible.add(DIR.DROITE);
     if (possible.size()==0) return DIR.DROITE;
     int dir = rand.nextInt(possible.size());
     return direction = possible.get(dir);
   }

   public void dessiner(Graphics g) {
     if (!effraye)
       g.drawImage(fantome, getX()*ParamPacMan.TAILLE_CELLULE,
         getY()*ParamPacMan.TAILLE_CELLULE, 
         ParamPacMan.TAILLE_CELLULE, 
         ParamPacMan.TAILLE_CELLULE, null);
     else
       g.drawImage(fantomeEff,getX()*ParamPacMan.TAILLE_CELLULE,
         getY()*ParamPacMan.TAILLE_CELLULE, 
         ParamPacMan.TAILLE_CELLULE, 
         ParamPacMan.TAILLE_CELLULE, null);
   }

   public void accelerer() {
     if (vitesse > ParamPacMan.VITESSE_FANTOME/6) 
       vitesse -= ParamPacMan.VITESSE_FANTOME/6;
   }

   public boolean estEffraye() { return effraye;}

   public void setEffraye(boolean b) { effraye = b;}
}
