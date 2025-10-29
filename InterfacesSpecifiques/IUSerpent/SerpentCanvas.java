package InterfacesSpecifiques.IUSerpent;

import MoteurGenerique.Moteur;
import MoteursSpecifiques.JeuSerpent.MoteurSerpent;
import MoteursSpecifiques.JeuSerpent.ParamSerpent;
import MoteursSpecifiques.JeuSerpent.Serpent;
import MoteursSpecifiques.JeuSerpent.Mur;
import InterfaceGenerique.IUJeu;
import InterfaceGenerique.ICanvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
   
public class SerpentCanvas implements ICanvas {
  private IUJeu jeu;

  public void peindre(Graphics g) {
    jeu.getPanneauJeu().remplirImageFond(ParamSerpent.FICHIER_HERBE, g);  
    MoteurSerpent moteur = (MoteurSerpent) Moteur.getInstance().getMoteurSpecifique();
    switch (Moteur.getInstance().getEtat()) {
      case EN_COURS:
      case ARRETE:
        moteur.getSerpent().dessiner(g);
        moteur.getNourriture().dessiner(g);
        Mur [] murs = moteur.getMurs();
        for (int i=0; i<ParamSerpent.NB_MURS; i++) {
          murs[i].dessiner(g);
        }
        break;
      case GAMEOVER:
        jeu.getPanneauJeu().dessineGameover(g);
        break;
    }
  }

  public void appuiTouche(int code) {
    MoteurSerpent moteur = (MoteurSerpent) Moteur.getInstance().getMoteurSpecifique();
    switch (code) {
      case KeyEvent.VK_UP:
        moteur.getSerpent().setDirection(Serpent.Direction.HAUT);
        break;
      case KeyEvent.VK_DOWN:
        moteur.getSerpent().setDirection(Serpent.Direction.BAS);
        break;
      case KeyEvent.VK_LEFT:
        moteur.getSerpent().setDirection(Serpent.Direction.GAUCHE);
        break;
      case KeyEvent.VK_RIGHT:
        moteur.getSerpent().setDirection(Serpent.Direction.DROITE);
        break;
    }
  }

  public void setIU(IUJeu j) {
    jeu = j;
  }
 
  public int getNbLignes() { return ParamSerpent.LIGNES; }

  public int getNbColonnes() { return ParamSerpent.COLONNES; }

  public int getTailleCellule() { 
    return ParamSerpent.TAILLE_CELLULE;
  }
}
   