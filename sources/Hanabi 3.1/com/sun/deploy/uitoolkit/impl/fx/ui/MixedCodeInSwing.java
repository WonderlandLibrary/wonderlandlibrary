package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.ui.AppInfo;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

public class MixedCodeInSwing {
   private static boolean haveAppContext = false;
   private static Class sysUtils;
   private static Class tClass;
   private static Constructor cMethod;
   private static Method setContentMethod;
   private static Method getDialogMethod;
   private static Method setVisibleMethod;
   private static Method getAnswerMethod;
   private static Method disposeMethod;
   private static Method createSysThreadMethod;

   public static int show(Object var0, AppInfo var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8, boolean var9, String var10) {
      Helper var11 = new Helper((Component)null, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);

      try {
         Thread var12 = (Thread)createSysThreadMethod.invoke((Object)null, var11);
         var12.start();
         var12.join();
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      return var11.getAnswer();
   }

   private static void placeWindow(Window var0) {
      Rectangle var1 = getMouseScreenBounds();
      Rectangle var2 = var0.getBounds();
      var0.setLocation((var1.width - var2.width) / 2, (var1.height - var2.height) / 2);
   }

   public static Rectangle getMouseScreenBounds() {
      Point var0 = MouseInfo.getPointerInfo().getLocation();
      GraphicsDevice[] var1 = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         Rectangle var3 = var1[var2].getDefaultConfiguration().getBounds();
         if (var0.x >= var3.x && var0.y >= var3.y && var0.x <= var3.x + var3.width && var0.y <= var3.y + var3.height) {
            return var3;
         }
      }

      return new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize());
   }

   static {
      try {
         tClass = Class.forName("com.sun.deploy.ui.DialogTemplate", true, (ClassLoader)null);
         cMethod = tClass.getDeclaredConstructor(AppInfo.class, Component.class, String.class, String.class, Boolean.TYPE);
         cMethod.setAccessible(true);
         setContentMethod = tClass.getDeclaredMethod("setMixedCodeContent", String.class, Boolean.TYPE, String.class, String.class, String.class, String.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE, String.class);
         setContentMethod.setAccessible(true);
         getDialogMethod = tClass.getDeclaredMethod("getDialog");
         getDialogMethod.setAccessible(true);
         setVisibleMethod = tClass.getDeclaredMethod("setVisible", Boolean.TYPE);
         setVisibleMethod.setAccessible(true);
         disposeMethod = tClass.getDeclaredMethod("disposeDialog");
         disposeMethod.setAccessible(true);
         getAnswerMethod = tClass.getDeclaredMethod("getUserAnswer");
         getAnswerMethod.setAccessible(true);
         sysUtils = Class.forName("sun.plugin.util.PluginSysUtil", false, (ClassLoader)null);
         createSysThreadMethod = sysUtils.getMethod("createPluginSysThread", Runnable.class);
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   static class Helper implements Runnable {
      private AppInfo appInfo;
      private String title;
      private String masthead;
      private String message;
      private String info;
      private String okBtnStr;
      private String cancelBtnStr;
      private boolean useWarning;
      private boolean isJsDialog;
      private int userAnswer = -1;
      private String showAlways;

      Helper(Component var1, AppInfo var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9, boolean var10, String var11) {
         this.appInfo = var2 == null ? new AppInfo() : var2;
         this.title = var3;
         this.masthead = var4;
         this.message = var5;
         this.info = var6;
         this.okBtnStr = var7;
         this.cancelBtnStr = var8;
         this.useWarning = var9;
         this.isJsDialog = var10;
         this.showAlways = var11;
      }

      public void run() {
         Class var1 = MixedCodeInSwing.class;
         synchronized(MixedCodeInSwing.class) {
            if (!MixedCodeInSwing.haveAppContext) {
               AppContext var2 = SunToolkit.createNewAppContext();
               MixedCodeInSwing.haveAppContext = true;
            }
         }

         try {
            SwingUtilities.invokeAndWait(new Runnable() {
               public void run() {
                  try {
                     Object var1 = MixedCodeInSwing.cMethod.newInstance(Helper.this.appInfo, null, Helper.this.title, Helper.this.masthead, false);
                     MixedCodeInSwing.setContentMethod.invoke(var1, null, false, Helper.this.message, Helper.this.info, Helper.this.okBtnStr, Helper.this.cancelBtnStr, true, Helper.this.useWarning, Helper.this.isJsDialog, Helper.this.showAlways);
                     MixedCodeInSwing.setVisibleMethod.invoke(var1, true);
                     Helper.this.userAnswer = (Integer)MixedCodeInSwing.getAnswerMethod.invoke(var1);
                     MixedCodeInSwing.disposeMethod.invoke(var1);
                  } catch (Throwable var2) {
                     var2.printStackTrace();
                  }

               }
            });
         } catch (InterruptedException var4) {
            var4.printStackTrace();
         } catch (InvocationTargetException var5) {
            var5.printStackTrace();
         } catch (Throwable var6) {
            var6.printStackTrace();
         }

      }

      int getAnswer() {
         return this.userAnswer;
      }
   }
}
