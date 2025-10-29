package MoteursSpecifiques.JeuTetris;

import MoteurGenerique.FabriqueObjets;
import MoteurGenerique.ObjetJeu;

public class FabriqueTetris extends FabriqueObjets {
  public ObjetJeu creerObjet(int type) {
    ObjetJeu res = null;
    if (type == ParamTetris.TETROMINO) res = new Tetromino();
    return res;
  }
}