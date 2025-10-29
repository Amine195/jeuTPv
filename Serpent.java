
import JeuGenerique.MontageJeu;import InterfacesSpecifiques.IUSerpent.SerpentCanvas;
import MoteursSpecifiques.JeuSerpent.MoteurSerpent;
import PersistScore.Persistance;

public class Serpent {
  public static void main(String[] args) {
    MontageJeu.construire("Serpent", Persistance.SERIALISATION, 
      new SerpentCanvas(), new MoteurSerpent());
  }
}  