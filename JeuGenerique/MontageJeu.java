package JeuGenerique;

import InterfaceGenerique.ICanvas;
import InterfaceGenerique.PanneauJeu;
import InterfaceGenerique.IUJeu;
import PersistScore.Persistance;
import MoteurGenerique.IMoteurSpecifique;
import MoteurGenerique.Moteur;

public class MontageJeu {

  public static void construire(String nom, Persistance pers,
    ICanvas ic, IMoteurSpecifique ims) { 
    IUJeu jeu = new IUJeu();
    jeu.setNom(nom);
    jeu.setPersistance(pers);
    PanneauJeu pjeu = new PanneauJeu(jeu); 
    jeu.setPanneauJeu(pjeu);             
    pjeu.include(ic); 
    ic.setIU(jeu);  
    Moteur moteur = Moteur.getInstance();
    moteur.setIU(jeu);
    moteur.include(ims);
    jeu.construireIU();                             
  }
}