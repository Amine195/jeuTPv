package InterfaceGenerique;

import MoteurGenerique.IIUJeu;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.Observer;
import java.util.Observable;

public class PanneauScore extends JPanel implements Observer {
  private JLabel score;
  private JLabel niveau;

  public PanneauScore(final IIUJeu iu) {
    JLabel labscore = new JLabel("Score: ");
    add(labscore);
    score = new JLabel("0");
    add(score);
    JLabel separation = new JLabel("      ");
    add(separation);
    JLabel labniv = new JLabel("Niveau: ");
    add(labniv);
    niveau = new JLabel("1");
    add(niveau);
  }

  public void update(Observable o, Object arg) {
    String [] vals = (arg.toString()).split(" ");
    score.setText(vals[0]);
    niveau.setText(vals[1]);
  }
}