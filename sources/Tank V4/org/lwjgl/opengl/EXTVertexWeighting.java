package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTVertexWeighting {
   public static final int GL_MODELVIEW0_STACK_DEPTH_EXT = 2979;
   public static final int GL_MODELVIEW1_STACK_DEPTH_EXT = 34050;
   public static final int GL_MODELVIEW0_MATRIX_EXT = 2982;
   public static final int GL_MODELVIEW1_MATRIX_EXT = 34054;
   public static final int GL_VERTEX_WEIGHTING_EXT = 34057;
   public static final int GL_MODELVIEW0_EXT = 5888;
   public static final int GL_MODELVIEW1_EXT = 34058;
   public static final int GL_CURRENT_VERTEX_WEIGHT_EXT = 34059;
   public static final int GL_VERTEX_WEIGHT_ARRAY_EXT = 34060;
   public static final int GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT = 34061;
   public static final int GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT = 34062;
   public static final int GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT = 34063;
   public static final int GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT = 34064;

   private EXTVertexWeighting() {
   }

   public static void glVertexWeightfEXT(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexWeightfEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglVertexWeightfEXT(var0, var2);
   }

   static native void nglVertexWeightfEXT(float var0, long var1);

   public static void glVertexWeightPointerEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexWeightPointerEXT;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = var2;
      }

      nglVertexWeightPointerEXT(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglVertexWeightPointerEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glVertexWeightPointerEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexWeightPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglVertexWeightPointerEXTBO(var0, var1, var2, var3, var6);
   }

   static native void nglVertexWeightPointerEXTBO(int var0, int var1, int var2, long var3, long var5);
}
