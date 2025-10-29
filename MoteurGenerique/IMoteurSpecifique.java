package MoteurGenerique;

public interface IMoteurSpecifique {
   void initialiserJeu();
   void genererJeu();
   void mettreAJourJeu();
   void traiterCollision();
   long getPeriodeMaj();
}