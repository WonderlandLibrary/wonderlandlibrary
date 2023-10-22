package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class DrawPgram_ImagePattern_AlphaTest_Loader {
   private DrawPgram_ImagePattern_AlphaTest_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      var2.put("inputTex", 0);
      HashMap var3 = new HashMap();
      var3.put("perspVec", 3);
      var3.put("xParams", 1);
      var3.put("idim", 0);
      var3.put("yParams", 2);
      var3.put("content", 4);
      return var0.createShader(var1, var2, var3, 1, true, true);
   }
}
