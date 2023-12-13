package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SVGMorph;
import org.newdawn.slick.svg.SimpleDiagramRenderer;

public class MorphSVGTest extends BasicGame {
   private SVGMorph morph;
   private Diagram base;
   private float time;
   private float x = -300.0F;

   public MorphSVGTest() {
      super("MorphShapeTest");
   }

   public void init(GameContainer var1) throws SlickException {
      this.base = InkscapeLoader.load("testdata/svg/walk1.svg");
      this.morph = new SVGMorph(this.base);
      this.morph.addStep(InkscapeLoader.load("testdata/svg/walk2.svg"));
      this.morph.addStep(InkscapeLoader.load("testdata/svg/walk3.svg"));
      this.morph.addStep(InkscapeLoader.load("testdata/svg/walk4.svg"));
      var1.setVSync(true);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.morph.updateMorphTime((float)var2 * 0.003F);
      this.x += (float)var2 * 0.2F;
      if (this.x > 550.0F) {
         this.x = -450.0F;
      }

   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.translate(this.x, 0.0F);
      SimpleDiagramRenderer.render(var2, this.morph);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new MorphSVGTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
