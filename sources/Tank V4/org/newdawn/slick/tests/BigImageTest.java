package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class BigImageTest extends BasicGame {
   private Image original;
   private Image image;
   private Image imageX;
   private Image imageY;
   private Image sub;
   private Image scaledSub;
   private float x;
   private float y;
   private float ang = 30.0F;
   private SpriteSheet bigSheet;

   public BigImageTest() {
      super("Big Image Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.original = this.image = new BigImage("testdata/bigimage.tga", 2, 512);
      this.sub = this.image.getSubImage(210, 210, 200, 130);
      this.scaledSub = this.sub.getScaledCopy(2.0F);
      this.image = this.image.getScaledCopy(0.3F);
      this.imageX = this.image.getFlippedCopy(true, false);
      this.imageY = this.imageX.getFlippedCopy(true, true);
      this.bigSheet = new SpriteSheet(this.original, 16, 16);
   }

   public void render(GameContainer var1, Graphics var2) {
      this.original.draw(0.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, 0.4F));
      this.image.draw(this.x, this.y);
      this.imageX.draw(this.x + 400.0F, this.y);
      this.imageY.draw(this.x, this.y + 300.0F);
      this.scaledSub.draw(this.x + 300.0F, this.y + 300.0F);
      this.bigSheet.getSprite(7, 5).draw(50.0F, 10.0F);
      var2.setColor(Color.white);
      var2.drawRect(50.0F, 10.0F, 64.0F, 64.0F);
      var2.rotate(this.x + 400.0F, this.y + 165.0F, this.ang);
      var2.drawImage(this.sub, this.x + 300.0F, this.y + 100.0F);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new BigImageTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.ang += (float)var2 * 0.1F;
      if (var1.getInput().isKeyDown(203)) {
         this.x -= (float)var2 * 0.1F;
      }

      if (var1.getInput().isKeyDown(205)) {
         this.x += (float)var2 * 0.1F;
      }

      if (var1.getInput().isKeyDown(200)) {
         this.y -= (float)var2 * 0.1F;
      }

      if (var1.getInput().isKeyDown(208)) {
         this.y += (float)var2 * 0.1F;
      }

   }
}
