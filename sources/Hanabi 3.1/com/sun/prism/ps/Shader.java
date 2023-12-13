package com.sun.prism.ps;

import com.sun.prism.GraphicsResource;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface Shader extends GraphicsResource {
   void enable();

   void disable();

   boolean isValid();

   void setConstant(String var1, int var2);

   void setConstant(String var1, int var2, int var3);

   void setConstant(String var1, int var2, int var3, int var4);

   void setConstant(String var1, int var2, int var3, int var4, int var5);

   void setConstants(String var1, IntBuffer var2, int var3, int var4);

   void setConstant(String var1, float var2);

   void setConstant(String var1, float var2, float var3);

   void setConstant(String var1, float var2, float var3, float var4);

   void setConstant(String var1, float var2, float var3, float var4, float var5);

   void setConstants(String var1, FloatBuffer var2, int var3, int var4);
}
