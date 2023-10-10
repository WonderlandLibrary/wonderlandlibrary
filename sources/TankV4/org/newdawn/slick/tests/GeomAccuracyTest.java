package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;

public class GeomAccuracyTest extends BasicGame {
   private GameContainer container;
   private Color geomColor;
   private Color overlayColor;
   private boolean hideOverlay;
   private int colorIndex;
   private int curTest;
   private static final int NUMTESTS = 3;
   private Image magImage;

   public GeomAccuracyTest() {
      super("Geometry Accuracy Tests");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.geomColor = Color.magenta;
      this.overlayColor = Color.white;
      this.magImage = new Image(21, 21);
   }

   public void render(GameContainer var1, Graphics var2) {
      String var3 = new String();
      switch(this.curTest) {
      case 0:
         var3 = "Rectangles";
         this.rectTest(var2);
         break;
      case 1:
         var3 = "Ovals";
         this.ovalTest(var2);
         break;
      case 2:
         var3 = "Arcs";
         this.arcTest(var2);
      }

      var2.setColor(Color.white);
      var2.drawString("Press T to toggle overlay", 200.0F, 55.0F);
      var2.drawString("Press N to switch tests", 200.0F, 35.0F);
      var2.drawString("Press C to cycle drawing colors", 200.0F, 15.0F);
      var2.drawString("Current Test:", 400.0F, 35.0F);
      var2.setColor(Color.blue);
      var2.drawString(var3, 485.0F, 35.0F);
      var2.setColor(Color.white);
      var2.drawString("Normal:", 10.0F, 150.0F);
      var2.drawString("Filled:", 10.0F, 300.0F);
      var2.drawString("Drawn with Graphics context", 125.0F, 400.0F);
      var2.drawString("Drawn using Shapes", 450.0F, 400.0F);
      var2.copyArea(this.magImage, var1.getInput().getMouseX() - 10, var1.getInput().getMouseY() - 10);
      this.magImage.draw(351.0F, 451.0F, 5.0F);
      var2.drawString("Mag Area -", 250.0F, 475.0F);
      var2.setColor(Color.darkGray);
      var2.drawRect(350.0F, 450.0F, 106.0F, 106.0F);
      var2.setColor(Color.white);
      var2.drawString("NOTE:", 500.0F, 450.0F);
      var2.drawString("lines should be flush with edges", 525.0F, 470.0F);
      var2.drawString("corners should be symetric", 525.0F, 490.0F);
   }

   void arcTest(Graphics var1) {
      if (!this.hideOverlay) {
         var1.setColor(this.overlayColor);
         var1.drawLine(198.0F, 100.0F, 198.0F, 198.0F);
         var1.drawLine(100.0F, 198.0F, 198.0F, 198.0F);
      }

      var1.setColor(this.geomColor);
      var1.drawArc(100.0F, 100.0F, 99.0F, 99.0F, 0.0F, 90.0F);
   }

   void ovalTest(Graphics var1) {
      var1.setColor(this.geomColor);
      var1.drawOval(100.0F, 100.0F, 99.0F, 99.0F);
      var1.fillOval(100.0F, 250.0F, 99.0F, 99.0F);
      Ellipse var2 = new Ellipse(449.0F, 149.0F, 49.0F, 49.0F);
      var1.draw(var2);
      var2 = new Ellipse(449.0F, 299.0F, 49.0F, 49.0F);
      var1.fill(var2);
      if (!this.hideOverlay) {
         var1.setColor(this.overlayColor);
         var1.drawLine(100.0F, 149.0F, 198.0F, 149.0F);
         var1.drawLine(149.0F, 100.0F, 149.0F, 198.0F);
         var1.drawLine(100.0F, 299.0F, 198.0F, 299.0F);
         var1.drawLine(149.0F, 250.0F, 149.0F, 348.0F);
         var1.drawLine(400.0F, 149.0F, 498.0F, 149.0F);
         var1.drawLine(449.0F, 100.0F, 449.0F, 198.0F);
         var1.drawLine(400.0F, 299.0F, 498.0F, 299.0F);
         var1.drawLine(449.0F, 250.0F, 449.0F, 348.0F);
      }

   }

   void rectTest(Graphics var1) {
      var1.setColor(this.geomColor);
      var1.drawRect(100.0F, 100.0F, 99.0F, 99.0F);
      var1.fillRect(100.0F, 250.0F, 99.0F, 99.0F);
      var1.drawRoundRect(250.0F, 100.0F, 99.0F, 99.0F, 10);
      var1.fillRoundRect(250.0F, 250.0F, 99.0F, 99.0F, 10);
      Rectangle var2 = new Rectangle(400.0F, 100.0F, 99.0F, 99.0F);
      var1.draw(var2);
      var2 = new Rectangle(400.0F, 250.0F, 99.0F, 99.0F);
      var1.fill(var2);
      RoundedRectangle var3 = new RoundedRectangle(550.0F, 100.0F, 99.0F, 99.0F, 10.0F);
      var1.draw(var3);
      var3 = new RoundedRectangle(550.0F, 250.0F, 99.0F, 99.0F, 10.0F);
      var1.fill(var3);
      if (!this.hideOverlay) {
         var1.setColor(this.overlayColor);
         var1.drawLine(100.0F, 149.0F, 198.0F, 149.0F);
         var1.drawLine(149.0F, 100.0F, 149.0F, 198.0F);
         var1.drawLine(250.0F, 149.0F, 348.0F, 149.0F);
         var1.drawLine(299.0F, 100.0F, 299.0F, 198.0F);
         var1.drawLine(400.0F, 149.0F, 498.0F, 149.0F);
         var1.drawLine(449.0F, 100.0F, 449.0F, 198.0F);
         var1.drawLine(550.0F, 149.0F, 648.0F, 149.0F);
         var1.drawLine(599.0F, 100.0F, 599.0F, 198.0F);
         var1.drawLine(100.0F, 299.0F, 198.0F, 299.0F);
         var1.drawLine(149.0F, 250.0F, 149.0F, 348.0F);
         var1.drawLine(250.0F, 299.0F, 348.0F, 299.0F);
         var1.drawLine(299.0F, 250.0F, 299.0F, 348.0F);
         var1.drawLine(400.0F, 299.0F, 498.0F, 299.0F);
         var1.drawLine(449.0F, 250.0F, 449.0F, 348.0F);
         var1.drawLine(550.0F, 299.0F, 648.0F, 299.0F);
         var1.drawLine(599.0F, 250.0F, 599.0F, 348.0F);
      }

   }

   public void update(GameContainer var1, int var2) {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 49) {
         ++this.curTest;
         this.curTest %= 3;
      }

      if (var1 == 46) {
         ++this.colorIndex;
         this.colorIndex %= 4;
         this.setColors();
      }

      if (var1 == 20) {
         this.hideOverlay = !this.hideOverlay;
      }

   }

   private void setColors() {
      switch(this.colorIndex) {
      case 0:
         this.overlayColor = Color.white;
         this.geomColor = Color.magenta;
         break;
      case 1:
         this.overlayColor = Color.magenta;
         this.geomColor = Color.white;
         break;
      case 2:
         this.overlayColor = Color.red;
         this.geomColor = Color.green;
         break;
      case 3:
         this.overlayColor = Color.red;
         this.geomColor = Color.white;
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new GeomAccuracyTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
