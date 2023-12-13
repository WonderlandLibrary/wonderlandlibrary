package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageTest extends BasicGame {
   private Image tga;
   private Image scaleMe;
   private Image scaled;
   private Image gif;
   private Image image;
   private Image subImage;
   private Image rotImage;
   private float rot;
   public static boolean exitMe = true;

   public ImageTest() {
      super("Image Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = this.tga = new Image("testdata/logo.png");
      this.rotImage = new Image("testdata/logo.png");
      this.rotImage = this.rotImage.getScaledCopy(this.rotImage.getWidth() / 2, this.rotImage.getHeight() / 2);
      this.scaleMe = new Image("testdata/logo.tga", true, 2);
      this.gif = new Image("testdata/logo.gif");
      this.gif.destroy();
      this.gif = new Image("testdata/logo.gif");
      this.scaled = this.gif.getScaledCopy(120, 120);
      this.subImage = this.image.getSubImage(200, 0, 70, 260);
      this.rot = 0.0F;
      if (exitMe) {
         var1.exit();
      }

      Image var2 = this.tga.getSubImage(50, 50, 50, 50);
      System.out.println(var2.getColor(50, 50));
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawRect(0.0F, 0.0F, (float)this.image.getWidth(), (float)this.image.getHeight());
      this.image.draw(0.0F, 0.0F);
      this.image.draw(500.0F, 0.0F, 200.0F, 100.0F);
      this.scaleMe.draw(500.0F, 100.0F, 200.0F, 100.0F);
      this.scaled.draw(400.0F, 500.0F);
      Image var3 = this.scaled.getFlippedCopy(true, false);
      var3.draw(520.0F, 500.0F);
      Image var4 = var3.getFlippedCopy(false, true);
      var4.draw(520.0F, 380.0F);
      Image var5 = var4.getFlippedCopy(true, false);
      var5.draw(400.0F, 380.0F);

      for(int var6 = 0; var6 < 3; ++var6) {
         this.subImage.draw((float)(200 + var6 * 30), 300.0F);
      }

      var2.translate(500.0F, 200.0F);
      var2.rotate(50.0F, 50.0F, this.rot);
      var2.scale(0.3F, 0.3F);
      this.image.draw();
      var2.resetTransform();
      this.rotImage.setRotation(this.rot);
      this.rotImage.draw(100.0F, 200.0F);
   }

   public void update(GameContainer var1, int var2) {
      this.rot += (float)var2 * 0.1F;
      if (this.rot > 360.0F) {
         this.rot -= 360.0F;
      }

   }

   public static void main(String[] var0) {
      boolean var1 = false;

      try {
         exitMe = false;
         if (var1) {
            GameContainer.enableSharedContext();
            exitMe = true;
         }

         AppGameContainer var2 = new AppGameContainer(new ImageTest());
         var2.setForceExit(!var1);
         var2.setDisplayMode(800, 600, false);
         var2.start();
         if (var1) {
            System.out.println("Exit first instance");
            exitMe = false;
            var2 = new AppGameContainer(new ImageTest());
            var2.setDisplayMode(800, 600, false);
            var2.start();
         }
      } catch (SlickException var3) {
         var3.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 57) {
         if (this.image == this.gif) {
            this.image = this.tga;
         } else {
            this.image = this.gif;
         }
      }

   }
}
