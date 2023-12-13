package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

abstract class InfoUtilAbstract implements InfoUtil {
   protected InfoUtilAbstract() {
   }

   protected abstract int getInfo(CLObject var1, int var2, ByteBuffer var3, PointerBuffer var4);

   protected int getInfoSizeArraySize(CLObject var1, int var2) {
      throw new UnsupportedOperationException();
   }

   protected PointerBuffer getSizesBuffer(CLObject var1, int var2) {
      int var3 = this.getInfoSizeArraySize(var1, var2);
      PointerBuffer var4 = APIUtil.getBufferPointer(var3);
      var4.limit(var3);
      this.getInfo(var1, var2, var4.getBuffer(), (PointerBuffer)null);
      return var4;
   }

   public int getInfoInt(CLObject var1, int var2) {
      var1.checkValid();
      ByteBuffer var3 = APIUtil.getBufferByte(4);
      this.getInfo(var1, var2, var3, (PointerBuffer)null);
      return var3.getInt(0);
   }

   public long getInfoSize(CLObject var1, int var2) {
      var1.checkValid();
      PointerBuffer var3 = APIUtil.getBufferPointer();
      this.getInfo(var1, var2, var3.getBuffer(), (PointerBuffer)null);
      return var3.get(0);
   }

   public long[] getInfoSizeArray(CLObject var1, int var2) {
      var1.checkValid();
      int var3 = this.getInfoSizeArraySize(var1, var2);
      PointerBuffer var4 = APIUtil.getBufferPointer(var3);
      this.getInfo(var1, var2, var4.getBuffer(), (PointerBuffer)null);
      long[] var5 = new long[var3];

      for(int var6 = 0; var6 < var3; ++var6) {
         var5[var6] = var4.get(var6);
      }

      return var5;
   }

   public long getInfoLong(CLObject var1, int var2) {
      var1.checkValid();
      ByteBuffer var3 = APIUtil.getBufferByte(8);
      this.getInfo(var1, var2, var3, (PointerBuffer)null);
      return var3.getLong(0);
   }

   public String getInfoString(CLObject var1, int var2) {
      var1.checkValid();
      int var3 = this.getSizeRet(var1, var2);
      if (var3 <= 1) {
         return null;
      } else {
         ByteBuffer var4 = APIUtil.getBufferByte(var3);
         this.getInfo(var1, var2, var4, (PointerBuffer)null);
         var4.limit(var3 - 1);
         return APIUtil.getString(var4);
      }
   }

   protected final int getSizeRet(CLObject var1, int var2) {
      PointerBuffer var3 = APIUtil.getBufferPointer();
      int var4 = this.getInfo(var1, var2, (ByteBuffer)null, var3);
      if (var4 != 0) {
         throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(var2));
      } else {
         return (int)var3.get(0);
      }
   }
}
