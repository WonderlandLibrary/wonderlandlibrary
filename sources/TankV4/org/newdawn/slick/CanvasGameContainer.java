package org.newdawn.slick;

import java.awt.Canvas;
import javax.swing.SwingUtilities;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.util.Log;

public class CanvasGameContainer extends Canvas {
   protected CanvasGameContainer.Container container;
   protected Game game;

   public CanvasGameContainer(Game var1) throws SlickException {
      this(var1, false);
   }

   public CanvasGameContainer(Game var1, boolean var2) throws SlickException {
      this.game = var1;
      this.setIgnoreRepaint(true);
      this.requestFocus();
      this.setSize(500, 500);
      this.container = new CanvasGameContainer.Container(this, var1, var2);
      this.container.setForceExit(false);
   }

   public void start() throws SlickException {
      SwingUtilities.invokeLater(new Runnable(this) {
         private final CanvasGameContainer this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            try {
               Input.disableControllers();

               try {
                  Display.setParent(this.this$0);
               } catch (LWJGLException var2) {
                  throw new SlickException("Failed to setParent of canvas", var2);
               }

               this.this$0.container.setup();
               CanvasGameContainer.access$000(this.this$0);
            } catch (SlickException var3) {
               var3.printStackTrace();
               System.exit(0);
            }

         }
      });
   }

   private void scheduleUpdate() {
      if (this.isVisible()) {
         SwingUtilities.invokeLater(new Runnable(this) {
            private final CanvasGameContainer this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               try {
                  this.this$0.container.gameLoop();
               } catch (SlickException var2) {
                  var2.printStackTrace();
               }

               this.this$0.container.checkDimensions();
               CanvasGameContainer.access$000(this.this$0);
            }
         });
      }
   }

   public void dispose() {
   }

   public GameContainer getContainer() {
      return this.container;
   }

   static void access$000(CanvasGameContainer var0) {
      var0.scheduleUpdate();
   }

   private class Container extends AppGameContainer {
      private final CanvasGameContainer this$0;

      public Container(CanvasGameContainer var1, Game var2, boolean var3) throws SlickException {
         super(var2, var1.getWidth(), var1.getHeight(), false);
         this.this$0 = var1;
         this.width = var1.getWidth();
         this.height = var1.getHeight();
         if (var3) {
            enableSharedContext();
         }

      }

      protected void updateFPS() {
         super.updateFPS();
      }

      protected boolean running() {
         return super.running() && this.this$0.isDisplayable();
      }

      public int getHeight() {
         return this.this$0.getHeight();
      }

      public int getWidth() {
         return this.this$0.getWidth();
      }

      public void checkDimensions() {
         if (this.width != this.this$0.getWidth() || this.height != this.this$0.getHeight()) {
            try {
               this.setDisplayMode(this.this$0.getWidth(), this.this$0.getHeight(), false);
            } catch (SlickException var2) {
               Log.error((Throwable)var2);
            }
         }

      }
   }
}
