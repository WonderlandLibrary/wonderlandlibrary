package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

final class LinuxDisplay implements DisplayImplementation {
   public static final int CurrentTime = 0;
   public static final int GrabSuccess = 0;
   public static final int AutoRepeatModeOff = 0;
   public static final int AutoRepeatModeOn = 1;
   public static final int AutoRepeatModeDefault = 2;
   public static final int None = 0;
   private static final int KeyPressMask = 1;
   private static final int KeyReleaseMask = 2;
   private static final int ButtonPressMask = 4;
   private static final int ButtonReleaseMask = 8;
   private static final int NotifyAncestor = 0;
   private static final int NotifyNonlinear = 3;
   private static final int NotifyPointer = 5;
   private static final int NotifyPointerRoot = 6;
   private static final int NotifyDetailNone = 7;
   private static final int SetModeInsert = 0;
   private static final int SaveSetRoot = 1;
   private static final int SaveSetUnmap = 1;
   private static final int X_SetInputFocus = 42;
   private static final int FULLSCREEN_LEGACY = 1;
   private static final int FULLSCREEN_NETWM = 2;
   private static final int WINDOWED = 3;
   private static int current_window_mode = 3;
   private static final int XRANDR = 10;
   private static final int XF86VIDMODE = 11;
   private static final int NONE = 12;
   private static long display;
   private static long current_window;
   private static long saved_error_handler;
   private static int display_connection_usage_count;
   private final LinuxEvent event_buffer = new LinuxEvent();
   private final LinuxEvent tmp_event_buffer = new LinuxEvent();
   private int current_displaymode_extension = 12;
   private long delete_atom;
   private PeerInfo peer_info;
   private ByteBuffer saved_gamma;
   private ByteBuffer current_gamma;
   private DisplayMode saved_mode;
   private DisplayMode current_mode;
   private XRandR.Screen[] savedXrandrConfig;
   private boolean keyboard_grabbed;
   private boolean pointer_grabbed;
   private boolean input_released;
   private boolean grab;
   private boolean focused;
   private boolean minimized;
   private boolean dirty;
   private boolean close_requested;
   private long current_cursor;
   private long blank_cursor;
   private boolean mouseInside = true;
   private boolean resizable;
   private boolean resized;
   private int window_x;
   private int window_y;
   private int window_width;
   private int window_height;
   private Canvas parent;
   private long parent_window;
   private static boolean xembedded;
   private long parent_proxy_focus_window;
   private boolean parent_focused;
   private boolean parent_focus_changed;
   private long last_window_focus = 0L;
   private LinuxKeyboard keyboard;
   private LinuxMouse mouse;
   private String wm_class;
   private final FocusListener focus_listener = new FocusListener(this) {
      final LinuxDisplay this$0;

      {
         this.this$0 = var1;
      }

      public void focusGained(FocusEvent var1) {
         Object var2;
         synchronized(var2 = GlobalLock.lock){}
         LinuxDisplay.access$002(this.this$0, true);
         LinuxDisplay.access$102(this.this$0, true);
      }

      public void focusLost(FocusEvent var1) {
         Object var2;
         synchronized(var2 = GlobalLock.lock){}
         LinuxDisplay.access$002(this.this$0, false);
         LinuxDisplay.access$102(this.this$0, true);
      }
   };

   private static ByteBuffer getCurrentGammaRamp() throws LWJGLException {
      lockAWT();
      incDisplay();
      ByteBuffer var0;
      if (isXF86VidModeSupported()) {
         var0 = nGetCurrentGammaRamp(getDisplay(), getDefaultScreen());
         decDisplay();
         unlockAWT();
         return var0;
      } else {
         var0 = null;
         decDisplay();
         unlockAWT();
         return var0;
      }
   }

   private static native ByteBuffer nGetCurrentGammaRamp(long var0, int var2) throws LWJGLException;

   private static int getBestDisplayModeExtension() {
      // $FF: Couldn't be decompiled
   }

   private static native boolean nIsXrandrSupported(long var0) throws LWJGLException;

   private static boolean isXF86VidModeSupported() {
      lockAWT();

      boolean var0;
      try {
         incDisplay();
         var0 = nIsXF86VidModeSupported(getDisplay());
         decDisplay();
      } catch (LWJGLException var3) {
         LWJGLUtil.log("Got exception while querying XF86VM support: " + var3);
         boolean var1 = false;
         unlockAWT();
         return var1;
      }

      unlockAWT();
      return var0;
   }

   private static native boolean nIsXF86VidModeSupported(long var0) throws LWJGLException;

   private static native boolean nIsNetWMFullscreenSupported(long var0, int var2) throws LWJGLException;

   static void lockAWT() {
      try {
         nLockAWT();
      } catch (LWJGLException var1) {
         LWJGLUtil.log("Caught exception while locking AWT: " + var1);
      }

   }

   private static native void nLockAWT() throws LWJGLException;

   static void unlockAWT() {
      try {
         nUnlockAWT();
      } catch (LWJGLException var1) {
         LWJGLUtil.log("Caught exception while unlocking AWT: " + var1);
      }

   }

