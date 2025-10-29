package PersistScore;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;

public class MeilleurScore {
  private String nom;
  private ArrayList<Score> top5;
  private IPersist persist;

  public MeilleurScore(String n, Persistance pers) {
    top5 = null;
    nom = n;
    if (pers == Persistance.FICHIER_TEXTE) 
      persist = new FichierTexte();
    else 
      persist = new Serialisation();
    top5 = persist.lireListe(nom);
    if (top5 == null) {
      top5 = new ArrayList<Score>();
    }
  }

  public void ajouterTop5 (int score){
    if (top5.size() < 5) {
      saisie(score);
    } else {
      Score min = top5.get(0);
      if (score > min.getScore()) {
        top5.remove(0);
        saisie(score);
      }
    }
    persist.ecrireListe(nom, top5);
  }

  @SuppressWarnings("unchecked") 
  private void saisie(int score) {
    String nomJoueur = JOptionPane.showInputDialog(null,
     "Nouveau meilleur score!!! \nTapez votre nom",
     "Meilleur score",JOptionPane.PLAIN_MESSAGE);
    top5.add(new Score(nomJoueur, score));
    Collections.sort(top5);
  }

  public String afficherTop5() {
    String s = "";
    Score sc;
    for (int i = 0; i<top5.size(); i++) {
      sc = top5.get(i);
      s = s + sc.getNom()+" "+sc.getScore()+"\n";
    }
    return s;
  }
}