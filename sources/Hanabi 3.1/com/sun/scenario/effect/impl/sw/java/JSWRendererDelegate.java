package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.sw.RendererDelegate;

public class JSWRendererDelegate implements RendererDelegate {
   public Effect.AccelType getAccelType() {
      return Effect.AccelType.NONE;
   }

   public String getPlatformPeerName(String var1, int var2) {
      return "com.sun.scenario.effect.impl.sw.java.JSW" + var1 + "Peer";
   }
}
