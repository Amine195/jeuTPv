package PersistScore;

import java.util.ArrayList;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Serialisation implements IPersist {

  @SuppressWarnings("unchecked")
  public ArrayList<Score> lireListe(String nom) {
    File f = new File(CHEMIN_SCORES+nom+".sco");
    ArrayList <Score> res = new ArrayList<Score>();
    if (f.exists()) {
      try {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHEMIN_SCORES+nom+".sco"));
        res = (ArrayList<Score>) ois.readObject();
        ois.close();
      } catch(Exception e) {System.out.println("read error: "+e.getMessage());}
    }
    return res;
  }

  public void ecrireListe(String nom, ArrayList<Score> liste) {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHEMIN_SCORES+nom+".sco"));
      oos.writeObject(liste);
      oos.close();
    } catch(Exception e) {System.out.println("write error: "+e);} 
  }
}