   private static native void nUnlockAWT() throws LWJGLException;

   static void incDisplay() throws LWJGLException {
      if (display_connection_usage_count == 0) {
         try {
            GLContext.loadOpenGLLibrary();
            org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
         } catch (Throwable var1) {
         }

         saved_error_handler = setErrorHandler();
         display = openDisplay();
      }

      ++display_connection_usage_count;
   }

   private static native int callErrorHandler(long var0, long var2, long var4);

   private static native long setErrorHandler();

   private static native long resetErrorHandler(long var0);

   private static native void synchronize(long var0, boolean var2);

   private static int globalErrorHandler(long var0, long var2, long var4, long var6, long var8, long var10, long var12) throws LWJGLException {
      if (xembedded && var10 == 42L) {
         return 0;
      } else if (var0 == getDisplay()) {
         String var14 = getErrorText(var0, var8);
         throw new LWJGLException("X Error - disp: 0x" + Long.toHexString(var4) + " serial: " + var6 + " error: " + var14 + " request_code: " + var10 + " minor_code: " + var12);
      } else {
         return 0;
      }
   }

   private static native String getErrorText(long var0, long var2);

   static void decDisplay() {
   }

   static native long openDisplay() throws LWJGLException;

   static native void closeDisplay(long var0);

   private int getWindowMode(boolean param1) throws LWJGLException {
      // $FF: Couldn't be decompiled
   }

   static long getDisplay() {
      if (display_connection_usage_count <= 0) {
         throw new InternalError("display_connection_usage_count = " + display_connection_usage_count);
      } else {
         return display;
      }
   }

   static int getDefaultScreen() {
      return nGetDefaultScreen(getDisplay());
   }

   static native int nGetDefaultScreen(long var0);

   static long getWindow() {
      return current_window;
   }

   private void ungrabKeyboard() {
      if (this.keyboard_grabbed) {
         nUngrabKeyboard(getDisplay());
         this.keyboard_grabbed = false;
      }

   }

   static native int nUngrabKeyboard(long var0);

   private void grabKeyboard() {
      if (!this.keyboard_grabbed) {
         int var1 = nGrabKeyboard(getDisplay(), getWindow());
         if (var1 == 0) {
            this.keyboard_grabbed = true;
         }
      }

   }

   static native int nGrabKeyboard(long var0, long var2);

   private void grabPointer() {
      // $FF: Couldn't be decompiled
   }

   static native int nGrabPointer(long var0, long var2, long var4);

   private static native void nSetViewPort(long var0, long var2, int var4);

   private void ungrabPointer() {
      if (this.pointer_grabbed) {
         this.pointer_grabbed = false;
         nUngrabPointer(getDisplay());
      }

   }

   static native int nUngrabPointer(long var0);

   private void updatePointerGrab() {
      // $FF: Couldn't be decompiled
   }

   private void updateCursor() {
      long var1;
      if (this == false) {
         var1 = this.blank_cursor;
      } else {
         var1 = this.current_cursor;
      }

      nDefineCursor(getDisplay(), getWindow(), var1);
   }

   private static native void nDefineCursor(long var0, long var2, long var4);

   private void updateKeyboardGrab() {
      // $FF: Couldn't be decompiled
   }

   public void createWindow(DrawableLWJGL var1, DisplayMode var2, Canvas var3, int var4, int var5) throws LWJGLException {
      lockAWT();
      incDisplay();

      try {
         if (var1 instanceof DrawableGLES) {
            this.peer_info = new LinuxDisplayPeerInfo();
         }

         ByteBuffer var6 = this.peer_info.lockAndGetHandle();
         current_window_mode = this.getWindowMode(Display.isFullscreen());
         if (current_window_mode != 3) {
            LinuxDisplay.Compiz.setLegacyFullscreenSupport(true);
         }

         boolean var7 = Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated") || current_window_mode != 3 && Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated_fs");
         this.parent = var3;
         this.parent_window = var3 != null ? getHandle(var3) : getRootWindow(getDisplay(), getDefaultScreen());
         this.resizable = Display.isResizable();
         this.resized = false;
         this.window_x = var4;
         this.window_y = var5;
         this.window_width = var2.getWidth();
         this.window_height = var2.getHeight();
         current_window = nCreateWindow(getDisplay(), getDefaultScreen(), var6, var2, current_window_mode, var4, var5, var7, this.parent_window, this.resizable);
         this.wm_class = Display.getPrivilegedString("LWJGL_WM_CLASS");
         if (this.wm_class == null) {
            this.wm_class = Display.getTitle();
         }

         this.setClassHint(Display.getTitle(), this.wm_class);
         mapRaised(getDisplay(), current_window);
         xembedded = var3 != null && this.parent_window != false;
         this.blank_cursor = createBlankCursor();
         this.current_cursor = 0L;
         this.focused = false;
         this.input_released = false;
         this.pointer_grabbed = false;
         this.keyboard_grabbed = false;
         this.close_requested = false;
         this.grab = false;
         this.minimized = false;
         this.dirty = true;
         if (var1 instanceof DrawableGLES) {
            ((DrawableGLES)var1).initialize(current_window, getDisplay(), 4, (org.lwjgl.opengles.PixelFormat)var1.getPixelFormat());
         }

         if (var3 != null) {
            var3.addFocusListener(this.focus_listener);
            this.parent_focused = var3.isFocusOwner();
            this.parent_focus_changed = true;
         }

         this.peer_info.unlock();
      } catch (LWJGLException var10) {
         decDisplay();
         throw var10;
      }

