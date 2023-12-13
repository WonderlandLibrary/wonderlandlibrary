package org.newdawn.slick.tests;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CanvasContainerTest extends BasicGame {
   private Image tga;
   private Image scaleMe;
   private Image scaled;
   private Image gif;
   private Image image;
   private Image subImage;
   private float rot;

   public CanvasContainerTest() {
      super("Canvas Container Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = this.tga = new Image("testdata/logo.tga");
      this.scaleMe = new Image("testdata/logo.tga", true, 2);
      this.gif = new Image("testdata/logo.gif");
      this.scaled = this.gif.getScaledCopy(120, 120);
      this.subImage = this.image.getSubImage(200, 0, 70, 260);
      this.rot = 0.0F;
   }

   public void render(GameContainer var1, Graphics var2) {
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
   }

   public void update(GameContainer var1, int var2) {
      this.rot += (float)var2 * 0.1F;
      if (this.rot > 360.0F) {
         this.rot -= 360.0F;
      }

   }

   public static void main(String[] var0) {
      try {
         CanvasGameContainer var1 = new CanvasGameContainer(new CanvasContainerTest());
         Frame var2 = new Frame("Test");
         var2.setLayout(new GridLayout(1, 2));
         var2.setSize(500, 500);
         var2.add(var1);
         var2.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
               System.exit(0);
            }
         });
         var2.setVisible(true);
         var1.start();
      } catch (Exception var3) {
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
