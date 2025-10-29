import JeuGenerique.MontageJeu;
import InterfacesSpecifiques.IUTetris.TetrisCanvas;
import MoteursSpecifiques.JeuTetris.MoteurTetris;
import PersistScore.Persistance;

public class Tetris {
  public static void main(String[] args) {
    MontageJeu.construire("Tetris", Persistance.FICHIER_TEXTE, 
      new TetrisCanvas(), new MoteurTetris());
  }
}
   