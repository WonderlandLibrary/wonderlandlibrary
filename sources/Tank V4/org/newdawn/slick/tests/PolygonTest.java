package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class PolygonTest extends BasicGame {
   private Polygon poly;
   private boolean in;
   private float y;

   public PolygonTest() {
      super("Polygon Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.poly = new Polygon();
      this.poly.addPoint(300.0F, 100.0F);
      this.poly.addPoint(320.0F, 200.0F);
      this.poly.addPoint(350.0F, 210.0F);
      this.poly.addPoint(280.0F, 250.0F);
      this.poly.addPoint(300.0F, 200.0F);
      this.poly.addPoint(240.0F, 150.0F);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.in = this.poly.contains((float)var1.getInput().getMouseX(), (float)var1.getInput().getMouseY());
      this.poly.setCenterY(0.0F);
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      if (this.in) {
         var2.setColor(Color.red);
         var2.fill(this.poly);
      }

      var2.setColor(Color.yellow);
      var2.fillOval(this.poly.getCenterX() - 3.0F, this.poly.getCenterY() - 3.0F, 6.0F, 6.0F);
      var2.setColor(Color.white);
      var2.draw(this.poly);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new PolygonTest(), 640, 480, false);
         var1.start();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
