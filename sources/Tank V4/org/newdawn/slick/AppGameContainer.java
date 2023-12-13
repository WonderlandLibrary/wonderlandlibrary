package org.newdawn.slick;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.opengl.TGAImageData;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class AppGameContainer extends GameContainer {
   protected DisplayMode originalDisplayMode;
   protected DisplayMode targetDisplayMode;
   protected boolean updateOnlyOnVisible;
   protected boolean alphaSupport;

   public AppGameContainer(Game var1) throws SlickException {
      this(var1, 640, 480, false);
   }

   public AppGameContainer(Game var1, int var2, int var3, boolean var4) throws SlickException {
      super(var1);
      this.updateOnlyOnVisible = true;
      this.alphaSupport = false;
      this.originalDisplayMode = Display.getDisplayMode();
      this.setDisplayMode(var2, var3, var4);
   }

   public boolean supportsAlphaInBackBuffer() {
      return this.alphaSupport;
   }

   public void setTitle(String var1) {
      Display.setTitle(var1);
   }

   public void setDisplayMode(int var1, int var2, boolean var3) throws SlickException {
      if (this.width != var1 || this.height != var2 || this.isFullscreen() != var3) {
         try {
            this.targetDisplayMode = null;
            if (!var3) {
               this.targetDisplayMode = new DisplayMode(var1, var2);
            } else {
               DisplayMode[] var4 = Display.getAvailableDisplayModes();
               int var5 = 0;

               for(int var6 = 0; var6 < var4.length; ++var6) {
                  DisplayMode var7 = var4[var6];
                  if (var7.getWidth() == var1 && var7.getHeight() == var2) {
                     if ((this.targetDisplayMode == null || var7.getFrequency() >= var5) && (this.targetDisplayMode == null || var7.getBitsPerPixel() > this.targetDisplayMode.getBitsPerPixel())) {
                        this.targetDisplayMode = var7;
                        var5 = this.targetDisplayMode.getFrequency();
                     }

                     if (var7.getBitsPerPixel() == this.originalDisplayMode.getBitsPerPixel() && var7.getFrequency() == this.originalDisplayMode.getFrequency()) {
                        this.targetDisplayMode = var7;
                        break;
                     }
                  }
               }
            }

            if (this.targetDisplayMode == null) {
               throw new SlickException("Failed to find value mode: " + var1 + "x" + var2 + " fs=" + var3);
            }

            this.width = var1;
            this.height = var2;
            Display.setDisplayMode(this.targetDisplayMode);
            Display.setFullscreen(var3);
            if (Display.isCreated()) {
               this.initGL();
               this.enterOrtho();
            }

            if (this.targetDisplayMode.getBitsPerPixel() == 16) {
               InternalTextureLoader.get().set16BitMode();
            }
         } catch (LWJGLException var8) {
            throw new SlickException("Unable to setup mode " + var1 + "x" + var2 + " fullscreen=" + var3, var8);
         }

         this.getDelta();
      }
   }

   public boolean isFullscreen() {
      return Display.isFullscreen();
   }

   public void setFullscreen(boolean var1) throws SlickException {
      if (this.isFullscreen() != var1) {
         if (!var1) {
            try {
               Display.setFullscreen(var1);
            } catch (LWJGLException var3) {
               throw new SlickException("Unable to set fullscreen=" + var1, var3);
            }
         } else {
            this.setDisplayMode(this.width, this.height, var1);
         }

         this.getDelta();
      }
   }

   public void setMouseCursor(String var1, int var2, int var3) throws SlickException {
      try {
         Cursor var4 = CursorLoader.get().getCursor(var1, var2, var3);
         Mouse.setNativeCursor(var4);
      } catch (Exception var5) {
         Log.error("Failed to load and apply cursor.", var5);
      }

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
         Cursor var7 = CursorLoader.get().getCursor(var6, var2, var3, var4.getWidth(), var1.getHeight());
         Mouse.setNativeCursor(var7);
      } catch (Exception var8) {
         Log.error("Failed to load and apply cursor.", var8);
      }

   }

   public void reinit() throws SlickException {
      InternalTextureLoader.get().clear();
      SoundStore.get().clear();
      this.initSystem();
      this.enterOrtho();

      try {
         this.game.init(this);
      } catch (SlickException var2) {
         Log.error((Throwable)var2);
         this.running = false;
      }

   }

   private void tryCreateDisplay(PixelFormat var1) throws LWJGLException {
      if (SHARED_DRAWABLE == null) {
         Display.create(var1);
      } else {
         Display.create(var1, SHARED_DRAWABLE);
      }

   }

   public void start() throws SlickException {
      this.setup();
      this.getDelta();

      while(this.running()) {
         this.gameLoop();
      }

      this.destroy();
      if (this.forceExit) {
         System.exit(0);
      }

   }

   protected void setup() throws SlickException {
      if (this.targetDisplayMode == null) {
         this.setDisplayMode(640, 480, false);
      }

      Display.setTitle(this.game.getTitle());
      Log.info("LWJGL Version: " + Sys.getVersion());
      Log.info("OriginalDisplayMode: " + this.originalDisplayMode);
      Log.info("TargetDisplayMode: " + this.targetDisplayMode);
      AccessController.doPrivileged(new PrivilegedAction(this) {
         private final AppGameContainer this$0;

         {
            this.this$0 = var1;
         }

         public Object run() {
            try {
               PixelFormat var1 = new PixelFormat(8, 8, 0, this.this$0.samples);
               AppGameContainer.access$000(this.this$0, var1);
               this.this$0.supportsMultiSample = true;
            } catch (Exception var6) {
               Display.destroy();

               try {
                  PixelFormat var2 = new PixelFormat(8, 8, 0);
                  AppGameContainer.access$000(this.this$0, var2);
                  this.this$0.alphaSupport = false;
               } catch (Exception var5) {
                  Display.destroy();

                  try {
                     AppGameContainer.access$000(this.this$0, new PixelFormat());
                  } catch (Exception var4) {
                     Log.error((Throwable)var4);
                  }
               }
            }

            return null;
         }
      });
      if (!Display.isCreated()) {
         throw new SlickException("Failed to initialise the LWJGL display");
      } else {
         this.initSystem();
         this.enterOrtho();

         try {
            this.getInput().initControllers();
         } catch (SlickException var3) {
            Log.info("Controllers not available");
         } catch (Throwable var4) {
            Log.info("Controllers not available");
         }

         try {
            this.game.init(this);
         } catch (SlickException var2) {
            Log.error((Throwable)var2);
            this.running = false;
         }

      }
   }

   protected void gameLoop() throws SlickException {
      int var1 = this.getDelta();
      if (!Display.isVisible() && this.updateOnlyOnVisible) {
         try {
            Thread.sleep(100L);
         } catch (Exception var4) {
         }
      } else {
         try {
            this.updateAndRender(var1);
         } catch (SlickException var3) {
            Log.error((Throwable)var3);
            this.running = false;
            return;
         }
      }

      this.updateFPS();
      Display.update();
      if (Display.isCloseRequested() && this.game.closeRequested()) {
         this.running = false;
      }

   }

   public void setUpdateOnlyWhenVisible(boolean var1) {
      this.updateOnlyOnVisible = var1;
   }

   public boolean isUpdatingOnlyWhenVisible() {
      return this.updateOnlyOnVisible;
   }

   public void setIcon(String var1) throws SlickException {
      this.setIcons(new String[]{var1});
   }

   public void setMouseGrabbed(boolean var1) {
      Mouse.setGrabbed(var1);
   }

   public boolean isMouseGrabbed() {
      return Mouse.isGrabbed();
   }

   public boolean hasFocus() {
      return Display.isActive();
   }

   public int getScreenHeight() {
      return this.originalDisplayMode.getHeight();
   }

   public int getScreenWidth() {
      return this.originalDisplayMode.getWidth();
   }

   public void destroy() {
      Display.destroy();
      AL.destroy();
   }

   public void setIcons(String[] var1) throws SlickException {
      ByteBuffer[] var2 = new ByteBuffer[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         boolean var5 = true;
         Object var4;
         if (var1[var3].endsWith(".tga")) {
            var4 = new TGAImageData();
         } else {
            var5 = false;
            var4 = new ImageIOImageData();
         }

         try {
            var2[var3] = ((LoadableImageData)var4).loadImage(ResourceLoader.getResourceAsStream(var1[var3]), var5, false, (int[])null);
         } catch (Exception var7) {
            Log.error((Throwable)var7);
            throw new SlickException("Failed to set the icon");
         }
      }

      Display.setIcon(var2);
   }

   public void setDefaultMouseCursor() {
      try {
         Mouse.setNativeCursor((Cursor)null);
      } catch (LWJGLException var2) {
         Log.error("Failed to reset mouse cursor", var2);
      }

   }

   static void access$000(AppGameContainer var0, PixelFormat var1) throws LWJGLException {
      var0.tryCreateDisplay(var1);
   }

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            try {
               Display.getDisplayMode();
            } catch (Exception var2) {
               Log.error((Throwable)var2);
            }

            return null;
         }
      });
   }

   private class NullOutputStream extends OutputStream {
      private final AppGameContainer this$0;

      private NullOutputStream(AppGameContainer var1) {
         this.this$0 = var1;
      }

      public void write(int var1) throws IOException {
      }
   }
}
