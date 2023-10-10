package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class DuplicateEmitterTest extends BasicGame {
   private GameContainer container;
   private ParticleSystem explosionSystem;
   private ConfigurableEmitter explosionEmitter;

   public DuplicateEmitterTest() {
      super("DuplicateEmitterTest");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;

      try {
         this.explosionSystem = ParticleIO.loadConfiguredSystem("testdata/endlessexplosion.xml");
         this.explosionEmitter = (ConfigurableEmitter)this.explosionSystem.getEmitter(0);
         this.explosionEmitter.setPosition(400.0F, 100.0F);

         for(int var2 = 0; var2 < 5; ++var2) {
            ConfigurableEmitter var3 = this.explosionEmitter.duplicate();
            if (var3 == null) {
               throw new SlickException("Failed to duplicate explosionEmitter");
            }

            var3.name = var3.name + "_" + var2;
            var3.setPosition((float)((var2 + 1) * 133), 400.0F);
            this.explosionSystem.addEmitter(var3);
         }

      } catch (IOException var4) {
         throw new SlickException("Failed to load particle systems", var4);
      }
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.explosionSystem.update(var2);
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      this.explosionSystem.render();
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         this.container.exit();
      }

      if (var1 == 37) {
         this.explosionEmitter.wrapUp();
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new DuplicateEmitterTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
