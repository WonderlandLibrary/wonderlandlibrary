package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Solid_TextureYV12_AlphaTest_Loader {
   private Solid_TextureYV12_AlphaTest_Loader() {
   }

   public static Shader loadShader(ShaderFactory var0, InputStream var1) {
      HashMap var2 = new HashMap();
      var2.put("crTex", 1);
      var2.put("alphaTex", 3);
      var2.put("cbTex", 2);
      var2.put("lumaTex", 0);
      HashMap var3 = new HashMap();
      var3.put("cbCrScale", 1);
      var3.put("lumaAlphaScale", 0);
      return var0.createShader(var1, var2, var3, 0, false, true);
   }
}
