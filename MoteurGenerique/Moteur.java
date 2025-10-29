package MoteurGenerique;

import java.util.Observable;
import Utils.Son.EffetSonore;

public class Moteur extends Observable {
  public static enum Etat {INITIALISE, EN_COURS, ARRETE, GAMEOVER}
  private static Moteur moteur;
  protected Etat etat;
  private int score;
  private int niveau;
  private IMoteurSpecifique specif;
  private IIUJeu jeu;

  private Moteur() {
    etat = Etat.INITIALISE;
    EffetSonore.init();
  }

  public static Moteur getInstance() {
    if (moteur == null) moteur = new Moteur();
    return moteur;
  }

  public void demarrerJeu() {
    Thread threadJeu =  new Thread() {
      public void run() {
        specif.initialiserJeu();
        boucleDeJeu();
      }
    };
    threadJeu.start();
  }

  private void boucleDeJeu() {
    if (etat == Etat.INITIALISE || etat == Etat.GAMEOVER) {
      specif.genererJeu();
      etat = Etat.EN_COURS;
    }
    long tempsDebut, tempsPris, tempsRestant;
    while (true) {
      tempsDebut = System.nanoTime();
      if (etat == Etat.GAMEOVER) {
        jeu.resetPanneauControle();
        break;
      }  
      if (etat == Etat.EN_COURS) {
        specif.mettreAJourJeu();
      }
      jeu.repaintPanneauJeu();
      tempsPris = System.nanoTime() - tempsDebut;
      tempsRestant = (specif.getPeriodeMaj() - tempsPris)/1000000;  
      if (tempsRestant < 10) tempsRestant = 10; 
      try {
        Thread.sleep(tempsRestant);
      } catch (InterruptedException ex) { }
    }
  }

  public void terminerJeu() {
    EffetSonore.MORT.play();
    setEtat(Etat.GAMEOVER);
    jeu.getMeilleurScore().ajouterTop5(score);
  }

  public void notification() {
    setChanged();
    notifyObservers(score+" "+niveau);
  }

  public void include(IMoteurSpecifique spe) {
    specif = spe;
  }

  public IMoteurSpecifique getMoteurSpecifique() { return specif;}

  public void setIU(IIUJeu j) {
    jeu = j;
  }

  public IIUJeu getIU() {
    return jeu;
  }

  public Etat getEtat() { return etat;}

  public void setEtat(Etat e) { etat = e;}

  public int getScore() { return score;}

  public int getNiveau() { return niveau;}

  public void setScore(int s) { score = s;}

  public void setNiveau(int n) { niveau = n;}
}

