package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class Display {
   private static final Thread shutdown_hook = new Thread() {
      public void run() {
         Display.access$000();
      }
   };
   private static final DisplayImplementation display_impl;
   private static final DisplayMode initial_mode;
   private static Canvas parent;
   private static DisplayMode current_mode;
   private static int x = -1;
   private static ByteBuffer[] cached_icons;
   private static int y = -1;
   private static int width = 0;
   private static int height = 0;
   private static String title = "Game";
   private static boolean fullscreen;
   private static int swap_interval;
   private static DrawableLWJGL drawable;
   private static boolean window_created;
   private static boolean parent_resized;
   private static boolean window_resized;
   private static boolean window_resizable;
   private static float r;
   private static float g;
   private static float b;
   private static final ComponentListener component_listener = new ComponentAdapter() {
      public void componentResized(ComponentEvent var1) {
         Object var2;
         synchronized(var2 = GlobalLock.lock){}
         Display.access$102(true);
      }
   };

   public static Drawable getDrawable() {
      return drawable;
   }

   private static DisplayImplementation createDisplayImplementation() {
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         return new LinuxDisplay();
      case 2:
         return new MacOSXDisplay();
      case 3:
         return new WindowsDisplay();
      default:
         throw new IllegalStateException("Unsupported platform");
      }
   }

   private Display() {
   }

   public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      DisplayMode[] var1 = display_impl.getAvailableDisplayModes();
      if (var1 == null) {
         return new DisplayMode[0];
      } else {
         HashSet var2 = new HashSet(var1.length);
         var2.addAll(Arrays.asList(var1));
         DisplayMode[] var3 = new DisplayMode[var2.size()];
         var2.toArray(var3);
         LWJGLUtil.log("Removed " + (var1.length - var3.length) + " duplicate displaymodes");
         return var3;
      }
   }

   public static DisplayMode getDesktopDisplayMode() {
      return initial_mode;
   }

   public static DisplayMode getDisplayMode() {
      return current_mode;
   }

   public static void setDisplayMode(DisplayMode param0) throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   private static DisplayMode getEffectiveMode() {
      // $FF: Couldn't be decompiled
   }

   private static int getWindowX() {
      // $FF: Couldn't be decompiled
   }

   private static int getWindowY() {
      // $FF: Couldn't be decompiled
   }

   private static void createWindow() throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   private static void releaseDrawable() {
      try {
         Context var0 = drawable.getContext();
         if (var0 != null && var0.isCurrent()) {
            var0.releaseCurrent();
            var0.releaseDrawable();
         }
      } catch (LWJGLException var1) {
         LWJGLUtil.log("Exception occurred while trying to release context: " + var1);
      }

   }

   private static void destroyWindow() {
      if (window_created) {
         if (parent != null) {
            parent.removeComponentListener(component_listener);
         }

         releaseDrawable();
         if (Mouse.isCreated()) {
            Mouse.destroy();
         }

         if (Keyboard.isCreated()) {
            Keyboard.destroy();
         }

         display_impl.destroyWindow();
         window_created = false;
      }
   }

   private static void switchDisplayMode() throws LWJGLException {
      if (!current_mode.isFullscreenCapable()) {
         throw new IllegalStateException("Only modes acquired from getAvailableDisplayModes() can be used for fullscreen display");
      } else {
         display_impl.switchDisplayMode(current_mode);
      }
   }

   public static void setDisplayConfiguration(float var0, float var1, float var2) throws LWJGLException {
      Object var3;
      synchronized(var3 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new LWJGLException("Display not yet created.");
      } else if (!(var1 < -1.0F) && !(var1 > 1.0F)) {
         if (var2 < 0.0F) {
            throw new IllegalArgumentException("Invalid contrast value");
         } else {
            int var4 = display_impl.getGammaRampLength();
            if (var4 == 0) {
               throw new LWJGLException("Display configuration not supported");
            } else {
               FloatBuffer var5 = BufferUtils.createFloatBuffer(var4);

               for(int var6 = 0; var6 < var4; ++var6) {
                  float var7 = (float)var6 / (float)(var4 - 1);
                  float var8 = (float)Math.pow((double)var7, (double)var0);
                  var8 += var1;
                  var8 = (var8 - 0.5F) * var2 + 0.5F;
                  if (var8 > 1.0F) {
                     var8 = 1.0F;
                  } else if (var8 < 0.0F) {
                     var8 = 0.0F;
                  }

                  var5.put(var6, var8);
               }

               display_impl.setGammaRamp(var5);
               LWJGLUtil.log("Gamma set, gamma = " + var0 + ", brightness = " + var1 + ", contrast = " + var2);
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid brightness value");
      }
   }

   public static void sync(int var0) {
      Sync.sync(var0);
   }

   public static String getTitle() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      return title;
   }

   public static Canvas getParent() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      return parent;
   }

   public static void setParent(Canvas param0) throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   public static void setFullscreen(boolean var0) throws LWJGLException {
      setDisplayModeAndFullscreenInternal(var0, current_mode);
   }

   public static void setDisplayModeAndFullscreen(DisplayMode var0) throws LWJGLException {
      setDisplayModeAndFullscreenInternal(var0.isFullscreenCapable(), var0);
   }

   private static void setDisplayModeAndFullscreenInternal(boolean param0, DisplayMode param1) throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   public static void setTitle(String var0) {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      if (var0 == null) {
         var0 = "";
      }

      title = var0;
      if (isCreated()) {
         display_impl.setTitle(title);
      }

   }

   public static boolean isCloseRequested() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Cannot determine close requested state of uncreated window");
      } else {
         return display_impl.isCloseRequested();
      }
   }

   public static boolean isVisible() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Cannot determine minimized state of uncreated window");
      } else {
         return display_impl.isVisible();
      }
   }

   public static boolean isActive() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Cannot determine focused state of uncreated window");
      } else {
         return display_impl.isActive();
      }
   }

   public static boolean isDirty() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Cannot determine dirty state of uncreated window");
      } else {
         return display_impl.isDirty();
      }
   }

   public static void processMessages() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Display not created");
      } else {
         display_impl.update();
         pollDevices();
      }
   }

   public static void swapBuffers() throws LWJGLException {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Display not created");
      } else {
         if (LWJGLUtil.DEBUG) {
            drawable.checkGLError();
         }

         drawable.swapBuffers();
      }
   }

   public static void update() {
      update(true);
   }

   public static void update(boolean param0) {
      // $FF: Couldn't be decompiled
   }

   static void pollDevices() {
      if (Mouse.isCreated()) {
         Mouse.poll();
         Mouse.updateCursor();
      }

      if (Keyboard.isCreated()) {
         Keyboard.poll();
      }

      if (Controllers.isCreated()) {
         Controllers.poll();
      }

   }

   public static void releaseContext() throws LWJGLException {
      drawable.releaseContext();
   }

   public static boolean isCurrent() throws LWJGLException {
      return drawable.isCurrent();
   }

   public static void makeCurrent() throws LWJGLException {
      drawable.makeCurrent();
   }

   private static void removeShutdownHook() {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            Runtime.getRuntime().removeShutdownHook(Display.access$200());
            return null;
         }
      });
   }

   private static void registerShutdownHook() {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            Runtime.getRuntime().addShutdownHook(Display.access$200());
            return null;
         }
      });
   }

   public static void create() throws LWJGLException {
      create(new PixelFormat());
   }

   public static void create(PixelFormat var0) throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      create((PixelFormat)var0, (Drawable)null, (ContextAttribs)((ContextAttribs)null));
   }

   public static void create(PixelFormat var0, Drawable var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = GlobalLock.lock){}
      create(var0, var1, (ContextAttribs)null);
   }

   public static void create(PixelFormat var0, ContextAttribs var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = GlobalLock.lock){}
      create((PixelFormat)var0, (Drawable)null, (ContextAttribs)var1);
   }

   public static void create(PixelFormat param0, Drawable param1, ContextAttribs param2) throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   public static void create(PixelFormatLWJGL var0) throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      create((PixelFormatLWJGL)var0, (Drawable)null, (org.lwjgl.opengles.ContextAttribs)null);
   }

   public static void create(PixelFormatLWJGL var0, Drawable var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = GlobalLock.lock){}
      create((PixelFormatLWJGL)var0, var1, (org.lwjgl.opengles.ContextAttribs)null);
   }

   public static void create(PixelFormatLWJGL var0, org.lwjgl.opengles.ContextAttribs var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = GlobalLock.lock){}
      create((PixelFormatLWJGL)var0, (Drawable)null, (org.lwjgl.opengles.ContextAttribs)var1);
   }

   public static void create(PixelFormatLWJGL param0, Drawable param1, org.lwjgl.opengles.ContextAttribs param2) throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   public static void setInitialBackground(float var0, float var1, float var2) {
      r = var0;
      g = var1;
      b = var2;
   }

   private static void makeCurrentAndSetSwapInterval() throws LWJGLException {
      makeCurrent();

      try {
         drawable.checkGLError();
      } catch (OpenGLException var1) {
         LWJGLUtil.log("OpenGL error during context creation: " + var1.getMessage());
      }

      setSwapInterval(swap_interval);
   }

   private static void initContext() {
      drawable.initContext(r, g, b);
      update();
   }

   static DisplayImplementation getImplementation() {
      return display_impl;
   }

   static boolean getPrivilegedBoolean(String var0) {
      return (Boolean)AccessController.doPrivileged(new PrivilegedAction(var0) {
         final String val$property_name;

         {
            this.val$property_name = var1;
         }

         public Boolean run() {
            return Boolean.getBoolean(this.val$property_name);
         }

         public Object run() {
            return this.run();
         }
      });
   }

   static String getPrivilegedString(String var0) {
      return (String)AccessController.doPrivileged(new PrivilegedAction(var0) {
         final String val$property_name;

         {
            this.val$property_name = var1;
         }

         public String run() {
            return System.getProperty(this.val$property_name);
         }

         public Object run() {
            return this.run();
         }
      });
   }

   private static void initControls() {
      if (!getPrivilegedBoolean("org.lwjgl.opengl.Display.noinput")) {
         if (!Mouse.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nomouse")) {
            try {
               Mouse.create();
            } catch (LWJGLException var2) {
               if (LWJGLUtil.DEBUG) {
                  var2.printStackTrace(System.err);
               } else {
                  LWJGLUtil.log("Failed to create Mouse: " + var2);
               }
            }
         }

         if (!Keyboard.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nokeyboard")) {
            try {
               Keyboard.create();
            } catch (LWJGLException var1) {
               if (LWJGLUtil.DEBUG) {
                  var1.printStackTrace(System.err);
               } else {
                  LWJGLUtil.log("Failed to create Keyboard: " + var1);
               }
            }
         }
      }

   }

   public static void destroy() {
      if (isCreated()) {
         drawable.destroy();
      }

   }

   private static void reset() {
      display_impl.resetDisplayMode();
      current_mode = initial_mode;
   }

   public static boolean isCreated() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      return window_created;
   }

   public static void setSwapInterval(int var0) {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      swap_interval = var0;
      if (isCreated()) {
         drawable.setSwapInterval(swap_interval);
      }

   }

   public static void setVSyncEnabled(boolean var0) {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      setSwapInterval(var0 ? 1 : 0);
   }

   public static void setLocation(int param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   private static void reshape() {
      DisplayMode var0 = getEffectiveMode();
      display_impl.reshape(getWindowX(), getWindowY(), var0.getWidth(), var0.getHeight());
   }

   public static String getAdapter() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      return display_impl.getAdapter();
   }

   public static String getVersion() {
      Object var0;
      synchronized(var0 = GlobalLock.lock){}
      return display_impl.getVersion();
   }

   public static int setIcon(ByteBuffer[] var0) {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      if (cached_icons != var0) {
         cached_icons = new ByteBuffer[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            cached_icons[var2] = BufferUtils.createByteBuffer(var0[var2].capacity());
            int var3 = var0[var2].position();
            cached_icons[var2].put(var0[var2]);
            var0[var2].position(var3);
            cached_icons[var2].flip();
         }
      }

      return isCreated() && parent == null ? display_impl.setIcon(cached_icons) : 0;
   }

   public static void setResizable(boolean var0) {
      window_resizable = var0;
      if (isCreated()) {
         display_impl.setResizable(var0);
      }

   }

   public static boolean isResizable() {
      return window_resizable;
   }

   public static boolean wasResized() {
      return window_resized;
   }

   public static int getX() {
      // $FF: Couldn't be decompiled
   }

   public static int getY() {
      // $FF: Couldn't be decompiled
   }

   public static int getWidth() {
      // $FF: Couldn't be decompiled
   }

   public static int getHeight() {
      // $FF: Couldn't be decompiled
   }

   public static float getPixelScaleFactor() {
      return display_impl.getPixelScaleFactor();
   }

   static void access$000() {
      reset();
   }

   static boolean access$102(boolean var0) {
      parent_resized = var0;
      return var0;
   }

   static Thread access$200() {
      return shutdown_hook;
   }

   static void access$300() {
      releaseDrawable();
   }

   static void access$400() {
      destroyWindow();
   }

   static int access$502(int var0) {
      x = var0;
      return var0;
   }

   static int access$602(int var0) {
      y = var0;
      return var0;
   }

   static ByteBuffer[] access$702(ByteBuffer[] var0) {
      cached_icons = var0;
      return var0;
   }

   static void access$800() {
      removeShutdownHook();
   }

   static {
      Sys.initialize();
      display_impl = createDisplayImplementation();

      try {
         current_mode = initial_mode = display_impl.init();
         LWJGLUtil.log("Initial mode: " + initial_mode);
      } catch (LWJGLException var1) {
         throw new RuntimeException(var1);
      }
   }
}
