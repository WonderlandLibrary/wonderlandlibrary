package com.ibm.icu.util;

import java.nio.ByteBuffer;

public final class BytesTrieBuilder extends StringTrieBuilder {
   private final byte[] intBytes = new byte[5];
   private byte[] bytes;
   private int bytesLength;
   static final boolean $assertionsDisabled = !BytesTrieBuilder.class.desiredAssertionStatus();

   public BytesTrieBuilder add(byte[] var1, int var2, int var3) {
      this.addImpl(new BytesTrieBuilder.BytesAsCharSequence(var1, var2), var3);
      return this;
   }

   public BytesTrie build(StringTrieBuilder.Option var1) {
      this.buildBytes(var1);
      return new BytesTrie(this.bytes, this.bytes.length - this.bytesLength);
   }

   public ByteBuffer buildByteBuffer(StringTrieBuilder.Option var1) {
      this.buildBytes(var1);
      return ByteBuffer.wrap(this.bytes, this.bytes.length - this.bytesLength, this.bytesLength);
   }

   private void buildBytes(StringTrieBuilder.Option var1) {
      if (this.bytes == null) {
         this.bytes = new byte[1024];
      }

      this.buildImpl(var1);
   }

   public BytesTrieBuilder clear() {
      this.clearImpl();
      this.bytes = null;
      this.bytesLength = 0;
      return this;
   }

   /** @deprecated */
   protected boolean matchNodesCanHaveValues() {
      return false;
   }

   /** @deprecated */
   protected int getMaxBranchLinearSubNodeLength() {
      return 5;
   }

   /** @deprecated */
   protected int getMinLinearMatch() {
      return 16;
   }

   /** @deprecated */
   protected int getMaxLinearMatchLength() {
      return 16;
   }

   private void ensureCapacity(int var1) {
      if (var1 > this.bytes.length) {
         int var2 = this.bytes.length;

         do {
            var2 *= 2;
         } while(var2 <= var1);

         byte[] var3 = new byte[var2];
         System.arraycopy(this.bytes, this.bytes.length - this.bytesLength, var3, var3.length - this.bytesLength, this.bytesLength);
         this.bytes = var3;
      }

   }

   /** @deprecated */
   protected int write(int var1) {
      int var2 = this.bytesLength + 1;
      this.ensureCapacity(var2);
      this.bytesLength = var2;
      this.bytes[this.bytes.length - this.bytesLength] = (byte)var1;
      return this.bytesLength;
   }

   /** @deprecated */
   protected int write(int var1, int var2) {
      int var3 = this.bytesLength + var2;
      this.ensureCapacity(var3);
      this.bytesLength = var3;

      for(int var4 = this.bytes.length - this.bytesLength; var2 > 0; --var2) {
         this.bytes[var4++] = (byte)this.strings.charAt(var1++);
      }

      return this.bytesLength;
   }

   private int write(byte[] var1, int var2) {
      int var3 = this.bytesLength + var2;
      this.ensureCapacity(var3);
      this.bytesLength = var3;
      System.arraycopy(var1, 0, this.bytes, this.bytes.length - this.bytesLength, var2);
      return this.bytesLength;
   }

   /** @deprecated */
   protected int writeValueAndFinal(int var1, boolean var2) {
      if (0 <= var1 && var1 <= 64) {
         return this.write(16 + var1 << 1 | (var2 ? 1 : 0));
      } else {
         int var3 = 1;
         if (var1 >= 0 && var1 <= 16777215) {
            if (var1 <= 6911) {
               this.intBytes[0] = (byte)(81 + (var1 >> 8));
            } else {
               if (var1 <= 1179647) {
                  this.intBytes[0] = (byte)(108 + (var1 >> 16));
               } else {
                  this.intBytes[0] = 126;
                  this.intBytes[1] = (byte)(var1 >> 16);
                  var3 = 2;
               }

               this.intBytes[var3++] = (byte)(var1 >> 8);
            }

            this.intBytes[var3++] = (byte)var1;
         } else {
            this.intBytes[0] = 127;
            this.intBytes[1] = (byte)(var1 >> 24);
            this.intBytes[2] = (byte)(var1 >> 16);
            this.intBytes[3] = (byte)(var1 >> 8);
            this.intBytes[4] = (byte)var1;
            var3 = 5;
         }

         this.intBytes[0] = (byte)(this.intBytes[0] << 1 | (var2 ? 1 : 0));
         return this.write(this.intBytes, var3);
      }
   }

   /** @deprecated */
   protected int writeValueAndType(boolean var1, int var2, int var3) {
      int var4 = this.write(var3);
      if (var1) {
         var4 = this.writeValueAndFinal(var2, false);
      }

      return var4;
   }

   /** @deprecated */
   protected int writeDeltaTo(int var1) {
      int var2 = this.bytesLength - var1;
      if (!$assertionsDisabled && var2 < 0) {
         throw new AssertionError();
      } else if (var2 <= 191) {
         return this.write(var2);
      } else {
         byte var3;
         if (var2 <= 12287) {
            this.intBytes[0] = (byte)(192 + (var2 >> 8));
            var3 = 1;
         } else {
            if (var2 <= 917503) {
               this.intBytes[0] = (byte)(240 + (var2 >> 16));
               var3 = 2;
            } else {
               if (var2 <= 16777215) {
                  this.intBytes[0] = -2;
                  var3 = 3;
               } else {
                  this.intBytes[0] = -1;
                  this.intBytes[1] = (byte)(var2 >> 24);
                  var3 = 4;
               }

               this.intBytes[1] = (byte)(var2 >> 16);
            }

            this.intBytes[1] = (byte)(var2 >> 8);
         }

         byte var10001 = var3;
         int var4 = var3 + 1;
         this.intBytes[var10001] = (byte)var2;
         return this.write(this.intBytes, var4);
      }
   }

   private static final class BytesAsCharSequence implements CharSequence {
      private byte[] s;
      private int len;

      public BytesAsCharSequence(byte[] var1, int var2) {
         this.s = var1;
         this.len = var2;
      }

      public char charAt(int var1) {
         return (char)(this.s[var1] & 255);
      }

      public int length() {
         return this.len;
      }

      public CharSequence subSequence(int var1, int var2) {
         return null;
      }
   }
}
