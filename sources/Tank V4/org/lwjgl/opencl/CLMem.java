package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;

public final class CLMem extends CLObjectChild {
   private static final CLMem.CLMemUtil util = (CLMem.CLMemUtil)CLPlatform.getInfoUtilInstance(CLMem.class, "CL_MEM_UTIL");

   CLMem(long var1, CLContext var3) {
      super(var1, var3);
      if (this.isValid()) {
         var3.getCLMemRegistry().registerObject(this);
      }

   }

   public static CLMem createImage2D(CLContext var0, long var1, CLImageFormat var3, long var4, long var6, long var8, Buffer var10, IntBuffer var11) {
      return util.createImage2D(var0, var1, var3, var4, var6, var8, var10, var11);
   }

   public static CLMem createImage3D(CLContext var0, long var1, CLImageFormat var3, long var4, long var6, long var8, long var10, long var12, Buffer var14, IntBuffer var15) {
      return util.createImage3D(var0, var1, var3, var4, var6, var8, var10, var12, var14, var15);
   }

   public CLMem createSubBuffer(long var1, int var3, CLBufferRegion var4, IntBuffer var5) {
      return util.createSubBuffer(this, var1, var3, var4, var5);
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public long getInfoSize(int var1) {
      return util.getInfoSize(this, var1);
   }

   public long getInfoLong(int var1) {
      return util.getInfoLong(this, var1);
   }

   public ByteBuffer getInfoHostBuffer() {
      return util.getInfoHostBuffer(this);
   }

   public long getImageInfoSize(int var1) {
      return util.getImageInfoSize(this, var1);
   }

   public CLImageFormat getImageFormat() {
      return util.getImageInfoFormat(this);
   }

   public int getImageChannelOrder() {
      return util.getImageInfoFormat(this, 0);
   }

   public int getImageChannelType() {
      return util.getImageInfoFormat(this, 1);
   }

   public int getGLObjectType() {
      return util.getGLObjectType(this);
   }

   public int getGLObjectName() {
      return util.getGLObjectName(this);
   }

   public int getGLTextureInfoInt(int var1) {
      return util.getGLTextureInfoInt(this, var1);
   }

   static CLMem create(long var0, CLContext var2) {
      CLMem var3 = (CLMem)var2.getCLMemRegistry().getObject(var0);
      if (var3 == null) {
         var3 = new CLMem(var0, var2);
      } else {
         var3.retain();
      }

      return var3;
   }

   int release() {
      int var1 = super.release();
      if (!this.isValid()) {
         ((CLContext)this.getParent()).getCLMemRegistry().unregisterObject(this);
      }

      return var1;
   }

   interface CLMemUtil extends InfoUtil {
      CLMem createImage2D(CLContext var1, long var2, CLImageFormat var4, long var5, long var7, long var9, Buffer var11, IntBuffer var12);

      CLMem createImage3D(CLContext var1, long var2, CLImageFormat var4, long var5, long var7, long var9, long var11, long var13, Buffer var15, IntBuffer var16);

      CLMem createSubBuffer(CLMem var1, long var2, int var4, CLBufferRegion var5, IntBuffer var6);

      ByteBuffer getInfoHostBuffer(CLMem var1);

      long getImageInfoSize(CLMem var1, int var2);

      CLImageFormat getImageInfoFormat(CLMem var1);

      int getImageInfoFormat(CLMem var1, int var2);

      int getGLObjectType(CLMem var1);

      int getGLObjectName(CLMem var1);

      int getGLTextureInfoInt(CLMem var1, int var2);
   }
}
