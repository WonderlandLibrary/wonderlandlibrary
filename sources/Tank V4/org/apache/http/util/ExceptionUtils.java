package org.apache.http.util;

import java.lang.reflect.Method;

/** @deprecated */
@Deprecated
public final class ExceptionUtils {
   private static final Method INIT_CAUSE_METHOD = getInitCauseMethod();

   private static Method getInitCauseMethod() {
      try {
         Class[] var0 = new Class[]{Throwable.class};
         return Throwable.class.getMethod("initCause", var0);
      } catch (NoSuchMethodException var1) {
         return null;
      }
   }

   public static void initCause(Throwable var0, Throwable var1) {
      if (INIT_CAUSE_METHOD != null) {
         try {
            INIT_CAUSE_METHOD.invoke(var0, var1);
         } catch (Exception var3) {
         }
      }

   }

   private ExceptionUtils() {
   }
}
