package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.ResourceLoader;

public class SoundURLTest extends BasicGame {
   private Sound sound;
   private Sound charlie;
   private Sound burp;
   private Music music;
   private Music musica;
   private Music musicb;
   private Sound engine;
   private int volume = 1;

   public SoundURLTest() {
      super("Sound URL Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.sound = new Sound(ResourceLoader.getResource("testdata/restart.ogg"));
      this.charlie = new Sound(ResourceLoader.getResource("testdata/cbrown01.wav"));
      this.engine = new Sound(ResourceLoader.getResource("testdata/engine.wav"));
      this.music = this.musica = new Music(ResourceLoader.getResource("testdata/restart.ogg"), false);
      this.musicb = new Music(ResourceLoader.getResource("testdata/kirby.ogg"), false);
      this.burp = new Sound(ResourceLoader.getResource("testdata/burp.aif"));
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setColor(Color.white);
      var2.drawString("The OGG loop is now streaming from the file, woot.", 100.0F, 60.0F);
      var2.drawString("Press space for sound effect (OGG)", 100.0F, 100.0F);
      var2.drawString("Press P to pause/resume music (XM)", 100.0F, 130.0F);
      var2.drawString("Press E to pause/resume engine sound (WAV)", 100.0F, 190.0F);
      var2.drawString("Press enter for charlie (WAV)", 100.0F, 160.0F);
      var2.drawString("Press C to change music", 100.0F, 210.0F);
      var2.drawString("Press B to burp (AIF)", 100.0F, 240.0F);
      var2.drawString("Press + or - to change volume of music", 100.0F, 270.0F);
      var2.setColor(Color.blue);
      var2.drawString("Music Volume Level: " + (float)this.volume / 10.0F, 150.0F, 300.0F);
   }

   public void update(GameContainer var1, int var2) {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 57) {
         this.sound.play();
      }

      if (var1 == 48) {
         this.burp.play();
      }

      if (var1 == 30) {
         this.sound.playAt(-1.0F, 0.0F, 0.0F);
      }

      if (var1 == 38) {
         this.sound.playAt(1.0F, 0.0F, 0.0F);
      }

      if (var1 == 28) {
         this.charlie.play(1.0F, 1.0F);
      }

      if (var1 == 25) {
         if (this.music.playing()) {
            this.music.pause();
         } else {
            this.music.resume();
         }
      }

      if (var1 == 46) {
         this.music.stop();
         if (this.music == this.musica) {
            this.music = this.musicb;
         } else {
            this.music = this.musica;
         }

         this.music.loop();
      }

      if (var1 == 18) {
         if (this.engine.playing()) {
            this.engine.stop();
         } else {
            this.engine.loop();
         }
      }

      if (var2 == '+') {
         ++this.volume;
         this.setVolume();
      }

      if (var2 == '-') {
         --this.volume;
         this.setVolume();
      }

   }

   private void setVolume() {
      if (this.volume > 10) {
         this.volume = 10;
      } else if (this.volume < 0) {
         this.volume = 0;
      }

      this.music.setVolume((float)this.volume / 10.0F);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new SoundURLTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
