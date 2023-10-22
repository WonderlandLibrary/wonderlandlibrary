package com.sun.scenario.effect.impl.hw.d3d;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import java.io.InputStream;

public class D3DShaderSource implements ShaderSource {
   public InputStream loadSource(String var1) {
      return D3DShaderSource.class.getResourceAsStream("hlsl/" + var1 + ".obj");
   }

   public Effect.AccelType getAccelType() {
      return Effect.AccelType.DIRECT3D;
   }
}
