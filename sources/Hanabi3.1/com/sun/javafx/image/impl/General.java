package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class General {
   public static ByteToBytePixelConverter create(BytePixelGetter var0, BytePixelSetter var1) {
      return new ByteToByteGeneralConverter(var0, var1);
   }

   public static ByteToIntPixelConverter create(BytePixelGetter var0, IntPixelSetter var1) {
      return new ByteToIntGeneralConverter(var0, var1);
   }

   public static IntToBytePixelConverter create(IntPixelGetter var0, BytePixelSetter var1) {
      return new IntToByteGeneralConverter(var0, var1);
   }

   public static IntToIntPixelConverter create(IntPixelGetter var0, IntPixelSetter var1) {
      return new IntToIntGeneralConverter(var0, var1);
   }

   static class IntToIntGeneralConverter extends BaseIntToIntConverter {
      boolean usePremult;

      public IntToIntGeneralConverter(IntPixelGetter var1, IntPixelSetter var2) {
         super(var1, var2);
         this.usePremult = var1.getAlphaType() != AlphaType.NONPREMULTIPLIED && var2.getAlphaType() != AlphaType.NONPREMULTIPLIED;
      }

      void doConvert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7;
         var6 -= var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               ++var2;
               ++var5;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(IntBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7;
         var6 -= var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               ++var2;
               ++var5;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class IntToByteGeneralConverter extends BaseIntToByteConverter {
      boolean usePremult;

      public IntToByteGeneralConverter(IntPixelGetter var1, BytePixelSetter var2) {
         super(var1, var2);
         this.usePremult = var1.getAlphaType() != AlphaType.NONPREMULTIPLIED && var2.getAlphaType() != AlphaType.NONPREMULTIPLIED;
      }

      void doConvert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7;
         var6 -= this.nDstElems * var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               ++var2;
               var5 += this.nDstElems;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(IntBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7;
         var6 -= this.nDstElems * var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               ++var2;
               var5 += this.nDstElems;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class ByteToIntGeneralConverter extends BaseByteToIntConverter {
      boolean usePremult;

      ByteToIntGeneralConverter(BytePixelGetter var1, IntPixelSetter var2) {
         super(var1, var2);
         this.usePremult = var1.getAlphaType() != AlphaType.NONPREMULTIPLIED && var2.getAlphaType() != AlphaType.NONPREMULTIPLIED;
      }

      void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         var3 -= this.nSrcElems * var7;
         var6 -= var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               var2 += this.nSrcElems;
               ++var5;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= this.nSrcElems * var7;
         var6 -= var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               var2 += this.nSrcElems;
               ++var5;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class ByteToByteGeneralConverter extends BaseByteToByteConverter {
      boolean usePremult;

      ByteToByteGeneralConverter(BytePixelGetter var1, BytePixelSetter var2) {
         super(var1, var2);
         this.usePremult = var1.getAlphaType() != AlphaType.NONPREMULTIPLIED && var2.getAlphaType() != AlphaType.NONPREMULTIPLIED;
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= this.nSrcElems * var7;
         var6 -= this.nDstElems * var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               var2 += this.nSrcElems;
               var5 += this.nDstElems;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= this.nSrcElems * var7;
         var6 -= this.nDstElems * var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               if (this.usePremult) {
                  this.setter.setArgbPre(var4, var5, this.getter.getArgbPre(var1, var2));
               } else {
                  this.setter.setArgb(var4, var5, this.getter.getArgb(var1, var2));
               }

               var2 += this.nSrcElems;
               var5 += this.nDstElems;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }
}
