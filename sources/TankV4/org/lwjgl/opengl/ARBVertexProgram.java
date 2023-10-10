package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class ARBVertexProgram extends ARBProgram {
   public static final int GL_VERTEX_PROGRAM_ARB = 34336;
   public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 34370;
   public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 34371;
   public static final int GL_COLOR_SUM_ARB = 33880;
   public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 34338;
   public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 34339;
   public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 34340;
   public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 34341;
   public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 34922;
   public static final int GL_CURRENT_VERTEX_ATTRIB_ARB = 34342;
   public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 34373;
   public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 34992;
   public static final int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 34993;
   public static final int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 34994;
   public static final int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 34995;
   public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 34921;

   private ARBVertexProgram() {
   }

   public static void glVertexAttrib1sARB(int var0, short var1) {
      ARBVertexShader.glVertexAttrib1sARB(var0, var1);
   }

   public static void glVertexAttrib1fARB(int var0, float var1) {
      ARBVertexShader.glVertexAttrib1fARB(var0, var1);
   }

   public static void glVertexAttrib1dARB(int var0, double var1) {
      ARBVertexShader.glVertexAttrib1dARB(var0, var1);
   }

   public static void glVertexAttrib2sARB(int var0, short var1, short var2) {
      ARBVertexShader.glVertexAttrib2sARB(var0, var1, var2);
   }

   public static void glVertexAttrib2fARB(int var0, float var1, float var2) {
      ARBVertexShader.glVertexAttrib2fARB(var0, var1, var2);
   }

   public static void glVertexAttrib2dARB(int var0, double var1, double var3) {
      ARBVertexShader.glVertexAttrib2dARB(var0, var1, var3);
   }

   public static void glVertexAttrib3sARB(int var0, short var1, short var2, short var3) {
      ARBVertexShader.glVertexAttrib3sARB(var0, var1, var2, var3);
   }

   public static void glVertexAttrib3fARB(int var0, float var1, float var2, float var3) {
      ARBVertexShader.glVertexAttrib3fARB(var0, var1, var2, var3);
   }

   public static void glVertexAttrib3dARB(int var0, double var1, double var3, double var5) {
      ARBVertexShader.glVertexAttrib3dARB(var0, var1, var3, var5);
   }

   public static void glVertexAttrib4sARB(int var0, short var1, short var2, short var3, short var4) {
      ARBVertexShader.glVertexAttrib4sARB(var0, var1, var2, var3, var4);
   }

   public static void glVertexAttrib4fARB(int var0, float var1, float var2, float var3, float var4) {
      ARBVertexShader.glVertexAttrib4fARB(var0, var1, var2, var3, var4);
   }

   public static void glVertexAttrib4dARB(int var0, double var1, double var3, double var5, double var7) {
      ARBVertexShader.glVertexAttrib4dARB(var0, var1, var3, var5, var7);
   }

   public static void glVertexAttrib4NubARB(int var0, byte var1, byte var2, byte var3, byte var4) {
      ARBVertexShader.glVertexAttrib4NubARB(var0, var1, var2, var3, var4);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, int var3, DoubleBuffer var4) {
      ARBVertexShader.glVertexAttribPointerARB(var0, var1, var2, var3, var4);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, int var3, FloatBuffer var4) {
      ARBVertexShader.glVertexAttribPointerARB(var0, var1, var2, var3, var4);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, boolean var3, int var4, ByteBuffer var5) {
      ARBVertexShader.glVertexAttribPointerARB(var0, var1, var2, var3, var4, var5);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, boolean var3, int var4, IntBuffer var5) {
      ARBVertexShader.glVertexAttribPointerARB(var0, var1, var2, var3, var4, var5);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, boolean var2, boolean var3, int var4, ShortBuffer var5) {
      ARBVertexShader.glVertexAttribPointerARB(var0, var1, var2, var3, var4, var5);
   }

   public static void glVertexAttribPointerARB(int var0, int var1, int var2, boolean var3, int var4, long var5) {
      ARBVertexShader.glVertexAttribPointerARB(var0, var1, var2, var3, var4, var5);
   }

   public static void glEnableVertexAttribArrayARB(int var0) {
      ARBVertexShader.glEnableVertexAttribArrayARB(var0);
   }

   public static void glDisableVertexAttribArrayARB(int var0) {
      ARBVertexShader.glDisableVertexAttribArrayARB(var0);
   }

   public static void glGetVertexAttribARB(int var0, int var1, FloatBuffer var2) {
      ARBVertexShader.glGetVertexAttribARB(var0, var1, var2);
   }

   public static void glGetVertexAttribARB(int var0, int var1, DoubleBuffer var2) {
      ARBVertexShader.glGetVertexAttribARB(var0, var1, var2);
   }

   public static void glGetVertexAttribARB(int var0, int var1, IntBuffer var2) {
      ARBVertexShader.glGetVertexAttribARB(var0, var1, var2);
   }

   public static ByteBuffer glGetVertexAttribPointerARB(int var0, int var1, long var2) {
      return ARBVertexShader.glGetVertexAttribPointerARB(var0, var1, var2);
   }
}
