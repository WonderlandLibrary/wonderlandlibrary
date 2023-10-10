package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CachedRender;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CachedRenderTest extends BasicGame {
   private Runnable operations;
   private CachedRender cached;
   private boolean drawCached;

   public CachedRenderTest() {
      super("Cached Render Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.operations = new Runnable(this, var1) {
         private final GameContainer val$container;
         private final CachedRenderTest this$0;

         {
            this.this$0 = var1;
            this.val$container = var2;
         }

         public void run() {
            for(int var1 = 0; var1 < 100; ++var1) {
               int var2 = var1 + 100;
               this.val$container.getGraphics().setColor(new Color(var2, var2, var2, var2));
               this.val$container.getGraphics().drawOval((float)(var1 * 5 + 50), (float)(var1 * 3 + 50), 100.0F, 100.0F);
            }

         }
      };
      this.cached = new CachedRender(this.operations);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (var1.getInput().isKeyPressed(57)) {
         this.drawCached = !this.drawCached;
      }

   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.white);
      var2.drawString("Press space to toggle caching", 10.0F, 130.0F);
      if (this.drawCached) {
         var2.drawString("Drawing from cache", 10.0F, 100.0F);
         this.cached.render();
      } else {
         var2.drawString("Drawing direct", 10.0F, 100.0F);
         this.operations.run();
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new CachedRenderTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
