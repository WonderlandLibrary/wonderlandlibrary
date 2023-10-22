package me.theresa.fontRenderer.font.opengl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNGDecoder {
   public static Format ALPHA = new Format(1, true);
   public static Format LUMINANCE = new Format(1, false);
   public static Format LUMINANCE_ALPHA = new Format(2, true);
   public static Format RGB = new Format(3, false);
   public static Format RGBA = new Format(4, true);
   public static Format BGRA = new Format(4, true);
   public static Format ABGR = new Format(4, true);
   private static final byte[] SIGNATURE = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   private static final int IHDR = 1229472850;
   private static final int PLTE = 1347179589;
   private static final int tRNS = 1951551059;
   private static final int IDAT = 1229209940;
   private static final int IEND = 1229278788;
   private static final byte COLOR_GREYSCALE = 0;
   private static final byte COLOR_TRUECOLOR = 2;
   private static final byte COLOR_INDEXED = 3;
   private static final byte COLOR_GREYALPHA = 4;
   private static final byte COLOR_TRUEALPHA = 6;
   private final InputStream input;
   private final CRC32 crc;
   private final byte[] buffer;
   private int chunkLength;
   private int chunkType;
   private int chunkRemaining;
   private int width;
   private int height;
   private int bitdepth;
   private int colorType;
   private int bytesPerPixel;
   private byte[] palette;
   private byte[] paletteA;
   private byte[] transPixel;

   public PNGDecoder(InputStream input) throws IOException {
      this.input = input;
      this.crc = new CRC32();
      this.buffer = new byte[4096];
      this.readFully(this.buffer, 0, SIGNATURE.length);
      if (!checkSignature(this.buffer)) {
         throw new IOException("Not a valid PNG file");
      } else {
         this.openChunk(1229472850);
         this.readIHDR();
         this.closeChunk();

         while(true) {
            this.openChunk();
            switch (this.chunkType) {
               case 1229209940:
                  if (this.colorType == 3 && this.palette == null) {
                     throw new IOException("Missing PLTE chunk");
                  }

                  return;
               case 1347179589:
                  this.readPLTE();
                  break;
               case 1951551059:
                  this.readtRNS();
            }

            this.closeChunk();
         }
      }
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public boolean hasAlpha() {
      return this.colorType == 6 || this.paletteA != null || this.transPixel != null;
   }

   public boolean isRGB() {
      return this.colorType == 6 || this.colorType == 2 || this.colorType == 3;
   }

   public Format decideTextureFormat(Format fmt) {
      switch (this.colorType) {
         case 0:
            if (fmt != LUMINANCE && fmt != ALPHA) {
               return LUMINANCE;
            }

            return fmt;
         case 1:
         case 5:
         default:
            throw new UnsupportedOperationException("Not yet implemented");
         case 2:
            if (fmt != ABGR && fmt != RGBA && fmt != BGRA && fmt != RGB) {
               return RGB;
            }

            return fmt;
         case 3:
            if (fmt != ABGR && fmt != RGBA && fmt != BGRA) {
               return RGBA;
            }

            return fmt;
         case 4:
            return LUMINANCE_ALPHA;
         case 6:
            return fmt != ABGR && fmt != RGBA && fmt != BGRA && fmt != RGB ? RGBA : fmt;
      }
   }

   public void decode(ByteBuffer buffer, int stride, Format fmt) throws IOException {
      int offset = buffer.position();
      int lineSize = (this.width * this.bitdepth + 7) / 8 * this.bytesPerPixel;
      byte[] curLine = new byte[lineSize + 1];
      byte[] prevLine = new byte[lineSize + 1];
      byte[] palLine = this.bitdepth < 8 ? new byte[this.width + 1] : null;
      Inflater inflater = new Inflater();

      try {
         for(int y = 0; y < this.height; ++y) {
            this.readChunkUnzip(inflater, curLine, 0, curLine.length);
            this.unfilter(curLine, prevLine);
            buffer.position(offset + y * stride);
            switch (this.colorType) {
               case 0:
                  if (fmt != LUMINANCE && fmt != ALPHA) {
                     throw new UnsupportedOperationException("Unsupported format for this image");
                  }

                  this.copy(buffer, curLine);
                  break;
               case 1:
               case 5:
               default:
                  throw new UnsupportedOperationException("Not yet implemented");
               case 2:
                  if (fmt == ABGR) {
                     this.copyRGBtoABGR(buffer, curLine);
                  } else if (fmt == RGBA) {
                     this.copyRGBtoRGBA(buffer, curLine);
                  } else if (fmt == BGRA) {
                     this.copyRGBtoBGRA(buffer, curLine);
                  } else {
                     if (fmt != RGB) {
                        throw new UnsupportedOperationException("Unsupported format for this image");
                     }

                     this.copy(buffer, curLine);
                  }
                  break;
               case 3:
                  switch (this.bitdepth) {
                     case 1:
                        this.expand1(curLine, palLine);
                        break;
                     case 2:
                        this.expand2(curLine, palLine);
                        break;
                     case 3:
                     case 5:
                     case 6:
                     case 7:
                     default:
                        throw new UnsupportedOperationException("Unsupported bitdepth for this image");
                     case 4:
                        this.expand4(curLine, palLine);
                        break;
                     case 8:
                        palLine = curLine;
                  }

                  if (fmt == ABGR) {
                     this.copyPALtoABGR(buffer, palLine);
                  } else if (fmt == RGBA) {
                     this.copyPALtoRGBA(buffer, palLine);
                  } else {
                     if (fmt != BGRA) {
                        throw new UnsupportedOperationException("Unsupported format for this image");
                     }

                     this.copyPALtoBGRA(buffer, palLine);
                  }
                  break;
               case 4:
                  if (fmt != LUMINANCE_ALPHA) {
                     throw new UnsupportedOperationException("Unsupported format for this image");
                  }

                  this.copy(buffer, curLine);
                  break;
               case 6:
                  if (fmt == ABGR) {
                     this.copyRGBAtoABGR(buffer, curLine);
                  } else if (fmt == RGBA) {
                     this.copy(buffer, curLine);
                  } else if (fmt == BGRA) {
                     this.copyRGBAtoBGRA(buffer, curLine);
                  } else {
                     if (fmt != RGB) {
                        throw new UnsupportedOperationException("Unsupported format for this image");
                     }

                     this.copyRGBAtoRGB(buffer, curLine);
                  }
            }

            byte[] tmp = curLine;
            curLine = prevLine;
            prevLine = tmp;
         }
      } finally {
         inflater.end();
      }

   }

   private void copy(ByteBuffer buffer, byte[] curLine) {
      buffer.put(curLine, 1, curLine.length - 1);
   }

   private void copyRGBtoABGR(ByteBuffer buffer, byte[] curLine) {
      int tr;
      int tg;
      if (this.transPixel != null) {
         tr = this.transPixel[1];
         tg = this.transPixel[3];
         byte tb = this.transPixel[5];
         int i = 1;

         for(int n = curLine.length; i < n; i += 3) {
            byte r = curLine[i];
            byte g = curLine[i + 1];
            byte b = curLine[i + 2];
            byte a = -1;
            if (r == tr && g == tg && b == tb) {
               a = 0;
            }

            buffer.put(a).put(b).put(g).put(r);
         }
      } else {
         tr = 1;

         for(tg = curLine.length; tr < tg; tr += 3) {
            buffer.put((byte)-1).put(curLine[tr + 2]).put(curLine[tr + 1]).put(curLine[tr]);
         }
      }

   }

   private void copyRGBtoRGBA(ByteBuffer buffer, byte[] curLine) {
      int tr;
      int tg;
      if (this.transPixel != null) {
         tr = this.transPixel[1];
         tg = this.transPixel[3];
         byte tb = this.transPixel[5];
         int i = 1;

         for(int n = curLine.length; i < n; i += 3) {
            byte r = curLine[i];
            byte g = curLine[i + 1];
            byte b = curLine[i + 2];
            byte a = -1;
            if (r == tr && g == tg && b == tb) {
               a = 0;
            }

            buffer.put(r).put(g).put(b).put(a);
         }
      } else {
         tr = 1;

         for(tg = curLine.length; tr < tg; tr += 3) {
            buffer.put(curLine[tr]).put(curLine[tr + 1]).put(curLine[tr + 2]).put((byte)-1);
         }
      }

   }

   private void copyRGBtoBGRA(ByteBuffer buffer, byte[] curLine) {
      int tr;
      int tg;
      if (this.transPixel != null) {
         tr = this.transPixel[1];
         tg = this.transPixel[3];
         byte tb = this.transPixel[5];
         int i = 1;

         for(int n = curLine.length; i < n; i += 3) {
            byte r = curLine[i];
            byte g = curLine[i + 1];
            byte b = curLine[i + 2];
            byte a = -1;
            if (r == tr && g == tg && b == tb) {
               a = 0;
            }

            buffer.put(b).put(g).put(r).put(a);
         }
      } else {
         tr = 1;

         for(tg = curLine.length; tr < tg; tr += 3) {
            buffer.put(curLine[tr + 2]).put(curLine[tr + 1]).put(curLine[tr]).put((byte)-1);
         }
      }

   }

   private void copyRGBAtoABGR(ByteBuffer buffer, byte[] curLine) {
      int i = 1;

      for(int n = curLine.length; i < n; i += 4) {
         buffer.put(curLine[i + 3]).put(curLine[i + 2]).put(curLine[i + 1]).put(curLine[i]);
      }

   }

   private void copyRGBAtoBGRA(ByteBuffer buffer, byte[] curLine) {
      int i = 1;

      for(int n = curLine.length; i < n; i += 4) {
         buffer.put(curLine[i + 2]).put(curLine[i + 1]).put(curLine[i + 0]).put(curLine[i + 3]);
      }

   }

   private void copyRGBAtoRGB(ByteBuffer buffer, byte[] curLine) {
      int i = 1;

      for(int n = curLine.length; i < n; i += 4) {
         buffer.put(curLine[i]).put(curLine[i + 1]).put(curLine[i + 2]);
      }

   }

   private void copyPALtoABGR(ByteBuffer buffer, byte[] curLine) {
      int i;
      int n;
      int idx;
      byte r;
      byte g;
      byte b;
      byte a;
      if (this.paletteA != null) {
         i = 1;

         for(n = curLine.length; i < n; ++i) {
            idx = curLine[i] & 255;
            r = this.palette[idx * 3 + 0];
            g = this.palette[idx * 3 + 1];
            b = this.palette[idx * 3 + 2];
            a = this.paletteA[idx];
            buffer.put(a).put(b).put(g).put(r);
         }
      } else {
         i = 1;

         for(n = curLine.length; i < n; ++i) {
            idx = curLine[i] & 255;
            r = this.palette[idx * 3 + 0];
            g = this.palette[idx * 3 + 1];
            b = this.palette[idx * 3 + 2];
            a = -1;
            buffer.put(a).put(b).put(g).put(r);
         }
      }

   }

   private void copyPALtoRGBA(ByteBuffer buffer, byte[] curLine) {
      int i;
      int n;
      int idx;
      byte r;
      byte g;
      byte b;
      byte a;
      if (this.paletteA != null) {
         i = 1;

         for(n = curLine.length; i < n; ++i) {
            idx = curLine[i] & 255;
            r = this.palette[idx * 3 + 0];
            g = this.palette[idx * 3 + 1];
            b = this.palette[idx * 3 + 2];
            a = this.paletteA[idx];
            buffer.put(r).put(g).put(b).put(a);
         }
      } else {
         i = 1;

         for(n = curLine.length; i < n; ++i) {
            idx = curLine[i] & 255;
            r = this.palette[idx * 3 + 0];
            g = this.palette[idx * 3 + 1];
            b = this.palette[idx * 3 + 2];
            a = -1;
            buffer.put(r).put(g).put(b).put(a);
         }
      }

   }

   private void copyPALtoBGRA(ByteBuffer buffer, byte[] curLine) {
      int i;
      int n;
      int idx;
      byte r;
      byte g;
      byte b;
      byte a;
      if (this.paletteA != null) {
         i = 1;

         for(n = curLine.length; i < n; ++i) {
            idx = curLine[i] & 255;
            r = this.palette[idx * 3 + 0];
            g = this.palette[idx * 3 + 1];
            b = this.palette[idx * 3 + 2];
            a = this.paletteA[idx];
            buffer.put(b).put(g).put(r).put(a);
         }
      } else {
         i = 1;

         for(n = curLine.length; i < n; ++i) {
            idx = curLine[i] & 255;
            r = this.palette[idx * 3 + 0];
            g = this.palette[idx * 3 + 1];
            b = this.palette[idx * 3 + 2];
            a = -1;
            buffer.put(b).put(g).put(r).put(a);
         }
      }

   }

   private void expand4(byte[] src, byte[] dst) {
      int i = 1;
      int n = dst.length;

      while(i < n) {
         int val = src[1 + (i >> 1)] & 255;
         switch (n - i) {
            default:
               dst[i + 1] = (byte)(val & 15);
            case 1:
               dst[i] = (byte)(val >> 4);
               i += 2;
         }
      }

   }

   private void expand2(byte[] src, byte[] dst) {
      int i = 1;
      int n = dst.length;

      while(i < n) {
         int val = src[1 + (i >> 2)] & 255;
         switch (n - i) {
            default:
               dst[i + 3] = (byte)(val & 3);
            case 3:
               dst[i + 2] = (byte)(val >> 2 & 3);
            case 2:
               dst[i + 1] = (byte)(val >> 4 & 3);
            case 1:
               dst[i] = (byte)(val >> 6);
               i += 4;
         }
      }

   }

   private void expand1(byte[] src, byte[] dst) {
      int i = 1;
      int n = dst.length;

      while(i < n) {
         int val = src[1 + (i >> 3)] & 255;
         switch (n - i) {
            default:
               dst[i + 7] = (byte)(val & 1);
            case 7:
               dst[i + 6] = (byte)(val >> 1 & 1);
            case 6:
               dst[i + 5] = (byte)(val >> 2 & 1);
            case 5:
               dst[i + 4] = (byte)(val >> 3 & 1);
            case 4:
               dst[i + 3] = (byte)(val >> 4 & 1);
            case 3:
               dst[i + 2] = (byte)(val >> 5 & 1);
            case 2:
               dst[i + 1] = (byte)(val >> 6 & 1);
            case 1:
               dst[i] = (byte)(val >> 7);
               i += 8;
         }
      }

   }

   private void unfilter(byte[] curLine, byte[] prevLine) throws IOException {
      switch (curLine[0]) {
         case 0:
            break;
         case 1:
            this.unfilterSub(curLine);
            break;
         case 2:
            this.unfilterUp(curLine, prevLine);
            break;
         case 3:
            this.unfilterAverage(curLine, prevLine);
            break;
         case 4:
            this.unfilterPaeth(curLine, prevLine);
            break;
         default:
            throw new IOException("invalide filter type in scanline: " + curLine[0]);
      }

   }

   private void unfilterSub(byte[] curLine) {
      int bpp = this.bytesPerPixel;
      int i = bpp + 1;

      for(int n = curLine.length; i < n; ++i) {
         curLine[i] += curLine[i - bpp];
      }

   }

   private void unfilterUp(byte[] curLine, byte[] prevLine) {
      int bpp = this.bytesPerPixel;
      int i = 1;

      for(int n = curLine.length; i < n; ++i) {
         curLine[i] += prevLine[i];
      }

   }

   private void unfilterAverage(byte[] curLine, byte[] prevLine) {
      int bpp = this.bytesPerPixel;

      int i;
      for(i = 1; i <= bpp; ++i) {
         curLine[i] += (byte)((prevLine[i] & 255) >>> 1);
      }

      for(int n = curLine.length; i < n; ++i) {
         curLine[i] += (byte)((prevLine[i] & 255) + (curLine[i - bpp] & 255) >>> 1);
      }

   }

   private void unfilterPaeth(byte[] curLine, byte[] prevLine) {
      int bpp = this.bytesPerPixel;

      int i;
      for(i = 1; i <= bpp; ++i) {
         curLine[i] += prevLine[i];
      }

      for(int n = curLine.length; i < n; ++i) {
         int a = curLine[i - bpp] & 255;
         int b = prevLine[i] & 255;
         int c = prevLine[i - bpp] & 255;
         int p = a + b - c;
         int pa = p - a;
         if (pa < 0) {
            pa = -pa;
         }

         int pb = p - b;
         if (pb < 0) {
            pb = -pb;
         }

         int pc = p - c;
         if (pc < 0) {
            pc = -pc;
         }

         if (pa <= pb && pa <= pc) {
            c = a;
         } else if (pb <= pc) {
            c = b;
         }

         curLine[i] += (byte)c;
      }

   }

   private void readIHDR() throws IOException {
      this.checkChunkLength(13);
      this.readChunk(this.buffer, 0, 13);
      this.width = this.readInt(this.buffer, 0);
      this.height = this.readInt(this.buffer, 4);
      this.bitdepth = this.buffer[8] & 255;
      this.colorType = this.buffer[9] & 255;
      label43:
      switch (this.colorType) {
         case 0:
            if (this.bitdepth != 8) {
               throw new IOException("Unsupported bit depth: " + this.bitdepth);
            }

            this.bytesPerPixel = 1;
            break;
         case 1:
         case 5:
         default:
            throw new IOException("unsupported color format: " + this.colorType);
         case 2:
            if (this.bitdepth != 8) {
               throw new IOException("Unsupported bit depth: " + this.bitdepth);
            }

            this.bytesPerPixel = 3;
            break;
         case 3:
            switch (this.bitdepth) {
               case 1:
               case 2:
               case 4:
               case 8:
                  this.bytesPerPixel = 1;
                  break label43;
               case 3:
               case 5:
               case 6:
               case 7:
               default:
                  throw new IOException("Unsupported bit depth: " + this.bitdepth);
            }
         case 4:
            if (this.bitdepth != 8) {
               throw new IOException("Unsupported bit depth: " + this.bitdepth);
            }

            this.bytesPerPixel = 2;
            break;
         case 6:
            if (this.bitdepth != 8) {
               throw new IOException("Unsupported bit depth: " + this.bitdepth);
            }

            this.bytesPerPixel = 4;
      }

      if (this.buffer[10] != 0) {
         throw new IOException("unsupported compression method");
      } else if (this.buffer[11] != 0) {
         throw new IOException("unsupported filtering method");
      } else if (this.buffer[12] != 0) {
         throw new IOException("unsupported interlace method");
      }
   }

   private void readPLTE() throws IOException {
      int paletteEntries = this.chunkLength / 3;
      if (paletteEntries >= 1 && paletteEntries <= 256 && this.chunkLength % 3 == 0) {
         this.palette = new byte[paletteEntries * 3];
         this.readChunk(this.palette, 0, this.palette.length);
      } else {
         throw new IOException("PLTE chunk has wrong length");
      }
   }

   private void readtRNS() throws IOException {
      switch (this.colorType) {
         case 0:
            this.checkChunkLength(2);
            this.transPixel = new byte[2];
            this.readChunk(this.transPixel, 0, 2);
         case 1:
         default:
            break;
         case 2:
            this.checkChunkLength(6);
            this.transPixel = new byte[6];
            this.readChunk(this.transPixel, 0, 6);
            break;
         case 3:
            if (this.palette == null) {
               throw new IOException("tRNS chunk without PLTE chunk");
            }

            this.paletteA = new byte[this.palette.length / 3];
            Arrays.fill(this.paletteA, (byte)-1);
            this.readChunk(this.paletteA, 0, this.paletteA.length);
      }

   }

   private void closeChunk() throws IOException {
      if (this.chunkRemaining > 0) {
         this.skip((long)(this.chunkRemaining + 4));
      } else {
         this.readFully(this.buffer, 0, 4);
         int expectedCrc = this.readInt(this.buffer, 0);
         int computedCrc = (int)this.crc.getValue();
         if (computedCrc != expectedCrc) {
            throw new IOException("Invalid CRC");
         }
      }

      this.chunkRemaining = 0;
      this.chunkLength = 0;
      this.chunkType = 0;
   }

   private void openChunk() throws IOException {
      this.readFully(this.buffer, 0, 8);
      this.chunkLength = this.readInt(this.buffer, 0);
      this.chunkType = this.readInt(this.buffer, 4);
      this.chunkRemaining = this.chunkLength;
      this.crc.reset();
      this.crc.update(this.buffer, 4, 4);
   }

   private void openChunk(int expected) throws IOException {
      this.openChunk();
      if (this.chunkType != expected) {
         throw new IOException("Expected chunk: " + Integer.toHexString(expected));
      }
   }

   private void checkChunkLength(int expected) throws IOException {
      if (this.chunkLength != expected) {
         throw new IOException("Chunk has wrong size");
      }
   }

   private int readChunk(byte[] buffer, int offset, int length) throws IOException {
      if (length > this.chunkRemaining) {
         length = this.chunkRemaining;
      }

      this.readFully(buffer, offset, length);
      this.crc.update(buffer, offset, length);
      this.chunkRemaining -= length;
      return length;
   }

   private void refillInflater(Inflater inflater) throws IOException {
      while(this.chunkRemaining == 0) {
         this.closeChunk();
         this.openChunk(1229209940);
      }

      int read = this.readChunk(this.buffer, 0, this.buffer.length);
      inflater.setInput(this.buffer, 0, read);
   }

   private void readChunkUnzip(Inflater inflater, byte[] buffer, int offset, int length) throws IOException {
      try {
         do {
            int read = inflater.inflate(buffer, offset, length);
            if (read <= 0) {
               if (inflater.finished()) {
                  throw new EOFException();
               }

               if (!inflater.needsInput()) {
                  throw new IOException("Can't inflate " + length + " bytes");
               }

               this.refillInflater(inflater);
            } else {
               offset += read;
               length -= read;
            }
         } while(length > 0);

      } catch (DataFormatException var6) {
         throw new IOException("inflate error", var6);
      }
   }

   private void readFully(byte[] buffer, int offset, int length) throws IOException {
      do {
         int read = this.input.read(buffer, offset, length);
         if (read < 0) {
            throw new EOFException();
         }

         offset += read;
         length -= read;
      } while(length > 0);

   }

   private int readInt(byte[] buffer, int offset) {
      return buffer[offset] << 24 | (buffer[offset + 1] & 255) << 16 | (buffer[offset + 2] & 255) << 8 | buffer[offset + 3] & 255;
   }

   private void skip(long amount) throws IOException {
      while(amount > 0L) {
         long skipped = this.input.skip(amount);
         if (skipped < 0L) {
            throw new EOFException();
         }

         amount -= skipped;
      }

   }

   private static boolean checkSignature(byte[] buffer) {
      for(int i = 0; i < SIGNATURE.length; ++i) {
         if (buffer[i] != SIGNATURE[i]) {
            return false;
         }
      }

      return true;
   }

   public static class Format {
      final int numComponents;
      final boolean hasAlpha;

      private Format(int numComponents, boolean hasAlpha) {
         this.numComponents = numComponents;
         this.hasAlpha = hasAlpha;
      }

      public int getNumComponents() {
         return this.numComponents;
      }

      public boolean isHasAlpha() {
         return this.hasAlpha;
      }

      // $FF: synthetic method
      Format(int x0, boolean x1, Object x2) {
         this(x0, x1);
      }
   }
}
