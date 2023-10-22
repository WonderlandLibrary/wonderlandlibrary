package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class FillRoundRect_Color_Loader {
   private FillRoundRect_Color_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap();
      var3.put("oinvarcradii", 0);
      return var0.createShader(var1, var2, var3, 1, false, true);
   }
}
