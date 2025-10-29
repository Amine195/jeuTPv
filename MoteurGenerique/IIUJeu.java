package MoteurGenerique;

import PersistScore.MeilleurScore;
import PersistScore.Persistance;

public interface IIUJeu {
  void construireIU();
  void setNom(String n);
  void setPersistance(Persistance p);
  void resetPanneauControle();
  void repaintPanneauJeu();
  boolean containsPanneauJeu(int x, int y);
  MeilleurScore getMeilleurScore();
}