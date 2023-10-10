package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Robot;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

final class MacOSXDisplay implements DisplayImplementation {
   private static final int PBUFFER_HANDLE_SIZE = 24;
   private static final int GAMMA_LENGTH = 256;
   private Canvas canvas;
   private Robot robot;
   private MacOSXMouseEventQueue mouse_queue;
   private KeyboardEventQueue keyboard_queue;
   private DisplayMode requested_mode;
   private MacOSXNativeMouse mouse;
   private MacOSXNativeKeyboard keyboard;
   private ByteBuffer window;
   private ByteBuffer context;
   private boolean skipViewportValue = false;
   private static final IntBuffer current_viewport = BufferUtils.createIntBuffer(16);
   private boolean mouseInsideWindow;
   private boolean close_requested;
   private boolean native_mode = true;
   private boolean updateNativeCursor = false;
   private long currentNativeCursor = 0L;
   private boolean enableHighDPI = false;
   private float scaleFactor = 1.0F;

   private native ByteBuffer nCreateWindow(int var1, int var2, int var3, int var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10, ByteBuffer var11, ByteBuffer var12) throws LWJGLException;

   private native Object nGetCurrentDisplayMode();

   private native void nGetDisplayModes(Object var1);

   private native boolean nIsMiniaturized(ByteBuffer var1);

   private native boolean nIsFocused(ByteBuffer var1);

   private native void nSetResizable(ByteBuffer var1, boolean var2);

   private native void nResizeWindow(ByteBuffer var1, int var2, int var3, int var4, int var5);

   private native boolean nWasResized(ByteBuffer var1);

   private native int nGetX(ByteBuffer var1);

   private native int nGetY(ByteBuffer var1);

   private native int nGetWidth(ByteBuffer var1);

   private native int nGetHeight(ByteBuffer var1);

   private native boolean nIsNativeMode(ByteBuffer var1);

   private static boolean isUndecorated() {
      return Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated");
   }

