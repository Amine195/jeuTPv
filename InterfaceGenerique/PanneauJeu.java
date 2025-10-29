package InterfaceGenerique;

import MoteurGenerique.Moteur;
import MoteurGenerique.IIUJeu;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.TexturePaint;
import java.io.File;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PanneauJeu extends JPanel implements KeyListener{
  private ICanvas specifique;
  private IIUJeu iu;

  public PanneauJeu(IIUJeu iu) {
    this.iu = iu;
    setFocusable(true); 
    requestFocus();
    addKeyListener(this);
  } 

  public void keyPressed(KeyEvent e) {
    if (Moteur.getInstance().getEtat()==Moteur.Etat.ARRETE) return;
    specifique.appuiTouche(e.getKeyCode());
  }
  
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
  
  public boolean contains(int x, int y) {
    if ((x < 0) || (x >= specifique.getNbLignes())) {
      return false;
    }
    if ((y < 0) || (y >= specifique.getNbColonnes())) {
      return false;
    }
    return true;
  }

  public void include(ICanvas spe) {
    specifique = spe;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);   
    specifique.peindre(g);
  }

  public void dessineGameover(Graphics g) {
    int h = getHauteur()/5;
    g.setColor(Color.YELLOW);
    g.fillRect(0,0,getLargeur(), getHauteur());
    g.setFont(new Font("Verdana", Font.BOLD, 20));
    g.setColor(Color.RED);
    g.drawString("GAME OVER!", getLargeur()/2-
      (g.getFontMetrics().stringWidth("GAME OVER!"))/2, h);
    h += h;
    String s = iu.getMeilleurScore().afficherTop5();
    g.setColor(Color.GRAY);
    g.drawString("MEILLEURS SCORES:", getLargeur()/2
      -(g.getFontMetrics().stringWidth("HIGH SCORES:"))/2, h);
    for (String line : s.split("\n")) {
      h += g.getFontMetrics().getHeight();
      g.drawString(line, getLargeur()/2
        -(g.getFontMetrics().stringWidth(line))/2, h);
    }
  }
  
  public void remplirImageFond(String imageFile, Graphics g) {
    try {
      BufferedImage bimg = ImageIO.read(new File("Images/"+imageFile));
      Graphics2D g2 = (Graphics2D) g;
      Rectangle r = new Rectangle(0, 0, 32, 32);
      g2.setPaint(new TexturePaint(bimg, r));
      Rectangle rect = new Rectangle(0,0,getLargeur(), 
        getHauteur());
      g2.fill(rect);
    } catch(Exception e) {}
  }

  public ICanvas getSpecificPart() { 
    return specifique;
  }

  public int getLargeur() { 
    return specifique.getNbColonnes()*specifique.getTailleCellule();
  }

  public int getHauteur() { 
    return specifique.getNbLignes()*specifique.getTailleCellule();
  }
}