package PersistScore;

import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class FichierTexte implements IPersist {

  @SuppressWarnings("unchecked")
  public ArrayList<Score> lireListe(String nom) {
    File f = new File(CHEMIN_SCORES+nom+".sco.txt");
    ArrayList <Score> res = new ArrayList<Score>();
    if (f.exists()) {
      try {
        String ligne = null;
        String[] mots = new String[2];
        Score s = null;
        BufferedReader br = new BufferedReader(new FileReader(CHEMIN_SCORES+nom+".sco.txt"));
        while ((ligne = br.readLine()) != null) {
          mots = ligne.split("\t");
          s = new Score(mots[0], Integer.parseInt(mots[1]));
          res.add(s);
        }
        br.close();
      } catch(Exception e) {System.out.println("read error: "+e.getMessage());}
    }
    return res;
  }

  public void ecrireListe(String nom, ArrayList<Score> liste) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(CHEMIN_SCORES+nom+".sco.txt"));
      for (Score s : liste) {
        bw.write(s.getNom()+"\t"+s.getScore());
        bw.newLine();
      }
      bw.close();
    } catch(Exception e) {System.out.println("write error: "+e);} 
  }
}