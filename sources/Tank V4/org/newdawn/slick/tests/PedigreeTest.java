package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class PedigreeTest extends BasicGame {
   private Image image;
   private GameContainer container;
   private ParticleSystem trail;
   private ParticleSystem fire;
   private float rx;
   private float ry = 900.0F;

   public PedigreeTest() {
      super("Pedigree Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;

      try {
         this.fire = ParticleIO.loadConfiguredSystem("testdata/system.xml");
         this.trail = ParticleIO.loadConfiguredSystem("testdata/smoketrail.xml");
      } catch (IOException var3) {
         throw new SlickException("Failed to load particle systems", var3);
      }

      this.image = new Image("testdata/rocket.png");
      this.spawnRocket();
   }

   private void spawnRocket() {
      this.ry = 700.0F;
      this.rx = (float)(Math.random() * 600.0D + 100.0D);
   }

   public void render(GameContainer var1, Graphics var2) {
      ((ConfigurableEmitter)this.trail.getEmitter(0)).setPosition(this.rx + 14.0F, this.ry + 35.0F);
      this.trail.render();
      this.image.draw((float)((int)this.rx), (float)((int)this.ry));
      var2.translate(400.0F, 300.0F);
      this.fire.render();
   }

   public void update(GameContainer var1, int var2) {
      this.fire.update(var2);
      this.trail.update(var2);
      this.ry -= (float)var2 * 0.25F;
      if (this.ry < -100.0F) {
         this.spawnRocket();
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new PedigreeTest());
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
