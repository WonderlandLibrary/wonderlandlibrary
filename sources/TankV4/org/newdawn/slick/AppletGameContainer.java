package org.newdawn.slick;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.util.Log;

public class AppletGameContainer extends Applet {
   protected AppletGameContainer.ContainerPanel canvas;
   protected AppletGameContainer.Container container;
   protected Canvas displayParent;
   protected Thread gameThread;
   protected boolean alphaSupport = true;

   public void destroy() {
      if (this.displayParent != null) {
         this.remove(this.displayParent);
      }

      super.destroy();
      Log.info("Clear up");
   }

   private void destroyLWJGL() {
      this.container.stopApplet();

      try {
         this.gameThread.join();
      } catch (InterruptedException var2) {
         Log.error((Throwable)var2);
      }

   }

   public void start() {
   }

   public void startLWJGL() {
      if (this.gameThread == null) {
         this.gameThread = new Thread(this) {
            private final AppletGameContainer this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               try {
                  this.this$0.canvas.start();
               } catch (Exception var2) {
                  var2.printStackTrace();
                  if (Display.isCreated()) {
                     Display.destroy();
                  }

                  this.this$0.displayParent.setVisible(false);
                  this.this$0.add(this.this$0.new ConsolePanel(this.this$0, var2));
                  this.this$0.validate();
               }

            }
         };
         this.gameThread.start();
      }
   }

   public void stop() {
   }

   public void init() {
      this.removeAll();
      this.setLayout(new BorderLayout());
      this.setIgnoreRepaint(true);

      try {
         Game var1 = (Game)Class.forName(this.getParameter("game")).newInstance();
         this.container = new AppletGameContainer.Container(this, var1);
         this.canvas = new AppletGameContainer.ContainerPanel(this, this.container);
         this.displayParent = new Canvas(this) {
            private final AppletGameContainer this$0;

            {
               this.this$0 = var1;
            }

            public final void addNotify() {
               super.addNotify();
               this.this$0.startLWJGL();
            }

            public final void removeNotify() {
               AppletGameContainer.access$000(this.this$0);
               super.removeNotify();
            }
         };
         this.displayParent.setSize(this.getWidth(), this.getHeight());
         this.add(this.displayParent);
         this.displayParent.setFocusable(true);
         this.displayParent.requestFocus();
         this.displayParent.setIgnoreRepaint(true);
         this.setVisible(true);
      } catch (Exception var2) {
         Log.error((Throwable)var2);
         throw new RuntimeException("Unable to create game container");
      }
   }

   public GameContainer getContainer() {
      return this.container;
   }

   static void access$000(AppletGameContainer var0) {
      var0.destroyLWJGL();
   }

   public class ConsolePanel extends Panel {
      TextArea textArea;
      private final AppletGameContainer this$0;

      public ConsolePanel(AppletGameContainer var1, Exception var2) {
         this.this$0 = var1;
         this.textArea = new TextArea();
         this.setLayout(new BorderLayout());
         this.setBackground(java.awt.Color.black);
         this.setForeground(java.awt.Color.white);
         java.awt.Font var3 = new java.awt.Font("Arial", 1, 14);
         Label var4 = new Label("SLICK CONSOLE", 1);
         var4.setFont(var3);
         this.add(var4, "First");
         StringWriter var5 = new StringWriter();
         var2.printStackTrace(new PrintWriter(var5));
         this.textArea.setText(var5.toString());
         this.textArea.setEditable(false);
         this.add(this.textArea, "Center");
         this.add(new Panel(), "Before");
         this.add(new Panel(), "After");
         Panel var6 = new Panel();
         var6.setLayout(new GridLayout(0, 1));
         Label var7 = new Label("An error occured while running the applet.", 1);
         Label var8 = new Label("Plese contact support to resolve this issue.", 1);
         var7.setFont(var3);
         var8.setFont(var3);
         var6.add(var7);
         var6.add(var8);
         this.add(var6, "Last");
      }
   }

   public class Container extends GameContainer {
      private final AppletGameContainer this$0;

      public Container(AppletGameContainer var1, Game var2) {
         super(var2);
         this.this$0 = var1;
         this.width = var1.getWidth();
         this.height = var1.getHeight();
      }

      public void initApplet() throws SlickException {
         this.initSystem();
         this.enterOrtho();

         try {
            this.getInput().initControllers();
         } catch (SlickException var2) {
            Log.info("Controllers not available");
         } catch (Throwable var3) {
            Log.info("Controllers not available");
         }

         this.game.init(this);
         this.getDelta();
      }

      public boolean isRunning() {
         return this.running;
      }

      public void stopApplet() {
         this.running = false;
      }

      public int getScreenHeight() {
         return 0;
      }

      public int getScreenWidth() {
         return 0;
      }

      public boolean supportsAlphaInBackBuffer() {
         return this.this$0.alphaSupport;
      }

      public boolean hasFocus() {
         return true;
      }

      public Applet getApplet() {
         return this.this$0;
      }

      public void setIcon(String var1) throws SlickException {
      }

      public void setMouseGrabbed(boolean var1) {
         Mouse.setGrabbed(var1);
      }

      public boolean isMouseGrabbed() {
         return Mouse.isGrabbed();
      }

      public void setMouseCursor(String var1, int var2, int var3) throws SlickException {
         try {
            Cursor var4 = CursorLoader.get().getCursor(var1, var2, var3);
            Mouse.setNativeCursor(var4);
         } catch (Exception var5) {
            Log.error("Failed to load and apply cursor.", var5);
         }

      }

      private int get2Fold(int var1) {
         int var2;
         for(var2 = 2; var2 < var1; var2 *= 2) {
         }

         return var2;
      }

      public void setMouseCursor(Image var1, int var2, int var3) throws SlickException {
         try {
            Image var4 = new Image(this.get2Fold(var1.getWidth()), this.get2Fold(var1.getHeight()));
            Graphics var5 = var4.getGraphics();
            ByteBuffer var6 = BufferUtils.createByteBuffer(var4.getWidth() * var4.getHeight() * 4);
            var5.drawImage(var1.getFlippedCopy(false, true), 0.0F, 0.0F);
            var5.flush();
            var5.getArea(0, 0, var4.getWidth(), var4.getHeight(), var6);
            Cursor var7 = CursorLoader.get().getCursor(var6, var2, var3, var4.getWidth(), var4.getHeight());
            Mouse.setNativeCursor(var7);
         } catch (Exception var8) {
            Log.error("Failed to load and apply cursor.", var8);
         }

      }

      public void setIcons(String[] var1) throws SlickException {
      }

      public void setMouseCursor(ImageData var1, int var2, int var3) throws SlickException {
         try {
            Cursor var4 = CursorLoader.get().getCursor(var1, var2, var3);
            Mouse.setNativeCursor(var4);
         } catch (Exception var5) {
            Log.error("Failed to load and apply cursor.", var5);
         }

      }

      public void setMouseCursor(Cursor var1, int var2, int var3) throws SlickException {
         try {
            Mouse.setNativeCursor(var1);
         } catch (Exception var5) {
            Log.error("Failed to load and apply cursor.", var5);
         }

      }

      public void setDefaultMouseCursor() {
      }

      public boolean isFullscreen() {
         return Display.isFullscreen();
      }

      public void setFullscreen(boolean var1) throws SlickException {
         if (var1 != this.isFullscreen()) {
            try {
               if (var1) {
                  int var2 = Display.getDisplayMode().getWidth();
                  int var3 = Display.getDisplayMode().getHeight();
                  float var4 = (float)this.width / (float)this.height;
                  float var5 = (float)var2 / (float)var3;
                  int var6;
                  int var7;
                  if (var4 >= var5) {
                     var6 = var2;
                     var7 = (int)((float)this.height / ((float)this.width / (float)var2));
                  } else {
                     var6 = (int)((float)this.width / ((float)this.height / (float)var3));
                     var7 = var3;
                  }

                  int var8 = (var2 - var6) / 2;
                  int var9 = (var3 - var7) / 2;
                  GL11.glViewport(var8, var9, var6, var7);
                  this.enterOrtho();
                  this.getInput().setOffset((float)(-var8) * (float)this.width / (float)var6, (float)(-var9) * (float)this.height / (float)var7);
                  this.getInput().setScale((float)this.width / (float)var6, (float)this.height / (float)var7);
                  this.width = var2;
                  this.height = var3;
                  Display.setFullscreen(true);
               } else {
                  this.getInput().setOffset(0.0F, 0.0F);
                  this.getInput().setScale(1.0F, 1.0F);
                  this.width = this.this$0.getWidth();
                  this.height = this.this$0.getHeight();
                  GL11.glViewport(0, 0, this.width, this.height);
                  this.enterOrtho();
                  Display.setFullscreen(false);
               }
            } catch (LWJGLException var10) {
               Log.error((Throwable)var10);
            }

         }
      }

      public void runloop() throws Exception {
         while(this.running) {
            int var1 = this.getDelta();
            this.updateAndRender(var1);
            this.updateFPS();
            Display.update();
         }

         Display.destroy();
      }
   }

   public class ContainerPanel {
      private AppletGameContainer.Container container;
      private final AppletGameContainer this$0;

      public ContainerPanel(AppletGameContainer var1, AppletGameContainer.Container var2) {
         this.this$0 = var1;
         this.container = var2;
      }

      private void createDisplay() throws Exception {
         try {
            Display.create(new PixelFormat(8, 8, 0));
            this.this$0.alphaSupport = true;
         } catch (Exception var2) {
            this.this$0.alphaSupport = false;
            Display.destroy();
            Display.create();
         }

      }

      public void start() throws Exception {
         Display.setParent(this.this$0.displayParent);
         Display.setVSyncEnabled(true);

         try {
            this.createDisplay();
         } catch (LWJGLException var2) {
            var2.printStackTrace();
            Thread.sleep(1000L);
            this.createDisplay();
         }

         this.initGL();
         this.this$0.displayParent.requestFocus();
         this.container.runloop();
      }

      protected void initGL() {
         try {
            InternalTextureLoader.get().clear();
            SoundStore.get().clear();
            this.container.initApplet();
         } catch (Exception var2) {
            Log.error((Throwable)var2);
            this.container.stopApplet();
         }

      }
   }
}
