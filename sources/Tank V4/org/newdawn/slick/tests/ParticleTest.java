package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

public class ParticleTest extends BasicGame {
   private ParticleSystem system;
   private int mode = 2;

   public ParticleTest() {
      super("Particle Test");
   }

   public void init(GameContainer var1) throws SlickException {
      Image var2 = new Image("testdata/particle.tga", true);
      this.system = new ParticleSystem(var2);
      this.system.addEmitter(new FireEmitter(400, 300, 45.0F));
      this.system.addEmitter(new FireEmitter(200, 300, 60.0F));
      this.system.addEmitter(new FireEmitter(600, 300, 30.0F));
   }

   public void render(GameContainer var1, Graphics var2) {
      for(int var3 = 0; var3 < 100; ++var3) {
         var2.translate(1.0F, 1.0F);
         this.system.render();
      }

      var2.resetTransform();
      var2.drawString("Press space to toggle blending mode", 200.0F, 500.0F);
      var2.drawString("Particle Count: " + this.system.getParticleCount() * 100, 200.0F, 520.0F);
   }

   public void update(GameContainer var1, int var2) {
      this.system.update(var2);
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 57) {
         this.mode = 1 == this.mode ? 2 : 1;
         this.system.setBlendingMode(this.mode);
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new ParticleTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
