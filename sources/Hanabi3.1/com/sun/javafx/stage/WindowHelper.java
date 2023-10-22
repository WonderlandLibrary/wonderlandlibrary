package com.sun.javafx.stage;

import java.security.AccessControlContext;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.stage.Window;

public final class WindowHelper {
   private static WindowAccessor windowAccessor;

   private WindowHelper() {
   }

   public static void notifyLocationChanged(Window var0, double var1, double var3) {
      windowAccessor.notifyLocationChanged(var0, var1, var3);
   }

   public static void notifySizeChanged(Window var0, double var1, double var3) {
      windowAccessor.notifySizeChanged(var0, var1, var3);
   }

   public static void notifyScaleChanged(Window var0, double var1, double var3) {
      windowAccessor.notifyScaleChanged(var0, var1, var3);
   }

   static AccessControlContext getAccessControlContext(Window var0) {
      return windowAccessor.getAccessControlContext(var0);
   }

   public static void setWindowAccessor(WindowAccessor var0) {
      if (windowAccessor != null) {
         throw new IllegalStateException();
      } else {
         windowAccessor = var0;
      }
   }

   public static WindowAccessor getWindowAccessor() {
      return windowAccessor;
   }

   private static void forceInit(Class var0) {
      try {
         Class.forName(var0.getName(), true, var0.getClassLoader());
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   static {
      forceInit(Window.class);
   }

   public interface WindowAccessor {
      void notifyLocationChanged(Window var1, double var2, double var4);

      void notifySizeChanged(Window var1, double var2, double var4);

      void notifyScreenChanged(Window var1, Object var2, Object var3);

      float getRenderScale(Window var1);

      float getPlatformScaleX(Window var1);

      float getPlatformScaleY(Window var1);

      void notifyScaleChanged(Window var1, double var2, double var4);

      ReadOnlyObjectProperty screenProperty(Window var1);

      AccessControlContext getAccessControlContext(Window var1);
   }
}
