package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.opengl.renderer.Renderer;

public class GradientTest extends BasicGame {
   private GameContainer container;
   private GradientFill gradient;
   private GradientFill gradient2;
   private GradientFill gradient4;
   private Rectangle rect;
   private Rectangle center;
   private RoundedRectangle round;
   private RoundedRectangle round2;
   private Polygon poly;
   private float ang;

   public GradientTest() {
      super("Gradient Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.rect = new Rectangle(400.0F, 100.0F, 200.0F, 150.0F);
      this.round = new RoundedRectangle(150.0F, 100.0F, 200.0F, 150.0F, 50.0F);
      this.round2 = new RoundedRectangle(150.0F, 300.0F, 200.0F, 150.0F, 50.0F);
      this.center = new Rectangle(350.0F, 250.0F, 100.0F, 100.0F);
      this.poly = new Polygon();
      this.poly.addPoint(400.0F, 350.0F);
      this.poly.addPoint(550.0F, 320.0F);
      this.poly.addPoint(600.0F, 380.0F);
      this.poly.addPoint(620.0F, 450.0F);
      this.poly.addPoint(500.0F, 450.0F);
      this.gradient = new GradientFill(0.0F, -75.0F, Color.red, 0.0F, 75.0F, Color.yellow, true);
      this.gradient2 = new GradientFill(0.0F, -75.0F, Color.blue, 0.0F, 75.0F, Color.white, true);
      this.gradient4 = new GradientFill(-50.0F, -40.0F, Color.green, 50.0F, 40.0F, Color.cyan, true);
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.rotate(400.0F, 300.0F, this.ang);
      var2.fill(this.rect, this.gradient);
      var2.fill(this.round, this.gradient);
      var2.fill(this.poly, this.gradient2);
      var2.fill(this.center, this.gradient4);
      var2.setAntiAlias(true);
      var2.setLineWidth(10.0F);
      var2.draw(this.round2, this.gradient2);
      var2.setLineWidth(2.0F);
      var2.draw(this.poly, this.gradient);
      var2.setAntiAlias(false);
      var2.fill(this.center, this.gradient4);
      var2.setAntiAlias(true);
      var2.setColor(Color.black);
      var2.draw(this.center);
      var2.setAntiAlias(false);
   }

   public void update(GameContainer var1, int var2) {
      this.ang += (float)var2 * 0.01F;
   }

   public static void main(String[] var0) {
      try {
         Renderer.setRenderer(2);
         AppGameContainer var1 = new AppGameContainer(new GradientTest());
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
