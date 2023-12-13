package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

public class CurveTest extends BasicGame {
   private Curve curve;
   private Vector2f p1 = new Vector2f(100.0F, 300.0F);
   private Vector2f c1 = new Vector2f(100.0F, 100.0F);
   private Vector2f c2 = new Vector2f(300.0F, 100.0F);
   private Vector2f p2 = new Vector2f(300.0F, 300.0F);
   private Polygon poly;

   public CurveTest() {
      super("Curve Test");
   }

   public void init(GameContainer var1) throws SlickException {
      var1.getGraphics().setBackground(Color.white);
      this.curve = new Curve(this.p2, this.c2, this.c1, this.p1);
      this.poly = new Polygon();
      this.poly.addPoint(500.0F, 200.0F);
      this.poly.addPoint(600.0F, 200.0F);
      this.poly.addPoint(700.0F, 300.0F);
      this.poly.addPoint(400.0F, 300.0F);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   private void drawMarker(Graphics var1, Vector2f var2) {
      var1.drawRect(var2.x - 5.0F, var2.y - 5.0F, 10.0F, 10.0F);
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.gray);
      this.drawMarker(var2, this.p1);
      this.drawMarker(var2, this.p2);
      var2.setColor(Color.red);
      this.drawMarker(var2, this.c1);
      this.drawMarker(var2, this.c2);
      var2.setColor(Color.black);
      var2.draw(this.curve);
      var2.fill(this.curve);
      var2.draw(this.poly);
      var2.fill(this.poly);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new CurveTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
