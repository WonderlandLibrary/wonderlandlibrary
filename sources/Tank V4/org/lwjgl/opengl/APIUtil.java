package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

final class APIUtil {
   private static final int INITIAL_BUFFER_SIZE = 256;
   private static final int INITIAL_LENGTHS_SIZE = 4;
   private static final int BUFFERS_SIZE = 32;
   private char[] array = new char[256];
   private ByteBuffer buffer = BufferUtils.createByteBuffer(256);
   private IntBuffer lengths = BufferUtils.createIntBuffer(4);
   private final IntBuffer ints = BufferUtils.createIntBuffer(32);
   private final LongBuffer longs = BufferUtils.createLongBuffer(32);
   private final FloatBuffer floats = BufferUtils.createFloatBuffer(32);
   private final DoubleBuffer doubles = BufferUtils.createDoubleBuffer(32);

   private static char[] getArray(ContextCapabilities var0, int var1) {
      char[] var2 = var0.util.array;
      if (var2.length < var1) {
         for(int var3 = var2.length << 1; var3 < var1; var3 <<= 1) {
         }

         var2 = new char[var1];
         var0.util.array = var2;
      }

      return var2;
   }

   static ByteBuffer getBufferByte(ContextCapabilities var0, int var1) {
      ByteBuffer var2 = var0.util.buffer;
      if (var2.capacity() < var1) {
         for(int var3 = var2.capacity() << 1; var3 < var1; var3 <<= 1) {
         }

         var2 = BufferUtils.createByteBuffer(var1);
         var0.util.buffer = var2;
      } else {
         var2.clear();
      }

      return var2;
   }

   private static ByteBuffer getBufferByteOffset(ContextCapabilities var0, int var1) {
      ByteBuffer var2 = var0.util.buffer;
      if (var2.capacity() < var1) {
         for(int var3 = var2.capacity() << 1; var3 < var1; var3 <<= 1) {
         }

         ByteBuffer var4 = BufferUtils.createByteBuffer(var1);
         var4.put(var2);
         var2 = var4;
         var0.util.buffer = var4;
      } else {
         var2.position(var2.limit());
         var2.limit(var2.capacity());
      }

      return var2;
   }

   static IntBuffer getBufferInt(ContextCapabilities var0) {
      return var0.util.ints;
   }

   static LongBuffer getBufferLong(ContextCapabilities var0) {
      return var0.util.longs;
   }

   static FloatBuffer getBufferFloat(ContextCapabilities var0) {
      return var0.util.floats;
   }

   static DoubleBuffer getBufferDouble(ContextCapabilities var0) {
      return var0.util.doubles;
   }

   static IntBuffer getLengths(ContextCapabilities var0) {
      return getLengths(var0, 1);
   }

   static IntBuffer getLengths(ContextCapabilities var0, int var1) {
      IntBuffer var2 = var0.util.lengths;
      if (var2.capacity() < var1) {
         for(int var3 = var2.capacity(); var3 < var1; var3 <<= 1) {
         }

         var2 = BufferUtils.createIntBuffer(var1);
         var0.util.lengths = var2;
      } else {
         var2.clear();
      }

      return var2;
   }

   private static ByteBuffer encode(ByteBuffer var0, CharSequence var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         char var3 = var1.charAt(var2);
         if (LWJGLUtil.DEBUG && 128 <= var3) {
            var0.put((byte)26);
         } else {
            var0.put((byte)var3);
         }
      }

      return var0;
   }

   static String getString(ContextCapabilities var0, ByteBuffer var1) {
      int var2 = var1.remaining();
      char[] var3 = getArray(var0, var2);

      for(int var4 = var1.position(); var4 < var1.limit(); ++var4) {
         var3[var4 - var1.position()] = (char)var1.get(var4);
      }

      return new String(var3, 0, var2);
   }

   static long getBuffer(ContextCapabilities var0, CharSequence var1) {
      ByteBuffer var2 = encode(getBufferByte(var0, var1.length()), var1);
      var2.flip();
      return MemoryUtil.getAddress0((Buffer)var2);
   }

   static long getBuffer(ContextCapabilities var0, CharSequence var1, int var2) {
      ByteBuffer var3 = encode(getBufferByteOffset(var0, var2 + var1.length()), var1);
      var3.flip();
      return MemoryUtil.getAddress(var3);
   }

   static long getBufferNT(ContextCapabilities var0, CharSequence var1) {
      ByteBuffer var2 = encode(getBufferByte(var0, var1.length() + 1), var1);
      var2.put((byte)0);
      var2.flip();
      return MemoryUtil.getAddress0((Buffer)var2);
   }

   static int getTotalLength(CharSequence[] var0) {
      int var1 = 0;
      CharSequence[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CharSequence var5 = var2[var4];
         var1 += var5.length();
      }

      return var1;
   }

   static long getBuffer(ContextCapabilities var0, CharSequence[] var1) {
      ByteBuffer var2 = getBufferByte(var0, getTotalLength(var1));
      CharSequence[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CharSequence var6 = var3[var5];
         encode(var2, var6);
      }

      var2.flip();
      return MemoryUtil.getAddress0((Buffer)var2);
   }

   static long getBufferNT(ContextCapabilities var0, CharSequence[] var1) {
      ByteBuffer var2 = getBufferByte(var0, getTotalLength(var1) + var1.length);
      CharSequence[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CharSequence var6 = var3[var5];
         encode(var2, var6);
         var2.put((byte)0);
      }

      var2.flip();
      return MemoryUtil.getAddress0((Buffer)var2);
   }

   static long getLengths(ContextCapabilities var0, CharSequence[] var1) {
      IntBuffer var2 = getLengths(var0, var1.length);
      CharSequence[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CharSequence var6 = var3[var5];
         var2.put(var6.length());
      }

      var2.flip();
      return MemoryUtil.getAddress0((Buffer)var2);
   }

   static long getInt(ContextCapabilities var0, int var1) {
      return MemoryUtil.getAddress0((Buffer)getBufferInt(var0).put(0, var1));
   }

   static long getBufferByte0(ContextCapabilities var0) {
      return MemoryUtil.getAddress0((Buffer)getBufferByte(var0, 0));
   }
}
