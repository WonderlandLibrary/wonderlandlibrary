package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageCornerTest extends BasicGame {
   private Image image;
   private Image[] images;
   private int width;
   private int height;

   public ImageCornerTest() {
      super("Image Corner Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = new Image("testdata/logo.png");
      this.width = this.image.getWidth() / 3;
      this.height = this.image.getHeight() / 3;
      this.images = new Image[]{this.image.getSubImage(0, 0, this.width, this.height), this.image.getSubImage(this.width, 0, this.width, this.height), this.image.getSubImage(this.width * 2, 0, this.width, this.height), this.image.getSubImage(0, this.height, this.width, this.height), this.image.getSubImage(this.width, this.height, this.width, this.height), this.image.getSubImage(this.width * 2, this.height, this.width, this.height), this.image.getSubImage(0, this.height * 2, this.width, this.height), this.image.getSubImage(this.width, this.height * 2, this.width, this.height), this.image.getSubImage(this.width * 2, this.height * 2, this.width, this.height)};
      this.images[0].setColor(2, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[1].setColor(3, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[1].setColor(2, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[2].setColor(3, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[3].setColor(1, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[3].setColor(2, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[4].setColor(1, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[4].setColor(0, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[4].setColor(3, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[4].setColor(2, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[5].setColor(0, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[5].setColor(3, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[6].setColor(1, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[7].setColor(1, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[7].setColor(0, 0.0F, 1.0F, 1.0F, 1.0F);
      this.images[8].setColor(0, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void render(GameContainer var1, Graphics var2) {
      for(int var3 = 0; var3 < 3; ++var3) {
         for(int var4 = 0; var4 < 3; ++var4) {
            this.images[var3 + var4 * 3].draw((float)(100 + var3 * this.width), (float)(100 + var4 * this.height));
         }
      }

   }

   public static void main(String[] var0) {
      boolean var1 = false;

      try {
         AppGameContainer var2 = new AppGameContainer(new ImageCornerTest());
         var2.setDisplayMode(800, 600, false);
         var2.start();
      } catch (SlickException var3) {
         var3.printStackTrace();
      }

   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }
}
