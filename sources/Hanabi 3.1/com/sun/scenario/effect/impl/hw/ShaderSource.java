package com.sun.scenario.effect.impl.hw;

import com.sun.scenario.effect.Effect;
import java.io.InputStream;

public interface ShaderSource {
   InputStream loadSource(String var1);

   Effect.AccelType getAccelType();
}
