package com.sun.javafx.image;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface IntToBytePixelConverter extends PixelConverter {
   void convert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

   void convert(IntBuffer var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

   void convert(int[] var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);
}
