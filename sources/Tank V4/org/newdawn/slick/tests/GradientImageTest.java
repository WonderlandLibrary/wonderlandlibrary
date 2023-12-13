package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class GradientImageTest extends BasicGame {
   private Image image1;
   private Image image2;
   private GradientFill fill;
   private Shape shape;
   private Polygon poly;
   private GameContainer container;
   private float ang;
   private boolean rotating = false;

   public GradientImageTest() {
      super("Gradient Image Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.image1 = new Image("testdata/grass.png");
      this.image2 = new Image("testdata/rocks.png");
      this.fill = new GradientFill(-64.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, 1.0F), 64.0F, 0.0F, new Color(0, 0, 0, 0));
      this.shape = new Rectangle(336.0F, 236.0F, 128.0F, 128.0F);
      this.poly = new Polygon();
      this.poly.addPoint(320.0F, 220.0F);
      this.poly.addPoint(350.0F, 200.0F);
      this.poly.addPoint(450.0F, 200.0F);
      this.poly.addPoint(480.0F, 220.0F);
      this.poly.addPoint(420.0F, 400.0F);
      this.poly.addPoint(400.0F, 390.0F);
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawString("R - Toggle Rotationg", 10.0F, 50.0F);
      var2.drawImage(this.image1, 100.0F, 236.0F);
      var2.drawImage(this.image2, 600.0F, 236.0F);
      var2.translate(0.0F, -150.0F);
      var2.rotate(400.0F, 300.0F, this.ang);
      var2.texture(this.shape, this.image2);
      var2.texture(this.shape, this.image1, this.fill);
      var2.resetTransform();
      var2.translate(0.0F, 150.0F);
      var2.rotate(400.0F, 300.0F, this.ang);
      var2.texture(this.poly, this.image2);
      var2.texture(this.poly, this.image1, this.fill);
      var2.resetTransform();
   }

   public void update(GameContainer var1, int var2) {
      if (this.rotating) {
         this.ang += (float)var2 * 0.1F;
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new GradientImageTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 19) {
         this.rotating = !this.rotating;
      }

      if (var1 == 1) {
         this.container.exit();
      }

   }
}
