package MoteursSpecifiques.JeuPacMan;

import MoteurGenerique.ObjetJeu;
import MoteurGenerique.FabriqueObjets;

public class FabriquePacMan extends FabriqueObjets {
  public ObjetJeu creerObjet(int type) {
    ObjetJeu result = null;
    if (type == ParamPacMan.PAC) result = new Pac();
    else if (type == ParamPacMan.FANTOME) result = new Fantome();
    else if (type == ParamPacMan.LABYRINTHE) 
      result = new Labyrinthe();
    return result;
  }
}
