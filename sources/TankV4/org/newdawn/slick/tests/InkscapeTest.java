package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SimpleDiagramRenderer;

public class InkscapeTest extends BasicGame {
   private SimpleDiagramRenderer[] renderer = new SimpleDiagramRenderer[5];
   private float zoom = 1.0F;
   private float x;
   private float y;

   public InkscapeTest() {
      super("Inkscape Test");
   }

   public void init(GameContainer var1) throws SlickException {
      var1.getGraphics().setBackground(Color.white);
      InkscapeLoader.RADIAL_TRIANGULATION_LEVEL = 2;
      this.renderer[3] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/clonetest.svg"));
      var1.getGraphics().setBackground(new Color(0.5F, 0.7F, 1.0F));
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (var1.getInput().isKeyDown(16)) {
         this.zoom += (float)var2 * 0.01F;
         if (this.zoom > 10.0F) {
            this.zoom = 10.0F;
         }
      }

      if (var1.getInput().isKeyDown(30)) {
         this.zoom -= (float)var2 * 0.01F;
         if (this.zoom < 0.1F) {
            this.zoom = 0.1F;
         }
      }

      if (var1.getInput().isKeyDown(205)) {
         this.x += (float)var2 * 0.1F;
      }

      if (var1.getInput().isKeyDown(203)) {
         this.x -= (float)var2 * 0.1F;
      }

      if (var1.getInput().isKeyDown(208)) {
         this.y += (float)var2 * 0.1F;
      }

      if (var1.getInput().isKeyDown(200)) {
         this.y -= (float)var2 * 0.1F;
      }

   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.scale(this.zoom, this.zoom);
      var2.translate(this.x, this.y);
      var2.scale(0.3F, 0.3F);
      var2.scale(3.3333333F, 3.3333333F);
      var2.translate(400.0F, 0.0F);
      var2.translate(100.0F, 300.0F);
      var2.scale(0.7F, 0.7F);
      var2.scale(1.4285715F, 1.4285715F);
      var2.scale(0.5F, 0.5F);
      var2.translate(-1100.0F, -380.0F);
      this.renderer[3].render(var2);
      var2.scale(2.0F, 2.0F);
      var2.resetTransform();
   }

   public static void main(String[] var0) {
      try {
         Renderer.setRenderer(2);
         Renderer.setLineStripRenderer(4);
         AppGameContainer var1 = new AppGameContainer(new InkscapeTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
