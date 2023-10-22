package com.sun.prism.ps;

import com.sun.prism.ResourceFactory;
import java.io.InputStream;
import java.util.Map;

public interface ShaderFactory extends ResourceFactory {
   Shader createShader(InputStream var1, Map var2, Map var3, int var4, boolean var5, boolean var6);

   Shader createStockShader(String var1);
}
