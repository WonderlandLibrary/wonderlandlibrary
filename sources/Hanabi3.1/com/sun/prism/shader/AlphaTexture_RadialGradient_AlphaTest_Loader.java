package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class AlphaTexture_RadialGradient_AlphaTest_Loader {
   private AlphaTexture_RadialGradient_AlphaTest_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      var2.put("maskInput", 0);
      var2.put("colors", 1);
      HashMap var3 = new HashMap();
      var3.put("precalc", 1);
      var3.put("content", 0);
      return var0.createShader(var1, var2, var3, 1, false, true);
   }
}
