package org.lwjgl.opengl;

public final class ARBVertexAttribBinding {
   public static final int GL_VERTEX_ATTRIB_BINDING = 33492;
   public static final int GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 33493;
   public static final int GL_VERTEX_BINDING_DIVISOR = 33494;
   public static final int GL_VERTEX_BINDING_OFFSET = 33495;
   public static final int GL_VERTEX_BINDING_STRIDE = 33496;
   public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 33497;
   public static final int GL_MAX_VERTEX_ATTRIB_BINDINGS = 33498;

   private ARBVertexAttribBinding() {
   }

   public static void glBindVertexBuffer(int var0, int var1, long var2, int var4) {
      GL43.glBindVertexBuffer(var0, var1, var2, var4);
   }

   public static void glVertexAttribFormat(int var0, int var1, int var2, boolean var3, int var4) {
      GL43.glVertexAttribFormat(var0, var1, var2, var3, var4);
   }

   public static void glVertexAttribIFormat(int var0, int var1, int var2, int var3) {
      GL43.glVertexAttribIFormat(var0, var1, var2, var3);
   }

   public static void glVertexAttribLFormat(int var0, int var1, int var2, int var3) {
      GL43.glVertexAttribLFormat(var0, var1, var2, var3);
   }

   public static void glVertexAttribBinding(int var0, int var1) {
      GL43.glVertexAttribBinding(var0, var1);
   }

   public static void glVertexBindingDivisor(int var0, int var1) {
      GL43.glVertexBindingDivisor(var0, var1);
   }
}
