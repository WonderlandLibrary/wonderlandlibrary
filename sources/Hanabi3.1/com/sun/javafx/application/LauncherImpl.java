package com.sun.javafx.application;

import com.sun.javafx.jmx.MXExtension;
import com.sun.javafx.runtime.SystemProperties;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class LauncherImpl {
   public static final String LAUNCH_MODE_CLASS = "LM_CLASS";
   public static final String LAUNCH_MODE_JAR = "LM_JAR";
   private static final boolean trace = false;
   private static boolean verbose = false;
   private static final String MF_MAIN_CLASS = "Main-Class";
   private static final String MF_JAVAFX_MAIN = "JavaFX-Application-Class";
   private static final String MF_JAVAFX_PRELOADER = "JavaFX-Preloader-Class";
   private static final String MF_JAVAFX_CLASS_PATH = "JavaFX-Class-Path";
   private static final String MF_JAVAFX_FEATURE_PROXY = "JavaFX-Feature-Proxy";
   private static final String MF_JAVAFX_ARGUMENT_PREFIX = "JavaFX-Argument-";
   private static final String MF_JAVAFX_PARAMETER_NAME_PREFIX = "JavaFX-Parameter-Name-";
   private static final String MF_JAVAFX_PARAMETER_VALUE_PREFIX = "JavaFX-Parameter-Value-";
   private static final boolean simulateSlowProgress = false;
   private static AtomicBoolean launchCalled = new AtomicBoolean(false);
   private static final AtomicBoolean toolkitStarted = new AtomicBoolean(false);
   private static volatile RuntimeException launchException = null;
   private static Preloader currentPreloader = null;
   private static Class savedPreloaderClass = null;
   private static ClassLoader savedMainCcl = null;
   private static volatile boolean error = false;
   private static volatile Throwable pConstructorError = null;
   private static volatile Throwable pInitError = null;
   private static volatile Throwable pStartError = null;
   private static volatile Throwable pStopError = null;
   private static volatile Throwable constructorError = null;
   private static volatile Throwable initError = null;
   private static volatile Throwable startError = null;
   private static volatile Throwable stopError = null;
   private static Method notifyMethod = null;

   public static void launchApplication(Class var0, String[] var1) {
      Class var2 = savedPreloaderClass;
      if (var2 == null) {
         String var3 = (String)AccessController.doPrivileged(() -> {
            return System.getProperty("javafx.preloader");
         });
         if (var3 != null) {
            try {
               var2 = Class.forName(var3, false, var0.getClassLoader());
            } catch (Exception var5) {
               System.err.printf("Could not load preloader class '" + var3 + "', continuing without preloader.");
               var5.printStackTrace();
            }
         }
      }

      launchApplication(var0, var2, var1);
   }

   public static void launchApplication(Class var0, Class var1, String[] var2) {
      if (launchCalled.getAndSet(true)) {
         throw new IllegalStateException("Application launch must not be called more than once");
      } else if (!Application.class.isAssignableFrom(var0)) {
         throw new IllegalArgumentException("Error: " + var0.getName() + " is not a subclass of javafx.application.Application");
      } else if (var1 != null && !Preloader.class.isAssignableFrom(var1)) {
         throw new IllegalArgumentException("Error: " + var1.getName() + " is not a subclass of javafx.application.Preloader");
      } else {
         CountDownLatch var3 = new CountDownLatch(1);
         Thread var4 = new Thread(() -> {
            try {
               launchApplication1(var0, var1, var2);
            } catch (RuntimeException var10) {
               launchException = var10;
            } catch (Exception var11) {
               launchException = new RuntimeException("Application launch exception", var11);
            } catch (Error var12) {
               launchException = new RuntimeException("Application launch error", var12);
            } finally {
               var3.countDown();
            }

         });
         var4.setName("JavaFX-Launcher");
         var4.start();

         try {
            var3.await();
         } catch (InterruptedException var6) {
            throw new RuntimeException("Unexpected exception: ", var6);
         }

         if (launchException != null) {
            throw launchException;
         }
      }
   }

   public static void launchApplication(String var0, String var1, String[] var2) {
      if (verbose) {
         System.err.println("Java 8 launchApplication method: launchMode=" + var1);
      }

      String var3 = null;
      String var4 = null;
      String[] var5 = var2;
      ClassLoader var6 = null;
      verbose = Boolean.getBoolean("javafx.verbose");
      if (var1.equals("LM_JAR")) {
         Attributes var7 = getJarAttributes(var0);
         if (var7 == null) {
            abort((Throwable)null, "Can't get manifest attributes from jar");
         }

         String var8 = var7.getValue("JavaFX-Class-Path");
         if (var8 != null) {
            if (var8.trim().length() == 0) {
               var8 = null;
            } else {
               if (verbose) {
                  System.err.println("WARNING: Application jar uses deprecated JavaFX-Class-Path attribute. Please use Class-Path instead.");
               }

               var6 = setupJavaFXClassLoader(new File(var0), var8);
            }
         }

         String var9 = var7.getValue("JavaFX-Feature-Proxy");
         if (var9 != null && "auto".equals(var9.toLowerCase())) {
            trySetAutoProxy();
         }

         if (var2.length == 0) {
            var5 = getAppArguments(var7);
         }

         var3 = var7.getValue("JavaFX-Application-Class");
         if (var3 == null) {
            var3 = var7.getValue("Main-Class");
            if (var3 == null) {
               abort((Throwable)null, "JavaFX jar manifest requires a valid JavaFX-Appliation-Class or Main-Class entry");
            }
         }

         var3 = var3.trim();
         var4 = var7.getValue("JavaFX-Preloader-Class");
         if (var4 != null) {
            var4 = var4.trim();
         }
      } else if (var1.equals("LM_CLASS")) {
         var3 = var0;
         var4 = System.getProperty("javafx.preloader");
      } else {
         abort(new IllegalArgumentException("The launchMode argument must be one of LM_CLASS or LM_JAR"), "Invalid launch mode: %1$s", var1);
      }

      if (var3 == null) {
         abort((Throwable)null, "No main JavaFX class to launch");
      }

      if (var6 != null) {
         try {
            Class var11 = var6.loadClass(LauncherImpl.class.getName());
            Method var12 = var11.getMethod("launchApplicationWithArgs", String.class, String.class, (new String[0]).getClass());
            Thread.currentThread().setContextClassLoader(var6);
            var12.invoke((Object)null, var3, var4, var5);
         } catch (Exception var10) {
            abort(var10, "Exception while launching application");
         }
      } else {
         launchApplicationWithArgs(var3, var4, var5);
      }

   }

   public static void launchApplicationWithArgs(String var0, String var1, String[] var2) {
      try {
         startToolkit();
      } catch (InterruptedException var11) {
         abort(var11, "Toolkit initialization error", var0);
      }

      Class var4 = null;
      Class var5 = null;
      ClassLoader var6 = Thread.currentThread().getContextClassLoader();
      AtomicReference var7 = new AtomicReference();
      AtomicReference var8 = new AtomicReference();
      PlatformImpl.runAndWait(() -> {
         Class var5 = null;

         try {
            var5 = Class.forName(var0, true, var6);
         } catch (ClassNotFoundException var8x) {
            abort(var8x, "Missing JavaFX application class %1$s", var0);
         }

         var7.set(var5);
         if (var1 != null) {
            try {
               var5 = Class.forName(var1, true, var6);
            } catch (ClassNotFoundException var7x) {
               abort(var7x, "Missing JavaFX preloader class %1$s", var1);
            }

            if (!Preloader.class.isAssignableFrom(var5)) {
               abort((Throwable)null, "JavaFX preloader class %1$s does not extend javafx.application.Preloader", var5.getName());
            }

            var8.set(var5.asSubclass(Preloader.class));
         }

      });
      var4 = (Class)var8.get();
      var5 = (Class)var7.get();
      savedPreloaderClass = var4;
      Object var9 = null;

      try {
         Method var10 = var5.getMethod("main", (new String[0]).getClass());
         if (verbose) {
            System.err.println("Calling main(String[]) method");
         }

         savedMainCcl = Thread.currentThread().getContextClassLoader();
         var10.invoke((Object)null, var2);
      } catch (IllegalAccessException | NoSuchMethodException var12) {
         savedPreloaderClass = null;
         if (!Application.class.isAssignableFrom(var5)) {
            abort(var12, "JavaFX application class %1$s does not extend javafx.application.Application", var5.getName());
         }

         Class var3 = var5.asSubclass(Application.class);
         if (verbose) {
            System.err.println("Launching application directly");
         }

         launchApplication(var3, var4, var2);
      } catch (InvocationTargetException var13) {
         var13.printStackTrace();
         abort((Throwable)null, "Exception running application %1$s", var5.getName());
      }
   }

   private static URL fileToURL(File var0) throws IOException {
      return var0.getCanonicalFile().toURI().toURL();
   }

   private static ClassLoader setupJavaFXClassLoader(File var0, String var1) {
      try {
         File var2 = var0.getParentFile();
         ArrayList var3 = new ArrayList();
         String var4 = var1;
         int var5;
         if (var1 != null) {
            for(; var4.length() > 0; var4 = var4.substring(var5 + 1)) {
               var5 = var4.indexOf(" ");
               File var7;
               if (var5 < 0) {
                  var7 = var2 == null ? new File(var4) : new File(var2, var4);
                  if (var7.exists()) {
                     var3.add(fileToURL(var7));
                  } else if (verbose) {
                     System.err.println("Class Path entry \"" + var4 + "\" does not exist, ignoring");
                  }
                  break;
               }

               if (var5 > 0) {
                  String var6 = var4.substring(0, var5);
                  var7 = var2 == null ? new File(var6) : new File(var2, var6);
                  if (var7.exists()) {
                     var3.add(fileToURL(var7));
                  } else if (verbose) {
                     System.err.println("Class Path entry \"" + var6 + "\" does not exist, ignoring");
                  }
               }
            }
         }

         if (!var3.isEmpty()) {
            ArrayList var10 = new ArrayList();
            var4 = System.getProperty("java.class.path");
            int var11;
            if (var4 != null) {
               for(; var4.length() > 0; var4 = var4.substring(var11 + 1)) {
                  var11 = var4.indexOf(File.pathSeparatorChar);
                  if (var11 < 0) {
                     var10.add(fileToURL(new File(var4)));
                     break;
                  }

                  if (var11 > 0) {
                     String var13 = var4.substring(0, var11);
                     var10.add(fileToURL(new File(var13)));
                  }
               }
            }

            URL var12 = LauncherImpl.class.getProtectionDomain().getCodeSource().getLocation();
            var10.add(var12);
            var10.addAll(var3);
            URL[] var14 = (URL[])((URL[])var10.toArray(new URL[0]));
            if (verbose) {
               System.err.println("===== URL list");

               for(int var8 = 0; var8 < var14.length; ++var8) {
                  System.err.println("" + var14[var8]);
               }

               System.err.println("=====");
            }

            return new URLClassLoader(var14, (ClassLoader)null);
         }
      } catch (Exception var9) {
      }

      return null;
   }

   private static void trySetAutoProxy() {
      if (System.getProperty("http.proxyHost") == null && System.getProperty("https.proxyHost") == null && System.getProperty("ftp.proxyHost") == null && System.getProperty("socksProxyHost") == null) {
         if (System.getProperty("javafx.autoproxy.disable") != null) {
            if (verbose) {
               System.out.println("Disable autoproxy on request.");
            }

         } else {
            String var0 = System.getProperty("java.home");
            File var1 = new File(var0, "lib");
            File var2 = new File(var1, "deploy.jar");

            URL[] var3;
            try {
               var3 = new URL[]{var2.toURI().toURL()};
            } catch (MalformedURLException var14) {
               return;
            }

            try {
               URLClassLoader var4 = new URLClassLoader(var3);
               Class var5 = Class.forName("com.sun.deploy.services.ServiceManager", true, var4);
               Class[] var6 = new Class[]{Integer.TYPE};
               Method var7 = var5.getDeclaredMethod("setService", var6);
               String var8 = System.getProperty("os.name");
               String var9;
               if (var8.startsWith("Win")) {
                  var9 = "STANDALONE_TIGER_WIN32";
               } else if (var8.contains("Mac")) {
                  var9 = "STANDALONE_TIGER_MACOSX";
               } else {
                  var9 = "STANDALONE_TIGER_UNIX";
               }

               Object[] var10 = new Object[1];
               Class var11 = Class.forName("com.sun.deploy.services.PlatformType", true, var4);
               var10[0] = var11.getField(var9).get((Object)null);
               var7.invoke((Object)null, var10);
               Class var12 = Class.forName("com.sun.deploy.net.proxy.DeployProxySelector", true, var4);
               Method var13 = var12.getDeclaredMethod("reset");
               var13.invoke((Object)null);
               if (verbose) {
                  System.out.println("Autoconfig of proxy is completed.");
               }
            } catch (Exception var15) {
               if (verbose) {
                  System.err.println("Failed to autoconfig proxy due to " + var15);
               }
            }

         }
      } else {
         if (verbose) {
            System.out.println("Explicit proxy settings detected. Skip autoconfig.");
            System.out.println("  http.proxyHost=" + System.getProperty("http.proxyHost"));
            System.out.println("  https.proxyHost=" + System.getProperty("https.proxyHost"));
            System.out.println("  ftp.proxyHost=" + System.getProperty("ftp.proxyHost"));
            System.out.println("  socksProxyHost=" + System.getProperty("socksProxyHost"));
         }

      }
   }

   private static String decodeBase64(String var0) throws IOException {
      return new String(Base64.getDecoder().decode(var0));
   }

   private static String[] getAppArguments(Attributes var0) {
      LinkedList var1 = new LinkedList();

      try {
         int var2 = 1;

         for(String var3 = "JavaFX-Argument-"; var0.getValue(var3 + var2) != null; ++var2) {
            var1.add(decodeBase64(var0.getValue(var3 + var2)));
         }

         String var4 = "JavaFX-Parameter-Name-";
         String var5 = "JavaFX-Parameter-Value-";

         for(var2 = 1; var0.getValue(var4 + var2) != null; ++var2) {
            String var6 = decodeBase64(var0.getValue(var4 + var2));
            String var7 = null;
            if (var0.getValue(var5 + var2) != null) {
               var7 = decodeBase64(var0.getValue(var5 + var2));
            }

            var1.add("--" + var6 + "=" + (var7 != null ? var7 : ""));
         }
      } catch (IOException var8) {
         if (verbose) {
            System.err.println("Failed to extract application parameters");
         }

         var8.printStackTrace();
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }

   private static void abort(Throwable var0, String var1, Object... var2) {
      String var3 = String.format(var1, var2);
      if (var3 != null) {
         System.err.println(var3);
      }

      System.exit(1);
   }

   private static Attributes getJarAttributes(String var0) {
      JarFile var1 = null;

      try {
         var1 = new JarFile(var0);
         Manifest var2 = var1.getManifest();
         if (var2 == null) {
            abort((Throwable)null, "No manifest in jar file %1$s", var0);
         }

         Attributes var3 = var2.getMainAttributes();
         return var3;
      } catch (IOException var13) {
         abort(var13, "Error launching jar file %1%s", var0);
      } finally {
         try {
            var1.close();
         } catch (IOException var12) {
         }

      }

      return null;
   }

   private static void startToolkit() throws InterruptedException {
      if (!toolkitStarted.getAndSet(true)) {
         if (SystemProperties.isDebug()) {
            MXExtension.initializeIfAvailable();
         }

         CountDownLatch var0 = new CountDownLatch(1);
         PlatformImpl.startup(() -> {
            var0.countDown();
         });
         var0.await();
      }
   }

   private static void launchApplication1(Class var0, Class var1, String[] var2) throws Exception {
      startToolkit();
      if (savedMainCcl != null) {
         ClassLoader var3 = Thread.currentThread().getContextClassLoader();
         if (var3 != null && var3 != savedMainCcl) {
            PlatformImpl.runLater(() -> {
               Thread.currentThread().setContextClassLoader(var3);
            });
         }
      }

      final AtomicBoolean var23 = new AtomicBoolean(false);
      final AtomicBoolean var4 = new AtomicBoolean(false);
      final AtomicBoolean var5 = new AtomicBoolean(false);
      new AtomicBoolean(false);
      final CountDownLatch var7 = new CountDownLatch(1);
      final CountDownLatch var8 = new CountDownLatch(1);
      PlatformImpl.FinishListener var9 = new PlatformImpl.FinishListener() {
         public void idle(boolean var1) {
            if (var1) {
               if (var4.get()) {
                  var7.countDown();
               } else if (var23.get()) {
                  var8.countDown();
               }

            }
         }

         public void exitCalled() {
            var5.set(true);
            var7.countDown();
         }
      };
      PlatformImpl.addListener(var9);
      boolean var19 = false;

      try {
         var19 = true;
         AtomicReference var10 = new AtomicReference();
         if (var1 != null) {
            PlatformImpl.runAndWait(() -> {
               try {
                  Constructor var3 = var1.getConstructor();
                  var10.set(var3.newInstance());
                  ParametersImpl.registerParameters((Application)var10.get(), new ParametersImpl(var2));
               } catch (Throwable var4) {
                  System.err.println("Exception in Preloader constructor");
                  pConstructorError = var4;
                  error = true;
               }

            });
         }

         currentPreloader = (Preloader)var10.get();
         if (currentPreloader != null && !error && !var5.get()) {
            try {
               currentPreloader.init();
            } catch (Throwable var21) {
               System.err.println("Exception in Preloader init method");
               pInitError = var21;
               error = true;
            }
         }

         if (currentPreloader != null && !error && !var5.get()) {
            PlatformImpl.runAndWait(() -> {
               try {
                  var23.set(true);
                  Stage var1 = new Stage();
                  var1.impl_setPrimary(true);
                  currentPreloader.start(var1);
               } catch (Throwable var2) {
                  System.err.println("Exception in Preloader start method");
                  pStartError = var2;
                  error = true;
               }

            });
            if (!error && !var5.get()) {
               notifyProgress(currentPreloader, 0.0);
            }
         }

         AtomicReference var11 = new AtomicReference();
         if (!error && !var5.get()) {
            if (currentPreloader != null) {
               notifyProgress(currentPreloader, 1.0);
               notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_LOAD, (Application)null);
            }

            PlatformImpl.runAndWait(() -> {
               try {
                  Constructor var3 = var0.getConstructor();
                  var11.set(var3.newInstance());
                  ParametersImpl.registerParameters((Application)var11.get(), new ParametersImpl(var2));
                  PlatformImpl.setApplicationName(var0);
               } catch (Throwable var4) {
                  System.err.println("Exception in Application constructor");
                  constructorError = var4;
                  error = true;
               }

            });
         }

         Application var12 = (Application)var11.get();
         if (!error && !var5.get()) {
            if (currentPreloader != null) {
               notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_INIT, var12);
            }

            try {
               var12.init();
            } catch (Throwable var20) {
               System.err.println("Exception in Application init method");
               initError = var20;
               error = true;
            }
         }

         if (!error && !var5.get()) {
            if (currentPreloader != null) {
               notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_START, var12);
            }

            PlatformImpl.runAndWait(() -> {
               try {
                  var4.set(true);
                  Stage var2 = new Stage();
                  var2.impl_setPrimary(true);
                  var12.start(var2);
               } catch (Throwable var3) {
                  System.err.println("Exception in Application start method");
                  startError = var3;
                  error = true;
               }

            });
         }

         if (!error) {
            var7.await();
         }

         if (var4.get()) {
            PlatformImpl.runAndWait(() -> {
               try {
                  var12.stop();
               } catch (Throwable var2) {
                  System.err.println("Exception in Application stop method");
                  stopError = var2;
                  error = true;
               }

            });
         }

         if (error) {
            if (pConstructorError != null) {
               throw new RuntimeException("Unable to construct Preloader instance: " + var0, pConstructorError);
            }

            if (pInitError != null) {
               throw new RuntimeException("Exception in Preloader init method", pInitError);
            }

            if (pStartError != null) {
               throw new RuntimeException("Exception in Preloader start method", pStartError);
            }

            if (pStopError != null) {
               throw new RuntimeException("Exception in Preloader stop method", pStopError);
            }

            String var13;
            if (constructorError != null) {
               var13 = "Unable to construct Application instance: " + var0;
               if (!notifyError(var13, constructorError)) {
                  throw new RuntimeException(var13, constructorError);
               }

               var19 = false;
            } else if (initError != null) {
               var13 = "Exception in Application init method";
               if (!notifyError(var13, initError)) {
                  throw new RuntimeException(var13, initError);
               }

               var19 = false;
            } else if (startError != null) {
               var13 = "Exception in Application start method";
               if (!notifyError(var13, startError)) {
                  throw new RuntimeException(var13, startError);
               }

               var19 = false;
            } else if (stopError != null) {
               var13 = "Exception in Application stop method";
               if (!notifyError(var13, stopError)) {
                  throw new RuntimeException(var13, stopError);
               }

               var19 = false;
            } else {
               var19 = false;
            }
         } else {
            var19 = false;
         }
      } finally {
         if (var19) {
            PlatformImpl.removeListener(var9);
            boolean var15 = System.getSecurityManager() != null;
            if (error && var15) {
               System.err.println("Workaround until RT-13281 is implemented: keep toolkit alive");
            } else {
               PlatformImpl.tkExit();
            }

         }
      }

      PlatformImpl.removeListener(var9);
      boolean var24 = System.getSecurityManager() != null;
      if (error && var24) {
         System.err.println("Workaround until RT-13281 is implemented: keep toolkit alive");
      } else {
         PlatformImpl.tkExit();
      }

   }

   private static void notifyStateChange(Preloader var0, Preloader.StateChangeNotification.Type var1, Application var2) {
      PlatformImpl.runAndWait(() -> {
         var0.handleStateChangeNotification(new Preloader.StateChangeNotification(var1, var2));
      });
   }

   private static void notifyProgress(Preloader var0, double var1) {
      PlatformImpl.runAndWait(() -> {
         var0.handleProgressNotification(new Preloader.ProgressNotification(var1));
      });
   }

   private static boolean notifyError(String var0, Throwable var1) {
      AtomicBoolean var2 = new AtomicBoolean(false);
      PlatformImpl.runAndWait(() -> {
         if (currentPreloader != null) {
            try {
               Preloader.ErrorNotification var3 = new Preloader.ErrorNotification((String)null, var0, var1);
               boolean var4 = currentPreloader.handleErrorNotification(var3);
               var2.set(var4);
            } catch (Throwable var5) {
               var5.printStackTrace();
            }
         }

      });
      return var2.get();
   }

   private static void notifyCurrentPreloader(Preloader.PreloaderNotification var0) {
      PlatformImpl.runAndWait(() -> {
         if (currentPreloader != null) {
            currentPreloader.handleApplicationNotification(var0);
         }

      });
   }

   public static void notifyPreloader(Application var0, Preloader.PreloaderNotification var1) {
      if (launchCalled.get()) {
         notifyCurrentPreloader(var1);
      } else {
         Class var2 = LauncherImpl.class;
         synchronized(LauncherImpl.class) {
            if (notifyMethod == null) {
               try {
                  Class var4 = Class.forName("com.sun.deploy.uitoolkit.impl.fx.FXPreloader");
                  notifyMethod = var4.getMethod("notifyCurrentPreloader", Preloader.PreloaderNotification.class);
               } catch (Exception var7) {
                  var7.printStackTrace();
                  return;
               }
            }
         }

         try {
            notifyMethod.invoke((Object)null, var1);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

      }
   }

   private LauncherImpl() {
      throw new InternalError();
   }
}
