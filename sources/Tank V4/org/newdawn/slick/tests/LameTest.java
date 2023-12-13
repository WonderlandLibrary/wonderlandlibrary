package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class LameTest extends BasicGame {
   private Polygon poly = new Polygon();
   private Image image;

   public LameTest() {
      super("Lame Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.poly.addPoint(100.0F, 100.0F);
      this.poly.addPoint(120.0F, 100.0F);
      this.poly.addPoint(120.0F, 120.0F);
      this.poly.addPoint(100.0F, 120.0F);
      this.image = new Image("testdata/rocks.png");
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.white);
      var2.texture(this.poly, this.image);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new LameTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
