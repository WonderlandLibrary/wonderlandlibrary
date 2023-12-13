package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageReadTest extends BasicGame {
   private Image image;
   private Color[] read = new Color[6];
   private Graphics g;

   public ImageReadTest() {
      super("Image Read Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = new Image("testdata/testcard.png");
      this.read[0] = this.image.getColor(0, 0);
      this.read[1] = this.image.getColor(30, 40);
      this.read[2] = this.image.getColor(55, 70);
      this.read[3] = this.image.getColor(80, 90);
   }

   public void render(GameContainer var1, Graphics var2) {
      this.g = var2;
      this.image.draw(100.0F, 100.0F);
      var2.setColor(Color.white);
      var2.drawString("Move mouse over test image", 200.0F, 20.0F);
      var2.setColor(this.read[0]);
      var2.drawString(this.read[0].toString(), 100.0F, 300.0F);
      var2.setColor(this.read[1]);
      var2.drawString(this.read[1].toString(), 150.0F, 320.0F);
      var2.setColor(this.read[2]);
      var2.drawString(this.read[2].toString(), 200.0F, 340.0F);
      var2.setColor(this.read[3]);
      var2.drawString(this.read[3].toString(), 250.0F, 360.0F);
      if (this.read[4] != null) {
         var2.setColor(this.read[4]);
         var2.drawString("On image: " + this.read[4].toString(), 100.0F, 250.0F);
      }

      if (this.read[5] != null) {
         var2.setColor(Color.white);
         var2.drawString("On screen: " + this.read[5].toString(), 100.0F, 270.0F);
      }

   }

   public void update(GameContainer var1, int var2) {
      int var3 = var1.getInput().getMouseX();
      int var4 = var1.getInput().getMouseY();
      if (var3 >= 100 && var4 >= 100 && var3 < 200 && var4 < 200) {
         this.read[4] = this.image.getColor(var3 - 100, var4 - 100);
      } else {
         this.read[4] = Color.black;
      }

      this.read[5] = this.g.getPixel(var3, var4);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new ImageReadTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
