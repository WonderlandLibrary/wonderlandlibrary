package org.newdawn.slick;

import java.io.IOException;
import java.util.Properties;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public abstract class GameContainer implements GUIContext {
   protected static SGL GL = Renderer.get();
   protected static Drawable SHARED_DRAWABLE;
   protected long lastFrame;
   protected long lastFPS;
   protected int recordedFPS;
   protected int fps;
   protected boolean running = true;
   protected int width;
   protected int height;
   protected Game game;
   private Font defaultFont;
   private Graphics graphics;
   protected Input input;
   protected int targetFPS = -1;
   private boolean showFPS = true;
   protected long minimumLogicInterval = 1L;
   protected long storedDelta;
   protected long maximumLogicInterval = 0L;
   protected Game lastGame;
   protected boolean clearEachFrame = true;
   protected boolean paused;
   protected boolean forceExit = true;
   protected boolean vsync;
   protected boolean smoothDeltas;
   protected int samples;
   protected boolean supportsMultiSample;
   protected boolean alwaysRender;

   protected GameContainer(Game var1) {
      this.game = var1;
      this.lastFrame = this.getTime();
      getBuildVersion();
      Log.checkVerboseLogSetting();
   }

   public void setDefaultFont(Font var1) {
      if (var1 != null) {
         this.defaultFont = var1;
      } else {
         Log.warn("Please provide a non null font");
      }

   }

   public void setMultiSample(int var1) {
      this.samples = var1;
   }

   public boolean supportsMultiSample() {
      return this.supportsMultiSample;
   }

   public int getSamples() {
      return this.samples;
   }

   public void setForceExit(boolean var1) {
      this.forceExit = var1;
   }

   public void setSmoothDeltas(boolean var1) {
      this.smoothDeltas = var1;
   }

   public boolean isFullscreen() {
      return false;
   }

   public float getAspectRatio() {
      return (float)(this.getWidth() / this.getHeight());
   }

   public void setFullscreen(boolean var1) throws SlickException {
   }

   public static void enableSharedContext() throws SlickException {
      try {
         SHARED_DRAWABLE = new Pbuffer(64, 64, new PixelFormat(8, 0, 0), (Drawable)null);
      } catch (LWJGLException var1) {
         throw new SlickException("Unable to create the pbuffer used for shard context, buffers not supported", var1);
      }
   }

   public static Drawable getSharedContext() {
      return SHARED_DRAWABLE;
   }

   public void setClearEachFrame(boolean var1) {
      this.clearEachFrame = var1;
   }

   public void reinit() throws SlickException {
   }

   public void pause() {
      this.setPaused(true);
   }

   public void resume() {
      this.setPaused(false);
   }

   public boolean isPaused() {
      return this.paused;
   }

   public void setPaused(boolean var1) {
      this.paused = var1;
   }

   public boolean getAlwaysRender() {
      return this.alwaysRender;
   }

   public void setAlwaysRender(boolean var1) {
      this.alwaysRender = var1;
   }

   public static int getBuildVersion() {
      try {
         Properties var0 = new Properties();
         var0.load(ResourceLoader.getResourceAsStream("version"));
         int var1 = Integer.parseInt(var0.getProperty("build"));
         Log.info("Slick Build #" + var1);
         return var1;
      } catch (Exception var2) {
         Log.error("Unable to determine Slick build number");
         return -1;
      }
   }

   public Font getDefaultFont() {
      return this.defaultFont;
   }

   public boolean isSoundOn() {
      return SoundStore.get().soundsOn();
   }

   public boolean isMusicOn() {
      return SoundStore.get().musicOn();
   }

   public void setMusicOn(boolean var1) {
      SoundStore.get().setMusicOn(var1);
   }

   public void setSoundOn(boolean var1) {
      SoundStore.get().setSoundsOn(var1);
   }

   public float getMusicVolume() {
      return SoundStore.get().getMusicVolume();
   }

   public float getSoundVolume() {
      return SoundStore.get().getSoundVolume();
   }

   public void setSoundVolume(float var1) {
      SoundStore.get().setSoundVolume(var1);
   }

   public void setMusicVolume(float var1) {
      SoundStore.get().setMusicVolume(var1);
   }

   public abstract int getScreenWidth();

   public abstract int getScreenHeight();

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public abstract void setIcon(String var1) throws SlickException;

   public abstract void setIcons(String[] var1) throws SlickException;

   public long getTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public void sleep(int var1) {
      long var2 = this.getTime() + (long)var1;

      while(this.getTime() < var2) {
         try {
            Thread.sleep(1L);
         } catch (Exception var5) {
         }
      }

   }

   public abstract void setMouseCursor(String var1, int var2, int var3) throws SlickException;

   public abstract void setMouseCursor(ImageData var1, int var2, int var3) throws SlickException;

   public abstract void setMouseCursor(Image var1, int var2, int var3) throws SlickException;

   public abstract void setMouseCursor(Cursor var1, int var2, int var3) throws SlickException;

   public void setAnimatedMouseCursor(String var1, int var2, int var3, int var4, int var5, int[] var6) throws SlickException {
      try {
         Cursor var7 = CursorLoader.get().getAnimatedCursor(var1, var2, var3, var4, var5, var6);
         this.setMouseCursor(var7, var2, var3);
      } catch (IOException var8) {
         throw new SlickException("Failed to set mouse cursor", var8);
      } catch (LWJGLException var9) {
         throw new SlickException("Failed to set mouse cursor", var9);
      }
   }

   public abstract void setDefaultMouseCursor();

   public Input getInput() {
      return this.input;
   }

   public int getFPS() {
      return this.recordedFPS;
   }

   public abstract void setMouseGrabbed(boolean var1);

   public abstract boolean isMouseGrabbed();

   protected int getDelta() {
      long var1 = this.getTime();
      int var3 = (int)(var1 - this.lastFrame);
      this.lastFrame = var1;
      return var3;
   }

   protected void updateFPS() {
      if (this.getTime() - this.lastFPS > 1000L) {
         this.lastFPS = this.getTime();
         this.recordedFPS = this.fps;
         this.fps = 0;
      }

      ++this.fps;
   }

   public void setMinimumLogicUpdateInterval(int var1) {
      this.minimumLogicInterval = (long)var1;
   }

   public void setMaximumLogicUpdateInterval(int var1) {
      this.maximumLogicInterval = (long)var1;
   }

   protected void updateAndRender(int var1) throws SlickException {
      if (this.smoothDeltas && this.getFPS() != 0) {
         var1 = 1000 / this.getFPS();
      }

      this.input.poll(this.width, this.height);
      Music.poll(var1);
      if (!this.paused) {
         this.storedDelta += (long)var1;
         if (this.storedDelta >= this.minimumLogicInterval) {
            try {
               if (this.maximumLogicInterval != 0L) {
                  long var2 = this.storedDelta / this.maximumLogicInterval;

                  int var4;
                  for(var4 = 0; (long)var4 < var2; ++var4) {
                     this.game.update(this, (int)this.maximumLogicInterval);
                  }

                  var4 = (int)((long)var1 % this.maximumLogicInterval);
                  if ((long)var4 > this.minimumLogicInterval) {
                     this.game.update(this, (int)((long)var1 % this.maximumLogicInterval));
                     this.storedDelta = 0L;
                  } else {
                     this.storedDelta = (long)var4;
                  }
               } else {
                  this.game.update(this, (int)this.storedDelta);
                  this.storedDelta = 0L;
               }
            } catch (Throwable var6) {
               Log.error(var6);
               throw new SlickException("Game.update() failure - check the game code.");
            }
         }
      } else {
         this.game.update(this, 0);
      }

      if (this.hasFocus() || this.getAlwaysRender()) {
         if (this.clearEachFrame) {
            GL.glClear(16640);
         }

         GL.glLoadIdentity();
         this.graphics.resetFont();
         this.graphics.resetLineWidth();
         this.graphics.setAntiAlias(false);

         try {
            this.game.render(this, this.graphics);
         } catch (Throwable var5) {
            Log.error(var5);
            throw new SlickException("Game.render() failure - check the game code.");
         }

         this.graphics.resetTransform();
         if (this.showFPS) {
            this.defaultFont.drawString(10.0F, 10.0F, "FPS: " + this.recordedFPS);
         }

         GL.flush();
      }

      if (this.targetFPS != -1) {
         Display.sync(this.targetFPS);
      }

   }

   public void setUpdateOnlyWhenVisible(boolean var1) {
   }

   public boolean isUpdatingOnlyWhenVisible() {
      return true;
   }

   protected void initGL() {
      Log.info("Starting display " + this.width + "x" + this.height);
      GL.initDisplay(this.width, this.height);
      if (this.input == null) {
         this.input = new Input(this.height);
      }

      this.input.init(this.height);
      if (this.game instanceof InputListener) {
         this.input.removeListener((InputListener)this.game);
         this.input.addListener((InputListener)this.game);
      }

      if (this.graphics != null) {
         this.graphics.setDimensions(this.getWidth(), this.getHeight());
      }

      this.lastGame = this.game;
   }

   protected void initSystem() throws SlickException {
      this.initGL();
      this.setMusicVolume(1.0F);
      this.setSoundVolume(1.0F);
      this.graphics = new Graphics(this.width, this.height);
      this.defaultFont = this.graphics.getFont();
   }

   protected void enterOrtho() {
      this.enterOrtho(this.width, this.height);
   }

   public void setShowFPS(boolean var1) {
      this.showFPS = var1;
   }

   public boolean isShowingFPS() {
      return this.showFPS;
   }

   public void setTargetFrameRate(int var1) {
      this.targetFPS = var1;
   }

   public void setVSync(boolean var1) {
      this.vsync = var1;
      Display.setVSyncEnabled(var1);
   }

   public boolean isVSyncRequested() {
      return this.vsync;
   }

   protected boolean running() {
      return this.running;
   }

   public void setVerbose(boolean var1) {
      Log.setVerbose(var1);
   }

   public void exit() {
      this.running = false;
   }

   public abstract boolean hasFocus();

   public Graphics getGraphics() {
      return this.graphics;
   }

   protected void enterOrtho(int var1, int var2) {
      GL.enterOrtho(var1, var2);
   }
}
