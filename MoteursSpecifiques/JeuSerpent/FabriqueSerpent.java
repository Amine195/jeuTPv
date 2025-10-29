package MoteursSpecifiques.JeuSerpent;

import MoteurGenerique.FabriqueObjets;
import MoteurGenerique.ObjetJeu;

public class FabriqueSerpent extends FabriqueObjets {

  public ObjetJeu creerObjet(int type) {
    ObjetJeu result = null;
    if (type==ParamSerpent.NOURRITURE) result = new Nourriture();
    else if (type==ParamSerpent.SERPENT) result =  new Serpent();
    else if (type==ParamSerpent.MUR) result = new Mur();
    return result;
  }
}