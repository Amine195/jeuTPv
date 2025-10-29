import JeuGenerique.MontageJeu;
import InterfacesSpecifiques.IUPacMan.PacManCanvas;
import MoteursSpecifiques.JeuPacMan.MoteurPacMan;
import PersistScore.Persistance;

public class PacMan {
  public static void main(String[] args) {
    MontageJeu.construire("PacMan",
      Persistance.SERIALISATION, 
      new PacManCanvas(), 
      new MoteurPacMan());
  }
}

   