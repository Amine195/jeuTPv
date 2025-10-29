package InterfacesSpecifiques.IUTetris;

import MoteurGenerique.Moteur;
import MoteursSpecifiques.JeuTetris.MoteurTetris;
import MoteursSpecifiques.JeuTetris.ParamTetris;
import InterfaceGenerique.ICanvas;
import InterfaceGenerique.IUJeu;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TetrisCanvas implements ICanvas {
  private IUJeu jeu;

  public void peindre(Graphics g) {
    jeu.getPanneauJeu().remplirImageFond(ParamTetris.FICHIER_BLANC, g);    
    MoteurTetris moteur = (MoteurTetris) Moteur.getInstance().getMoteurSpecifique();
    switch (Moteur.getInstance().getEtat()) {
      case EN_COURS:
      case ARRETE:
        moteur.getTetromino().dessiner(g);
        moteur.dessinerTas(g);
        break;
      case GAMEOVER:
        jeu.getPanneauJeu().dessineGameover(g);
        break;
    }
  }

  public void appuiTouche(int code) {
    MoteurTetris moteur = (MoteurTetris) Moteur.getInstance().getMoteurSpecifique();
    switch (code) {
      case KeyEvent.VK_UP:
        moteur.getTetromino().tournerGauche();
        break;
      case KeyEvent.VK_DOWN:
        moteur.getTetromino().tournerDroite();
        break;
      case KeyEvent.VK_RIGHT:
        moteur.getTetromino().deplacerDroite();
        break;
      case KeyEvent.VK_LEFT:
        moteur.getTetromino().deplacerGauche();
        break;
    }
  }

  public void setIU(IUJeu j) {
    jeu = j;
  }

  public int getNbLignes() { return ParamTetris.LIGNES; }

  public int getNbColonnes() { return ParamTetris.COLONNES; }

  public int getTailleCellule() { 
    return ParamTetris.TAILLE_CELLULE;
  }
} 