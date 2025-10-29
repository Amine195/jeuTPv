package Utils.Son;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;

/**
 * Sound management class.
 */
public enum EffetSonore {
 
   MORT("gameover.wav"),
   TOC("event.wav"); 

   public enum Volume {OFF, ON}

   public static Volume volume = Volume.OFF;

   private Clip clip;

   EffetSonore(String nomFichierSon) {
     try {
       File fich = new File("Sons/"+nomFichierSon);
       AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fich);
       clip = AudioSystem.getClip();
       clip.open(audioInputStream);
     } catch (UnsupportedAudioFileException e) {
       e.printStackTrace();
     } catch (IOException e) {
       e.printStackTrace();
     } catch (LineUnavailableException e) {
       e.printStackTrace();
     }
   }

   public void play() {
     if (volume == Volume.ON) {
       if (clip.isRunning()) clip.stop();  
       clip.setFramePosition(0); 
       clip.start();    
     }
   }

   public static void init() {
     values(); 
   }
 }