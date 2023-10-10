package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class Base64 {
   private static final int MAX_LINE_LENGTH = 76;
   private static final byte EQUALS_SIGN = 61;
   private static final byte NEW_LINE = 10;
   private static final byte WHITE_SPACE_ENC = -5;
   private static final byte EQUALS_SIGN_ENC = -1;

   private static byte[] alphabet(Base64Dialect var0) {
      if (var0 == null) {
         throw new NullPointerException("dialect");
      } else {
         return var0.alphabet;
      }
   }

   private static byte[] decodabet(Base64Dialect var0) {
      if (var0 == null) {
         throw new NullPointerException("dialect");
      } else {
         return var0.decodabet;
      }
   }

   private static boolean breakLines(Base64Dialect var0) {
      if (var0 == null) {
         throw new NullPointerException("dialect");
      } else {
         return var0.breakLinesByDefault;
      }
   }

   public static ByteBuf encode(ByteBuf var0) {
      return encode(var0, Base64Dialect.STANDARD);
   }

   public static ByteBuf encode(ByteBuf var0, Base64Dialect var1) {
      return encode(var0, breakLines(var1), var1);
   }

   public static ByteBuf encode(ByteBuf var0, boolean var1) {
      return encode(var0, var1, Base64Dialect.STANDARD);
   }

   public static ByteBuf encode(ByteBuf var0, boolean var1, Base64Dialect var2) {
      if (var0 == null) {
         throw new NullPointerException("src");
      } else {
         ByteBuf var3 = encode(var0, var0.readerIndex(), var0.readableBytes(), var1, var2);
         var0.readerIndex(var0.writerIndex());
         return var3;
      }
   }

   public static ByteBuf encode(ByteBuf var0, int var1, int var2) {
      return encode(var0, var1, var2, Base64Dialect.STANDARD);
   }

   public static ByteBuf encode(ByteBuf var0, int var1, int var2, Base64Dialect var3) {
      return encode(var0, var1, var2, breakLines(var3), var3);
   }

   public static ByteBuf encode(ByteBuf var0, int var1, int var2, boolean var3) {
      return encode(var0, var1, var2, var3, Base64Dialect.STANDARD);
   }

   public static ByteBuf encode(ByteBuf var0, int var1, int var2, boolean var3, Base64Dialect var4) {
      if (var0 == null) {
         throw new NullPointerException("src");
      } else if (var4 == null) {
         throw new NullPointerException("dialect");
      } else {
         int var5 = var2 * 4 / 3;
         ByteBuf var6 = Unpooled.buffer(var5 + (var2 % 3 > 0 ? 4 : 0) + (var3 ? var5 / 76 : 0)).order(var0.order());
         int var7 = 0;
         int var8 = 0;
         int var9 = var2 - 2;

         for(int var10 = 0; var7 < var9; var8 += 4) {
            encode3to4(var0, var7 + var1, 3, var6, var8, var4);
            var10 += 4;
            if (var3 && var10 == 76) {
               var6.setByte(var8 + 4, 10);
               ++var8;
               var10 = 0;
            }

            var7 += 3;
         }

         if (var7 < var2) {
            encode3to4(var0, var7 + var1, var2 - var7, var6, var8, var4);
            var8 += 4;
         }

         return var6.slice(0, var8);
      }
   }

   private static void encode3to4(ByteBuf var0, int var1, int var2, ByteBuf var3, int var4, Base64Dialect var5) {
      byte[] var6 = alphabet(var5);
      int var7 = (var2 > 0 ? var0.getByte(var1) << 24 >>> 8 : 0) | (var2 > 1 ? var0.getByte(var1 + 1) << 24 >>> 16 : 0) | (var2 > 2 ? var0.getByte(var1 + 2) << 24 >>> 24 : 0);
      switch(var2) {
      case 1:
         var3.setByte(var4, var6[var7 >>> 18]);
         var3.setByte(var4 + 1, var6[var7 >>> 12 & 63]);
         var3.setByte(var4 + 2, 61);
         var3.setByte(var4 + 3, 61);
         break;
      case 2:
         var3.setByte(var4, var6[var7 >>> 18]);
         var3.setByte(var4 + 1, var6[var7 >>> 12 & 63]);
         var3.setByte(var4 + 2, var6[var7 >>> 6 & 63]);
         var3.setByte(var4 + 3, 61);
         break;
      case 3:
         var3.setByte(var4, var6[var7 >>> 18]);
         var3.setByte(var4 + 1, var6[var7 >>> 12 & 63]);
         var3.setByte(var4 + 2, var6[var7 >>> 6 & 63]);
         var3.setByte(var4 + 3, var6[var7 & 63]);
      }

   }

   public static ByteBuf decode(ByteBuf var0) {
      return decode(var0, Base64Dialect.STANDARD);
   }

   public static ByteBuf decode(ByteBuf var0, Base64Dialect var1) {
      if (var0 == null) {
         throw new NullPointerException("src");
      } else {
         ByteBuf var2 = decode(var0, var0.readerIndex(), var0.readableBytes(), var1);
         var0.readerIndex(var0.writerIndex());
         return var2;
      }
   }

   public static ByteBuf decode(ByteBuf var0, int var1, int var2) {
      return decode(var0, var1, var2, Base64Dialect.STANDARD);
   }

   public static ByteBuf decode(ByteBuf var0, int var1, int var2, Base64Dialect var3) {
      if (var0 == null) {
         throw new NullPointerException("src");
      } else if (var3 == null) {
         throw new NullPointerException("dialect");
      } else {
         byte[] var4 = decodabet(var3);
         int var5 = var2 * 3 / 4;
         ByteBuf var6 = var0.alloc().buffer(var5).order(var0.order());
         int var7 = 0;
         byte[] var8 = new byte[4];
         int var9 = 0;

         for(int var10 = var1; var10 < var1 + var2; ++var10) {
            byte var11 = (byte)(var0.getByte(var10) & 127);
            byte var12 = var4[var11];
            if (var12 < -5) {
               throw new IllegalArgumentException("bad Base64 input character at " + var10 + ": " + var0.getUnsignedByte(var10) + " (decimal)");
            }

            if (var12 >= -1) {
               var8[var9++] = var11;
               if (var9 > 3) {
                  var7 += decode4to3(var8, 0, var6, var7, var3);
                  var9 = 0;
                  if (var11 == 61) {
                     break;
                  }
               }
            }
         }

         return var6.slice(0, var7);
      }
   }

   private static int decode4to3(byte[] var0, int var1, ByteBuf var2, int var3, Base64Dialect var4) {
      byte[] var5 = decodabet(var4);
      int var6;
      if (var0[var1 + 2] == 61) {
         var6 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12;
         var2.setByte(var3, (byte)(var6 >>> 16));
         return 1;
      } else if (var0[var1 + 3] == 61) {
         var6 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12 | (var5[var0[var1 + 2]] & 255) << 6;
         var2.setByte(var3, (byte)(var6 >>> 16));
         var2.setByte(var3 + 1, (byte)(var6 >>> 8));
         return 2;
      } else {
         try {
            var6 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12 | (var5[var0[var1 + 2]] & 255) << 6 | var5[var0[var1 + 3]] & 255;
         } catch (IndexOutOfBoundsException var8) {
            throw new IllegalArgumentException("not encoded in Base64");
         }

         var2.setByte(var3, (byte)(var6 >> 16));
         var2.setByte(var3 + 1, (byte)(var6 >> 8));
         var2.setByte(var3 + 2, (byte)var6);
         return 3;
      }
   }

   private Base64() {
   }
}
