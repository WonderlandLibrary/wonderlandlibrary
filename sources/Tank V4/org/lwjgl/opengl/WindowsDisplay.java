package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

final class WindowsDisplay implements DisplayImplementation {
   private static final int GAMMA_LENGTH = 256;
   private static final int WM_WINDOWPOSCHANGED = 71;
   private static final int WM_MOVE = 3;
   private static final int WM_CANCELMODE = 31;
   private static final int WM_MOUSEMOVE = 512;
   private static final int WM_LBUTTONDOWN = 513;
   private static final int WM_LBUTTONUP = 514;
   private static final int WM_LBUTTONDBLCLK = 515;
   private static final int WM_RBUTTONDOWN = 516;
   private static final int WM_RBUTTONUP = 517;
   private static final int WM_RBUTTONDBLCLK = 518;
   private static final int WM_MBUTTONDOWN = 519;
   private static final int WM_MBUTTONUP = 520;
   private static final int WM_MBUTTONDBLCLK = 521;
   private static final int WM_XBUTTONDOWN = 523;
   private static final int WM_XBUTTONUP = 524;
   private static final int WM_XBUTTONDBLCLK = 525;
   private static final int WM_MOUSEWHEEL = 522;
   private static final int WM_CAPTURECHANGED = 533;
   private static final int WM_MOUSELEAVE = 675;
   private static final int WM_ENTERSIZEMOVE = 561;
   private static final int WM_EXITSIZEMOVE = 562;
   private static final int WM_SIZING = 532;
   private static final int WM_KEYDOWN = 256;
   private static final int WM_KEYUP = 257;
   private static final int WM_SYSKEYUP = 261;
   private static final int WM_SYSKEYDOWN = 260;
   private static final int WM_SYSCHAR = 262;
   private static final int WM_CHAR = 258;
   private static final int WM_GETICON = 127;
   private static final int WM_SETICON = 128;
   private static final int WM_SETCURSOR = 32;
   private static final int WM_MOUSEACTIVATE = 33;
   private static final int WM_QUIT = 18;
   private static final int WM_SYSCOMMAND = 274;
   private static final int WM_PAINT = 15;
   private static final int WM_KILLFOCUS = 8;
   private static final int WM_SETFOCUS = 7;
   private static final int SC_SIZE = 61440;
   private static final int SC_MOVE = 61456;
   private static final int SC_MINIMIZE = 61472;
   private static final int SC_MAXIMIZE = 61488;
   private static final int SC_NEXTWINDOW = 61504;
   private static final int SC_PREVWINDOW = 61520;
   private static final int SC_CLOSE = 61536;
   private static final int SC_VSCROLL = 61552;
   private static final int SC_HSCROLL = 61568;
   private static final int SC_MOUSEMENU = 61584;
   private static final int SC_KEYMENU = 61696;
   private static final int SC_ARRANGE = 61712;
   private static final int SC_RESTORE = 61728;
   private static final int SC_TASKLIST = 61744;
   private static final int SC_SCREENSAVE = 61760;
   private static final int SC_HOTKEY = 61776;
   private static final int SC_DEFAULT = 61792;
   private static final int SC_MONITORPOWER = 61808;
   private static final int SC_CONTEXTHELP = 61824;
   private static final int SC_SEPARATOR = 61455;
   static final int SM_CXCURSOR = 13;
   static final int SM_CYCURSOR = 14;
   static final int SM_CMOUSEBUTTONS = 43;
   static final int SM_MOUSEWHEELPRESENT = 75;
   private static final int SIZE_RESTORED = 0;
   private static final int SIZE_MINIMIZED = 1;
   private static final int SIZE_MAXIMIZED = 2;
   private static final int WM_SIZE = 5;
   private static final int WM_ACTIVATE = 6;
   private static final int WA_INACTIVE = 0;
   private static final int WA_ACTIVE = 1;
   private static final int WA_CLICKACTIVE = 2;
   private static final int SW_NORMAL = 1;
   private static final int SW_SHOWMINNOACTIVE = 7;
   private static final int SW_SHOWDEFAULT = 10;
   private static final int SW_RESTORE = 9;
   private static final int SW_MAXIMIZE = 3;
   private static final int ICON_SMALL = 0;
   private static final int ICON_BIG = 1;
   private static final IntBuffer rect_buffer = BufferUtils.createIntBuffer(4);
   private static final WindowsDisplay.Rect rect = new WindowsDisplay.Rect();
   private static final long HWND_TOP = 0L;
   private static final long HWND_BOTTOM = 1L;
   private static final long HWND_TOPMOST = -1L;
   private static final long HWND_NOTOPMOST = -2L;
   private static final int SWP_NOSIZE = 1;
   private static final int SWP_NOMOVE = 2;
   private static final int SWP_NOZORDER = 4;
   private static final int SWP_FRAMECHANGED = 32;
   private static final int GWL_STYLE = -16;
   private static final int GWL_EXSTYLE = -20;
   private static final int WS_THICKFRAME = 262144;
   private static final int WS_MAXIMIZEBOX = 65536;
   private static final int HTCLIENT = 1;
   private static final int MK_XBUTTON1 = 32;
   private static final int MK_XBUTTON2 = 64;
   private static final int XBUTTON1 = 1;
   private static final int XBUTTON2 = 2;
   private static WindowsDisplay current_display;
   private static boolean cursor_clipped;
   private WindowsDisplayPeerInfo peer_info;
   private Object current_cursor;
   private static boolean hasParent;
   private Canvas parent;
   private long parent_hwnd;
   private FocusAdapter parent_focus_tracker;
   private AtomicBoolean parent_focused;
   private WindowsKeyboard keyboard;
   private WindowsMouse mouse;
   private boolean close_requested;
   private boolean is_dirty;
   private ByteBuffer current_gamma;
   private ByteBuffer saved_gamma;
   private DisplayMode current_mode;
   private boolean mode_set;
   private boolean isMinimized;
   private boolean isFocused;
   private boolean redoMakeContextCurrent;
   private boolean inAppActivate;
   private boolean resized;
   private boolean resizable;
   private boolean maximized;
   private int x;
   private int y;
   private int width;
   private int height;
   private long hwnd;
   private long hdc;
   private long small_icon;
   private long large_icon;
   private boolean iconsLoaded;
   private int captureMouse = -1;
   private boolean trackingMouse;
   private boolean mouseInside;

