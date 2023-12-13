package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ClipTest extends BasicGame {
   private float ang = 0.0F;
   private boolean world;
   private boolean clip;

   public ClipTest() {
      super("Clip Test");
   }

   public void init(GameContainer var1) throws SlickException {
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.ang += (float)var2 * 0.01F;
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.white);
      var2.drawString("1 - No Clipping", 100.0F, 10.0F);
      var2.drawString("2 - Screen Clipping", 100.0F, 30.0F);
      var2.drawString("3 - World Clipping", 100.0F, 50.0F);
      if (this.world) {
         var2.drawString("WORLD CLIPPING ENABLED", 200.0F, 80.0F);
      }

      if (this.clip) {
         var2.drawString("SCREEN CLIPPING ENABLED", 200.0F, 80.0F);
      }

      var2.rotate(400.0F, 400.0F, this.ang);
      if (this.world) {
         var2.setWorldClip(350.0F, 302.0F, 100.0F, 196.0F);
      }

      if (this.clip) {
         var2.setClip(350, 302, 100, 196);
      }

      var2.setColor(Color.red);
      var2.fillOval(300.0F, 300.0F, 200.0F, 200.0F);
      var2.setColor(Color.blue);
      var2.fillRect(390.0F, 200.0F, 20.0F, 400.0F);
      var2.clearClip();
      var2.clearWorldClip();
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 2) {
         this.world = false;
         this.clip = false;
      }

      if (var1 == 3) {
         this.world = false;
         this.clip = true;
      }

      if (var1 == 4) {
         this.world = true;
         this.clip = false;
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new ClipTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
