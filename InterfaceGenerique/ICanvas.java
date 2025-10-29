package InterfaceGenerique;

import java.awt.Graphics;

public interface ICanvas {
  int getNbLignes();
  int getNbColonnes();
  int getTailleCellule();
  void peindre(Graphics g);
  void appuiTouche(int code);
  void setIU(IUJeu j);
}