   WindowsDisplay() {
      current_display = this;
   }

   public void createWindow(DrawableLWJGL var1, DisplayMode var2, Canvas var3, int var4, int var5) throws LWJGLException {
      this.close_requested = false;
      this.is_dirty = false;
      this.isMinimized = false;
      this.isFocused = false;
      this.redoMakeContextCurrent = false;
      this.maximized = false;
      this.parent = var3;
      hasParent = var3 != null;
      this.parent_hwnd = var3 != null ? getHwnd(var3) : 0L;
      this.hwnd = nCreateWindow(var4, var5, var2.getWidth(), var2.getHeight(), Display.isFullscreen() || isUndecorated(), var3 != null, this.parent_hwnd);
      this.resizable = false;
      if (this.hwnd == 0L) {
         throw new LWJGLException("Failed to create window");
      } else {
         this.hdc = getDC(this.hwnd);
         if (this.hdc == 0L) {
            nDestroyWindow(this.hwnd);
            throw new LWJGLException("Failed to get dc");
         } else {
            try {
               if (var1 instanceof DrawableGL) {
                  int var6 = WindowsPeerInfo.choosePixelFormat(this.getHdc(), 0, 0, (PixelFormat)var1.getPixelFormat(), (IntBuffer)null, true, true, false, true);
                  WindowsPeerInfo.setPixelFormat(this.getHdc(), var6);
               } else {
                  this.peer_info = new WindowsDisplayPeerInfo(true);
                  ((DrawableGLES)var1).initialize(this.hwnd, this.hdc, 4, (org.lwjgl.opengles.PixelFormat)var1.getPixelFormat());
               }

               this.peer_info.initDC(this.getHwnd(), this.getHdc());
               showWindow(this.getHwnd(), 10);
               this.updateWidthAndHeight();
               if (var3 == null) {
                  if (Display.isResizable()) {
                     this.setResizable(true);
                  }

                  setForegroundWindow(this.getHwnd());
               } else {
                  this.parent_focused = new AtomicBoolean(false);
                  var3.addFocusListener(this.parent_focus_tracker = new FocusAdapter(this) {
                     final WindowsDisplay this$0;

                     {
                        this.this$0 = var1;
                     }

                     public void focusGained(FocusEvent var1) {
                        WindowsDisplay.access$100(this.this$0).set(true);
                        WindowsDisplay.access$200(this.this$0);
                     }
                  });
                  SwingUtilities.invokeLater(new Runnable(this) {
                     final WindowsDisplay this$0;

                     {
                        this.this$0 = var1;
                     }

                     public void run() {
                        WindowsDisplay.access$200(this.this$0);
                     }
                  });
               }

               this.grabFocus();
            } catch (LWJGLException var7) {
               nReleaseDC(this.hwnd, this.hdc);
               nDestroyWindow(this.hwnd);
               throw var7;
            }
         }
      }
   }

