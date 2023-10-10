package org.apache.commons.codec.binary;

import java.util.Arrays;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public abstract class BaseNCodec implements BinaryEncoder, BinaryDecoder {
   static final int EOF = -1;
   public static final int MIME_CHUNK_SIZE = 76;
   public static final int PEM_CHUNK_SIZE = 64;
   private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
   private static final int DEFAULT_BUFFER_SIZE = 8192;
   protected static final int MASK_8BITS = 255;
   protected static final byte PAD_DEFAULT = 61;
   protected final byte PAD = 61;
   private final int unencodedBlockSize;
   private final int encodedBlockSize;
   protected final int lineLength;
   private final int chunkSeparatorLength;

   protected BaseNCodec(int var1, int var2, int var3, int var4) {
      this.unencodedBlockSize = var1;
      this.encodedBlockSize = var2;
      boolean var5 = var3 > 0 && var4 > 0;
      this.lineLength = var5 ? var3 / var2 * var2 : 0;
      this.chunkSeparatorLength = var4;
   }

   boolean hasData(BaseNCodec.Context var1) {
      return var1.buffer != null;
   }

   int available(BaseNCodec.Context var1) {
      return var1.buffer != null ? var1.pos - var1.readPos : 0;
   }

   protected int getDefaultBufferSize() {
      return 8192;
   }

   private byte[] resizeBuffer(BaseNCodec.Context var1) {
      if (var1.buffer == null) {
         var1.buffer = new byte[this.getDefaultBufferSize()];
         var1.pos = 0;
         var1.readPos = 0;
      } else {
         byte[] var2 = new byte[var1.buffer.length * 2];
         System.arraycopy(var1.buffer, 0, var2, 0, var1.buffer.length);
         var1.buffer = var2;
      }

      return var1.buffer;
   }

   protected byte[] ensureBufferSize(int var1, BaseNCodec.Context var2) {
      return var2.buffer != null && var2.buffer.length >= var2.pos + var1 ? var2.buffer : this.resizeBuffer(var2);
   }

   int readResults(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (var4.buffer != null) {
         int var5 = Math.min(this.available(var4), var3);
         System.arraycopy(var4.buffer, var4.readPos, var1, var2, var5);
         var4.readPos += var5;
         if (var4.readPos >= var4.pos) {
            var4.buffer = null;
         }

         return var5;
      } else {
         return var4.eof ? -1 : 0;
      }
   }

   protected static boolean isWhiteSpace(byte var0) {
      switch(var0) {
      case 9:
      case 10:
      case 13:
      case 32:
         return true;
      default:
         return false;
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof byte[])) {
         throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
      } else {
         return this.encode((byte[])((byte[])var1));
      }
   }

   public String encodeToString(byte[] var1) {
      return StringUtils.newStringUtf8(this.encode(var1));
   }

   public String encodeAsString(byte[] var1) {
      return StringUtils.newStringUtf8(this.encode(var1));
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 instanceof byte[]) {
         return this.decode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.decode((String)var1);
      } else {
         throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
      }
   }

   public byte[] decode(String var1) {
      return this.decode(StringUtils.getBytesUtf8(var1));
   }

   public byte[] decode(byte[] var1) {
      if (var1 != null && var1.length != 0) {
         BaseNCodec.Context var2 = new BaseNCodec.Context();
         this.decode(var1, 0, var1.length, var2);
         this.decode(var1, 0, -1, var2);
         byte[] var3 = new byte[var2.pos];
         this.readResults(var3, 0, var3.length, var2);
         return var3;
      } else {
         return var1;
      }
   }

   public byte[] encode(byte[] var1) {
      if (var1 != null && var1.length != 0) {
         BaseNCodec.Context var2 = new BaseNCodec.Context();
         this.encode(var1, 0, var1.length, var2);
         this.encode(var1, 0, -1, var2);
         byte[] var3 = new byte[var2.pos - var2.readPos];
         this.readResults(var3, 0, var3.length, var2);
         return var3;
      } else {
         return var1;
      }
   }

   abstract void encode(byte[] var1, int var2, int var3, BaseNCodec.Context var4);

   abstract void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4);

   protected abstract boolean isInAlphabet(byte var1);

   public boolean isInAlphabet(byte[] var1, boolean var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (!this.isInAlphabet(var1[var3]) && (!var2 || var1[var3] != 61 && !isWhiteSpace(var1[var3]))) {
            return false;
         }
      }

      return true;
   }

   public boolean isInAlphabet(String var1) {
      return this.isInAlphabet(StringUtils.getBytesUtf8(var1), true);
   }

   protected boolean containsAlphabetOrPad(byte[] var1) {
      if (var1 == null) {
         return false;
      } else {
         byte[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            if (61 == var5 || this.isInAlphabet(var5)) {
               return true;
            }
         }

         return false;
      }
   }

   public long getEncodedLength(byte[] var1) {
      long var2 = (long)((var1.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize) * (long)this.encodedBlockSize;
      if (this.lineLength > 0) {
         var2 += (var2 + (long)this.lineLength - 1L) / (long)this.lineLength * (long)this.chunkSeparatorLength;
      }

      return var2;
   }

   static class Context {
      int ibitWorkArea;
      long lbitWorkArea;
      byte[] buffer;
      int pos;
      int readPos;
      boolean eof;
      int currentLinePos;
      int modulus;

      public String toString() {
         return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", this.getClass().getSimpleName(), Arrays.toString(this.buffer), this.currentLinePos, this.eof, this.ibitWorkArea, this.lbitWorkArea, this.modulus, this.pos, this.readPos);
      }
   }
}
