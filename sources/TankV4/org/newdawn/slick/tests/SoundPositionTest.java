package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;

public class SoundPositionTest extends BasicGame {
   private GameContainer myContainer;
   private Music music;
   private int[] engines = new int[3];

   public SoundPositionTest() {
      super("Music Position Test");
   }

   public void init(GameContainer var1) throws SlickException {
      SoundStore.get().setMaxSources(32);
      this.myContainer = var1;
      this.music = new Music("testdata/kirby.ogg", true);
      this.music.play();
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setColor(Color.white);
      var2.drawString("Position: " + this.music.getPosition(), 100.0F, 100.0F);
      var2.drawString("Space - Pause/Resume", 100.0F, 130.0F);
      var2.drawString("Right Arrow - Advance 5 seconds", 100.0F, 145.0F);
   }

   public void update(GameContainer var1, int var2) {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 57) {
         if (this.music.playing()) {
            this.music.pause();
         } else {
            this.music.resume();
         }
      }

      if (var1 == 205) {
         this.music.setPosition(this.music.getPosition() + 5.0F);
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new SoundPositionTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
