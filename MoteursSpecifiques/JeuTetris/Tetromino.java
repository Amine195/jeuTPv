package MoteursSpecifiques.JeuTetris;

import MoteurGenerique.Moteur;
import MoteurGenerique.ObjetJeu;
import java.awt.Graphics;
import java.awt.Color;

public class Tetromino extends ObjetJeu {
  public static enum Tetrominoes 
    {Vide, Z, S, Ligne, T, Carre, L, LRetourne}
  private Tetrominoes forme;
  private int coords[][];

  public Tetromino() {
    coords = new int[4][2];
    donnerFormeAuHasard();
    x = (ParamTetris.COLONNES/2)-1;    
    y = 0;                             
  }

  public void donnerFormeAuHasard() {
    int x = Math.abs(rand.nextInt()) % 7 + 1;
    Tetrominoes[] valeurs = Tetrominoes.values(); 
    donnerForme(valeurs[x]);
  }

  private void donnerForme(Tetrominoes s) {
    int[][][] coordsTable = new int[][][] {                  
    // coords[i][0] <=> x & coords[i][1] <=> y 
      { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
      { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
      { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
      { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
      { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
      { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
      { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
      { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
    };
    for (int i = 0; i < 4 ; i++) {
      for (int j = 0; j < 2; j++) {
        coords[i][j] = coordsTable[s.ordinal()][i][j];       
      }
    }
    forme = s;
  }

  public void generer() {}

  public void mettreAJour() {
     y++;
  }

  public void deplacerGauche() { 
    if (((MoteurTetris) Moteur.getInstance().getMoteurSpecifique()).
     peutAllerGauche(getCells(coords))) x--;
  }

  //Transforme les coordonnees relatives en absolues.
  private int [][] getCells(int[][] c) {
    int[][] cells = new int[4][2];
    for (int i = 0; i < 4; i++) {
      cells[i][0]=x+c[i][0];
      cells[i][1]=y+c[i][1];
    }
    return cells;
  }

  public void deplacerDroite() { 
    if (((MoteurTetris) Moteur.getInstance().getMoteurSpecifique()).
      peutAllerDroite(getCells(coords))) x++;
  }

  public void tournerGauche() {
    if (forme == Tetrominoes.Carre) return;
    int old;
    int [][] ncoords = new int[4][2];
    // calcule les nouvelles coordonnees pour tester
    for (int i = 0; i < 4; i++) {
      old = coords[i][0];
      ncoords[i][0] = coords[i][1];  
      ncoords[i][1] = -old;
    }
    if (((MoteurTetris) Moteur.getInstance().getMoteurSpecifique()).
      peutTourner(getCells(ncoords))) {
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 2; j++) {
          coords[i][j] = ncoords[i][j];
        } 
      }
    }
  }

  public void tournerDroite() {      
    if (forme == Tetrominoes.Carre) return;
    int old;
    int [][] ncoords = new int[4][2];
    // calcule les nouvelles coordonnees pour tester
    for (int i = 0; i < 4; i++) {
      old = coords[i][0];
      ncoords[i][0] = -coords[i][1]; 
      ncoords[i][1] = old;  
    }
    if (((MoteurTetris) Moteur.getInstance().getMoteurSpecifique()).
      peutTourner(getCells(ncoords))) {
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 2; j++) {
          coords[i][j] = ncoords[i][j];
        } 
      }
    }
  }

  public void dessiner(Graphics g) {
    if (forme != Tetrominoes.Vide) {
      for (int i = 0; i < 4; i++) {
        dessineCarre(g, (x+coords[i][0])*ParamTetris.TAILLE_CELLULE,
         (y+coords[i][1])*ParamTetris.TAILLE_CELLULE);
      }
    }
  }

  private void dessineCarre(Graphics g, int i, int j) {
    g.setColor(Tetromino.getCouleur(forme));
    g.fillRect(i, j, ParamTetris.TAILLE_CELLULE,
      ParamTetris.TAILLE_CELLULE);
  }

  public static Color getCouleur(Tetrominoes forme) {
    Color couleurs[] = {new Color(0,0,0), new Color(204,102,102), 
      new Color(102,204,102), new Color(102,102,204), 
      new Color(204,204,102), new Color(204,102,204), 
      new Color(102,204,204), new Color(218,170,0)
    };
    return couleurs[forme.ordinal()];
  }

  public Tetrominoes getForme()  { return forme; }

  public int[][] getCoords() { return coords; }
}