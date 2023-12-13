package com.sun.javafx.application;

import com.sun.glass.ui.Application;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.runtime.SystemProperties;
import com.sun.javafx.tk.TKListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.ConditionalFeature;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;

public class PlatformImpl {
   private static AtomicBoolean initialized = new AtomicBoolean(false);
   private static AtomicBoolean platformExit = new AtomicBoolean(false);
   private static AtomicBoolean toolkitExit = new AtomicBoolean(false);
   private static CountDownLatch startupLatch = new CountDownLatch(1);
   private static AtomicBoolean listenersRegistered = new AtomicBoolean(false);
   private static TKListener toolkitListener = null;
   private static volatile boolean implicitExit = true;
   private static boolean taskbarApplication = true;
   private static boolean contextual2DNavigation;
   private static AtomicInteger pendingRunnables = new AtomicInteger(0);
   private static AtomicInteger numWindows = new AtomicInteger(0);
   private static volatile boolean firstWindowShown = false;
   private static volatile boolean lastWindowClosed = false;
   private static AtomicBoolean reallyIdle = new AtomicBoolean(false);
   private static Set finishListeners = new CopyOnWriteArraySet();
   private static final Object runLaterLock = new Object();
   private static Boolean isGraphicsSupported;
   private static Boolean isControlsSupported;
   private static Boolean isMediaSupported;
   private static Boolean isWebSupported;
   private static Boolean isSWTSupported;
   private static Boolean isSwingSupported;
   private static Boolean isFXMLSupported;
   private static Boolean hasTwoLevelFocus;
   private static Boolean hasVirtualKeyboard;
   private static Boolean hasTouch;
   private static Boolean hasMultiTouch;
   private static Boolean hasPointer;
   private static boolean isThreadMerged = false;
   private static BooleanProperty accessibilityActive = new SimpleBooleanProperty();
   private static final CountDownLatch platformExitLatch = new CountDownLatch(1);
   private static boolean isModena = false;
   private static boolean isCaspian = false;
   private static String accessibilityTheme;

   public static void setTaskbarApplication(boolean var0) {
      taskbarApplication = var0;
   }

   public static boolean isTaskbarApplication() {
      return taskbarApplication;
   }

   public static void setApplicationName(Class var0) {
      runLater(() -> {
         Application.GetApplication().setName(var0.getName());
      });
   }

   public static boolean isContextual2DNavigation() {
      return contextual2DNavigation;
   }

   public static void startup(Runnable var0) {
      if (platformExit.get()) {
         throw new IllegalStateException("Platform.exit has been called");
      } else if (initialized.getAndSet(true)) {
         runLater(var0);
      } else {
         AccessController.doPrivileged(() -> {
            contextual2DNavigation = Boolean.getBoolean("com.sun.javafx.isContextual2DNavigation");
            String var0 = System.getProperty("com.sun.javafx.twoLevelFocus");
            if (var0 != null) {
               hasTwoLevelFocus = Boolean.valueOf(var0);
            }

            var0 = System.getProperty("com.sun.javafx.virtualKeyboard");
            if (var0 != null) {
               if (var0.equalsIgnoreCase("none")) {
                  hasVirtualKeyboard = false;
               } else if (var0.equalsIgnoreCase("javafx")) {
                  hasVirtualKeyboard = true;
               } else if (var0.equalsIgnoreCase("native")) {
                  hasVirtualKeyboard = true;
               }
            }

            var0 = System.getProperty("com.sun.javafx.touch");
            if (var0 != null) {
               hasTouch = Boolean.valueOf(var0);
            }

            var0 = System.getProperty("com.sun.javafx.multiTouch");
            if (var0 != null) {
               hasMultiTouch = Boolean.valueOf(var0);
            }

            var0 = System.getProperty("com.sun.javafx.pointer");
            if (var0 != null) {
               hasPointer = Boolean.valueOf(var0);
            }

            var0 = System.getProperty("javafx.embed.singleThread");
            if (var0 != null) {
               isThreadMerged = Boolean.valueOf(var0);
            }

            return null;
         });
         if (!taskbarApplication) {
            AccessController.doPrivileged(() -> {
               System.setProperty("glass.taskbarApplication", "false");
               return null;
            });
         }

         toolkitListener = new TKListener() {
            public void changedTopLevelWindows(List var1) {
               PlatformImpl.numWindows.set(var1.size());
               PlatformImpl.checkIdle();
            }

            public void exitedLastNestedLoop() {
               PlatformImpl.checkIdle();
            }
         };
         Toolkit.getToolkit().addTkListener(toolkitListener);
         Toolkit.getToolkit().startup(() -> {
            startupLatch.countDown();
            var0.run();
         });
         if (isThreadMerged) {
            installFwEventQueue();
         }

      }
   }

