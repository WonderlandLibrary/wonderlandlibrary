package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.opengl.renderer.Renderer;

public class GeomTest extends BasicGame {
   private Shape rect = new Rectangle(100.0F, 100.0F, 100.0F, 100.0F);
   private Shape circle = new Circle(500.0F, 200.0F, 50.0F);
   private Shape rect1 = (new Rectangle(150.0F, 120.0F, 50.0F, 100.0F)).transform(Transform.createTranslateTransform(50.0F, 50.0F));
   private Shape rect2 = (new Rectangle(310.0F, 210.0F, 50.0F, 100.0F)).transform(Transform.createRotateTransform((float)Math.toRadians(45.0D), 335.0F, 260.0F));
   private Shape circle1 = new Circle(150.0F, 90.0F, 30.0F);
   private Shape circle2 = new Circle(310.0F, 110.0F, 70.0F);
   private Shape circle3 = new Ellipse(510.0F, 150.0F, 70.0F, 70.0F);
   private Shape circle4 = (new Ellipse(510.0F, 350.0F, 30.0F, 30.0F)).transform(Transform.createTranslateTransform(-510.0F, -350.0F)).transform(Transform.createScaleTransform(2.0F, 2.0F)).transform(Transform.createTranslateTransform(510.0F, 350.0F));
   private Shape roundRect = new RoundedRectangle(50.0F, 175.0F, 100.0F, 100.0F, 20.0F);
   private Shape roundRect2 = new RoundedRectangle(50.0F, 280.0F, 50.0F, 50.0F, 20.0F, 20, 5);

   public GeomTest() {
      super("Geom Test");
   }

   public void init(GameContainer var1) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setColor(Color.white);
      var2.drawString("Red indicates a collision, green indicates no collision", 50.0F, 420.0F);
      var2.drawString("White are the targets", 50.0F, 435.0F);
      var2.pushTransform();
      var2.translate(100.0F, 100.0F);
      var2.pushTransform();
      var2.translate(-50.0F, -50.0F);
      var2.scale(10.0F, 10.0F);
      var2.setColor(Color.red);
      var2.fillRect(0.0F, 0.0F, 5.0F, 5.0F);
      var2.setColor(Color.white);
      var2.drawRect(0.0F, 0.0F, 5.0F, 5.0F);
      var2.popTransform();
      var2.setColor(Color.green);
      var2.fillRect(20.0F, 20.0F, 50.0F, 50.0F);
      var2.popTransform();
      var2.setColor(Color.white);
      var2.draw(this.rect);
      var2.draw(this.circle);
      var2.setColor(this.rect1.intersects(this.rect) ? Color.red : Color.green);
      var2.draw(this.rect1);
      var2.setColor(this.rect2.intersects(this.rect) ? Color.red : Color.green);
      var2.draw(this.rect2);
      var2.setColor(this.roundRect.intersects(this.rect) ? Color.red : Color.green);
      var2.draw(this.roundRect);
      var2.setColor(this.circle1.intersects(this.rect) ? Color.red : Color.green);
      var2.draw(this.circle1);
      var2.setColor(this.circle2.intersects(this.rect) ? Color.red : Color.green);
      var2.draw(this.circle2);
      var2.setColor(this.circle3.intersects(this.circle) ? Color.red : Color.green);
      var2.fill(this.circle3);
      var2.setColor(this.circle4.intersects(this.circle) ? Color.red : Color.green);
      var2.draw(this.circle4);
      var2.fill(this.roundRect2);
      var2.setColor(Color.blue);
      var2.draw(this.roundRect2);
      var2.setColor(Color.blue);
      var2.draw(new Circle(100.0F, 100.0F, 50.0F));
      var2.drawRect(50.0F, 50.0F, 100.0F, 100.0F);
   }

   public void update(GameContainer var1, int var2) {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

   }

   public static void main(String[] var0) {
      try {
         Renderer.setRenderer(2);
         AppGameContainer var1 = new AppGameContainer(new GeomTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
