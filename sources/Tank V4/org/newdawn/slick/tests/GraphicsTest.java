package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.util.FastTrig;

public class GraphicsTest extends BasicGame {
   private boolean clip;
   private float ang;
   private Image image;
   private Polygon poly;
   private GameContainer container;

   public GraphicsTest() {
      super("Graphics Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.image = new Image("testdata/logo.tga", true);
      Image var2 = new Image("testdata/palette_tool.png");
      var1.setMouseCursor((Image)var2, 0, 0);
      var1.setIcons(new String[]{"testdata/icon.tga"});
      var1.setTargetFrameRate(100);
      this.poly = new Polygon();
      float var3 = 100.0F;

      for(int var4 = 0; var4 < 360; var4 += 30) {
         if (var3 == 100.0F) {
            var3 = 50.0F;
         } else {
            var3 = 100.0F;
         }

         this.poly.addPoint((float)FastTrig.cos(Math.toRadians((double)var4)) * var3, (float)FastTrig.sin(Math.toRadians((double)var4)) * var3);
      }

   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.white);
      var2.setAntiAlias(true);

      for(int var3 = 0; var3 < 360; var3 += 10) {
         var2.drawLine(700.0F, 100.0F, (float)((int)(700.0D + Math.cos(Math.toRadians((double)var3)) * 100.0D)), (float)((int)(100.0D + Math.sin(Math.toRadians((double)var3)) * 100.0D)));
      }

      var2.setAntiAlias(false);
      var2.setColor(Color.yellow);
      var2.drawString("The Graphics Test!", 300.0F, 50.0F);
      var2.setColor(Color.white);
      var2.drawString("Space - Toggles clipping", 400.0F, 80.0F);
      var2.drawString("Frame rate capped to 100", 400.0F, 120.0F);
      if (this.clip) {
         var2.setColor(Color.gray);
         var2.drawRect(100.0F, 260.0F, 400.0F, 100.0F);
         var2.setClip(100, 260, 400, 100);
      }

      var2.setColor(Color.yellow);
      var2.translate(100.0F, 120.0F);
      var2.fill(this.poly);
      var2.setColor(Color.blue);
      var2.setLineWidth(3.0F);
      var2.draw(this.poly);
      var2.setLineWidth(1.0F);
      var2.translate(0.0F, 230.0F);
      var2.draw(this.poly);
      var2.resetTransform();
      var2.setColor(Color.magenta);
      var2.drawRoundRect(10.0F, 10.0F, 100.0F, 100.0F, 10);
      var2.fillRoundRect(10.0F, 210.0F, 100.0F, 100.0F, 10);
      var2.rotate(400.0F, 300.0F, this.ang);
      var2.setColor(Color.green);
      var2.drawRect(200.0F, 200.0F, 200.0F, 200.0F);
      var2.setColor(Color.blue);
      var2.fillRect(250.0F, 250.0F, 100.0F, 100.0F);
      var2.drawImage(this.image, 300.0F, 270.0F);
      var2.setColor(Color.red);
      var2.drawOval(100.0F, 100.0F, 200.0F, 200.0F);
      var2.setColor(Color.red.darker());
      var2.fillOval(300.0F, 300.0F, 150.0F, 100.0F);
      var2.setAntiAlias(true);
      var2.setColor(Color.white);
      var2.setLineWidth(5.0F);
      var2.drawOval(300.0F, 300.0F, 150.0F, 100.0F);
      var2.setAntiAlias(true);
      var2.resetTransform();
      if (this.clip) {
         var2.clearClip();
      }

   }

   public void update(GameContainer var1, int var2) {
      this.ang += (float)var2 * 0.1F;
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 57) {
         this.clip = !this.clip;
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new GraphicsTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