   private void updateWidthAndHeight() {
      getClientRect(this.hwnd, rect_buffer);
      rect.copyFromBuffer(rect_buffer);
      this.width = rect.right - rect.left;
      this.height = rect.bottom - rect.top;
   }

   private static native long nCreateWindow(int var0, int var1, int var2, int var3, boolean var4, boolean var5, long var6) throws LWJGLException;

   private static boolean isUndecorated() {
      return Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated");
   }

   private static long getHwnd(Canvas var0) throws LWJGLException {
      AWTCanvasImplementation var1 = AWTGLCanvas.createImplementation();
      WindowsPeerInfo var2 = (WindowsPeerInfo)var1.createPeerInfo(var0, (PixelFormat)null, (ContextAttribs)null);
      ByteBuffer var3 = var2.lockAndGetHandle();
      long var4 = var2.getHwnd();
      var2.unlock();
      return var4;
   }

   public void destroyWindow() {
      if (this.parent != null) {
         this.parent.removeFocusListener(this.parent_focus_tracker);
         this.parent_focus_tracker = null;
      }

      nReleaseDC(this.hwnd, this.hdc);
      nDestroyWindow(this.hwnd);
      this.freeLargeIcon();
      this.freeSmallIcon();
      resetCursorClipping();
   }

   private static native void nReleaseDC(long var0, long var2);

   private static native void nDestroyWindow(long var0);

   static void resetCursorClipping() {
      if (cursor_clipped) {
         try {
            clipCursor((IntBuffer)null);
         } catch (LWJGLException var1) {
            LWJGLUtil.log("Failed to reset cursor clipping: " + var1);
         }

         cursor_clipped = false;
      }

   }

   private static void getGlobalClientRect(long var0, WindowsDisplay.Rect var2) {
      rect_buffer.put(0, 0).put(1, 0);
      clientToScreen(var0, rect_buffer);
      int var3 = rect_buffer.get(0);
      int var4 = rect_buffer.get(1);
      getClientRect(var0, rect_buffer);
      var2.copyFromBuffer(rect_buffer);
      var2.offset(var3, var4);
   }

   static void setupCursorClipping(long var0) throws LWJGLException {
      cursor_clipped = true;
      getGlobalClientRect(var0, rect);
      rect.copyToBuffer(rect_buffer);
      clipCursor(rect_buffer);
   }

   private static native void clipCursor(IntBuffer var0) throws LWJGLException;

   public void switchDisplayMode(DisplayMode var1) throws LWJGLException {
      nSwitchDisplayMode(var1);
      this.current_mode = var1;
      this.mode_set = true;
   }

   private static native void nSwitchDisplayMode(DisplayMode var0) throws LWJGLException;

   private void appActivate(boolean var1) {
      if (!this.inAppActivate) {
         this.inAppActivate = true;
         this.isFocused = var1;
         if (var1) {
            if (Display.isFullscreen()) {
               this.restoreDisplayMode();
            }

            if (this.parent == null) {
               setForegroundWindow(this.getHwnd());
            }

            setFocus(this.getHwnd());
            this.redoMakeContextCurrent = true;
            if (Display.isFullscreen()) {
               this.updateClipping();
            }

            if (this.keyboard != null) {
               this.keyboard.fireLostKeyEvents();
            }
         } else if (Display.isFullscreen()) {
            showWindow(this.getHwnd(), 7);
            this.resetDisplayMode();
         } else {
            this.updateClipping();
         }

         this.updateCursor();
         this.inAppActivate = false;
      }
   }

   private static native void showWindow(long var0, int var2);

   private static native void setForegroundWindow(long var0);

   private static native void setFocus(long var0);

   private void clearAWTFocus() {
      this.parent.setFocusable(false);
      this.parent.setFocusable(true);
      KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
   }

