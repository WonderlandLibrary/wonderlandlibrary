package com.sun.javafx.image;

import java.nio.ByteBuffer;

public interface ByteToBytePixelConverter extends PixelConverter {
   void convert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

   void convert(ByteBuffer var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

   void convert(byte[] var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);
}
