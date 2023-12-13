package cn.hanabi.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl.Type;

public class SoundFxPlayer {
   public void playSound(SoundType st, float volume) {
      (new Thread(() -> {
         try {
            AudioInputStream as = AudioSystem.getAudioInputStream(new BufferedInputStream((InputStream)Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/Client/SoundFx/" + st.getName()))));
            Clip clip = AudioSystem.getClip();
            clip.open(as);
            clip.start();
            FloatControl gainControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
            gainControl.setValue(volume);
            clip.start();
         } catch (IOException | LineUnavailableException | UnsupportedAudioFileException var6) {
            var6.printStackTrace();
         }

      })).start();
   }

   public static enum SoundType {
      Enable("enable.wav"),
      Disable("disable.wav"),
      Enable2("enable2.wav"),
      Disable2("disable2.wav"),
      Enter("enter.wav"),
      Notification("notification.wav"),
      Startup("startup.wav"),
      ClickGuiOpen("clickguiopen.wav"),
      Ding("dingsound.wav"),
      Crack("cracksound.wav"),
      EDITION("ingame.wav"),
      VICTORY("victory.wav"),
      SPECIAL("spec.wav");

      final String name;

      private SoundType(String fileName) {
         this.name = fileName;
      }

      String getName() {
         return this.name;
      }
   }
}