   private void grabFocus() {
      if (this.parent == null) {
         setFocus(this.getHwnd());
      } else {
         SwingUtilities.invokeLater(new Runnable(this) {
            final WindowsDisplay this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               WindowsDisplay.access$300(this.this$0).requestFocus();
            }
         });
      }

   }

   private void restoreDisplayMode() {
      try {
         this.doSetGammaRamp(this.current_gamma);
      } catch (LWJGLException var3) {
         LWJGLUtil.log("Failed to restore gamma: " + var3.getMessage());
      }

      if (!this.mode_set) {
         this.mode_set = true;

         try {
            nSwitchDisplayMode(this.current_mode);
         } catch (LWJGLException var2) {
            LWJGLUtil.log("Failed to restore display mode: " + var2.getMessage());
         }
      }

   }

   public void resetDisplayMode() {
      try {
         this.doSetGammaRamp(this.saved_gamma);
      } catch (LWJGLException var2) {
         LWJGLUtil.log("Failed to reset gamma ramp: " + var2.getMessage());
      }

      this.current_gamma = this.saved_gamma;
      if (this.mode_set) {
         this.mode_set = false;
         nResetDisplayMode();
      }

      resetCursorClipping();
   }

   private static native void nResetDisplayMode();

   public int getGammaRampLength() {
      return 256;
   }

   public void setGammaRamp(FloatBuffer var1) throws LWJGLException {
      this.doSetGammaRamp(convertToNativeRamp(var1));
   }

   private static native ByteBuffer convertToNativeRamp(FloatBuffer var0) throws LWJGLException;

   private static native ByteBuffer getCurrentGammaRamp() throws LWJGLException;

   private void doSetGammaRamp(ByteBuffer var1) throws LWJGLException {
      nSetGammaRamp(var1);
      this.current_gamma = var1;
   }

   private static native void nSetGammaRamp(ByteBuffer var0) throws LWJGLException;

   public String getAdapter() {
      try {
         String var1 = WindowsRegistry.queryRegistrationKey(3, "HARDWARE\\DeviceMap\\Video", "MaxObjectNumber");
         char var2 = var1.charAt(0);
         String var3 = "";

         for(int var4 = 0; var4 < var2; ++var4) {
            String var5 = WindowsRegistry.queryRegistrationKey(3, "HARDWARE\\DeviceMap\\Video", "\\Device\\Video" + var4);
            String var6 = "\\registry\\machine\\";
            if (var5.toLowerCase().startsWith(var6)) {
               String var7 = WindowsRegistry.queryRegistrationKey(3, var5.substring(var6.length()), "InstalledDisplayDrivers");
               if (var7.toUpperCase().startsWith("VGA")) {
                  var3 = var7;
               } else if (!var7.toUpperCase().startsWith("RDP") && !var7.toUpperCase().startsWith("NMNDD")) {
                  return var7;
               }
            }
         }

         if (!var3.equals("")) {
            return var3;
         }
      } catch (LWJGLException var8) {
         LWJGLUtil.log("Exception occurred while querying registry: " + var8);
      }

      return null;
   }

   public String getVersion() {
      String var1 = this.getAdapter();
      if (var1 != null) {
         String[] var2 = var1.split(",");
         if (var2.length > 0) {
            WindowsFileVersion var3 = this.nGetVersion(var2[0] + ".dll");
            if (var3 != null) {
               return var3.toString();
            }
         }
      }

      return null;
   }

   private native WindowsFileVersion nGetVersion(String var1);

   public DisplayMode init() throws LWJGLException {
      this.current_gamma = this.saved_gamma = getCurrentGammaRamp();
      return this.current_mode = getCurrentDisplayMode();
   }

   private static native DisplayMode getCurrentDisplayMode() throws LWJGLException;

   public void setTitle(String var1) {
      ByteBuffer var2 = MemoryUtil.encodeUTF16(var1);
      nSetTitle(this.hwnd, MemoryUtil.getAddress0((Buffer)var2));
   }

   private static native void nSetTitle(long var0, long var2);

   public boolean isCloseRequested() {
      boolean var1 = this.close_requested;
      this.close_requested = false;
      return var1;
   }

   public boolean isVisible() {
      return !this.isMinimized;
   }

   public boolean isActive() {
      return this.isFocused;
   }

   public boolean isDirty() {
      boolean var1 = this.is_dirty;
      this.is_dirty = false;
      return var1;
   }

   public PeerInfo createPeerInfo(PixelFormat var1, ContextAttribs var2) throws LWJGLException {
      this.peer_info = new WindowsDisplayPeerInfo(false);
      return this.peer_info;
   }

   public void update() {
      nUpdate();
      if (!this.isFocused && this.parent != null && this.parent_focused.compareAndSet(true, false)) {
         setFocus(this.getHwnd());
      }

      if (this.redoMakeContextCurrent) {
         this.redoMakeContextCurrent = false;

         try {
            Context var1 = ((DrawableLWJGL)Display.getDrawable()).getContext();
            if (var1 != null && var1.isCurrent()) {
               var1.makeCurrent();
            }
         } catch (LWJGLException var2) {
            LWJGLUtil.log("Exception occurred while trying to make context current: " + var2);
         }
      }

   }

   private static native void nUpdate();

   public void reshape(int var1, int var2, int var3, int var4) {
      nReshape(this.getHwnd(), var1, var2, var3, var4, Display.isFullscreen() || isUndecorated(), this.parent != null);
   }

   private static native void nReshape(long var0, int var2, int var3, int var4, int var5, boolean var6, boolean var7);

   public native DisplayMode[] getAvailableDisplayModes() throws LWJGLException;

   public boolean hasWheel() {
      return this.mouse.hasWheel();
   }

   public int getButtonCount() {
      return this.mouse.getButtonCount();
   }

   public void createMouse() throws LWJGLException {
      this.mouse = new WindowsMouse(this.getHwnd());
   }

   public void destroyMouse() {
      if (this.mouse != null) {
         this.mouse.destroy();
      }

      this.mouse = null;
   }

   public void pollMouse(IntBuffer var1, ByteBuffer var2) {
      this.mouse.poll(var1, var2);
   }

   public void readMouse(ByteBuffer var1) {
      this.mouse.read(var1);
   }

   public void grabMouse(boolean var1) {
      this.mouse.grab(var1, this.shouldGrab());
      this.updateCursor();
   }

   public int getNativeCursorCapabilities() {
      return 1;
   }

   public void setCursorPosition(int var1, int var2) {
      getGlobalClientRect(this.getHwnd(), rect);
      int var3 = rect.left + var1;
      int var4 = rect.bottom - 1 - var2;
      nSetCursorPosition(var3, var4);
      this.setMousePosition(var1, var2);
   }

   private static native void nSetCursorPosition(int var0, int var1);

   public void setNativeCursor(Object var1) throws LWJGLException {
      this.current_cursor = var1;
      this.updateCursor();
   }

   private void updateCursor() {
      try {
         if (this.mouse != null && this == false) {
            nSetNativeCursor(this.getHwnd(), this.mouse.getBlankCursor());
         } else {
            nSetNativeCursor(this.getHwnd(), this.current_cursor);
         }
      } catch (LWJGLException var2) {
         LWJGLUtil.log("Failed to update cursor: " + var2);
      }

   }

   static native void nSetNativeCursor(long var0, Object var2) throws LWJGLException;

   public int getMinCursorSize() {
      return getSystemMetrics(13);
   }

   public int getMaxCursorSize() {
      return getSystemMetrics(13);
   }

   static native int getSystemMetrics(int var0);

   private static native long getDllInstance();

   private long getHwnd() {
      return this.hwnd;
   }

   private long getHdc() {
      return this.hdc;
   }

   private static native long getDC(long var0);

   private static native long getDesktopWindow();

   private static native long getForegroundWindow();

   static void centerCursor(long var0) {
      if (getForegroundWindow() == var0 || hasParent) {
         getGlobalClientRect(var0, rect);
         int var2 = rect.left;
         int var3 = rect.top;
         int var4 = (rect.left + rect.right) / 2;
         int var5 = (rect.top + rect.bottom) / 2;
         nSetCursorPosition(var4, var5);
         int var6 = var4 - var2;
         int var7 = var5 - var3;
         if (current_display != null) {
            current_display.setMousePosition(var6, transformY(var0, var7));
         }

      }
   }

   private void setMousePosition(int var1, int var2) {
      if (this.mouse != null) {
         this.mouse.setPosition(var1, var2);
      }

   }

   public void createKeyboard() throws LWJGLException {
      this.keyboard = new WindowsKeyboard();
   }

   public void destroyKeyboard() {
      this.keyboard = null;
   }

   public void pollKeyboard(ByteBuffer var1) {
      this.keyboard.poll(var1);
   }

   public void readKeyboard(ByteBuffer var1) {
      this.keyboard.read(var1);
   }

   public static native ByteBuffer nCreateCursor(int var0, int var1, int var2, int var3, int var4, IntBuffer var5, int var6, IntBuffer var7, int var8) throws LWJGLException;

   public Object createCursor(int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) throws LWJGLException {
      return doCreateCursor(var1, var2, var3, var4, var5, var6, var7);
   }

   static Object doCreateCursor(int var0, int var1, int var2, int var3, int var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      return nCreateCursor(var0, var1, var2, var3, var4, var5, var5.position(), var6, var6 != null ? var6.position() : -1);
   }

   public void destroyCursor(Object var1) {
      doDestroyCursor(var1);
   }

   static native void doDestroyCursor(Object var0);

   public int getPbufferCapabilities() {
      try {
         return this.nGetPbufferCapabilities(new PixelFormat(0, 0, 0, 0, 0, 0, 0, 0, false));
      } catch (LWJGLException var2) {
         LWJGLUtil.log("Exception occurred while determining pbuffer capabilities: " + var2);
         return 0;
      }
   }

   private native int nGetPbufferCapabilities(PixelFormat var1) throws LWJGLException;

   public boolean isBufferLost(PeerInfo var1) {
      return ((WindowsPbufferPeerInfo)var1).isBufferLost();
   }

   public PeerInfo createPbuffer(int var1, int var2, PixelFormat var3, ContextAttribs var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      return new WindowsPbufferPeerInfo(var1, var2, var3, var5, var6);
   }

   public void setPbufferAttrib(PeerInfo var1, int var2, int var3) {
      ((WindowsPbufferPeerInfo)var1).setPbufferAttrib(var2, var3);
   }

   public void bindTexImageToPbuffer(PeerInfo var1, int var2) {
      ((WindowsPbufferPeerInfo)var1).bindTexImageToPbuffer(var2);
   }

   public void releaseTexImageFromPbuffer(PeerInfo var1, int var2) {
      ((WindowsPbufferPeerInfo)var1).releaseTexImageFromPbuffer(var2);
   }

   private void freeSmallIcon() {
      if (this.small_icon != 0L) {
         destroyIcon(this.small_icon);
         this.small_icon = 0L;
      }

   }

   private void freeLargeIcon() {
      if (this.large_icon != 0L) {
         destroyIcon(this.large_icon);
         this.large_icon = 0L;
      }

   }

   public int setIcon(ByteBuffer[] var1) {
      boolean var2 = false;
      boolean var3 = false;
      int var4 = 0;
      byte var5 = 16;
      byte var6 = 32;
      ByteBuffer[] var7 = var1;
      int var8 = var1.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         ByteBuffer var10 = var7[var9];
         int var11 = var10.limit() / 4;
         long var12;
         if ((int)Math.sqrt((double)var11) == var5 && !var2) {
            var12 = createIcon(var5, var5, var10.asIntBuffer());
            sendMessage(this.hwnd, 128L, 0L, var12);
            this.freeSmallIcon();
            this.small_icon = var12;
            ++var4;
            var2 = true;
         }

         if ((int)Math.sqrt((double)var11) == var6 && !var3) {
            var12 = createIcon(var6, var6, var10.asIntBuffer());
            sendMessage(this.hwnd, 128L, 1L, var12);
            this.freeLargeIcon();
            this.large_icon = var12;
            ++var4;
            var3 = true;
            this.iconsLoaded = false;
            long var14 = System.nanoTime();
            long var16 = 500000000L;

            while(true) {
               nUpdate();
               if (this.iconsLoaded || var16 < System.nanoTime() - var14) {
                  break;
               }

               Thread.yield();
            }
         }
      }

      return var4;
   }

   private static native long createIcon(int var0, int var1, IntBuffer var2);

   private static native void destroyIcon(long var0);

   private static native long sendMessage(long var0, long var2, long var4, long var6);

   private static native long setWindowLongPtr(long var0, int var2, long var3);

   private static native long getWindowLongPtr(long var0, int var2);

   private static native boolean setWindowPos(long var0, long var2, int var4, int var5, int var6, int var7, long var8);

   private void handleMouseButton(int var1, int var2, long var3) {
      if (this.mouse != null) {
         this.mouse.handleMouseButton((byte)var1, (byte)var2, var3);
         if (this.captureMouse == -1 && var1 != -1 && var2 == 1) {
            this.captureMouse = var1;
            nSetCapture(this.hwnd);
         }

         if (this.captureMouse != -1 && var1 == this.captureMouse && var2 == 0) {
            this.captureMouse = -1;
            nReleaseCapture();
         }
      }

   }

   private void handleMouseMoved(int var1, int var2, long var3) {
      if (this.mouse != null) {
         this.mouse.handleMouseMoved(var1, var2, var3, this.shouldGrab());
      }

   }

   private static native long nSetCapture(long var0);

   private static native boolean nReleaseCapture();

   private void handleMouseScrolled(int var1, long var2) {
      if (this.mouse != null) {
         this.mouse.handleMouseScrolled(var1, var2);
      }

   }

   private static native void getClientRect(long var0, IntBuffer var2);

   private void handleChar(long var1, long var3, long var5) {
      byte var7 = (byte)((int)(var3 >>> 30 & 1L));
      byte var8 = (byte)((int)(1L - (var3 >>> 31 & 1L)));
      boolean var9 = var8 == var7;
      if (this.keyboard != null) {
         this.keyboard.handleChar((int)(var1 & 65535L), var5, var9);
      }

   }

   private void handleKeyButton(long var1, long var3, long var5) {
      if (this.keyboard != null) {
         byte var7 = (byte)((int)(var3 >>> 30 & 1L));
         byte var8 = (byte)((int)(1L - (var3 >>> 31 & 1L)));
         boolean var9 = var8 == var7;
         byte var10 = (byte)((int)(var3 >>> 24 & 1L));
         int var11 = (int)(var3 >>> 16 & 255L);
         this.keyboard.handleKey((int)var1, var11, var10 != 0, var8, var5, var9);
      }
   }

   private static int transformY(long var0, int var2) {
      getClientRect(var0, rect_buffer);
      rect.copyFromBuffer(rect_buffer);
      return rect.bottom - rect.top - 1 - var2;
   }

   private static native void clientToScreen(long var0, IntBuffer var2);

   private static native void setWindowProc(Method var0);

   private static long handleMessage(long var0, int var2, long var3, long var5, long var7) {
      return current_display != null ? current_display.doHandleMessage(var0, var2, var3, var5, var7) : defWindowProc(var0, var2, var3, var5);
   }

   private static native long defWindowProc(long var0, int var2, long var3, long var5);

   private void checkCursorState() {
      this.updateClipping();
   }

   private void updateClipping() {
      if (!Display.isFullscreen() && (this.mouse == null || !this.mouse.isGrabbed()) || this.isMinimized || !this.isFocused || getForegroundWindow() != this.getHwnd() && !hasParent) {
         resetCursorClipping();
      } else {
         try {
            setupCursorClipping(this.getHwnd());
         } catch (LWJGLException var2) {
            LWJGLUtil.log("setupCursorClipping failed: " + var2.getMessage());
         }
      }

   }

   private void setMinimized(boolean var1) {
      this.isMinimized = var1;
      this.checkCursorState();
   }

   private long doHandleMessage(long var1, int var3, long var4, long var6, long var8) {
      if (this.parent != null && !this.isFocused) {
         switch(var3) {
         case 513:
         case 516:
         case 519:
         case 523:
            sendMessage(this.parent_hwnd, (long)var3, var4, var6);
         }
      }

      switch(var3) {
      case 5:
         switch((int)var4) {
         case 0:
         case 2:
            this.maximized = (int)var4 == 2;
            this.resized = true;
            this.updateWidthAndHeight();
            this.setMinimized(false);
            return defWindowProc(var1, var3, var4, var6);
         case 1:
            this.setMinimized(true);
            return defWindowProc(var1, var3, var4, var6);
         default:
            return defWindowProc(var1, var3, var4, var6);
         }
      case 6:
         return 0L;
      case 7:
         this.appActivate(true);
         return 0L;
      case 8:
         this.appActivate(false);
         return 0L;
      case 15:
         this.is_dirty = true;
         break;
      case 18:
         this.close_requested = true;
         return 0L;
      case 31:
         nReleaseCapture();
      case 533:
         if (this.captureMouse != -1) {
            this.handleMouseButton(this.captureMouse, 0, var8);
            this.captureMouse = -1;
         }

         return 0L;
      case 32:
         if ((var6 & 65535L) == 1L) {
            this.updateCursor();
            return -1L;
         }

         return defWindowProc(var1, var3, var4, var6);
      case 33:
         if (this.parent != null) {
            if (!this.isFocused) {
               this.grabFocus();
            }

            return 3L;
         }
         break;
      case 71:
         if (this.getWindowRect(var1, rect_buffer)) {
            rect.copyFromBuffer(rect_buffer);
            this.x = rect.top;
            this.y = rect.bottom;
         } else {
            LWJGLUtil.log("WM_WINDOWPOSCHANGED: Unable to get window rect");
         }
         break;
      case 127:
         this.iconsLoaded = true;
         break;
      case 257:
      case 261:
         if (var4 == 44L && this.keyboard != null && !this.keyboard.isKeyDown(183)) {
            long var13 = var6 & 2147483647L;
            var13 &= -1073741825L;
            this.handleKeyButton(var4, var13, var8);
         }
      case 256:
      case 260:
         this.handleKeyButton(var4, var6, var8);
         break;
      case 258:
      case 262:
         this.handleChar(var4, var6, var8);
         return 0L;
      case 274:
         switch((int)(var4 & 65520L)) {
         case 61536:
            this.close_requested = true;
            return 0L;
         case 61584:
         case 61696:
         case 61760:
         case 61808:
            return 0L;
         default:
            return defWindowProc(var1, var3, var4, var6);
         }
      case 512:
         short var10 = (short)((int)(var6 & 65535L));
         int var11 = transformY(this.getHwnd(), (short)((int)(var6 >> 16 & 65535L)));
         this.handleMouseMoved(var10, var11, var8);
         this.checkCursorState();
         this.mouseInside = true;
         if (!this.trackingMouse) {
            this.trackingMouse = this.nTrackMouseEvent(var1);
         }

         return 0L;
      case 513:
         this.handleMouseButton(0, 1, var8);
         return 0L;
      case 514:
         this.handleMouseButton(0, 0, var8);
         return 0L;
      case 516:
         this.handleMouseButton(1, 1, var8);
         return 0L;
      case 517:
         this.handleMouseButton(1, 0, var8);
         return 0L;
      case 519:
         this.handleMouseButton(2, 1, var8);
         return 0L;
      case 520:
         this.handleMouseButton(2, 0, var8);
         return 0L;
      case 522:
         short var12 = (short)((int)(var4 >> 16 & 65535L));
         this.handleMouseScrolled(var12, var8);
         return 0L;
      case 523:
         if ((var4 & 255L) == 32L) {
            this.handleMouseButton(3, 1, var8);
         } else {
            this.handleMouseButton(4, 1, var8);
         }

         return 1L;
      case 524:
         if (var4 >> 16 == 1L) {
            this.handleMouseButton(3, 0, var8);
         } else {
            this.handleMouseButton(4, 0, var8);
         }

         return 1L;
      case 532:
         this.resized = true;
         this.updateWidthAndHeight();
         break;
      case 675:
         this.mouseInside = false;
         this.trackingMouse = false;
      }

      return defWindowProc(var1, var3, var4, var6);
   }

   private native boolean getWindowRect(long var1, IntBuffer var3);

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   private native boolean nTrackMouseEvent(long var1);

   public boolean isInsideWindow() {
      return this.mouseInside;
   }

   public void setResizable(boolean var1) {
      if (this.resizable != var1) {
         int var2 = (int)getWindowLongPtr(this.hwnd, -16);
         int var3 = (int)getWindowLongPtr(this.hwnd, -20);
         if (var1 && !Display.isFullscreen()) {
            setWindowLongPtr(this.hwnd, -16, (long)(var2 |= 327680));
         } else {
            setWindowLongPtr(this.hwnd, -16, (long)(var2 &= -327681));
         }

         getClientRect(this.hwnd, rect_buffer);
         rect.copyFromBuffer(rect_buffer);
         this.adjustWindowRectEx(rect_buffer, var2, false, var3);
         rect.copyFromBuffer(rect_buffer);
         setWindowPos(this.hwnd, 0L, 0, 0, rect.right - rect.left, rect.bottom - rect.top, 38L);
         this.updateWidthAndHeight();
         this.resized = false;
      }

      this.resizable = var1;
   }

   private native boolean adjustWindowRectEx(IntBuffer var1, int var2, boolean var3, int var4);

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

   static AtomicBoolean access$100(WindowsDisplay var0) {
      return var0.parent_focused;
   }

   static void access$200(WindowsDisplay var0) {
      var0.clearAWTFocus();
   }

   static Canvas access$300(WindowsDisplay var0) {
      return var0.parent;
   }

   static {
      try {
         Method var0 = WindowsDisplay.class.getDeclaredMethod("handleMessage", Long.TYPE, Integer.TYPE, Long.TYPE, Long.TYPE, Long.TYPE);
         setWindowProc(var0);
      } catch (NoSuchMethodException var1) {
         throw new RuntimeException(var1);
      }
   }

   private static final class Rect {
      public int top;
      public int bottom;
      public int left;
      public int right;

      private Rect() {
      }

      public void copyToBuffer(IntBuffer var1) {
         var1.put(0, this.top).put(1, this.bottom).put(2, this.left).put(3, this.right);
      }

      public void copyFromBuffer(IntBuffer var1) {
         this.top = var1.get(0);
         this.bottom = var1.get(1);
         this.left = var1.get(2);
         this.right = var1.get(3);
      }

      public void offset(int var1, int var2) {
         this.left += var1;
         this.right += var1;
         this.top += var2;
         this.bottom += var2;
      }

      public static void intersect(WindowsDisplay.Rect var0, WindowsDisplay.Rect var1, WindowsDisplay.Rect var2) {
         var2.top = Math.max(var0.top, var1.top);
         var2.bottom = Math.min(var0.bottom, var1.bottom);
         var2.left = Math.max(var0.left, var1.left);
         var2.right = Math.min(var0.right, var1.right);
      }

      public String toString() {
         return "Rect: top = " + this.top + " bottom = " + this.bottom + " left = " + this.left + " right = " + this.right + ", width: " + (this.right - this.left) + ", height: " + (this.bottom - this.top);
      }

      Rect(Object var1) {
         this();
      }
   }
}
