package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

public class ImageBufferTest extends BasicGame {
   private Image image;

   public ImageBufferTest() {
      super("Image Buffer Test");
   }

   public void init(GameContainer var1) throws SlickException {
      ImageBuffer var2 = new ImageBuffer(320, 200);

      for(int var3 = 0; var3 < 320; ++var3) {
         for(int var4 = 0; var4 < 200; ++var4) {
            if (var4 == 20) {
               var2.setRGBA(var3, var4, 255, 255, 255, 255);
            } else {
               var2.setRGBA(var3, var4, var3, var4, 0, 255);
            }
         }
      }

      this.image = var2.getImage();
   }

   public void render(GameContainer var1, Graphics var2) {
      this.image.draw(50.0F, 50.0F);
   }

   public void update(GameContainer var1, int var2) {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new ImageBufferTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
