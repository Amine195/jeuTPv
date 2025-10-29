package InterfaceGenerique;

import MoteurGenerique.Moteur;
import Utils.Son.EffetSonore;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class PanneauControle extends JPanel {

  private JButton demarrer_pause;
  private JButton arret;
  private JButton son_on_off;
  private ImageIcon idemarrer;

  public PanneauControle(final IUJeu iu) {
    final ImageIcon ipause = new ImageIcon(
      "Images/pause.png");
    final ImageIcon iarret = new ImageIcon("Images/stop.png");
    final ImageIcon ison_on = new ImageIcon(
      "Images/audio-high.png");
    final ImageIcon ison_off = new ImageIcon(
      "Images/audio-muted.png");
    idemarrer = new ImageIcon("Images/start.png");
    setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    demarrer_pause = new JButton(idemarrer);
    demarrer_pause.setEnabled(true);
    add(demarrer_pause);
    arret = new JButton(iarret);
    arret.setEnabled(false);
    add(arret);
    son_on_off = new JButton(ison_off);
    son_on_off.setEnabled(true);
    add(son_on_off);
    demarrer_pause.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Moteur.Etat etat = Moteur.getInstance().getEtat();
        switch (etat) {
          case INITIALISE:
          case GAMEOVER:
           demarrer_pause.setIcon(ipause);
           Moteur.getInstance().demarrerJeu();
           break;
          case EN_COURS:
           Moteur.getInstance().setEtat(Moteur.Etat.ARRETE);
           demarrer_pause.setIcon(idemarrer);
           break;
          case ARRETE:
           Moteur.getInstance().setEtat(Moteur.Etat.EN_COURS);
           demarrer_pause.setIcon(ipause);
           break;
        }
        arret.setEnabled(true);
        iu.getPanneauJeu().requestFocus();
      }
    });
    arret.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Moteur.getInstance().setEtat(Moteur.Etat.GAMEOVER);
        demarrer_pause.setIcon(idemarrer);
        demarrer_pause.setEnabled(true);
        arret.setEnabled(false);
        iu.getPanneauJeu().repaint();
      }
    });
    son_on_off.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (EffetSonore.volume == EffetSonore.Volume.OFF) {
          EffetSonore.volume = EffetSonore.Volume.ON;
          son_on_off.setIcon(ison_on);
        } else {
          EffetSonore.volume = EffetSonore.Volume.OFF;
          son_on_off.setIcon(ison_off);
        }
        iu.getPanneauJeu().requestFocus();
      }
    });
  }

  public void reset() {
    demarrer_pause.setIcon(idemarrer);
    demarrer_pause.setEnabled(true);
    arret.setEnabled(false);
  }
}