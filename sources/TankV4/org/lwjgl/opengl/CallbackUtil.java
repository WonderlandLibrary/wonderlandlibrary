package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;

final class CallbackUtil {
   private static final Map contextUserParamsARB = new HashMap();
   private static final Map contextUserParamsAMD = new HashMap();
   private static final Map contextUserParamsKHR = new HashMap();

   private CallbackUtil() {
   }

   static long createGlobalRef(Object var0) {
      return var0 == null ? 0L : ncreateGlobalRef(var0);
   }

   private static native long ncreateGlobalRef(Object var0);

   private static native void deleteGlobalRef(long var0);

   private static void registerContextCallback(long var0, Map var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      if (var3 == null) {
         deleteGlobalRef(var0);
         throw new IllegalStateException("No context is current.");
      } else {
         Long var4 = (Long)var2.remove(var3);
         if (var4 != null) {
            deleteGlobalRef(var4);
         }

         if (var0 != 0L) {
            var2.put(var3, var0);
         }

      }
   }

   static void unregisterCallbacks(Object var0) {
      ContextCapabilities var1 = GLContext.getCapabilities(var0);
      Long var2 = (Long)contextUserParamsARB.remove(var1);
      if (var2 != null) {
         deleteGlobalRef(var2);
      }

      var2 = (Long)contextUserParamsAMD.remove(var1);
      if (var2 != null) {
         deleteGlobalRef(var2);
      }

      var2 = (Long)contextUserParamsKHR.remove(var1);
      if (var2 != null) {
         deleteGlobalRef(var2);
      }

   }

   static native long getDebugOutputCallbackARB();

   static void registerContextCallbackARB(long var0) {
      registerContextCallback(var0, contextUserParamsARB);
   }

   static native long getDebugOutputCallbackAMD();

   static void registerContextCallbackAMD(long var0) {
      registerContextCallback(var0, contextUserParamsAMD);
   }

   static native long getDebugCallbackKHR();

   static void registerContextCallbackKHR(long var0) {
      registerContextCallback(var0, contextUserParamsKHR);
   }
}
