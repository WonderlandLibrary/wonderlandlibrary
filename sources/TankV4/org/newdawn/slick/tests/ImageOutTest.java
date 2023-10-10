package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class ImageOutTest extends BasicGame {
   private GameContainer container;
   private ParticleSystem fire;
   private Graphics g;
   private Image copy;
   private String message;

   public ImageOutTest() {
      super("Image Out Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;

      try {
         this.fire = ParticleIO.loadConfiguredSystem("testdata/system.xml");
      } catch (IOException var4) {
         throw new SlickException("Failed to load particle systems", var4);
      }

      this.copy = new Image(400, 300);
      String[] var2 = ImageOut.getSupportedFormats();
      this.message = "Formats supported: ";

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.message = this.message + var2[var3];
         if (var3 < var2.length - 1) {
            this.message = this.message + ",";
         }
      }

   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawString("T - TGA Snapshot", 10.0F, 50.0F);
      var2.drawString("J - JPG Snapshot", 10.0F, 70.0F);
      var2.drawString("P - PNG Snapshot", 10.0F, 90.0F);
      var2.setDrawMode(Graphics.MODE_ADD);
      var2.drawImage(this.copy, 200.0F, 300.0F);
      var2.setDrawMode(Graphics.MODE_NORMAL);
      var2.drawString(this.message, 10.0F, 400.0F);
      var2.drawRect(200.0F, 0.0F, 400.0F, 300.0F);
      var2.translate(400.0F, 250.0F);
      this.fire.render();
      this.g = var2;
   }

   private void writeTo(String var1) throws SlickException {
      this.g.copyArea(this.copy, 200, 0);
      ImageOut.write(this.copy, var1);
      this.message = "Written " + var1;
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.fire.update(var2);
      if (var1.getInput().isKeyPressed(25)) {
         this.writeTo("ImageOutTest.png");
      }

      if (var1.getInput().isKeyPressed(36)) {
         this.writeTo("ImageOutTest.jpg");
      }

      if (var1.getInput().isKeyPressed(20)) {
         this.writeTo("ImageOutTest.tga");
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new ImageOutTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         this.container.exit();
      }

   }
}