   private static void installFwEventQueue() {
      invokeSwingFXUtilsMethod("installFwEventQueue");
   }

   private static void removeFwEventQueue() {
      invokeSwingFXUtilsMethod("removeFwEventQueue");
   }

   private static void invokeSwingFXUtilsMethod(String var0) {
      try {
         Class var1 = Class.forName("javafx.embed.swing.SwingFXUtils");
         Method var2 = var1.getDeclaredMethod(var0);
         AccessController.doPrivileged(() -> {
            var2.setAccessible(true);
            return null;
         });
         waitForStart();
         var2.invoke((Object)null);
      } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException var3) {
         throw new RuntimeException("Property javafx.embed.singleThread is not supported");
      } catch (InvocationTargetException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static void waitForStart() {
      if (startupLatch.getCount() > 0L) {
         try {
            startupLatch.await();
         } catch (InterruptedException var1) {
            var1.printStackTrace();
         }
      }

   }

   public static boolean isFxApplicationThread() {
      return Toolkit.getToolkit().isFxUserThread();
   }

   public static void runLater(Runnable var0) {
      runLater(var0, false);
   }

   private static void runLater(Runnable var0, boolean var1) {
      if (!initialized.get()) {
         throw new IllegalStateException("Toolkit not initialized");
      } else {
         pendingRunnables.incrementAndGet();
         waitForStart();
         if (SystemProperties.isDebug()) {
            Toolkit.getToolkit().pauseCurrentThread();
         }

         synchronized(runLaterLock) {
            if (!var1 && toolkitExit.get()) {
               pendingRunnables.decrementAndGet();
            } else {
               AccessControlContext var3 = AccessController.getContext();
               Toolkit.getToolkit().defer(() -> {
                  try {
                     AccessController.doPrivileged(() -> {
                        var0.run();
                        return null;
                     }, var3);
                  } finally {
                     pendingRunnables.decrementAndGet();
                     checkIdle();
                  }

               });
            }
         }
      }
   }

   public static void runAndWait(Runnable var0) {
      runAndWait(var0, false);
   }

   private static void runAndWait(Runnable var0, boolean var1) {
      if (SystemProperties.isDebug()) {
         Toolkit.getToolkit().pauseCurrentThread();
      }

      if (isFxApplicationThread()) {
         try {
            var0.run();
         } catch (Throwable var5) {
            System.err.println("Exception in runnable");
            var5.printStackTrace();
         }
      } else {
         CountDownLatch var2 = new CountDownLatch(1);
         runLater(() -> {
            try {
               var0.run();
            } finally {
               var2.countDown();
            }

         }, var1);
         if (!var1 && toolkitExit.get()) {
            throw new IllegalStateException("Toolkit has exited");
         }

         try {
            var2.await();
         } catch (InterruptedException var4) {
            var4.printStackTrace();
         }
      }

   }

   public static void setImplicitExit(boolean var0) {
      implicitExit = var0;
      checkIdle();
   }

   public static boolean isImplicitExit() {
      return implicitExit;
   }

   public static void addListener(FinishListener var0) {
      listenersRegistered.set(true);
      finishListeners.add(var0);
   }

   public static void removeListener(FinishListener var0) {
      finishListeners.remove(var0);
      listenersRegistered.set(!finishListeners.isEmpty());
      if (!listenersRegistered.get()) {
         checkIdle();
      }

   }

   private static void notifyFinishListeners(boolean var0) {
      if (listenersRegistered.get()) {
         Iterator var1 = finishListeners.iterator();

         while(var1.hasNext()) {
            FinishListener var2 = (FinishListener)var1.next();
            if (var0) {
               var2.exitCalled();
            } else {
               var2.idle(implicitExit);
            }
         }
      } else if (implicitExit || platformExit.get()) {
         tkExit();
      }

   }

   private static void checkIdle() {
      if (initialized.get()) {
         if (!isFxApplicationThread()) {
            runLater(() -> {
            });
         } else {
            boolean var0 = false;
            Class var1 = PlatformImpl.class;
            synchronized(PlatformImpl.class) {
               int var2 = numWindows.get();
               if (var2 > 0) {
                  firstWindowShown = true;
                  lastWindowClosed = false;
                  reallyIdle.set(false);
               } else if (var2 == 0 && firstWindowShown) {
                  lastWindowClosed = true;
               }

               if (lastWindowClosed && pendingRunnables.get() == 0 && (toolkitExit.get() || !Toolkit.getToolkit().isNestedLoopRunning())) {
                  if (reallyIdle.getAndSet(true)) {
                     var0 = true;
                     lastWindowClosed = false;
                  } else {
                     runLater(() -> {
                     });
                  }
               }
            }

            if (var0) {
               notifyFinishListeners(false);
            }

         }
      }
   }

   static CountDownLatch test_getPlatformExitLatch() {
      return platformExitLatch;
   }

   public static void tkExit() {
      if (!toolkitExit.getAndSet(true)) {
         if (initialized.get()) {
            runAndWait(() -> {
               Toolkit.getToolkit().exit();
            }, true);
            if (isThreadMerged) {
               removeFwEventQueue();
            }

            Toolkit.getToolkit().removeTkListener(toolkitListener);
            toolkitListener = null;
            platformExitLatch.countDown();
         }

      }
   }

   public static BooleanProperty accessibilityActiveProperty() {
      return accessibilityActive;
   }

   public static void exit() {
      platformExit.set(true);
      notifyFinishListeners(true);
   }

   private static Boolean checkForClass(String var0) {
      try {
         Class.forName(var0, false, PlatformImpl.class.getClassLoader());
         return Boolean.TRUE;
      } catch (ClassNotFoundException var2) {
         return Boolean.FALSE;
      }
   }

   public static boolean isSupported(ConditionalFeature var0) {
      boolean var1 = isSupportedImpl(var0);
      if (var1 && var0 == ConditionalFeature.TRANSPARENT_WINDOW) {
         SecurityManager var2 = System.getSecurityManager();
         if (var2 != null) {
            try {
               var2.checkPermission(new AllPermission());
            } catch (SecurityException var4) {
               return false;
            }
         }

         return true;
      } else {
         return var1;
      }
   }

   public static void setDefaultPlatformUserAgentStylesheet() {
      setPlatformUserAgentStylesheet("MODENA");
   }

   public static boolean isModena() {
      return isModena;
   }

   public static boolean isCaspian() {
      return isCaspian;
   }

   public static void setPlatformUserAgentStylesheet(String var0) {
      if (isFxApplicationThread()) {
         _setPlatformUserAgentStylesheet(var0);
      } else {
         runLater(() -> {
            _setPlatformUserAgentStylesheet(var0);
         });
      }

   }

   public static boolean setAccessibilityTheme(String var0) {
      if (accessibilityTheme != null) {
         StyleManager.getInstance().removeUserAgentStylesheet(accessibilityTheme);
         accessibilityTheme = null;
      }

      _setAccessibilityTheme(var0);
      if (accessibilityTheme != null) {
         StyleManager.getInstance().addUserAgentStylesheet(accessibilityTheme);
         return true;
      } else {
         return false;
      }
   }

   private static void _setAccessibilityTheme(String var0) {
      String var1 = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("com.sun.javafx.highContrastTheme");
      });
      if (isCaspian()) {
         if (var0 != null || var1 != null) {
            accessibilityTheme = "com/sun/javafx/scene/control/skin/caspian/highcontrast.css";
         }
      } else if (isModena()) {
         if (var1 != null) {
            switch (var1.toUpperCase()) {
               case "BLACKONWHITE":
                  accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/blackOnWhite.css";
                  break;
               case "WHITEONBLACK":
                  accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css";
                  break;
               case "YELLOWONBLACK":
                  accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css";
            }
         } else if (var0 != null) {
            switch (var0) {
               case "High Contrast White":
                  accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/blackOnWhite.css";
                  break;
               case "High Contrast Black":
                  accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css";
                  break;
               case "High Contrast #1":
               case "High Contrast #2":
                  accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css";
            }
         }
      }

   }

   private static void _setPlatformUserAgentStylesheet(String var0) {
      isCaspian = false;
      isModena = false;
      String var1 = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("javafx.userAgentStylesheetUrl");
      });
      if (var1 != null) {
         var0 = var1;
      }

      ArrayList var2 = new ArrayList();
      if ("CASPIAN".equalsIgnoreCase(var0)) {
         isCaspian = true;
         var2.add("com/sun/javafx/scene/control/skin/caspian/caspian.css");
         if (isSupported(ConditionalFeature.INPUT_TOUCH)) {
            var2.add("com/sun/javafx/scene/control/skin/caspian/embedded.css");
            if (Utils.isQVGAScreen()) {
               var2.add("com/sun/javafx/scene/control/skin/caspian/embedded-qvga.css");
            }

            if (PlatformUtil.isAndroid()) {
               var2.add("com/sun/javafx/scene/control/skin/caspian/android.css");
            }
         }

         if (isSupported(ConditionalFeature.TWO_LEVEL_FOCUS)) {
            var2.add("com/sun/javafx/scene/control/skin/caspian/two-level-focus.css");
         }

         if (isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
            var2.add("com/sun/javafx/scene/control/skin/caspian/fxvk.css");
         }

         if (!isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
            var2.add("com/sun/javafx/scene/control/skin/caspian/caspian-no-transparency.css");
         }
      } else if ("MODENA".equalsIgnoreCase(var0)) {
         isModena = true;
         var2.add("com/sun/javafx/scene/control/skin/modena/modena.css");
         if (isSupported(ConditionalFeature.INPUT_TOUCH)) {
            var2.add("com/sun/javafx/scene/control/skin/modena/touch.css");
         }

         if (PlatformUtil.isEmbedded()) {
            var2.add("com/sun/javafx/scene/control/skin/modena/modena-embedded-performance.css");
         }

         if (PlatformUtil.isAndroid()) {
            var2.add("com/sun/javafx/scene/control/skin/modena/android.css");
         }

         if (isSupported(ConditionalFeature.TWO_LEVEL_FOCUS)) {
            var2.add("com/sun/javafx/scene/control/skin/modena/two-level-focus.css");
         }

         if (isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
            var2.add("com/sun/javafx/scene/control/skin/caspian/fxvk.css");
         }

         if (!isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
            var2.add("com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
         }
      } else {
         var2.add(var0);
      }

      _setAccessibilityTheme(Toolkit.getToolkit().getThemeName());
      if (accessibilityTheme != null) {
         var2.add(accessibilityTheme);
      }

      AccessController.doPrivileged(() -> {
         StyleManager.getInstance().setUserAgentStylesheets(var2);
         return null;
      });
   }

   public static void addNoTransparencyStylesheetToScene(Scene var0) {
      if (isCaspian()) {
         AccessController.doPrivileged(() -> {
            StyleManager.getInstance().addUserAgentStylesheet(var0, "com/sun/javafx/scene/control/skin/caspian/caspian-no-transparency.css");
            return null;
         });
      } else if (isModena()) {
         AccessController.doPrivileged(() -> {
            StyleManager.getInstance().addUserAgentStylesheet(var0, "com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
            return null;
         });
      }

   }

   private static boolean isSupportedImpl(ConditionalFeature var0) {
      switch (var0) {
         case GRAPHICS:
            if (isGraphicsSupported == null) {
               isGraphicsSupported = checkForClass("javafx.stage.Stage");
            }

            return isGraphicsSupported;
         case CONTROLS:
            if (isControlsSupported == null) {
               isControlsSupported = checkForClass("javafx.scene.control.Control");
            }

            return isControlsSupported;
         case MEDIA:
            if (isMediaSupported == null) {
               isMediaSupported = checkForClass("javafx.scene.media.MediaView");
               if (isMediaSupported && PlatformUtil.isEmbedded()) {
                  AccessController.doPrivileged(() -> {
                     String var0 = System.getProperty("com.sun.javafx.experimental.embedded.media", "false");
                     isMediaSupported = Boolean.valueOf(var0);
                     return null;
                  });
               }
            }

            return isMediaSupported;
         case WEB:
            if (isWebSupported == null) {
               isWebSupported = checkForClass("javafx.scene.web.WebView");
               if (isWebSupported && PlatformUtil.isEmbedded()) {
                  AccessController.doPrivileged(() -> {
                     String var0 = System.getProperty("com.sun.javafx.experimental.embedded.web", "false");
                     isWebSupported = Boolean.valueOf(var0);
                     return null;
                  });
               }
            }

            return isWebSupported;
         case SWT:
            if (isSWTSupported == null) {
               isSWTSupported = checkForClass("javafx.embed.swt.FXCanvas");
            }

            return isSWTSupported;
         case SWING:
            if (isSwingSupported == null) {
               isSwingSupported = checkForClass("javax.swing.JComponent") && checkForClass("javafx.embed.swing.JFXPanel");
            }

            return isSwingSupported;
         case FXML:
            if (isFXMLSupported == null) {
               isFXMLSupported = checkForClass("javafx.fxml.FXMLLoader") && checkForClass("javax.xml.stream.XMLInputFactory");
            }

            return isFXMLSupported;
         case TWO_LEVEL_FOCUS:
            if (hasTwoLevelFocus == null) {
               return Toolkit.getToolkit().isSupported(var0);
            }

            return hasTwoLevelFocus;
         case VIRTUAL_KEYBOARD:
            if (hasVirtualKeyboard == null) {
               return Toolkit.getToolkit().isSupported(var0);
            }

            return hasVirtualKeyboard;
         case INPUT_TOUCH:
            if (hasTouch == null) {
               return Toolkit.getToolkit().isSupported(var0);
            }

            return hasTouch;
         case INPUT_MULTITOUCH:
            if (hasMultiTouch == null) {
               return Toolkit.getToolkit().isSupported(var0);
            }

            return hasMultiTouch;
         case INPUT_POINTER:
            if (hasPointer == null) {
               return Toolkit.getToolkit().isSupported(var0);
            }

            return hasPointer;
         default:
            return Toolkit.getToolkit().isSupported(var0);
      }
   }

   public interface FinishListener {
      void idle(boolean var1);

      void exitCalled();
   }
}
