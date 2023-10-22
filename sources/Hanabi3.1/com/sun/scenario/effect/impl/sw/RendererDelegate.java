package com.sun.scenario.effect.impl.sw;

import com.sun.scenario.effect.Effect;

public interface RendererDelegate {
   Effect.AccelType getAccelType();

   String getPlatformPeerName(String var1, int var2);
}
