package MoteurGenerique;

import java.util.Random;
import java.awt.Graphics;

public abstract class ObjetJeu {

   protected Random rand; 
   protected int x;
   protected int y;

   public ObjetJeu() {
     rand = new Random();
   }  

   public int getX() { return x; }

   public int getY() { return y; }

   public void setX(int v) { x=v;}

   public void setY(int v) { y=v;}

   public abstract void generer();

   public abstract void mettreAJour();

   public abstract void dessiner(Graphics g);
}