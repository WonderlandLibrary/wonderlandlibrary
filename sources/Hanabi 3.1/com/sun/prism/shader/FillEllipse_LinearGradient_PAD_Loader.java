package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class FillEllipse_LinearGradient_PAD_Loader {
   private FillEllipse_LinearGradient_PAD_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      var2.put("colors", 0);
      HashMap var3 = new HashMap();
      var3.put("perspVec", 14);
      var3.put("gradParams", 13);
      var3.put("offset", 12);
      var3.put("fractions", 0);
      return var0.createShader(var1, var2, var3, 1, true, true);
   }
}
