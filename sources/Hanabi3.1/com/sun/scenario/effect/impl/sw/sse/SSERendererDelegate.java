package com.sun.scenario.effect.impl.sw.sse;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.sw.RendererDelegate;
import java.security.AccessController;

public class SSERendererDelegate implements RendererDelegate {
   public static native boolean isSupported();

   public SSERendererDelegate() {
      if (!isSupported()) {
         throw new UnsupportedOperationException("required instruction set (SSE2) not supported on this processor");
      }
   }

   public Effect.AccelType getAccelType() {
      return Effect.AccelType.SIMD;
   }

   public String getPlatformPeerName(String var1, int var2) {
      return "com.sun.scenario.effect.impl.sw.sse.SSE" + var1 + "Peer";
   }

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("decora_sse");
         return null;
      });
   }
}
