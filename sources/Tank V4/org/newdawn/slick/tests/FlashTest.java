package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FlashTest extends BasicGame {
   private Image image;
   private boolean flash;
   private GameContainer container;

   public FlashTest() {
      super("Flash Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.image = new Image("testdata/logo.tga");
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawString("Press space to toggle", 10.0F, 50.0F);
      if (this.flash) {
         this.image.draw(100.0F, 100.0F);
      } else {
         this.image.drawFlash(100.0F, 100.0F, (float)this.image.getWidth(), (float)this.image.getHeight(), new Color(1.0F, 0.0F, 1.0F, 1.0F));
      }

   }

   public void update(GameContainer var1, int var2) {
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new FlashTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 57) {
         this.flash = !this.flash;
      }

      if (var1 == 1) {
         this.container.exit();
      }

   }
}