   public void createWindow(DrawableLWJGL var1, DisplayMode var2, Canvas var3, int var4, int var5) throws LWJGLException {
      boolean var6 = Display.isFullscreen();
      boolean var7 = Display.isResizable();
      boolean var8 = var3 != null && !var6;
      boolean var9 = LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7) && var3 == null && !Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.disableOSXFullscreenModeAPI");
      this.enableHighDPI = LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7) && var3 == null && (Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.enableHighDPI") || var6);
      if (var8) {
         this.canvas = var3;
      } else {
         this.canvas = null;
      }

      this.close_requested = false;
      DrawableGL var10 = (DrawableGL)Display.getDrawable();
      PeerInfo var11 = var10.peer_info;
      ByteBuffer var12 = var11.lockAndGetHandle();
      ByteBuffer var13 = var8 ? ((MacOSXCanvasPeerInfo)var11).window_handle : this.window;

      try {
         this.window = this.nCreateWindow(var4, var5, var2.getWidth(), var2.getHeight(), var6, isUndecorated(), var7, var8, var9, this.enableHighDPI, var12, var13);
         if (var6) {
            this.skipViewportValue = true;
            if (current_viewport.get(2) == 0 && current_viewport.get(3) == 0) {
               current_viewport.put(2, var2.getWidth());
               current_viewport.put(3, var2.getHeight());
            }
         }

         this.native_mode = this.nIsNativeMode(var12);
         if (!this.native_mode) {
            this.robot = AWTUtil.createRobot(this.canvas);
         }
      } catch (LWJGLException var16) {
         this.destroyWindow();
         throw var16;
      }

      var11.unlock();
   }

   public void doHandleQuit() {
      synchronized(this){}
      this.close_requested = true;
   }

   public void mouseInsideWindow(boolean var1) {
      synchronized(this){}
      this.mouseInsideWindow = var1;
      this.updateNativeCursor = true;
   }

   public void setScaleFactor(float var1) {
      synchronized(this){}
      this.scaleFactor = var1;
   }

   public native void nDestroyCALayer(ByteBuffer var1);

   public native void nDestroyWindow(ByteBuffer var1);

   public void destroyWindow() {
      if (!this.native_mode) {
         DrawableGL var1 = (DrawableGL)Display.getDrawable();
         PeerInfo var2 = var1.peer_info;
         if (var2 != null) {
            ByteBuffer var3 = var2.getHandle();
            this.nDestroyCALayer(var3);
         }

         this.robot = null;
      }

      this.nDestroyWindow(this.window);
   }

   public int getGammaRampLength() {
      return 256;
   }

   public native void setGammaRamp(FloatBuffer var1) throws LWJGLException;

   public String getAdapter() {
      return null;
   }

   public String getVersion() {
      return null;
   }

   public void switchDisplayMode(DisplayMode var1) throws LWJGLException {
      DisplayMode[] var2 = this.getAvailableDisplayModes();
      DisplayMode[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         DisplayMode var6 = var3[var5];
         if (var6 == var1) {
            this.requested_mode = var6;
            return;
         }
      }

      throw new LWJGLException(var1 + " is not supported");
   }

   public void resetDisplayMode() {
      this.requested_mode = null;
      this.restoreGamma();
   }

   private native void restoreGamma();

   public Object createDisplayMode(int var1, int var2, int var3, int var4) {
      return new DisplayMode(var1, var2, var3, var4);
   }

   public DisplayMode init() throws LWJGLException {
      return (DisplayMode)this.nGetCurrentDisplayMode();
   }

   public void addDisplayMode(Object var1, int var2, int var3, int var4, int var5) {
      List var6 = (List)var1;
      DisplayMode var7 = new DisplayMode(var2, var3, var4, var5);
      var6.add(var7);
   }

   public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
      ArrayList var1 = new ArrayList();
      this.nGetDisplayModes(var1);
      var1.add(Display.getDesktopDisplayMode());
      return (DisplayMode[])var1.toArray(new DisplayMode[var1.size()]);
   }

   private native void nSetTitle(ByteBuffer var1, ByteBuffer var2);

   public void setTitle(String var1) {
      ByteBuffer var2 = MemoryUtil.encodeUTF8(var1);
      this.nSetTitle(this.window, var2);
   }

   public boolean isCloseRequested() {
      synchronized(this){}
      boolean var1 = this.close_requested;
      this.close_requested = false;
      return var1;
   }

   public boolean isVisible() {
      return true;
   }

   public boolean isActive() {
      return this.native_mode ? this.nIsFocused(this.window) : Display.getParent().hasFocus();
   }

   public Canvas getCanvas() {
      return this.canvas;
   }

   public boolean isDirty() {
      return false;
   }

   public PeerInfo createPeerInfo(PixelFormat var1, ContextAttribs var2) throws LWJGLException {
      try {
         return new MacOSXDisplayPeerInfo(var1, var2, true);
      } catch (LWJGLException var4) {
         return new MacOSXDisplayPeerInfo(var1, var2, false);
      }
   }

   public void update() {
      boolean var1 = true;
      DrawableGL var2 = (DrawableGL)Display.getDrawable();
      if (var1) {
         var2.context.update();
         if (this.skipViewportValue) {
            this.skipViewportValue = false;
         } else {
            GL11.glGetInteger(2978, current_viewport);
         }

         GL11.glViewport(current_viewport.get(0), current_viewport.get(1), current_viewport.get(2), current_viewport.get(3));
      }

      if (this.native_mode && this.updateNativeCursor) {
         this.updateNativeCursor = false;

         try {
            this.setNativeCursor(this.currentNativeCursor);
         } catch (LWJGLException var4) {
            var4.printStackTrace();
         }
      }

   }

   public void reshape(int var1, int var2, int var3, int var4) {
   }

   public boolean hasWheel() {
      return AWTUtil.hasWheel();
   }

   public int getButtonCount() {
      return AWTUtil.getButtonCount();
   }

   public void createMouse() throws LWJGLException {
      if (this.native_mode) {
         this.mouse = new MacOSXNativeMouse(this, this.window);
         this.mouse.register();
      } else {
         this.mouse_queue = new MacOSXMouseEventQueue(this.canvas);
         this.mouse_queue.register();
      }

   }

   public void destroyMouse() {
      if (this.native_mode) {
         try {
            MacOSXNativeMouse.setCursor(0L);
         } catch (LWJGLException var2) {
         }

         this.grabMouse(false);
         if (this.mouse != null) {
            this.mouse.unregister();
         }

         this.mouse = null;
      } else {
         if (this.mouse_queue != null) {
            MacOSXMouseEventQueue.nGrabMouse(false);
            this.mouse_queue.unregister();
         }

         this.mouse_queue = null;
      }

   }

   public void pollMouse(IntBuffer var1, ByteBuffer var2) {
      if (this.native_mode) {
         this.mouse.poll(var1, var2);
      } else {
         this.mouse_queue.poll(var1, var2);
      }

   }

   public void readMouse(ByteBuffer var1) {
      if (this.native_mode) {
         this.mouse.copyEvents(var1);
      } else {
         this.mouse_queue.copyEvents(var1);
      }

   }

   public void grabMouse(boolean var1) {
      if (this.native_mode) {
         this.mouse.setGrabbed(var1);
      } else {
         this.mouse_queue.setGrabbed(var1);
      }

   }

   public int getNativeCursorCapabilities() {
      return this.native_mode ? 7 : AWTUtil.getNativeCursorCapabilities();
   }

   public void setCursorPosition(int var1, int var2) {
      if (this.native_mode && this.mouse != null) {
         this.mouse.setCursorPosition(var1, var2);
      }

   }

   public void setNativeCursor(Object var1) throws LWJGLException {
      if (this.native_mode) {
         this.currentNativeCursor = getCursorHandle(var1);
         if (Display.isCreated()) {
            if (this.mouseInsideWindow) {
               MacOSXNativeMouse.setCursor(this.currentNativeCursor);
            } else {
               MacOSXNativeMouse.setCursor(0L);
            }
         }
      }

   }

   public int getMinCursorSize() {
      return 1;
   }

   public int getMaxCursorSize() {
      DisplayMode var1 = Display.getDesktopDisplayMode();
      return Math.min(var1.getWidth(), var1.getHeight()) / 2;
   }

   public void createKeyboard() throws LWJGLException {
      if (this.native_mode) {
         this.keyboard = new MacOSXNativeKeyboard(this.window);
         this.keyboard.register();
      } else {
         this.keyboard_queue = new KeyboardEventQueue(this.canvas);
         this.keyboard_queue.register();
      }

   }

   public void destroyKeyboard() {
      if (this.native_mode) {
         if (this.keyboard != null) {
            this.keyboard.unregister();
         }

         this.keyboard = null;
      } else {
         if (this.keyboard_queue != null) {
            this.keyboard_queue.unregister();
         }

         this.keyboard_queue = null;
      }

   }

   public void pollKeyboard(ByteBuffer var1) {
      if (this.native_mode) {
         this.keyboard.poll(var1);
      } else {
         this.keyboard_queue.poll(var1);
      }

   }

   public void readKeyboard(ByteBuffer var1) {
      if (this.native_mode) {
         this.keyboard.copyEvents(var1);
      } else {
         this.keyboard_queue.copyEvents(var1);
      }

   }

   public Object createCursor(int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) throws LWJGLException {
      if (this.native_mode) {
         long var8 = MacOSXNativeMouse.createCursor(var1, var2, var3, var4, var5, var6, var7);
         return var8;
      } else {
         return AWTUtil.createCursor(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   public void destroyCursor(Object var1) {
      long var2 = getCursorHandle(var1);
      if (this.currentNativeCursor == var2) {
         this.currentNativeCursor = 0L;
      }

      MacOSXNativeMouse.destroyCursor(var2);
   }

   private static long getCursorHandle(Object var0) {
      return var0 != null ? (Long)var0 : 0L;
   }

   public int getPbufferCapabilities() {
      return 1;
   }

   public boolean isBufferLost(PeerInfo var1) {
      return false;
   }

   public PeerInfo createPbuffer(int var1, int var2, PixelFormat var3, ContextAttribs var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      return new MacOSXPbufferPeerInfo(var1, var2, var3, var4);
   }

   public void setPbufferAttrib(PeerInfo var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   public void bindTexImageToPbuffer(PeerInfo var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void releaseTexImageFromPbuffer(PeerInfo var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public int setIcon(ByteBuffer[] var1) {
      return 0;
   }

   public int getX() {
      return this.nGetX(this.window);
   }

   public int getY() {
      return this.nGetY(this.window);
   }

   public int getWidth() {
      return this.nGetWidth(this.window);
   }

   public int getHeight() {
      return this.nGetHeight(this.window);
   }

   public boolean isInsideWindow() {
      return this.mouseInsideWindow;
   }

   public void setResizable(boolean var1) {
      this.nSetResizable(this.window, var1);
   }

   public boolean wasResized() {
      return this.nWasResized(this.window);
   }

   public float getPixelScaleFactor() {
      return this.enableHighDPI && !Display.isFullscreen() ? this.scaleFactor : 1.0F;
   }
}
