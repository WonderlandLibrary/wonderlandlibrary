package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class AlphaOne_ImagePattern_AlphaTest_Loader {
   private AlphaOne_ImagePattern_AlphaTest_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      var2.put("inputTex", 0);
      HashMap var3 = new HashMap();
      var3.put("content", 0);
      return var0.createShader(var1, var2, var3, 1, false, true);
   }
}
