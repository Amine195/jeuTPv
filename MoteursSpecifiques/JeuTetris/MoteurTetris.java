package MoteursSpecifiques.JeuTetris;

import MoteurGenerique.Moteur;
import MoteurGenerique.IMoteurSpecifique;
import java.awt.Graphics;

public class MoteurTetris implements IMoteurSpecifique {
   private Tetromino tetromino;
   private Moteur moteur;
   private int frequenceMaj;
   private FabriqueTetris fab;
   private Tetromino.Tetrominoes [][] tas;

   public MoteurTetris() {
     fab = new FabriqueTetris();
     moteur = Moteur.getInstance();
   }

   public void initialiserJeu() {
     tas = new Tetromino.Tetrominoes[ParamTetris.LIGNES]
             [ParamTetris.COLONNES];  
     // i <=> y de Tetromino and j <=> x de Tetromino
     for (int i=0; i<ParamTetris.LIGNES; i++)
       for (int j=0; j<ParamTetris.COLONNES; j++)
          tas[i][j] = Tetromino.Tetrominoes.Vide;
     tetromino = (Tetromino) fab.creerObjet(
       ParamTetris.TETROMINO);
   }

   public void genererJeu() {
     moteur.setScore(0);
     moteur.setNiveau(1);
     frequenceMaj = ParamTetris.FREQUENCE_MAJ;
     moteur.notification();
     moteur.setEtat(Moteur.Etat.INITIALISE);
   }

   public void mettreAJourJeu() {
     traiterCollision(); 
     tetromino.mettreAJour();
     chercherLignePleine();
     if (tasPlein()) moteur.terminerJeu();
   }

   private void chercherLignePleine() {
     boolean plein = true;
     int ligne = 0;
     for (int i=0; i<ParamTetris.LIGNES; i++) {
       for (int j=0; j<ParamTetris.COLONNES; j++) {
         if (tas[i][j] == Tetromino.Tetrominoes.Vide) {
           plein = false;
           ligne = i;
           break; 
         }
       }
       if (plein) {
         moteur.setScore(moteur.getScore()+100);
         mettreAJourTas(i);
       } else {
         plein = true;
       }
     }
   }

   private void mettreAJourTas(int i) {
     for (int k=i; k>0; k--) {
      for (int l=0; l<ParamTetris.COLONNES; l++) {
        tas[k][l] = tas[k-1][l];
      }
     }
     changerNiveau();
     moteur.notification();
   }

   private boolean tasPlein() {
     boolean res = false;
     for (int j=0; j<ParamTetris.COLONNES; j++) {
       if (tas[1][j] != Tetromino.Tetrominoes.Vide) { 
         res = true;
         break;
       }
     }
     return res;
   }

   public boolean peutAllerGauche(int[][] cellules) {
     boolean res = true;
     for (int i = 0; i < 4 ; i++) {
       if (cellules[i][0]-1 < 0) { 
         res = false;
         break;
       } else if ((tas[cellules[i][1]][cellules[i][0]-1]) != 
         Tetromino.Tetrominoes.Vide) {
         res = false;
         break;
       }
     }
     return res;
   }

   public boolean peutAllerDroite(int[][] cellules) {
     boolean res = true;
     for (int i = 0; i < 4 ; i++) {
       if (cellules[i][0]+1 > ParamTetris.COLONNES-1) { 
         res = false;
         break;
       } else if ((tas[cellules[i][1]][cellules[i][0]+1]) !=
         Tetromino.Tetrominoes.Vide) {
         res =false;
         break;
       }
     }
     return res;
   }

   public boolean peutTourner(int[][] cellules) {
     boolean res = true;
     for (int i = 0; i < 4 ; i++) {
       if (cellules[i][0] > ParamTetris.COLONNES-1 || 
         cellules[i][0] < 0 || 
         cellules[i][1] > ParamTetris.LIGNES-1) { 
         res = false;
         break;
       } else if ((tas[cellules[i][1]][cellules[i][0]]) !=
         Tetromino.Tetrominoes.Vide) {
         res =false;
         break;
       }
     }
     return res;
   }

   public void traiterCollision() {
     // le tetromino est sur la derniere rangee ou juste au dessus
     // d’un autre. 
     int[][] coords = tetromino.getCoords();
     for (int i = 0; i < 4; i++) { 
       if ((tetromino.getY()+coords[i][1]==ParamTetris.LIGNES-1) ||
          (tas[tetromino.getY()+coords[i][1]+1]
          [tetromino.getX()+coords[i][0]] != 
          Tetromino.Tetrominoes.Vide) ) {
          stockerEtChanger();
          break;
       }
     }
   }

   private void stockerEtChanger() {
     // stocker le tetromino dans le tas
     int[][] coords = tetromino.getCoords();
     for (int i = 0; i < 4; i++) {
       tas[tetromino.getY()+coords[i][1]]
       [tetromino.getX()+coords[i][0]] = tetromino.getForme();
     } 
     // creer un nouveau tetromino qui tombe
     tetromino = (Tetromino) fab.creerObjet(ParamTetris.TETROMINO);
     tetromino.donnerFormeAuHasard();
     moteur.setScore(moteur.getScore()+1);
     moteur.notification();
   }

   public void changerNiveau() {
     moteur.setNiveau(moteur.getNiveau()+1);
     frequenceMaj += 1;
   }

   public void dessinerTas(Graphics g) {
     for (int i=0; i<ParamTetris.COLONNES; i++) {
       for (int j=0; j<ParamTetris.LIGNES; j++) {
         if (tas[j][i] != Tetromino.Tetrominoes.Vide) {
           g.setColor(Tetromino.getCouleur(tas[j][i]));
           g.fillRect(i*ParamTetris.TAILLE_CELLULE,
             j*ParamTetris.TAILLE_CELLULE, 
             ParamTetris.TAILLE_CELLULE,ParamTetris.TAILLE_CELLULE);
         } 
       }
     }
   }

   public Tetromino getTetromino() { return tetromino; }

   public long getPeriodeMaj() { return 1000000000L/frequenceMaj;}
}