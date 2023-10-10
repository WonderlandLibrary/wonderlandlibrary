package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.opengl.renderer.Renderer;

public class LineRenderTest extends BasicGame {
   private Polygon polygon = new Polygon();
   private Path path = new Path(100.0F, 100.0F);
   private float width = 10.0F;
   private boolean antialias = true;

   public LineRenderTest() {
      super("LineRenderTest");
   }

   public void init(GameContainer var1) throws SlickException {
      this.polygon.addPoint(100.0F, 100.0F);
      this.polygon.addPoint(200.0F, 80.0F);
      this.polygon.addPoint(320.0F, 150.0F);
      this.polygon.addPoint(230.0F, 210.0F);
      this.polygon.addPoint(170.0F, 260.0F);
      this.path.curveTo(200.0F, 200.0F, 200.0F, 100.0F, 100.0F, 200.0F);
      this.path.curveTo(400.0F, 100.0F, 400.0F, 200.0F, 200.0F, 100.0F);
      this.path.curveTo(500.0F, 500.0F, 400.0F, 200.0F, 200.0F, 100.0F);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (var1.getInput().isKeyPressed(57)) {
         this.antialias = !this.antialias;
      }

   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setAntiAlias(this.antialias);
      var2.setLineWidth(50.0F);
      var2.setColor(Color.red);
      var2.draw(this.path);
   }

   public static void main(String[] var0) {
      try {
         Renderer.setLineStripRenderer(4);
         Renderer.getLineStripRenderer().setLineCaps(true);
         AppGameContainer var1 = new AppGameContainer(new LineRenderTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
