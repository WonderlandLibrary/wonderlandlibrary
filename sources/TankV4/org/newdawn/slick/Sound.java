package org.newdawn.slick;

import java.net.URL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;

public class Sound {
   private Audio sound;

   public Sound(URL var1) throws SlickException {
      SoundStore.get().init();
      String var2 = var1.getFile();

      try {
         if (var2.toLowerCase().endsWith(".ogg")) {
            this.sound = SoundStore.get().getOgg(var1.openStream());
         } else if (var2.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(var1.openStream());
         } else if (var2.toLowerCase().endsWith(".aif")) {
            this.sound = SoundStore.get().getAIF(var1.openStream());
         } else {
            if (!var2.toLowerCase().endsWith(".xm") && !var2.toLowerCase().endsWith(".mod")) {
               throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
            }

            this.sound = SoundStore.get().getMOD(var1.openStream());
         }

      } catch (Exception var4) {
         Log.error((Throwable)var4);
         throw new SlickException("Failed to load sound: " + var2);
      }
   }

   public Sound(String var1) throws SlickException {
      SoundStore.get().init();

      try {
         if (var1.toLowerCase().endsWith(".ogg")) {
            this.sound = SoundStore.get().getOgg(var1);
         } else if (var1.toLowerCase().endsWith(".wav")) {
            this.sound = SoundStore.get().getWAV(var1);
         } else if (var1.toLowerCase().endsWith(".aif")) {
            this.sound = SoundStore.get().getAIF(var1);
         } else {
            if (!var1.toLowerCase().endsWith(".xm") && !var1.toLowerCase().endsWith(".mod")) {
               throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
            }

            this.sound = SoundStore.get().getMOD(var1);
         }

      } catch (Exception var3) {
         Log.error((Throwable)var3);
         throw new SlickException("Failed to load sound: " + var1);
      }
   }

   public void play() {
      this.play(1.0F, 1.0F);
   }

   public void play(float var1, float var2) {
      this.sound.playAsSoundEffect(var1, var2 * SoundStore.get().getSoundVolume(), false);
   }

   public void playAt(float var1, float var2, float var3) {
      this.playAt(1.0F, 1.0F, var1, var2, var3);
   }

   public void playAt(float var1, float var2, float var3, float var4, float var5) {
      this.sound.playAsSoundEffect(var1, var2 * SoundStore.get().getSoundVolume(), false, var3, var4, var5);
   }

   public void loop() {
      this.loop(1.0F, 1.0F);
   }

   public void loop(float var1, float var2) {
      this.sound.playAsSoundEffect(var1, var2 * SoundStore.get().getSoundVolume(), true);
   }

   public boolean playing() {
      return this.sound.isPlaying();
   }

   public void stop() {
      this.sound.stop();
   }
}
