package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVHalfFloat {
   public static final int GL_HALF_FLOAT_NV = 5131;

   private NVHalfFloat() {
   }

   public static void glVertex2hNV(short var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertex2hNV;
      BufferChecks.checkFunctionAddress(var3);
      nglVertex2hNV(var0, var1, var3);
   }

   static native void nglVertex2hNV(short var0, short var1, long var2);

   public static void glVertex3hNV(short var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertex3hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertex3hNV(var0, var1, var2, var4);
   }

   static native void nglVertex3hNV(short var0, short var1, short var2, long var3);

   public static void glVertex4hNV(short var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertex4hNV;
      BufferChecks.checkFunctionAddress(var5);
      nglVertex4hNV(var0, var1, var2, var3, var5);
   }

   static native void nglVertex4hNV(short var0, short var1, short var2, short var3, long var4);

   public static void glNormal3hNV(short var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNormal3hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglNormal3hNV(var0, var1, var2, var4);
   }

   static native void nglNormal3hNV(short var0, short var1, short var2, long var3);

   public static void glColor3hNV(short var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColor3hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglColor3hNV(var0, var1, var2, var4);
   }

   static native void nglColor3hNV(short var0, short var1, short var2, long var3);

   public static void glColor4hNV(short var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColor4hNV;
      BufferChecks.checkFunctionAddress(var5);
      nglColor4hNV(var0, var1, var2, var3, var5);
   }

   static native void nglColor4hNV(short var0, short var1, short var2, short var3, long var4);

   public static void glTexCoord1hNV(short var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glTexCoord1hNV;
      BufferChecks.checkFunctionAddress(var2);
      nglTexCoord1hNV(var0, var2);
   }

   static native void nglTexCoord1hNV(short var0, long var1);

   public static void glTexCoord2hNV(short var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoord2hNV;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoord2hNV(var0, var1, var3);
   }

   static native void nglTexCoord2hNV(short var0, short var1, long var2);

   public static void glTexCoord3hNV(short var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoord3hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglTexCoord3hNV(var0, var1, var2, var4);
   }

   static native void nglTexCoord3hNV(short var0, short var1, short var2, long var3);

   public static void glTexCoord4hNV(short var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTexCoord4hNV;
      BufferChecks.checkFunctionAddress(var5);
      nglTexCoord4hNV(var0, var1, var2, var3, var5);
   }

   static native void nglTexCoord4hNV(short var0, short var1, short var2, short var3, long var4);

   public static void glMultiTexCoord1hNV(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMultiTexCoord1hNV;
      BufferChecks.checkFunctionAddress(var3);
      nglMultiTexCoord1hNV(var0, var1, var3);
   }

   static native void nglMultiTexCoord1hNV(int var0, short var1, long var2);

   public static void glMultiTexCoord2hNV(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord2hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord2hNV(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoord2hNV(int var0, short var1, short var2, long var3);

   public static void glMultiTexCoord3hNV(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoord3hNV;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexCoord3hNV(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexCoord3hNV(int var0, short var1, short var2, short var3, long var4);

   public static void glMultiTexCoord4hNV(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord4hNV;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord4hNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglMultiTexCoord4hNV(int var0, short var1, short var2, short var3, short var4, long var5);

   public static void glFogCoordhNV(short var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFogCoordhNV;
      BufferChecks.checkFunctionAddress(var2);
      nglFogCoordhNV(var0, var2);
   }

   static native void nglFogCoordhNV(short var0, long var1);

   public static void glSecondaryColor3hNV(short var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3hNV(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3hNV(short var0, short var1, short var2, long var3);

   public static void glVertexWeighthNV(short var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexWeighthNV;
      BufferChecks.checkFunctionAddress(var2);
      nglVertexWeighthNV(var0, var2);
   }

   static native void nglVertexWeighthNV(short var0, long var1);

   public static void glVertexAttrib1hNV(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1hNV;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1hNV(var0, var1, var3);
   }

   static native void nglVertexAttrib1hNV(int var0, short var1, long var2);

   public static void glVertexAttrib2hNV(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2hNV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2hNV(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2hNV(int var0, short var1, short var2, long var3);

   public static void glVertexAttrib3hNV(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3hNV;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3hNV(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3hNV(int var0, short var1, short var2, short var3, long var4);

   public static void glVertexAttrib4hNV(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4hNV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4hNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4hNV(int var0, short var1, short var2, short var3, short var4, long var5);

   public static void glVertexAttribs1NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs1hvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs1hvNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs1hvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs2NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs2hvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs2hvNV(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs2hvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs3NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs3hvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs3hvNV(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs3hvNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribs4NV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribs4hvNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglVertexAttribs4hvNV(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribs4hvNV(int var0, int var1, long var2, long var4);
}
