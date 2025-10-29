package PersistScore;

import java.util.ArrayList;

public interface IPersist {
  static final String CHEMIN_SCORES = "Highscores/";  
  ArrayList<Score> lireListe(String nom);
  void ecrireListe(String nom, ArrayList<Score> liste);
}