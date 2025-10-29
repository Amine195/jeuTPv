package PersistScore;

import java.io.Serializable;

public  class Score implements Comparable, Serializable {

   private String nomJoueur;
   private int score;
  
   public Score(String n, int s) {
     nomJoueur = n;
     score = s;
   } 

   public int compareTo(Object o) {
     Score s = (Score) o;
     if (this.score < s.getScore()) return -1;
     else if (this.score == s.getScore()) return 0;
     else return 1;
   }

   public String getNom() { return nomJoueur;}

   public int getScore() { return score;}

   public String toString() { return nomJoueur+" "+score;}
} 