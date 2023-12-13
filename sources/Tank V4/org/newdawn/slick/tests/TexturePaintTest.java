package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Vector2f;

public class TexturePaintTest extends BasicGame {
   private Polygon poly = new Polygon();
   private Image image;
   private Rectangle texRect = new Rectangle(50.0F, 50.0F, 100.0F, 100.0F);
   private TexCoordGenerator texPaint;

   public TexturePaintTest() {
      super("Texture Paint Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.poly.addPoint(120.0F, 120.0F);
      this.poly.addPoint(420.0F, 100.0F);
      this.poly.addPoint(620.0F, 420.0F);
      this.poly.addPoint(300.0F, 320.0F);
      this.image = new Image("testdata/rocks.png");
      this.texPaint = new TexCoordGenerator(this) {
         private final TexturePaintTest this$0;

         {
            this.this$0 = var1;
         }

         public Vector2f getCoordFor(float var1, float var2) {
            float var3 = (TexturePaintTest.access$000(this.this$0).getX() - var1) / TexturePaintTest.access$000(this.this$0).getWidth();
            float var4 = (TexturePaintTest.access$000(this.this$0).getY() - var2) / TexturePaintTest.access$000(this.this$0).getHeight();
            return new Vector2f(var3, var4);
         }
      };
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.white);
      var2.texture(this.poly, this.image);
      ShapeRenderer.texture(this.poly, this.image, this.texPaint);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new TexturePaintTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   static Rectangle access$000(TexturePaintTest var0) {
      return var0.texRect;
   }
}
