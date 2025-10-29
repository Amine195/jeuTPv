package MoteursSpecifiques.JeuPacMan;

import MoteurGenerique.Moteur;
import MoteurGenerique.ObjetJeu;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Labyrinthe extends ObjetJeu {
  public enum CELLULE {BLANCHE, MUR, POINT, POINT_PUISSANCE,
    PORTE_FERMEE, PORTE_OUVERTE} 
  private CELLULE[][] etat;
  private ArrayList<Point> zoneDepart;
  private int initial_loc_pac_X;
  private int initial_loc_pac_Y;
  private Image blanche;
  private Image mur;
  private Image point;
  private Image point_puissance;
  private Image porte_ouverte;
  private Image porte_fermee;

  public Labyrinthe() {
    zoneDepart = new ArrayList<Point>();
    blanche = new ImageIcon(ParamPacMan.CHEMIN_BLANCHE).getImage();
    mur = new ImageIcon(ParamPacMan.CHEMIN_MUR).getImage();
    point = new ImageIcon(ParamPacMan.CHEMIN_POINT).getImage();
    point_puissance = new ImageIcon(ParamPacMan.
      CHEMIN_POINT_PUISSANCE).getImage();
    porte_ouverte = new ImageIcon(ParamPacMan.
      CHEMIN_PORTE_OUVERTE).getImage(); 
    porte_fermee = new ImageIcon(ParamPacMan.CHEMIN_PORTE_FERMEE).getImage(); 
    etat = new CELLULE[ParamPacMan.COLONNES][ParamPacMan.LIGNES];
    generer();     
  }

  public void generer() {
    for (int i=0; i<ParamPacMan.COLONNES; i++) {
      for (int j=0; j<ParamPacMan.LIGNES; j++) {
        switch (ParamPacMan.definition[j].charAt(i)) {
	  case 'B':
            zoneDepart.add(new Point(i,j));
          case ' ':
            etat[i][j]= CELLULE.BLANCHE;
	    break;
	  case 'X':
            etat[i][j]= CELLULE.MUR;
	    break;
	  case '.':
            etat[i][j]= CELLULE.POINT; 
	    break;
	  case 'O':
            etat[i][j]= CELLULE.POINT_PUISSANCE; 
	    break;
	  case '-':
            etat[i][j]= CELLULE.PORTE_FERMEE;
	    break;
          case 'P':
            etat[i][j]= CELLULE.BLANCHE;
            initial_loc_pac_X = i;
            initial_loc_pac_Y = j;
	    break;
        }
      }
    } 
  }

  public void mettreAJour() {}

  public void dessiner(Graphics g) {
    for (int i=0; i<ParamPacMan.COLONNES; i++) {
      for (int j=0; j<ParamPacMan.LIGNES; j++) {
        switch (etat[i][j]) {
	  case BLANCHE:
	    g.drawImage(blanche, i*ParamPacMan.TAILLE_CELLULE,
           j*ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
	    break;
	  case MUR:
	    g.drawImage(mur, i*ParamPacMan.TAILLE_CELLULE, 
           j*ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE,
           ParamPacMan.TAILLE_CELLULE, null);
	    break;
	  case POINT:
	    g.drawImage(point, i*ParamPacMan.TAILLE_CELLULE,
           j*ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
	    break;
	  case POINT_PUISSANCE:
	    g.drawImage(point_puissance, 
           i*ParamPacMan.TAILLE_CELLULE,
           j*ParamPacMan.TAILLE_CELLULE,    
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
	    break;
	  case PORTE_FERMEE:
	    g.drawImage(porte_fermee, i*ParamPacMan.TAILLE_CELLULE,
           j*ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, 
           ParamPacMan.TAILLE_CELLULE, null);
	    break;
	  case PORTE_OUVERTE:
	    g.drawImage(porte_ouverte, i*ParamPacMan.TAILLE_CELLULE,
          j*ParamPacMan.TAILLE_CELLULE, 
          ParamPacMan.TAILLE_CELLULE, 
          ParamPacMan.TAILLE_CELLULE, null);
	    break;
        }
      }
    }      
  }

  public CELLULE[][] getEtat() { return etat;}

  public int getPorteX() {
    int x = 0;
    for (int i=0; i<ParamPacMan.COLONNES; i++) {
      for (int j=0; j<ParamPacMan.LIGNES; j++) {
        if ((etat[i][j]==CELLULE.PORTE_FERMEE) || 
          (etat[i][j]==CELLULE.PORTE_OUVERTE)) x = i;
      }
    }
    return x;
  }

  public int getPorteY() {
    int y = 0;
    for (int i=0; i<ParamPacMan.COLONNES; i++) {
      for (int j=0; j<ParamPacMan.LIGNES; j++) {
        if ((etat[i][j]==CELLULE.PORTE_FERMEE) ||
         (etat[i][j]== CELLULE.PORTE_OUVERTE)) y = j;
      }
    }
    return y;
  }

  public ArrayList<Point> getZoneDepart() { return zoneDepart;}

  public int getInitialPacLocX() { return initial_loc_pac_X;}

  public int getInitialPacLocY() { return initial_loc_pac_Y;}

  public boolean celluleVide(int x, int y) {
    boolean res = true;
    ArrayList<Fantome> fantomes=((MoteurPacMan)Moteur.getInstance().
      getMoteurSpecifique()).getFantomes();
    for (int i=0; i<fantomes.size(); i++) 
      if ((fantomes.get(i).getX()==x)&&(fantomes.get(i).getY()==y))
        res=false;
    return res;
  }
}