      unlockAWT();
   }

   private static native long nCreateWindow(long var0, int var2, ByteBuffer var3, DisplayMode var4, int var5, int var6, int var7, boolean var8, long var9, boolean var11) throws LWJGLException;

   private static native long getRootWindow(long var0, int var2);

   private static native boolean hasProperty(long var0, long var2, long var4);

   private static native long getParentWindow(long var0, long var2) throws LWJGLException;

   private static native int getChildCount(long var0, long var2) throws LWJGLException;

   private static native void mapRaised(long var0, long var2);

   private static native void reparentWindow(long var0, long var2, long var4, int var6, int var7);

   private static native long nGetInputFocus(long var0) throws LWJGLException;

   private static native void nSetInputFocus(long var0, long var2, long var4);

   private static native void nSetWindowSize(long var0, long var2, int var4, int var5, boolean var6);

   private static native int nGetX(long var0, long var2);

   private static native int nGetY(long var0, long var2);

   private static native int nGetWidth(long var0, long var2);

   private static native int nGetHeight(long var0, long var2);

   private static long getHandle(Canvas var0) throws LWJGLException {
      AWTCanvasImplementation var1 = AWTGLCanvas.createImplementation();
      LinuxPeerInfo var2 = (LinuxPeerInfo)var1.createPeerInfo(var0, (PixelFormat)null, (ContextAttribs)null);
      ByteBuffer var3 = var2.lockAndGetHandle();
      long var4 = var2.getDrawable();
      var2.unlock();
      return var4;
   }

   private void updateInputGrab() {
      this.updatePointerGrab();
      this.updateKeyboardGrab();
   }

   public void destroyWindow() {
      lockAWT();
      if (this.parent != null) {
         this.parent.removeFocusListener(this.focus_listener);
      }

      try {
         this.setNativeCursor((Object)null);
      } catch (LWJGLException var3) {
         LWJGLUtil.log("Failed to reset cursor: " + var3.getMessage());
      }

      nDestroyCursor(getDisplay(), this.blank_cursor);
      this.blank_cursor = 0L;
      this.ungrabKeyboard();
      nDestroyWindow(getDisplay(), getWindow());
      decDisplay();
      if (current_window_mode != 3) {
         LinuxDisplay.Compiz.setLegacyFullscreenSupport(false);
      }

      unlockAWT();
   }

   static native void nDestroyWindow(long var0, long var2);

   public void switchDisplayMode(DisplayMode var1) throws LWJGLException {
      lockAWT();
      this.switchDisplayModeOnTmpDisplay(var1);
      this.current_mode = var1;
      unlockAWT();
   }

   private void switchDisplayModeOnTmpDisplay(DisplayMode var1) throws LWJGLException {
      incDisplay();
      nSwitchDisplayMode(getDisplay(), getDefaultScreen(), this.current_displaymode_extension, var1);
      decDisplay();
   }

   private static native void nSwitchDisplayMode(long var0, int var2, int var3, DisplayMode var4) throws LWJGLException;

   private static long internAtom(String var0, boolean var1) throws LWJGLException {
      incDisplay();
      long var2 = nInternAtom(getDisplay(), var0, var1);
      decDisplay();
      return var2;
   }

   static native long nInternAtom(long var0, String var2, boolean var3);

   public void resetDisplayMode() {
      lockAWT();

      try {
         if (this.current_displaymode_extension == 10 && this.savedXrandrConfig.length > 0) {
            AccessController.doPrivileged(new PrivilegedAction(this) {
               final LinuxDisplay this$0;

               {
                  this.this$0 = var1;
               }

               public Object run() {
                  XRandR.setConfiguration(LinuxDisplay.access$200(this.this$0));
                  return null;
               }
            });
         } else {
            this.switchDisplayMode(this.saved_mode);
         }

         if (isXF86VidModeSupported()) {
            this.doSetGamma(this.saved_gamma);
         }

         LinuxDisplay.Compiz.setLegacyFullscreenSupport(false);
      } catch (LWJGLException var3) {
         LWJGLUtil.log("Caught exception while resetting mode: " + var3);
         unlockAWT();
         return;
      }

      unlockAWT();
   }

   public int getGammaRampLength() {
      if (!isXF86VidModeSupported()) {
         return 0;
      } else {
         lockAWT();

         int var1;
         label26: {
            byte var2;
            try {
               label32: {
                  incDisplay();

                  try {
                     var1 = nGetGammaRampLength(getDisplay(), getDefaultScreen());
                  } catch (LWJGLException var5) {
                     LWJGLUtil.log("Got exception while querying gamma length: " + var5);
                     var2 = 0;
                     decDisplay();
                     break label32;
                  }

                  decDisplay();
                  break label26;
               }
            } catch (LWJGLException var6) {
               LWJGLUtil.log("Failed to get gamma ramp length: " + var6);
               var2 = 0;
               unlockAWT();
               return var2;
            }

            unlockAWT();
            return var2;
         }

         unlockAWT();
         return var1;
      }
   }

   private static native int nGetGammaRampLength(long var0, int var2) throws LWJGLException;

   public void setGammaRamp(FloatBuffer var1) throws LWJGLException {
      if (!isXF86VidModeSupported()) {
         throw new LWJGLException("No gamma ramp support (Missing XF86VM extension)");
      } else {
         this.doSetGamma(convertToNativeRamp(var1));
      }
   }

   private void doSetGamma(ByteBuffer var1) throws LWJGLException {
      lockAWT();
      setGammaRampOnTmpDisplay(var1);
      this.current_gamma = var1;
      unlockAWT();
   }

   private static void setGammaRampOnTmpDisplay(ByteBuffer var0) throws LWJGLException {
      incDisplay();
      nSetGammaRamp(getDisplay(), getDefaultScreen(), var0);
      decDisplay();
   }

   private static native void nSetGammaRamp(long var0, int var2, ByteBuffer var3) throws LWJGLException;

   private static ByteBuffer convertToNativeRamp(FloatBuffer var0) throws LWJGLException {
      return nConvertToNativeRamp(var0, var0.position(), var0.remaining());
   }

   private static native ByteBuffer nConvertToNativeRamp(FloatBuffer var0, int var1, int var2) throws LWJGLException;

   public String getAdapter() {
      return null;
   }

   public String getVersion() {
      return null;
   }

   public DisplayMode init() throws LWJGLException {
      lockAWT();
      LinuxDisplay.Compiz.init();
      this.delete_atom = internAtom("WM_DELETE_WINDOW", false);
      this.current_displaymode_extension = getBestDisplayModeExtension();
      if (this.current_displaymode_extension == 12) {
         throw new LWJGLException("No display mode extension is available");
      } else {
         DisplayMode[] var1 = this.getAvailableDisplayModes();
         if (var1 != null && var1.length != 0) {
            switch(this.current_displaymode_extension) {
            case 10:
               this.savedXrandrConfig = (XRandR.Screen[])AccessController.doPrivileged(new PrivilegedAction(this) {
                  final LinuxDisplay this$0;

                  {
                     this.this$0 = var1;
                  }

                  public XRandR.Screen[] run() {
                     return XRandR.getConfiguration();
                  }

                  public Object run() {
                     return this.run();
                  }
               });
               this.saved_mode = getCurrentXRandrMode();
               break;
            case 11:
               this.saved_mode = var1[0];
               break;
            default:
               throw new LWJGLException("Unknown display mode extension: " + this.current_displaymode_extension);
            }

            this.current_mode = this.saved_mode;
            this.saved_gamma = getCurrentGammaRamp();
            this.current_gamma = this.saved_gamma;
            DisplayMode var2 = this.saved_mode;
            unlockAWT();
            return var2;
         } else {
            throw new LWJGLException("No modes available");
         }
      }
   }

   private static DisplayMode getCurrentXRandrMode() throws LWJGLException {
      lockAWT();
      incDisplay();
      DisplayMode var0 = nGetCurrentXRandrMode(getDisplay(), getDefaultScreen());
      decDisplay();
      unlockAWT();
      return var0;
   }

   private static native DisplayMode nGetCurrentXRandrMode(long var0, int var2) throws LWJGLException;

   public void setTitle(String var1) {
      lockAWT();
      ByteBuffer var2 = MemoryUtil.encodeUTF8(var1);
      nSetTitle(getDisplay(), getWindow(), MemoryUtil.getAddress(var2), var2.remaining() - 1);
      unlockAWT();
      if (Display.isCreated()) {
         this.setClassHint(var1, this.wm_class);
      }

   }

   private static native void nSetTitle(long var0, long var2, long var4, int var6);

   private void setClassHint(String var1, String var2) {
      lockAWT();
      ByteBuffer var3 = MemoryUtil.encodeUTF8(var1);
      ByteBuffer var4 = MemoryUtil.encodeUTF8(var2);
      nSetClassHint(getDisplay(), getWindow(), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4));
      unlockAWT();
   }

   private static native void nSetClassHint(long var0, long var2, long var4, long var6);

   public boolean isCloseRequested() {
      boolean var1 = this.close_requested;
      this.close_requested = false;
      return var1;
   }

   public boolean isVisible() {
      return !this.minimized;
   }

   public boolean isActive() {
      // $FF: Couldn't be decompiled
   }

   public boolean isDirty() {
      boolean var1 = this.dirty;
      this.dirty = false;
      return var1;
   }

   public PeerInfo createPeerInfo(PixelFormat var1, ContextAttribs var2) throws LWJGLException {
      this.peer_info = new LinuxDisplayPeerInfo(var1);
      return this.peer_info;
   }

   private void relayEventToParent(LinuxEvent var1, int var2) {
      this.tmp_event_buffer.copyFrom(var1);
      this.tmp_event_buffer.setWindow(this.parent_window);
      this.tmp_event_buffer.sendEvent(getDisplay(), this.parent_window, true, (long)var2);
   }

   private void relayEventToParent(LinuxEvent var1) {
      if (this.parent != null) {
         switch(var1.getType()) {
         case 2:
            this.relayEventToParent(var1, 1);
            break;
         case 3:
            this.relayEventToParent(var1, 1);
            break;
         case 4:
            if (xembedded || !this.focused) {
               this.relayEventToParent(var1, 1);
            }
            break;
         case 5:
            if (xembedded || !this.focused) {
               this.relayEventToParent(var1, 1);
            }
         }

      }
   }

   private void processEvents() {
      while(LinuxEvent.getPending(getDisplay()) > 0) {
         this.event_buffer.nextEvent(getDisplay());
         long var1 = this.event_buffer.getWindow();
         this.relayEventToParent(this.event_buffer);
         if (var1 == getWindow() && !this.event_buffer.filterEvent(var1) && (this.mouse == null || !this.mouse.filterEvent(this.grab, this.shouldWarpPointer(), this.event_buffer)) && (this.keyboard == null || !this.keyboard.filterEvent(this.event_buffer))) {
            switch(this.event_buffer.getType()) {
            case 7:
               this.mouseInside = true;
               break;
            case 8:
               this.mouseInside = false;
               break;
            case 9:
               this.setFocused(true, this.event_buffer.getFocusDetail());
               break;
            case 10:
               this.setFocused(false, this.event_buffer.getFocusDetail());
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 20:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            default:
               break;
            case 12:
               this.dirty = true;
               break;
            case 18:
               this.dirty = true;
               this.minimized = true;
               break;
            case 19:
               this.dirty = true;
               this.minimized = false;
               break;
            case 22:
               int var3 = nGetX(getDisplay(), getWindow());
               int var4 = nGetY(getDisplay(), getWindow());
               int var5 = nGetWidth(getDisplay(), getWindow());
               int var6 = nGetHeight(getDisplay(), getWindow());
               this.window_x = var3;
               this.window_y = var4;
               if (this.window_width != var5 || this.window_height != var6) {
                  this.resized = true;
                  this.window_width = var5;
                  this.window_height = var6;
               }
               break;
            case 33:
               if (this.event_buffer.getClientFormat() == 32 && (long)this.event_buffer.getClientData(0) == this.delete_atom) {
                  this.close_requested = true;
               }
            }
         }
      }

   }

   public void update() {
      lockAWT();
      this.processEvents();
      this.checkInput();
      unlockAWT();
   }

   public void reshape(int var1, int var2, int var3, int var4) {
      lockAWT();
      nReshape(getDisplay(), getWindow(), var1, var2, var3, var4);
      unlockAWT();
   }

   private static native void nReshape(long var0, long var2, int var4, int var5, int var6, int var7);

   public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
      lockAWT();
      incDisplay();
      DisplayMode[] var1 = nGetAvailableDisplayModes(getDisplay(), getDefaultScreen(), this.current_displaymode_extension);
      decDisplay();
      unlockAWT();
      return var1;
   }

   private static native DisplayMode[] nGetAvailableDisplayModes(long var0, int var2, int var3) throws LWJGLException;

   public boolean hasWheel() {
      return true;
   }

   public int getButtonCount() {
      return this.mouse.getButtonCount();
   }

   public void createMouse() throws LWJGLException {
      lockAWT();
      this.mouse = new LinuxMouse(getDisplay(), getWindow(), getWindow());
      unlockAWT();
   }

   public void destroyMouse() {
      this.mouse = null;
      this.updateInputGrab();
   }

   public void pollMouse(IntBuffer var1, ByteBuffer var2) {
      lockAWT();
      this.mouse.poll(this.grab, var1, var2);
      unlockAWT();
   }

   public void readMouse(ByteBuffer var1) {
      lockAWT();
      this.mouse.read(var1);
      unlockAWT();
   }

   public void setCursorPosition(int var1, int var2) {
      lockAWT();
      this.mouse.setCursorPosition(var1, var2);
      unlockAWT();
   }

   private void checkInput() {
      if (this.parent != null) {
         if (xembedded) {
            long var1 = 0L;
            if (this.last_window_focus != var1 || this.parent_focused != this.focused) {
               if (var1 == false) {
                  if (this.parent_focused) {
                     nSetInputFocus(getDisplay(), current_window, 0L);
                     this.last_window_focus = current_window;
                     this.focused = true;
                  } else {
                     nSetInputFocus(getDisplay(), this.parent_proxy_focus_window, 0L);
                     this.last_window_focus = this.parent_proxy_focus_window;
                     this.focused = false;
                  }
               } else {
                  this.last_window_focus = var1;
                  this.focused = false;
               }
            }
         } else if (this.parent_focus_changed && this.parent_focused) {
            this.setInputFocusUnsafe(getWindow());
            this.parent_focus_changed = false;
         }

      }
   }

   private void setInputFocusUnsafe(long var1) {
      try {
         nSetInputFocus(getDisplay(), var1, 0L);
         nSync(getDisplay(), false);
      } catch (LWJGLException var4) {
         LWJGLUtil.log("Got exception while trying to focus: " + var4);
      }

   }

   private static native void nSync(long var0, boolean var2) throws LWJGLException;

   private void setFocused(boolean var1, int var2) {
      if (this.focused != var1 && var2 != 7 && var2 != 5 && var2 != 6 && !xembedded) {
         this.focused = var1;
         if (this.focused) {
            this.acquireInput();
         } else {
            this.releaseInput();
         }

      }
   }

   private void releaseInput() {
      // $FF: Couldn't be decompiled
   }

   private static native void nIconifyWindow(long var0, long var2, int var4);

   private void acquireInput() {
      // $FF: Couldn't be decompiled
   }

   public void grabMouse(boolean var1) {
      lockAWT();
      if (var1 != this.grab) {
         this.grab = var1;
         this.updateInputGrab();
         this.mouse.changeGrabbed(this.grab, this.shouldWarpPointer());
      }

      unlockAWT();
   }

   private boolean shouldWarpPointer() {
      return this.pointer_grabbed && this == false;
   }

   public int getNativeCursorCapabilities() {
      lockAWT();

      int var1;
      try {
         incDisplay();
         var1 = nGetNativeCursorCapabilities(getDisplay());
         decDisplay();
      } catch (LWJGLException var4) {
         throw new RuntimeException(var4);
      }

      unlockAWT();
      return var1;
   }

   private static native int nGetNativeCursorCapabilities(long var0) throws LWJGLException;

   public void setNativeCursor(Object var1) throws LWJGLException {
      this.current_cursor = getCursorHandle(var1);
      lockAWT();
      this.updateCursor();
      unlockAWT();
   }

   public int getMinCursorSize() {
      lockAWT();

      int var1;
      try {
         incDisplay();
         var1 = nGetMinCursorSize(getDisplay(), getWindow());
         decDisplay();
      } catch (LWJGLException var4) {
         LWJGLUtil.log("Exception occurred in getMinCursorSize: " + var4);
         byte var2 = 0;
         unlockAWT();
         return var2;
      }

      unlockAWT();
      return var1;
   }

   private static native int nGetMinCursorSize(long var0, long var2);

   public int getMaxCursorSize() {
      lockAWT();

      int var1;
      try {
         incDisplay();
         var1 = nGetMaxCursorSize(getDisplay(), getWindow());
         decDisplay();
      } catch (LWJGLException var4) {
         LWJGLUtil.log("Exception occurred in getMaxCursorSize: " + var4);
         byte var2 = 0;
         unlockAWT();
         return var2;
      }

      unlockAWT();
      return var1;
   }

   private static native int nGetMaxCursorSize(long var0, long var2);

   public void createKeyboard() throws LWJGLException {
      lockAWT();
      this.keyboard = new LinuxKeyboard(getDisplay(), getWindow());
      unlockAWT();
   }

   public void destroyKeyboard() {
      lockAWT();
      this.keyboard.destroy(getDisplay());
      this.keyboard = null;
      unlockAWT();
   }

   public void pollKeyboard(ByteBuffer var1) {
      lockAWT();
      this.keyboard.poll(var1);
      unlockAWT();
   }

   public void readKeyboard(ByteBuffer var1) {
      lockAWT();
      this.keyboard.read(var1);
      unlockAWT();
   }

   private static native long nCreateCursor(long var0, int var2, int var3, int var4, int var5, int var6, IntBuffer var7, int var8, IntBuffer var9, int var10) throws LWJGLException;

   private static long createBlankCursor() {
      return nCreateBlankCursor(getDisplay(), getWindow());
   }

   static native long nCreateBlankCursor(long var0, long var2);

   public Object createCursor(int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) throws LWJGLException {
      lockAWT();
      incDisplay();

      Long var10;
      try {
         long var8 = nCreateCursor(getDisplay(), var1, var2, var3, var4, var5, var6, var6.position(), var7, var7 != null ? var7.position() : -1);
         var10 = var8;
      } catch (LWJGLException var12) {
         decDisplay();
         throw var12;
      }

      unlockAWT();
      return var10;
   }

   private static long getCursorHandle(Object var0) {
      return var0 != null ? (Long)var0 : 0L;
   }

   public void destroyCursor(Object var1) {
      lockAWT();
      nDestroyCursor(getDisplay(), getCursorHandle(var1));
      decDisplay();
      unlockAWT();
   }

   static native void nDestroyCursor(long var0, long var2);

   public int getPbufferCapabilities() {
      lockAWT();

      int var1;
      try {
         incDisplay();
         var1 = nGetPbufferCapabilities(getDisplay(), getDefaultScreen());
         decDisplay();
      } catch (LWJGLException var4) {
         LWJGLUtil.log("Exception occurred in getPbufferCapabilities: " + var4);
         byte var2 = 0;
         unlockAWT();
         return var2;
      }

      unlockAWT();
      return var1;
   }

   private static native int nGetPbufferCapabilities(long var0, int var2);

   public boolean isBufferLost(PeerInfo var1) {
      return false;
   }

   public PeerInfo createPbuffer(int var1, int var2, PixelFormat var3, ContextAttribs var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      return new LinuxPbufferPeerInfo(var1, var2, var3);
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

   private static ByteBuffer convertIcons(ByteBuffer[] var0) {
      int var1 = 0;
      ByteBuffer[] var2 = var0;
      int var3 = var0.length;

      int var4;
      int var7;
      for(var4 = 0; var4 < var3; ++var4) {
         ByteBuffer var5 = var2[var4];
         int var6 = var5.limit() / 4;
         var7 = (int)Math.sqrt((double)var6);
         if (var7 > 0) {
            var1 += 8;
            var1 += var7 * var7 * 4;
         }
      }

      if (var1 == 0) {
         return null;
      } else {
         ByteBuffer var15 = BufferUtils.createByteBuffer(var1);
         var15.order(ByteOrder.BIG_ENDIAN);
         ByteBuffer[] var16 = var0;
         var4 = var0.length;

         for(int var17 = 0; var17 < var4; ++var17) {
            ByteBuffer var18 = var16[var17];
            var7 = var18.limit() / 4;
            int var8 = (int)Math.sqrt((double)var7);
            var15.putInt(var8);
            var15.putInt(var8);

            for(int var9 = 0; var9 < var8; ++var9) {
               for(int var10 = 0; var10 < var8; ++var10) {
                  byte var11 = var18.get(var10 * 4 + var9 * var8 * 4);
                  byte var12 = var18.get(var10 * 4 + var9 * var8 * 4 + 1);
                  byte var13 = var18.get(var10 * 4 + var9 * var8 * 4 + 2);
                  byte var14 = var18.get(var10 * 4 + var9 * var8 * 4 + 3);
                  var15.put(var14);
                  var15.put(var11);
                  var15.put(var12);
                  var15.put(var13);
               }
            }
         }

         return var15;
      }
   }

   public int setIcon(ByteBuffer[] var1) {
      lockAWT();

      byte var3;
      label23: {
         int var7;
         try {
            incDisplay();
            ByteBuffer var2 = convertIcons(var1);
            if (var2 == null) {
               var3 = 0;
               decDisplay();
               break label23;
            }

            nSetWindowIcon(getDisplay(), getWindow(), var2, var2.capacity());
            var7 = var1.length;
            decDisplay();
         } catch (LWJGLException var6) {
            LWJGLUtil.log("Failed to set display icon: " + var6);
            var3 = 0;
            unlockAWT();
            return var3;
         }

         unlockAWT();
         return var7;
      }

      unlockAWT();
      return var3;
   }

   private static native void nSetWindowIcon(long var0, long var2, ByteBuffer var4, int var5);

   public int getX() {
      return this.window_x;
   }

   public int getY() {
      return this.window_y;
   }

   public int getWidth() {
      return this.window_width;
   }

   public int getHeight() {
      return this.window_height;
   }

   public boolean isInsideWindow() {
      return this.mouseInside;
   }

   public void setResizable(boolean var1) {
      if (this.resizable != var1) {
         this.resizable = var1;
         nSetWindowSize(getDisplay(), getWindow(), this.window_width, this.window_height, var1);
      }
   }

   public boolean wasResized() {
      if (this.resized) {
         this.resized = false;
         return true;
      } else {
         return false;
      }
   }

   public float getPixelScaleFactor() {
      return 1.0F;
   }

   static boolean access$002(LinuxDisplay var0, boolean var1) {
      return var0.parent_focused = var1;
   }

   static boolean access$102(LinuxDisplay var0, boolean var1) {
      return var0.parent_focus_changed = var1;
   }

   static XRandR.Screen[] access$200(LinuxDisplay var0) {
      return var0.savedXrandrConfig;
   }

   private static final class Compiz {
      private static boolean applyFix;
      private static LinuxDisplay.Compiz.Provider provider;

      static void init() {
         if (!Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.nocompiz_lfs")) {
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  try {
                     String var1;
                     if (!LinuxDisplay.Compiz.access$300("compiz")) {
                        var1 = null;
                        return null;
                     } else {
                        LinuxDisplay.Compiz.access$402((LinuxDisplay.Compiz.Provider)null);
                        var1 = null;
                        if (LinuxDisplay.Compiz.access$300("dbus-daemon")) {
                           var1 = "Dbus";
                           LinuxDisplay.Compiz.access$402(new LinuxDisplay.Compiz.Provider(this) {
                              private static final String KEY = "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen";
                              final <undefinedtype> this$0;

                              {
                                 this.this$0 = var1;
                              }

                              public boolean hasLegacyFullscreenSupport() throws LWJGLException {
                                 List var1 = LinuxDisplay.Compiz.access$500(new String[]{"dbus-send", "--print-reply", "--type=method_call", "--dest=org.freedesktop.compiz", "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen", "org.freedesktop.compiz.get"});
                                 if (var1 != null && var1.size() >= 2) {
                                    String var2 = (String)var1.get(0);
                                    if (!var2.startsWith("method return")) {
                                       throw new LWJGLException("Invalid Dbus reply.");
                                    } else {
                                       var2 = ((String)var1.get(1)).trim();
                                       if (var2.startsWith("boolean") && var2.length() >= 12) {
                                          return "true".equalsIgnoreCase(var2.substring(8));
                                       } else {
                                          throw new LWJGLException("Invalid Dbus reply.");
                                       }
                                    }
                                 } else {
                                    throw new LWJGLException("Invalid Dbus reply.");
                                 }
                              }

                              public void setLegacyFullscreenSupport(boolean var1) throws LWJGLException {
                                 if (LinuxDisplay.Compiz.access$500(new String[]{"dbus-send", "--type=method_call", "--dest=org.freedesktop.compiz", "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen", "org.freedesktop.compiz.set", "boolean:" + Boolean.toString(var1)}) == null) {
                                    throw new LWJGLException("Failed to apply Compiz LFS workaround.");
                                 }
                              }
                           });
                        } else {
                           try {
                              Runtime.getRuntime().exec("gconftool");
                              var1 = "gconftool";
                              LinuxDisplay.Compiz.access$402(new LinuxDisplay.Compiz.Provider(this) {
                                 private static final String KEY = "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen";
                                 final <undefinedtype> this$0;

                                 {
                                    this.this$0 = var1;
                                 }

                                 public boolean hasLegacyFullscreenSupport() throws LWJGLException {
                                    List var1 = LinuxDisplay.Compiz.access$500(new String[]{"gconftool", "-g", "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen"});
                                    if (var1 != null && var1.size() != 0) {
                                       return Boolean.parseBoolean(((String)var1.get(0)).trim());
                                    } else {
                                       throw new LWJGLException("Invalid gconftool reply.");
                                    }
                                 }

                                 public void setLegacyFullscreenSupport(boolean var1) throws LWJGLException {
                                    if (LinuxDisplay.Compiz.access$500(new String[]{"gconftool", "-s", "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen", "-s", Boolean.toString(var1), "-t", "bool"}) == null) {
                                       throw new LWJGLException("Failed to apply Compiz LFS workaround.");
                                    } else {
                                       if (var1) {
                                          try {
                                             Thread.sleep(200L);
                                          } catch (InterruptedException var3) {
                                             var3.printStackTrace();
                                          }
                                       }

                                    }
                                 }
                              });
                           } catch (IOException var4) {
                           }
                        }

                        if (LinuxDisplay.Compiz.access$400() != null && !LinuxDisplay.Compiz.access$400().hasLegacyFullscreenSupport()) {
                           LinuxDisplay.Compiz.access$602(true);
                           LWJGLUtil.log("Using " + var1 + " to apply Compiz LFS workaround.");
                        }

                        return null;
                     }
                  } catch (LWJGLException var5) {
                     return null;
                  }
               }
            });
         }
      }

      static void setLegacyFullscreenSupport(boolean var0) {
         if (applyFix) {
            AccessController.doPrivileged(new PrivilegedAction(var0) {
               final boolean val$enabled;

               {
                  this.val$enabled = var1;
               }

               public Object run() {
                  try {
                     LinuxDisplay.Compiz.access$400().setLegacyFullscreenSupport(this.val$enabled);
                  } catch (LWJGLException var2) {
                     LWJGLUtil.log("Failed to change Compiz Legacy Fullscreen Support. Reason: " + var2.getMessage());
                  }

                  return null;
               }
            });
         }
      }

      private static List run(String... var0) throws LWJGLException {
         ArrayList var1 = new ArrayList();

         try {
            Process var2 = Runtime.getRuntime().exec(var0);

            try {
               int var3 = var2.waitFor();
               if (var3 != 0) {
                  return null;
               }
            } catch (InterruptedException var5) {
               throw new LWJGLException("Process interrupted.", var5);
            }

            BufferedReader var7 = new BufferedReader(new InputStreamReader(var2.getInputStream()));

            String var4;
            while((var4 = var7.readLine()) != null) {
               var1.add(var4);
            }

            var7.close();
            return var1;
         } catch (IOException var6) {
            throw new LWJGLException("Process failed.", var6);
         }
      }

      private static boolean isProcessActive(String var0) throws LWJGLException {
         List var1 = run("ps", "-C", var0);
         if (var1 == null) {
            return false;
         } else {
            Iterator var2 = var1.iterator();

            String var3;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               var3 = (String)var2.next();
            } while(!var3.contains(var0));

            return true;
         }
      }

      static boolean access$300(String var0) throws LWJGLException {
         return isProcessActive(var0);
      }

      static LinuxDisplay.Compiz.Provider access$402(LinuxDisplay.Compiz.Provider var0) {
         provider = var0;
         return var0;
      }

      static List access$500(String[] var0) throws LWJGLException {
         return run(var0);
      }

      static LinuxDisplay.Compiz.Provider access$400() {
         return provider;
      }

      static boolean access$602(boolean var0) {
         applyFix = var0;
         return var0;
      }

      private interface Provider {
         boolean hasLegacyFullscreenSupport() throws LWJGLException;

         void setLegacyFullscreenSupport(boolean var1) throws LWJGLException;
      }
   }
}
