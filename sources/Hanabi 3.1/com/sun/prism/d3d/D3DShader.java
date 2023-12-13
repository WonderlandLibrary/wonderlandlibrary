package com.sun.prism.d3d;

import com.sun.prism.impl.BufferUtil;
import com.sun.prism.ps.Shader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

final class D3DShader extends D3DResource implements Shader {
   private static IntBuffer itmp;
   private static FloatBuffer ftmp;
   private final Map registers;
   private boolean valid;

   D3DShader(D3DContext var1, long var2, Map var4) {
      super(new D3DResource.D3DRecord(var1, var2));
      this.valid = var2 != 0L;
      this.registers = var4;
   }

   static native long init(long var0, ByteBuffer var2, int var3, boolean var4, boolean var5);

   private static native int enable(long var0, long var2);

   private static native int disable(long var0, long var2);

   private static native int setConstantsF(long var0, long var2, int var4, FloatBuffer var5, int var6, int var7);

   private static native int setConstantsI(long var0, long var2, int var4, IntBuffer var5, int var6, int var7);

   private static native int nGetRegister(long var0, long var2, String var4);

   public void enable() {
      int var1 = enable(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource());
      this.valid &= var1 >= 0;
      this.d3dResRecord.getContext();
      D3DContext.validate(var1);
   }

   public void disable() {
      int var1 = disable(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource());
      this.valid &= var1 >= 0;
      this.d3dResRecord.getContext();
      D3DContext.validate(var1);
   }

   private static void checkTmpIntBuf() {
      if (itmp == null) {
         itmp = BufferUtil.newIntBuffer(4);
      }

      itmp.clear();
   }

   public void setConstant(String var1, int var2) {
      this.setConstant(var1, (float)var2);
   }

   public void setConstant(String var1, int var2, int var3) {
      this.setConstant(var1, (float)var2, (float)var3);
   }

   public void setConstant(String var1, int var2, int var3, int var4) {
      this.setConstant(var1, (float)var2, (float)var3, (float)var4);
   }

   public void setConstant(String var1, int var2, int var3, int var4, int var5) {
      this.setConstant(var1, (float)var2, (float)var3, (float)var4, (float)var5);
   }

   public void setConstants(String var1, IntBuffer var2, int var3, int var4) {
      throw new InternalError("Not yet implemented");
   }

   private static void checkTmpFloatBuf() {
      if (ftmp == null) {
         ftmp = BufferUtil.newFloatBuffer(4);
      }

      ftmp.clear();
   }

   public void setConstant(String var1, float var2) {
      checkTmpFloatBuf();
      ftmp.put(var2);
      this.setConstants(var1, (FloatBuffer)ftmp, 0, 1);
   }

   public void setConstant(String var1, float var2, float var3) {
      checkTmpFloatBuf();
      ftmp.put(var2);
      ftmp.put(var3);
      this.setConstants(var1, (FloatBuffer)ftmp, 0, 1);
   }

   public void setConstant(String var1, float var2, float var3, float var4) {
      checkTmpFloatBuf();
      ftmp.put(var2);
      ftmp.put(var3);
      ftmp.put(var4);
      this.setConstants(var1, (FloatBuffer)ftmp, 0, 1);
   }

   public void setConstant(String var1, float var2, float var3, float var4, float var5) {
      checkTmpFloatBuf();
      ftmp.put(var2);
      ftmp.put(var3);
      ftmp.put(var4);
      ftmp.put(var5);
      this.setConstants(var1, (FloatBuffer)ftmp, 0, 1);
   }

   public void setConstants(String var1, FloatBuffer var2, int var3, int var4) {
      int var5 = setConstantsF(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource(), this.getRegister(var1), var2, var3, var4);
      this.valid &= var5 >= 0;
      this.d3dResRecord.getContext();
      D3DContext.validate(var5);
   }

   private int getRegister(String var1) {
      Integer var2 = (Integer)this.registers.get(var1);
      if (var2 == null) {
         int var3 = nGetRegister(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource(), var1);
         if (var3 < 0) {
            throw new IllegalArgumentException("Register not found for: " + var1);
         } else {
            this.registers.put(var1, var3);
            return var3;
         }
      } else {
         return var2;
      }
   }

   public boolean isValid() {
      return this.valid;
   }

   public void dispose() {
      super.dispose();
      this.valid = false;
   }
}
