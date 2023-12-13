package org.lwjgl;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public final class MemoryUtil {
   private static final Charset ascii = Charset.forName("ISO-8859-1");
   private static final Charset utf8 = Charset.forName("UTF-8");
   private static final Charset utf16 = Charset.forName("UTF-16LE");
   private static final MemoryUtil.Accessor memUtil;

   private MemoryUtil() {
   }

   public static long getAddress0(Buffer var0) {
      return memUtil.getAddress(var0);
   }

   public static long getAddress0Safe(Buffer var0) {
      return var0 == null ? 0L : memUtil.getAddress(var0);
   }

   public static long getAddress0(PointerBuffer var0) {
      return memUtil.getAddress(var0.getBuffer());
   }

   public static long getAddress0Safe(PointerBuffer var0) {
      return var0 == null ? 0L : memUtil.getAddress(var0.getBuffer());
   }

   public static long getAddress(ByteBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(ByteBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)var1;
   }

   public static long getAddress(ShortBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(ShortBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)(var1 << 1);
   }

   public static long getAddress(CharBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(CharBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)(var1 << 1);
   }

   public static long getAddress(IntBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(IntBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)(var1 << 2);
   }

   public static long getAddress(FloatBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(FloatBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)(var1 << 2);
   }

   public static long getAddress(LongBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(LongBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)(var1 << 3);
   }

   public static long getAddress(DoubleBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(DoubleBuffer var0, int var1) {
      return getAddress0((Buffer)var0) + (long)(var1 << 3);
   }

   public static long getAddress(PointerBuffer var0) {
      return getAddress(var0, var0.position());
   }

   public static long getAddress(PointerBuffer var0, int var1) {
      return getAddress0(var0) + (long)(var1 * PointerBuffer.getPointerSize());
   }

   public static long getAddressSafe(ByteBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(ByteBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(ShortBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(ShortBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(CharBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(CharBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(IntBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(IntBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(FloatBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(FloatBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(LongBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(LongBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(DoubleBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(DoubleBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static long getAddressSafe(PointerBuffer var0) {
      return var0 == null ? 0L : getAddress(var0);
   }

   public static long getAddressSafe(PointerBuffer var0, int var1) {
      return var0 == null ? 0L : getAddress(var0, var1);
   }

   public static ByteBuffer encodeASCII(CharSequence var0) {
      return encode(var0, ascii);
   }

   public static ByteBuffer encodeUTF8(CharSequence var0) {
      return encode(var0, utf8);
   }

   public static ByteBuffer encodeUTF16(CharSequence var0) {
      return encode(var0, utf16);
   }

   private static ByteBuffer encode(CharSequence var0, Charset var1) {
      return var0 == null ? null : encode(CharBuffer.wrap(new MemoryUtil.CharSequenceNT(var0)), var1);
   }

   private static ByteBuffer encode(CharBuffer var0, Charset var1) {
      CharsetEncoder var2 = var1.newEncoder();
      int var3 = (int)((float)var0.remaining() * var2.averageBytesPerChar());
      ByteBuffer var4 = BufferUtils.createByteBuffer(var3);
      if (var3 == 0 && var0.remaining() == 0) {
         return var4;
      } else {
         var2.reset();

         while(true) {
            CoderResult var5 = var0.hasRemaining() ? var2.encode(var0, var4, true) : CoderResult.UNDERFLOW;
            if (var5.isUnderflow()) {
               var5 = var2.flush(var4);
            }

            if (var5.isUnderflow()) {
               var4.flip();
               return var4;
            }

            if (var5.isOverflow()) {
               var3 = 2 * var3 + 1;
               ByteBuffer var6 = BufferUtils.createByteBuffer(var3);
               var4.flip();
               var6.put(var4);
               var4 = var6;
            } else {
               try {
                  var5.throwException();
               } catch (CharacterCodingException var7) {
                  throw new RuntimeException(var7);
               }
            }
         }
      }
   }

   public static String decodeASCII(ByteBuffer var0) {
      return decode(var0, ascii);
   }

   public static String decodeUTF8(ByteBuffer var0) {
      return decode(var0, utf8);
   }

   public static String decodeUTF16(ByteBuffer var0) {
      return decode(var0, utf16);
   }

   private static String decode(ByteBuffer var0, Charset var1) {
      return var0 == null ? null : decodeImpl(var0, var1);
   }

   private static String decodeImpl(ByteBuffer var0, Charset var1) {
      CharsetDecoder var2 = var1.newDecoder();
      int var3 = (int)((float)var0.remaining() * var2.averageCharsPerByte());
      CharBuffer var4 = BufferUtils.createCharBuffer(var3);
      if (var3 == 0 && var0.remaining() == 0) {
         return "";
      } else {
         var2.reset();

         while(true) {
            CoderResult var5 = var0.hasRemaining() ? var2.decode(var0, var4, true) : CoderResult.UNDERFLOW;
            if (var5.isUnderflow()) {
               var5 = var2.flush(var4);
            }

            if (var5.isUnderflow()) {
               var4.flip();
               return var4.toString();
            }

            if (var5.isOverflow()) {
               var3 = 2 * var3 + 1;
               CharBuffer var6 = BufferUtils.createCharBuffer(var3);
               var4.flip();
               var6.put(var4);
               var4 = var6;
            } else {
               try {
                  var5.throwException();
               } catch (CharacterCodingException var7) {
                  throw new RuntimeException(var7);
               }
            }
         }
      }
   }

   private static MemoryUtil.Accessor loadAccessor(String var0) throws Exception {
      return (MemoryUtil.Accessor)Class.forName(var0).newInstance();
   }

   static Field getAddressField() throws NoSuchFieldException {
      return getDeclaredFieldRecursive(ByteBuffer.class, "address");
   }

   private static Field getDeclaredFieldRecursive(Class var0, String var1) throws NoSuchFieldException {
      Class var2 = var0;

      while(true) {
         try {
            return var2.getDeclaredField(var1);
         } catch (NoSuchFieldException var4) {
            var2 = var2.getSuperclass();
            if (var2 == null) {
               throw new NoSuchFieldException(var1 + " does not exist in " + var0.getSimpleName() + " or any of its superclasses.");
            }
         }
      }
   }

   static {
      Object var0;
      try {
         var0 = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorUnsafe");
      } catch (Exception var6) {
         try {
            var0 = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorReflectFast");
         } catch (Exception var5) {
            try {
               var0 = new MemoryUtil.AccessorReflect();
            } catch (Exception var4) {
               LWJGLUtil.log("Unsupported JVM detected, this will likely result in low performance. Please inform LWJGL developers.");
               var0 = new MemoryUtil.AccessorJNI();
            }
         }
      }

      LWJGLUtil.log("MemoryUtil Accessor: " + var0.getClass().getSimpleName());
      memUtil = (MemoryUtil.Accessor)var0;
   }

   private static class AccessorReflect implements MemoryUtil.Accessor {
      private final Field address;

      AccessorReflect() {
         try {
            this.address = MemoryUtil.getAddressField();
         } catch (NoSuchFieldException var2) {
            throw new UnsupportedOperationException(var2);
         }

         this.address.setAccessible(true);
      }

      public long getAddress(Buffer var1) {
         try {
            return this.address.getLong(var1);
         } catch (IllegalAccessException var3) {
            return 0L;
         }
      }
   }

   private static class AccessorJNI implements MemoryUtil.Accessor {
      private AccessorJNI() {
      }

      public long getAddress(Buffer var1) {
         return BufferUtils.getBufferAddress(var1);
      }

      AccessorJNI(Object var1) {
         this();
      }
   }

   interface Accessor {
      long getAddress(Buffer var1);
   }

   private static class CharSequenceNT implements CharSequence {
      final CharSequence source;

      CharSequenceNT(CharSequence var1) {
         this.source = var1;
      }

      public int length() {
         return this.source.length() + 1;
      }

      public char charAt(int var1) {
         return var1 == this.source.length() ? '\u0000' : this.source.charAt(var1);
      }

      public CharSequence subSequence(int var1, int var2) {
         return new MemoryUtil.CharSequenceNT(this.source.subSequence(var1, Math.min(var2, this.source.length())));
      }
   }
}
