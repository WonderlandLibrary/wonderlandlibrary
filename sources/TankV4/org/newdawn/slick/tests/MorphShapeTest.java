package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.MorphShape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class MorphShapeTest extends BasicGame {
   private Shape a;
   private Shape b;
   private Shape c;
   private MorphShape morph;
   private float time;

   public MorphShapeTest() {
      super("MorphShapeTest");
   }

   public void init(GameContainer var1) throws SlickException {
      this.a = new Rectangle(100.0F, 100.0F, 50.0F, 200.0F);
      this.a = this.a.transform(Transform.createRotateTransform(0.1F, 100.0F, 100.0F));
      this.b = new Rectangle(200.0F, 100.0F, 50.0F, 200.0F);
      this.b = this.b.transform(Transform.createRotateTransform(-0.6F, 100.0F, 100.0F));
      this.c = new Rectangle(300.0F, 100.0F, 50.0F, 200.0F);
      this.c = this.c.transform(Transform.createRotateTransform(-0.2F, 100.0F, 100.0F));
      this.morph = new MorphShape(this.a);
      this.morph.addShape(this.b);
      this.morph.addShape(this.c);
      var1.setVSync(true);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.time += (float)var2 * 0.001F;
      this.morph.setMorphTime(this.time);
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.green);
      var2.draw(this.a);
      var2.setColor(Color.red);
      var2.draw(this.b);
      var2.setColor(Color.blue);
      var2.draw(this.c);
      var2.setColor(Color.white);
      var2.draw(this.morph);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new MorphShapeTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
