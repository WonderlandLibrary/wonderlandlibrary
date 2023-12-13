package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.opengl.renderer.Renderer;

public class ShapeTest extends BasicGame {
   private Rectangle rect;
   private RoundedRectangle roundRect;
   private Ellipse ellipse;
   private Circle circle;
   private Polygon polygon;
   private ArrayList shapes;
   private boolean[] keys;
   private char[] lastChar;
   private Polygon randomShape = new Polygon();

   public ShapeTest() {
      super("Geom Test");
   }

   public void createPoly(float var1, float var2) {
      byte var3 = 20;
      byte var4 = 10;
      this.randomShape = new Polygon();
      this.randomShape.addPoint((float)(0 + (int)(Math.random() * (double)var4)), (float)(0 + (int)(Math.random() * (double)var4)));
      this.randomShape.addPoint((float)(var3 - (int)(Math.random() * (double)var4)), (float)(0 + (int)(Math.random() * (double)var4)));
      this.randomShape.addPoint((float)(var3 - (int)(Math.random() * (double)var4)), (float)(var3 - (int)(Math.random() * (double)var4)));
      this.randomShape.addPoint((float)(0 + (int)(Math.random() * (double)var4)), (float)(var3 - (int)(Math.random() * (double)var4)));
      this.randomShape.setCenterX(var1);
      this.randomShape.setCenterY(var2);
   }

   public void init(GameContainer var1) throws SlickException {
      this.shapes = new ArrayList();
      this.rect = new Rectangle(10.0F, 10.0F, 100.0F, 80.0F);
      this.shapes.add(this.rect);
      this.roundRect = new RoundedRectangle(150.0F, 10.0F, 60.0F, 80.0F, 20.0F);
      this.shapes.add(this.roundRect);
      this.ellipse = new Ellipse(350.0F, 40.0F, 50.0F, 30.0F);
      this.shapes.add(this.ellipse);
      this.circle = new Circle(470.0F, 60.0F, 50.0F);
      this.shapes.add(this.circle);
      this.polygon = new Polygon(new float[]{550.0F, 10.0F, 600.0F, 40.0F, 620.0F, 100.0F, 570.0F, 130.0F});
      this.shapes.add(this.polygon);
      this.keys = new boolean[256];
      this.lastChar = new char[256];
      this.createPoly(200.0F, 200.0F);
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setColor(Color.green);

      for(int var3 = 0; var3 < this.shapes.size(); ++var3) {
         var2.fill((Shape)this.shapes.get(var3));
      }

      var2.fill(this.randomShape);
      var2.setColor(Color.black);
      var2.setAntiAlias(true);
      var2.draw(this.randomShape);
      var2.setAntiAlias(false);
      var2.setColor(Color.white);
      var2.drawString("keys", 10.0F, 300.0F);
      var2.drawString("wasd - move rectangle", 10.0F, 315.0F);
      var2.drawString("WASD - resize rectangle", 10.0F, 330.0F);
      var2.drawString("tgfh - move rounded rectangle", 10.0F, 345.0F);
      var2.drawString("TGFH - resize rounded rectangle", 10.0F, 360.0F);
      var2.drawString("ry - resize corner radius on rounded rectangle", 10.0F, 375.0F);
      var2.drawString("ikjl - move ellipse", 10.0F, 390.0F);
      var2.drawString("IKJL - resize ellipse", 10.0F, 405.0F);
      var2.drawString("Arrows - move circle", 10.0F, 420.0F);
      var2.drawString("Page Up/Page Down - resize circle", 10.0F, 435.0F);
      var2.drawString("numpad 8546 - move polygon", 10.0F, 450.0F);
   }

