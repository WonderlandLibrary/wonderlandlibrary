package org.lwjgl.opencl;

import java.util.HashMap;
import java.util.Map;

final class CallbackUtil {
   private static final Map contextUserData = new HashMap();

   private CallbackUtil() {
   }

   static long createGlobalRef(Object var0) {
      return var0 == null ? 0L : ncreateGlobalRef(var0);
   }

   private static native long ncreateGlobalRef(Object var0);

   static native void deleteGlobalRef(long var0);

   static void checkCallback(int var0, long var1) {
      if (var0 != 0 && var1 != 0L) {
         deleteGlobalRef(var1);
      }

   }

   static native long getContextCallback();

   static native long getMemObjectDestructorCallback();

   static native long getProgramCallback();

   static native long getNativeKernelCallback();

   static native long getEventCallback();

   static native long getPrintfCallback();

   static native long getLogMessageToSystemLogAPPLE();

   static native long getLogMessageToStdoutAPPLE();

   static native long getLogMessageToStderrAPPLE();
}
