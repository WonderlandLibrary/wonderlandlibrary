package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.GeomUtil;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtilTest extends BasicGame implements GeomUtilListener {
   private Shape source;
   private Shape cut;
   private Shape[] result;
   private ArrayList points = new ArrayList();
   private ArrayList marks = new ArrayList();
   private ArrayList exclude = new ArrayList();
   private boolean dynamic;
   private GeomUtil util = new GeomUtil();
   private int xp;
   private int yp;
   private Circle circle;
   private Shape rect;
   private Polygon star;
   private boolean union;

   public GeomUtilTest() {
      super("GeomUtilTest");
   }

   public void init() {
      Polygon var1 = new Polygon();
      var1.addPoint(100.0F, 100.0F);
      var1.addPoint(150.0F, 80.0F);
      var1.addPoint(210.0F, 120.0F);
      var1.addPoint(340.0F, 150.0F);
      var1.addPoint(150.0F, 200.0F);
      var1.addPoint(120.0F, 250.0F);
      this.source = var1;
      this.circle = new Circle(0.0F, 0.0F, 50.0F);
      this.rect = new Rectangle(-100.0F, -40.0F, 200.0F, 80.0F);
      this.star = new Polygon();
      float var2 = 40.0F;

      for(int var3 = 0; var3 < 360; var3 += 30) {
         var2 = var2 == 40.0F ? 60.0F : 40.0F;
         double var4 = Math.cos(Math.toRadians((double)var3)) * (double)var2;
         double var6 = Math.sin(Math.toRadians((double)var3)) * (double)var2;
         this.star.addPoint((float)var4, (float)var6);
      }

      this.cut = this.circle;
      this.cut.setLocation(203.0F, 78.0F);
      this.xp = (int)this.cut.getCenterX();
      this.yp = (int)this.cut.getCenterY();
      this.makeBoolean();
   }

   public void init(GameContainer var1) throws SlickException {
      this.util.setListener(this);
      this.init();
      var1.setVSync(true);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (var1.getInput().isKeyPressed(57)) {
         this.dynamic = !this.dynamic;
      }

      if (var1.getInput().isKeyPressed(28)) {
         this.union = !this.union;
         this.makeBoolean();
      }

      if (var1.getInput().isKeyPressed(2)) {
         this.cut = this.circle;
         this.circle.setCenterX((float)this.xp);
         this.circle.setCenterY((float)this.yp);
         this.makeBoolean();
      }

      if (var1.getInput().isKeyPressed(3)) {
         this.cut = this.rect;
         this.rect.setCenterX((float)this.xp);
         this.rect.setCenterY((float)this.yp);
         this.makeBoolean();
      }

      if (var1.getInput().isKeyPressed(4)) {
         this.cut = this.star;
         this.star.setCenterX((float)this.xp);
         this.star.setCenterY((float)this.yp);
         this.makeBoolean();
      }

      if (this.dynamic) {
         this.xp = var1.getInput().getMouseX();
         this.yp = var1.getInput().getMouseY();
         this.makeBoolean();
      }

   }

   private void makeBoolean() {
      this.marks.clear();
      this.points.clear();
      this.exclude.clear();
      this.cut.setCenterX((float)this.xp);
      this.cut.setCenterY((float)this.yp);
      if (this.union) {
         this.result = this.util.union(this.source, this.cut);
      } else {
         this.result = this.util.subtract(this.source, this.cut);
      }

   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.drawString("Space - toggle movement of cutting shape", 530.0F, 10.0F);
      var2.drawString("1,2,3 - select cutting shape", 530.0F, 30.0F);
      var2.drawString("Mouse wheel - rotate shape", 530.0F, 50.0F);
      var2.drawString("Enter - toggle union/subtract", 530.0F, 70.0F);
      var2.drawString("MODE: " + (this.union ? "Union" : "Cut"), 530.0F, 200.0F);
      var2.setColor(Color.green);
      var2.draw(this.source);
      var2.setColor(Color.red);
      var2.draw(this.cut);
      var2.setColor(Color.white);

      int var3;
      Vector2f var4;
      for(var3 = 0; var3 < this.exclude.size(); ++var3) {
         var4 = (Vector2f)this.exclude.get(var3);
         var2.drawOval(var4.x - 3.0F, var4.y - 3.0F, 7.0F, 7.0F);
      }

      var2.setColor(Color.yellow);

      for(var3 = 0; var3 < this.points.size(); ++var3) {
         var4 = (Vector2f)this.points.get(var3);
         var2.fillOval(var4.x - 1.0F, var4.y - 1.0F, 3.0F, 3.0F);
      }

      var2.setColor(Color.white);

      for(var3 = 0; var3 < this.marks.size(); ++var3) {
         var4 = (Vector2f)this.marks.get(var3);
         var2.fillOval(var4.x - 1.0F, var4.y - 1.0F, 3.0F, 3.0F);
      }

      var2.translate(0.0F, 300.0F);
      var2.setColor(Color.white);
      if (this.result != null) {
         for(var3 = 0; var3 < this.result.length; ++var3) {
            var2.draw(this.result[var3]);
         }

         var2.drawString("Polys:" + this.result.length, 10.0F, 100.0F);
         var2.drawString("X:" + this.xp, 10.0F, 120.0F);
         var2.drawString("Y:" + this.yp, 10.0F, 130.0F);
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new GeomUtilTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void pointExcluded(float var1, float var2) {
      this.exclude.add(new Vector2f(var1, var2));
   }

   public void pointIntersected(float var1, float var2) {
      this.marks.add(new Vector2f(var1, var2));
   }

   public void pointUsed(float var1, float var2) {
      this.points.add(new Vector2f(var1, var2));
   }

   public void mouseWheelMoved(int var1) {
      if (this.dynamic) {
         if (var1 < 0) {
            this.cut = this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(10.0D), this.cut.getCenterX(), this.cut.getCenterY()));
         } else {
            this.cut = this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(-10.0D), this.cut.getCenterX(), this.cut.getCenterY()));
         }
      }

   }
}
