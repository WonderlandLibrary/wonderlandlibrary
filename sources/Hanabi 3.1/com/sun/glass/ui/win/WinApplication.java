package com.sun.glass.ui.win;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.InvokeLaterDispatcher;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class WinApplication extends Application implements InvokeLaterDispatcher.InvokeLaterSubmitter {
   static float overrideUIScale;
   static float minDPIScale;
   private final InvokeLaterDispatcher invokeLaterDispatcher;
   private static boolean verbose;
   private static final int Process_DPI_Unaware = 0;
   private static final int Process_System_DPI_Aware = 1;
   private static final int Process_Per_Monitor_DPI_Aware = 2;

   private static boolean getBoolean(String var0, boolean var1, String var2) {
      String var3 = System.getProperty(var0);
      if (var3 == null) {
         var3 = System.getenv(var0);
      }

      if (var3 == null) {
         return var1;
      } else {
         Boolean var4 = Boolean.parseBoolean(var3);
         if (PrismSettings.verbose) {
            System.out.println((var4 ? "" : "not ") + var2);
         }

         return var4;
      }
   }

   private static float getFloat(String var0, float var1, String var2) {
      String var3 = System.getProperty(var0);
      if (var3 == null) {
         var3 = System.getenv(var0);
      }

      if (var3 == null) {
         return var1;
      } else {
         var3 = var3.trim();
         float var4;
         if (var3.endsWith("%")) {
            var4 = (float)Integer.parseInt(var3.substring(0, var3.length() - 1)) / 100.0F;
         } else if (!var3.endsWith("DPI") && !var3.endsWith("dpi")) {
            var4 = Float.parseFloat(var3);
         } else {
            var4 = (float)Integer.parseInt(var3.substring(0, var3.length() - 3)) / 96.0F;
         }

         if (PrismSettings.verbose) {
            System.out.println(var2 + var4);
         }

         return var4;
      }
   }

   private static native void initIDs(float var0, float var1);

   WinApplication() {
      boolean var1 = (Boolean)AccessController.doPrivileged(() -> {
         return Boolean.getBoolean("javafx.embed.isEventThread");
      });
      if (!var1) {
         this.invokeLaterDispatcher = new InvokeLaterDispatcher(this);
         this.invokeLaterDispatcher.start();
      } else {
         this.invokeLaterDispatcher = null;
      }

   }

   private native long _init(int var1);

   private native void _setClassLoader(ClassLoader var1);

   private native void _runLoop(Runnable var1);

   private native void _terminateLoop();

   private static int getDesiredAwarenesslevel() {
      if (!PrismSettings.allowHiDPIScaling) {
         return 0;
      } else {
         String var0 = (String)AccessController.doPrivileged(() -> {
            return System.getProperty("javafx.glass.winDPIawareness");
         });
         if (var0 != null) {
            var0 = var0.toLowerCase();
            if (var0.equals("aware")) {
               return 1;
            } else if (var0.equals("permonitor")) {
               return 2;
            } else {
               if (!var0.equals("unaware")) {
                  System.err.println("unrecognized DPI awareness request, defaulting to unaware: " + var0);
               }

               return 0;
            }
         } else {
            return 2;
         }
      }
   }

   protected void runLoop(Runnable var1) {
      boolean var2 = (Boolean)AccessController.doPrivileged(() -> {
         return Boolean.getBoolean("javafx.embed.isEventThread");
      });
      int var3 = getDesiredAwarenesslevel();
      ClassLoader var4 = WinApplication.class.getClassLoader();
      this._setClassLoader(var4);
      if (var2) {
         this._init(var3);
         setEventThread(Thread.currentThread());
         var1.run();
      } else {
         long var5 = (Long)AccessController.doPrivileged(() -> {
            return Long.getLong("glass.win.stackSize", PlatformUtil.isWindows32Bit() ? 1048576L : 0L);
         });
         Thread var7 = (Thread)AccessController.doPrivileged(() -> {
            return new Thread((ThreadGroup)null, () -> {
               this._init(var3);
               this._runLoop(var1);
            }, "WindowsNativeRunloopThread", var5);
         });
         setEventThread(var7);
         var7.start();
      }
   }

   protected void finishTerminating() {
      Thread var1 = getEventThread();
      if (var1 != null) {
         this._terminateLoop();
         setEventThread((Thread)null);
      }

      super.finishTerminating();
   }

   public boolean shouldUpdateWindow() {
      return true;
   }

   private native Object _enterNestedEventLoopImpl();

   private native void _leaveNestedEventLoopImpl(Object var1);

   protected Object _enterNestedEventLoop() {
      if (this.invokeLaterDispatcher != null) {
         this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
      }

      Object var1;
      try {
         var1 = this._enterNestedEventLoopImpl();
      } finally {
         if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyLeftNestedEventLoop();
         }

      }

      return var1;
   }

   protected void _leaveNestedEventLoop(Object var1) {
      if (this.invokeLaterDispatcher != null) {
         this.invokeLaterDispatcher.notifyLeavingNestedEventLoop();
      }

      this._leaveNestedEventLoopImpl(var1);
   }

   public Window createWindow(Window var1, Screen var2, int var3) {
      return new WinWindow(var1, var2, var3);
   }

   public Window createWindow(long var1) {
      return new WinChildWindow(var1);
   }

   public View createView() {
      return new WinView();
   }

   public Cursor createCursor(int var1) {
      return new WinCursor(var1);
   }

   public Cursor createCursor(int var1, int var2, Pixels var3) {
      return new WinCursor(var1, var2, var3);
   }

   protected void staticCursor_setVisible(boolean var1) {
      WinCursor.setVisible_impl(var1);
   }

   protected Size staticCursor_getBestSize(int var1, int var2) {
      return WinCursor.getBestSize_impl(var1, var2);
   }

   public Pixels createPixels(int var1, int var2, ByteBuffer var3) {
      return new WinPixels(var1, var2, var3);
   }

   public Pixels createPixels(int var1, int var2, IntBuffer var3) {
      return new WinPixels(var1, var2, var3);
   }

   public Pixels createPixels(int var1, int var2, IntBuffer var3, float var4, float var5) {
      return new WinPixels(var1, var2, var3, var4, var5);
   }

   protected int staticPixels_getNativeFormat() {
      return WinPixels.getNativeFormat_impl();
   }

   public Robot createRobot() {
      return new WinRobot();
   }

   protected double staticScreen_getVideoRefreshPeriod() {
      return 0.0;
   }

   protected native Screen[] staticScreen_getScreens();

   public Timer createTimer(Runnable var1) {
      return new WinTimer(var1);
   }

   protected int staticTimer_getMinPeriod() {
      return WinTimer.getMinPeriod_impl();
   }

   protected int staticTimer_getMaxPeriod() {
      return WinTimer.getMaxPeriod_impl();
   }

   public Accessible createAccessible() {
      return new WinAccessible();
   }

   protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window var1, String var2, String var3, String var4, int var5, boolean var6, CommonDialogs.ExtensionFilter[] var7, int var8) {
      if (this.invokeLaterDispatcher != null) {
         this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
      }

      return WinCommonDialogs.showFileChooser_impl(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   protected File staticCommonDialogs_showFolderChooser(Window var1, String var2, String var3) {
      if (this.invokeLaterDispatcher != null) {
         this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
      }

      return WinCommonDialogs.showFolderChooser_impl(var1, var2, var3);
   }

   protected long staticView_getMultiClickTime() {
      return WinView.getMultiClickTime_impl();
   }

   protected int staticView_getMultiClickMaxX() {
      return WinView.getMultiClickMaxX_impl();
   }

   protected int staticView_getMultiClickMaxY() {
      return WinView.getMultiClickMaxY_impl();
   }

   protected native void _invokeAndWait(Runnable var1);

   private native void _submitForLaterInvocation(Runnable var1);

   public void submitForLaterInvocation(Runnable var1) {
      this._submitForLaterInvocation(var1);
   }

   protected void _invokeLater(Runnable var1) {
      if (this.invokeLaterDispatcher != null) {
         this.invokeLaterDispatcher.invokeLater(var1);
      } else {
         this.submitForLaterInvocation(var1);
      }

   }

   private native String _getHighContrastTheme();

   public String getHighContrastTheme() {
      checkEventThread();
      return this._getHighContrastTheme();
   }

   protected boolean _supportsInputMethods() {
      return true;
   }

   protected boolean _supportsTransparentWindows() {
      return true;
   }

   protected native boolean _supportsUnifiedWindows();

   public String getDataDirectory() {
      checkEventThread();
      String var1 = (String)AccessController.doPrivileged(() -> {
         return System.getenv("APPDATA");
      });
      return var1 != null && var1.length() != 0 ? var1 + File.separator + this.name + File.separator : super.getDataDirectory();
   }

   protected native int _getKeyCodeForChar(char var1);

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Void run() {
            WinApplication.verbose = Boolean.getBoolean("javafx.verbose");
            if (PrismSettings.allowHiDPIScaling) {
               WinApplication.overrideUIScale = WinApplication.getFloat("glass.win.uiScale", -1.0F, "Forcing UI scaling factor: ");
               WinApplication.minDPIScale = PrismSettings.winMinDPIScale;
               if (System.getProperty("glass.win.renderScale") != null) {
                  System.err.println("WARNING: glass.win.renderScale is ignored");
               }

               if (System.getProperty("glass.win.forceIntegerRenderScale") != null) {
                  System.err.println("WARNING: glass.win.forceIntegerRenderScale is ignored");
               }
            } else {
               WinApplication.overrideUIScale = 1.0F;
               WinApplication.minDPIScale = Float.MAX_VALUE;
            }

            Toolkit.loadMSWindowsLibraries();
            WinApplication.loadNativeLibrary();
            return null;
         }
      });
      initIDs(overrideUIScale, minDPIScale);
   }
}
