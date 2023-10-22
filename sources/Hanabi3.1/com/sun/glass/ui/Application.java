package com.sun.glass.ui;

import com.sun.glass.utils.NativeLibLoader;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public abstract class Application {
   private static final String DEFAULT_NAME = "java";
   protected String name = "java";
   private EventHandler eventHandler;
   private boolean initialActiveEventReceived = false;
   private String[] initialOpenedFiles = null;
   private static boolean loaded = false;
   private static Application application;
   private static Thread eventThread;
   private static final boolean disableThreadChecks = (Boolean)AccessController.doPrivileged(() -> {
      String var0 = System.getProperty("glass.disableThreadChecks", "false");
      return "true".equalsIgnoreCase(var0);
   });
   private static volatile Map deviceDetails = null;
   private boolean terminateWhenLastWindowClosed = true;
   private static int nestedEventLoopCounter = 0;

   protected static synchronized void loadNativeLibrary(String var0) {
      if (!loaded) {
         NativeLibLoader.loadLibrary(var0);
         loaded = true;
      }

   }

   protected static synchronized void loadNativeLibrary() {
      loadNativeLibrary("glass");
   }

   public static void setDeviceDetails(Map var0) {
      deviceDetails = var0;
   }

   public static Map getDeviceDetails() {
      return deviceDetails;
   }

   protected Application() {
   }

   public static void run(Runnable var0) {
      if (application != null) {
         throw new IllegalStateException("Application is already running");
      } else {
         application = PlatformFactory.getPlatformFactory().createApplication();

         try {
            application.runLoop(() -> {
               Screen.initScreens();
               var0.run();
            });
         } catch (Throwable var2) {
            var2.printStackTrace();
         }

      }
   }

   protected abstract void runLoop(Runnable var1);

   protected void finishTerminating() {
      application = null;
   }

   public String getName() {
      checkEventThread();
      return this.name;
   }

   public void setName(String var1) {
      checkEventThread();
      if (var1 != null && "java".equals(this.name)) {
         this.name = var1;
      }

   }

   public String getDataDirectory() {
      checkEventThread();
      String var1 = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("user.home");
      });
      return var1 + File.separator + "." + this.name + File.separator;
   }

   protected void notifyWillFinishLaunching() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleWillFinishLaunchingAction(this, System.nanoTime());
      }

   }

   protected void notifyDidFinishLaunching() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleDidFinishLaunchingAction(this, System.nanoTime());
      }

   }

   protected void notifyWillBecomeActive() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleWillBecomeActiveAction(this, System.nanoTime());
      }

   }

   protected void notifyDidBecomeActive() {
      this.initialActiveEventReceived = true;
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleDidBecomeActiveAction(this, System.nanoTime());
      }

   }

   protected void notifyWillResignActive() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleWillResignActiveAction(this, System.nanoTime());
      }

   }

   protected boolean notifyThemeChanged(String var1) {
      EventHandler var2 = this.getEventHandler();
      return var2 != null ? var2.handleThemeChanged(var1) : false;
   }

   protected void notifyDidResignActive() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleDidResignActiveAction(this, System.nanoTime());
      }

   }

   protected void notifyDidReceiveMemoryWarning() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleDidReceiveMemoryWarning(this, System.nanoTime());
      }

   }

   protected void notifyWillHide() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleWillHideAction(this, System.nanoTime());
      }

   }

   protected void notifyDidHide() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleDidHideAction(this, System.nanoTime());
      }

   }

   protected void notifyWillUnhide() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleWillUnhideAction(this, System.nanoTime());
      }

   }

   protected void notifyDidUnhide() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleDidUnhideAction(this, System.nanoTime());
      }

   }

   protected void notifyOpenFiles(String[] var1) {
      if (!this.initialActiveEventReceived && this.initialOpenedFiles == null) {
         this.initialOpenedFiles = var1;
      }

      EventHandler var2 = this.getEventHandler();
      if (var2 != null && var1 != null) {
         var2.handleOpenFilesAction(this, System.nanoTime(), var1);
      }

   }

   protected void notifyWillQuit() {
      EventHandler var1 = this.getEventHandler();
      if (var1 != null) {
         var1.handleQuitAction(this, System.nanoTime());
      }

   }

   public void installDefaultMenus(MenuBar var1) {
      checkEventThread();
   }

   public EventHandler getEventHandler() {
      return this.eventHandler;
   }

   public void setEventHandler(EventHandler var1) {
      checkEventThread();
      boolean var2 = this.eventHandler != null && this.initialOpenedFiles != null;
      this.eventHandler = var1;
      if (var2) {
         this.notifyOpenFiles(this.initialOpenedFiles);
      }

   }

   public final boolean shouldTerminateWhenLastWindowClosed() {
      checkEventThread();
      return this.terminateWhenLastWindowClosed;
   }

   public final void setTerminateWhenLastWindowClosed(boolean var1) {
      checkEventThread();
      this.terminateWhenLastWindowClosed = var1;
   }

   public boolean shouldUpdateWindow() {
      checkEventThread();
      return false;
   }

   public boolean hasWindowManager() {
      return true;
   }

   public void notifyRenderingFinished() {
   }

   public void terminate() {
      checkEventThread();

      try {
         LinkedList var1 = new LinkedList(Window.getWindows());
         Iterator var2 = var1.iterator();

         Window var3;
         while(var2.hasNext()) {
            var3 = (Window)var2.next();
            var3.setVisible(false);
         }

         var2 = var1.iterator();

         while(var2.hasNext()) {
            var3 = (Window)var2.next();
            var3.close();
         }
      } catch (Throwable var7) {
         var7.printStackTrace();
      } finally {
         this.finishTerminating();
      }

   }

   public static Application GetApplication() {
      return application;
   }

   protected static void setEventThread(Thread var0) {
      eventThread = var0;
   }

   protected static Thread getEventThread() {
      return eventThread;
   }

   public static boolean isEventThread() {
      return Thread.currentThread() == eventThread;
   }

   public static void checkEventThread() {
      if (!disableThreadChecks && Thread.currentThread() != eventThread) {
         throw new IllegalStateException("This operation is permitted on the event thread only; currentThread = " + Thread.currentThread().getName());
      }
   }

   public static void reportException(Throwable var0) {
      Thread var1 = Thread.currentThread();
      Thread.UncaughtExceptionHandler var2 = var1.getUncaughtExceptionHandler();
      var2.uncaughtException(var1, var0);
   }

   protected abstract void _invokeAndWait(Runnable var1);

   public static void invokeAndWait(Runnable var0) {
      if (var0 != null) {
         if (isEventThread()) {
            var0.run();
         } else {
            GetApplication()._invokeAndWait(var0);
         }

      }
   }

   protected abstract void _invokeLater(Runnable var1);

   public static void invokeLater(Runnable var0) {
      if (var0 != null) {
         GetApplication()._invokeLater(var0);
      }
   }

   protected abstract Object _enterNestedEventLoop();

   protected abstract void _leaveNestedEventLoop(Object var1);

   static Object enterNestedEventLoop() {
      checkEventThread();
      ++nestedEventLoopCounter;

      Object var0;
      try {
         var0 = GetApplication()._enterNestedEventLoop();
      } finally {
         --nestedEventLoopCounter;
      }

      return var0;
   }

   static void leaveNestedEventLoop(Object var0) {
      checkEventThread();
      if (nestedEventLoopCounter == 0) {
         throw new IllegalStateException("Not in a nested event loop");
      } else {
         GetApplication()._leaveNestedEventLoop(var0);
      }
   }

   public static boolean isNestedLoopRunning() {
      checkEventThread();
      return nestedEventLoopCounter > 0;
   }

   public void menuAboutAction() {
      System.err.println("about");
   }

   public abstract Window createWindow(Window var1, Screen var2, int var3);

   public final Window createWindow(Screen var1, int var2) {
      return this.createWindow((Window)null, var1, var2);
   }

   public abstract Window createWindow(long var1);

   public abstract View createView();

   public abstract Cursor createCursor(int var1);

   public abstract Cursor createCursor(int var1, int var2, Pixels var3);

   protected abstract void staticCursor_setVisible(boolean var1);

   protected abstract Size staticCursor_getBestSize(int var1, int var2);

   public final Menu createMenu(String var1) {
      return new Menu(var1);
   }

   public final Menu createMenu(String var1, boolean var2) {
      return new Menu(var1, var2);
   }

   public final MenuBar createMenuBar() {
      return new MenuBar();
   }

   public final MenuItem createMenuItem(String var1) {
      return this.createMenuItem(var1, (MenuItem.Callback)null);
   }

   public final MenuItem createMenuItem(String var1, MenuItem.Callback var2) {
      return this.createMenuItem(var1, var2, 0, 0);
   }

   public final MenuItem createMenuItem(String var1, MenuItem.Callback var2, int var3, int var4) {
      return this.createMenuItem(var1, var2, var3, var4, (Pixels)null);
   }

   public final MenuItem createMenuItem(String var1, MenuItem.Callback var2, int var3, int var4, Pixels var5) {
      return new MenuItem(var1, var2, var3, var4, var5);
   }

   public abstract Pixels createPixels(int var1, int var2, ByteBuffer var3);

   public abstract Pixels createPixels(int var1, int var2, IntBuffer var3);

   public abstract Pixels createPixels(int var1, int var2, IntBuffer var3, float var4, float var5);

   protected abstract int staticPixels_getNativeFormat();

   static Pixels createPixels(int var0, int var1, int[] var2, float var3, float var4) {
      return GetApplication().createPixels(var0, var1, IntBuffer.wrap(var2), var3, var4);
   }

   static float getScaleFactor(int var0, int var1, int var2, int var3) {
      float var4 = 0.0F;
      Iterator var5 = Screen.getScreens().iterator();

      while(var5.hasNext()) {
         Screen var6 = (Screen)var5.next();
         int var7 = var6.getX();
         int var8 = var6.getY();
         int var9 = var6.getWidth();
         int var10 = var6.getHeight();
         if (var0 < var7 + var9 && var0 + var2 > var7 && var1 < var8 + var10 && var1 + var3 > var8) {
            if (var4 < var6.getRecommendedOutputScaleX()) {
               var4 = var6.getRecommendedOutputScaleX();
            }

            if (var4 < var6.getRecommendedOutputScaleY()) {
               var4 = var6.getRecommendedOutputScaleY();
            }
         }
      }

      return var4 == 0.0F ? 1.0F : var4;
   }

   public abstract Robot createRobot();

   protected abstract double staticScreen_getVideoRefreshPeriod();

   protected abstract Screen[] staticScreen_getScreens();

   public abstract Timer createTimer(Runnable var1);

   protected abstract int staticTimer_getMinPeriod();

   protected abstract int staticTimer_getMaxPeriod();

   public final EventLoop createEventLoop() {
      return new EventLoop();
   }

   public Accessible createAccessible() {
      return null;
   }

   protected abstract CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window var1, String var2, String var3, String var4, int var5, boolean var6, CommonDialogs.ExtensionFilter[] var7, int var8);

   protected abstract File staticCommonDialogs_showFolderChooser(Window var1, String var2, String var3);

   protected abstract long staticView_getMultiClickTime();

   protected abstract int staticView_getMultiClickMaxX();

   protected abstract int staticView_getMultiClickMaxY();

   public String getHighContrastTheme() {
      checkEventThread();
      return null;
   }

   protected boolean _supportsInputMethods() {
      return false;
   }

   public final boolean supportsInputMethods() {
      checkEventThread();
      return this._supportsInputMethods();
   }

   protected abstract boolean _supportsTransparentWindows();

   public final boolean supportsTransparentWindows() {
      checkEventThread();
      return this._supportsTransparentWindows();
   }

   public boolean hasTwoLevelFocus() {
      return false;
   }

   public boolean hasVirtualKeyboard() {
      return false;
   }

   public boolean hasTouch() {
      return false;
   }

   public boolean hasMultiTouch() {
      return false;
   }

   public boolean hasPointer() {
      return true;
   }

   protected abstract boolean _supportsUnifiedWindows();

   public final boolean supportsUnifiedWindows() {
      checkEventThread();
      return this._supportsUnifiedWindows();
   }

   protected boolean _supportsSystemMenu() {
      return false;
   }

   public final boolean supportsSystemMenu() {
      checkEventThread();
      return this._supportsSystemMenu();
   }

   protected abstract int _getKeyCodeForChar(char var1);

   public static int getKeyCodeForChar(char var0) {
      return application._getKeyCodeForChar(var0);
   }

   public static class EventHandler {
      public void handleWillFinishLaunchingAction(Application var1, long var2) {
      }

      public void handleDidFinishLaunchingAction(Application var1, long var2) {
      }

      public void handleWillBecomeActiveAction(Application var1, long var2) {
      }

      public void handleDidBecomeActiveAction(Application var1, long var2) {
      }

      public void handleWillResignActiveAction(Application var1, long var2) {
      }

      public void handleDidResignActiveAction(Application var1, long var2) {
      }

      public void handleDidReceiveMemoryWarning(Application var1, long var2) {
      }

      public void handleWillHideAction(Application var1, long var2) {
      }

      public void handleDidHideAction(Application var1, long var2) {
      }

      public void handleWillUnhideAction(Application var1, long var2) {
      }

      public void handleDidUnhideAction(Application var1, long var2) {
      }

      public void handleOpenFilesAction(Application var1, long var2, String[] var4) {
      }

      public void handleQuitAction(Application var1, long var2) {
      }

      public boolean handleThemeChanged(String var1) {
         return false;
      }
   }
}