   public void update(GameContainer var1, int var2) {
      this.createPoly(200.0F, 200.0F);
      if (this.keys[1]) {
         System.exit(0);
      }

      if (this.keys[17]) {
         if (this.lastChar[17] == 'w') {
            this.rect.setY(this.rect.getY() - 1.0F);
         } else {
            this.rect.setHeight(this.rect.getHeight() - 1.0F);
         }
      }

      if (this.keys[31]) {
         if (this.lastChar[31] == 's') {
            this.rect.setY(this.rect.getY() + 1.0F);
         } else {
            this.rect.setHeight(this.rect.getHeight() + 1.0F);
         }
      }

      if (this.keys[30]) {
         if (this.lastChar[30] == 'a') {
            this.rect.setX(this.rect.getX() - 1.0F);
         } else {
            this.rect.setWidth(this.rect.getWidth() - 1.0F);
         }
      }

      if (this.keys[32]) {
         if (this.lastChar[32] == 'd') {
            this.rect.setX(this.rect.getX() + 1.0F);
         } else {
            this.rect.setWidth(this.rect.getWidth() + 1.0F);
         }
      }

      if (this.keys[20]) {
         if (this.lastChar[20] == 't') {
            this.roundRect.setY(this.roundRect.getY() - 1.0F);
         } else {
            this.roundRect.setHeight(this.roundRect.getHeight() - 1.0F);
         }
      }

      if (this.keys[34]) {
         if (this.lastChar[34] == 'g') {
            this.roundRect.setY(this.roundRect.getY() + 1.0F);
         } else {
            this.roundRect.setHeight(this.roundRect.getHeight() + 1.0F);
         }
      }

      if (this.keys[33]) {
         if (this.lastChar[33] == 'f') {
            this.roundRect.setX(this.roundRect.getX() - 1.0F);
         } else {
            this.roundRect.setWidth(this.roundRect.getWidth() - 1.0F);
         }
      }

      if (this.keys[35]) {
         if (this.lastChar[35] == 'h') {
            this.roundRect.setX(this.roundRect.getX() + 1.0F);
         } else {
            this.roundRect.setWidth(this.roundRect.getWidth() + 1.0F);
         }
      }

      if (this.keys[19]) {
         this.roundRect.setCornerRadius(this.roundRect.getCornerRadius() - 1.0F);
      }

      if (this.keys[21]) {
         this.roundRect.setCornerRadius(this.roundRect.getCornerRadius() + 1.0F);
      }

      if (this.keys[23]) {
         if (this.lastChar[23] == 'i') {
            this.ellipse.setCenterY(this.ellipse.getCenterY() - 1.0F);
         } else {
            this.ellipse.setRadius2(this.ellipse.getRadius2() - 1.0F);
         }
      }

      if (this.keys[37]) {
         if (this.lastChar[37] == 'k') {
            this.ellipse.setCenterY(this.ellipse.getCenterY() + 1.0F);
         } else {
            this.ellipse.setRadius2(this.ellipse.getRadius2() + 1.0F);
         }
      }

      if (this.keys[36]) {
         if (this.lastChar[36] == 'j') {
            this.ellipse.setCenterX(this.ellipse.getCenterX() - 1.0F);
         } else {
            this.ellipse.setRadius1(this.ellipse.getRadius1() - 1.0F);
         }
      }

      if (this.keys[38]) {
         if (this.lastChar[38] == 'l') {
            this.ellipse.setCenterX(this.ellipse.getCenterX() + 1.0F);
         } else {
            this.ellipse.setRadius1(this.ellipse.getRadius1() + 1.0F);
         }
      }

      if (this.keys[200]) {
         this.circle.setCenterY(this.circle.getCenterY() - 1.0F);
      }

      if (this.keys[208]) {
         this.circle.setCenterY(this.circle.getCenterY() + 1.0F);
      }

      if (this.keys[203]) {
         this.circle.setCenterX(this.circle.getCenterX() - 1.0F);
      }

      if (this.keys[205]) {
         this.circle.setCenterX(this.circle.getCenterX() + 1.0F);
      }

      if (this.keys[201]) {
         this.circle.setRadius(this.circle.getRadius() - 1.0F);
      }

      if (this.keys[209]) {
         this.circle.setRadius(this.circle.getRadius() + 1.0F);
      }

      if (this.keys[72]) {
         this.polygon.setY(this.polygon.getY() - 1.0F);
      }

      if (this.keys[76]) {
         this.polygon.setY(this.polygon.getY() + 1.0F);
      }

      if (this.keys[75]) {
         this.polygon.setX(this.polygon.getX() - 1.0F);
      }

      if (this.keys[77]) {
         this.polygon.setX(this.polygon.getX() + 1.0F);
      }

   }

   public void keyPressed(int var1, char var2) {
      this.keys[var1] = true;
      this.lastChar[var1] = var2;
   }

   public void keyReleased(int var1, char var2) {
      this.keys[var1] = false;
   }

   public static void main(String[] var0) {
      try {
         Renderer.setRenderer(2);
         AppGameContainer var1 = new AppGameContainer(new ShapeTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
