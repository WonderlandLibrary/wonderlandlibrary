package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class AlphaTextureDifference_RadialGradient_Loader {
   private AlphaTextureDifference_RadialGradient_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      var2.put("maskInput", 0);
      var2.put("colors", 1);
      HashMap var3 = new HashMap();
      var3.put("innerOffset", 0);
      var3.put("precalc", 2);
      var3.put("content", 1);
      return var0.createShader(var1, var2, var3, 1, false, true);
   }
}
