package InterfaceGenerique;

import MoteurGenerique.Moteur;
import MoteurGenerique.IIUJeu;
import PersistScore.MeilleurScore;
import PersistScore.Persistance;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Observable;

public class IUJeu extends JFrame implements IIUJeu {
  private PanneauControle panneau_c;
  private PanneauScore panneau_s;
  private PanneauJeu panneau_j;
  private MeilleurScore highscore;
  private String nom;
  private Persistance persist;

  public void construireIU() {
    highscore = new MeilleurScore(nom, persist);
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());   
    int larg = panneau_j.getLargeur();
    int haut = panneau_j.getHauteur();        
    panneau_j.setPreferredSize(new Dimension(larg, haut));
    cp.add(panneau_j, BorderLayout.CENTER);
    JPanel bas = new JPanel();
    bas.setLayout(new BorderLayout());
    panneau_s = new PanneauScore(this); 
    bas.add(panneau_s, BorderLayout.NORTH);
    panneau_c = new PanneauControle(this); 
    bas.add(panneau_c, BorderLayout.CENTER);
    cp.add(bas, BorderLayout.SOUTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(getParent());
    setVisible(true);  
    ((Observable)Moteur.getInstance()).addObserver(panneau_s);
   }

   public void setPanneauJeu(PanneauJeu pj) {
     panneau_j = pj;
   }

   public void setNom(String n) { 
     nom = n;
     setTitle(nom);
   }

   public void setPersistance(Persistance p) {
     persist = p;
   }

   public PanneauJeu getPanneauJeu() { 
     return panneau_j;
   }

   public PanneauControle getPanneauControle() { 
     return panneau_c;
   }

   public PanneauScore getPaneauScore() { 
     return panneau_s;
   }

   public MeilleurScore getMeilleurScore() { 
     return highscore;
   }

   public void resetPanneauControle() {
     panneau_c.reset();
   }

   public void repaintPanneauJeu() {
     panneau_j.repaint();
   }

   public boolean containsPanneauJeu(int x, int y) {
     return panneau_j.contains(x, y);
   }
} 





   