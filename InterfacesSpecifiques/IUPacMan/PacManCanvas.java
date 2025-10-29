package InterfacesSpecifiques.IUPacMan;

import MoteurGenerique.Moteur;
import MoteursSpecifiques.JeuPacMan.MoteurPacMan;
import MoteursSpecifiques.JeuPacMan.ParamPacMan;
import MoteursSpecifiques.JeuPacMan.Fantome;
import MoteursSpecifiques.JeuPacMan.Labyrinthe;
import MoteursSpecifiques.JeuPacMan.Pac;
import InterfaceGenerique.IUJeu;
import InterfaceGenerique.ICanvas;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class PacManCanvas implements ICanvas {
   private IUJeu jeu;

   public void peindre(Graphics g) {
    MoteurPacMan moteur = (MoteurPacMan)
      Moteur.getInstance().getMoteurSpecifique();
    switch (Moteur.getInstance().getEtat()) {
      case INITIALISE:
        new Labyrinthe().dessiner(g);
        break;
      case EN_COURS:
      case ARRETE:
        moteur.getLabyrinthe().dessiner(g);
        moteur.getPac().dessiner(g);
        ArrayList<Fantome> fantomes = moteur.getFantomes();
        for(int i=0;i<fantomes.size();i++)
          fantomes.get(i).dessiner(g);
        break;
      case GAMEOVER:
        jeu.getPanneauJeu().dessineGameover(g);
        break;
    }
  }

  public void appuiTouche (int code) {
    MoteurPacMan moteur = (MoteurPacMan)
      Moteur.getInstance().getMoteurSpecifique();
    switch (code) {
      case KeyEvent.VK_UP:
        moteur.getPac().setDirection(Pac.Direction.HAUT);
        break;
      case KeyEvent.VK_DOWN:
        moteur.getPac().setDirection(Pac.Direction.BAS);
        break;
      case KeyEvent.VK_LEFT:
        moteur.getPac().setDirection(Pac.Direction.GAUCHE);
        break;
      case KeyEvent.VK_RIGHT:
        moteur.getPac().setDirection(Pac.Direction.DROITE);
        break;
    }
  }

  public void setIU(IUJeu j) {
    jeu = j;
  }

  public int getNbLignes() {return ParamPacMan.LIGNES;}

  public int getNbColonnes() {return ParamPacMan.COLONNES;}

  public int getTailleCellule() {
    return ParamPacMan.TAILLE_CELLULE;
  }
}
