package me.theresa.fontRenderer.font.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class PNGImageData implements LoadableImageData {
   private int width;
   private int height;
   private int texHeight;
   private int texWidth;
   private PNGDecoder decoder;
   private int bitDepth;
   private ByteBuffer scratch;

   public int getDepth() {
      return this.bitDepth;
   }

   public ByteBuffer getImageBufferData() {
      return this.scratch;
   }

   public int getTexHeight() {
      return this.texHeight;
   }

   public int getTexWidth() {
      return this.texWidth;
   }

   public ByteBuffer loadImage(InputStream fis) throws IOException {
      return this.loadImage(fis, false, (int[])null);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
      return this.loadImage(fis, flipped, false, transparent);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
      if (transparent != null) {
         forceAlpha = true;
         throw new IOException("Transparent color not support in custom PNG Decoder");
      } else {
         PNGDecoder decoder = new PNGDecoder(fis);
         if (!decoder.isRGB()) {
            throw new IOException("Only RGB formatted images are supported by the PNGLoader");
         } else {
            this.width = decoder.getWidth();
            this.height = decoder.getHeight();
            this.texWidth = this.get2Fold(this.width);
            this.texHeight = this.get2Fold(this.height);
            int perPixel = decoder.hasAlpha() ? 4 : 3;
            this.bitDepth = decoder.hasAlpha() ? 32 : 24;
            this.scratch = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * perPixel);
            decoder.decode(this.scratch, this.texWidth * perPixel, perPixel == 4 ? PNGDecoder.RGBA : PNGDecoder.RGB);
            int y;
            int x;
            int y;
            int srcOffset;
            if (this.height < this.texHeight - 1) {
               y = (this.texHeight - 1) * this.texWidth * perPixel;
               x = (this.height - 1) * this.texWidth * perPixel;

               for(y = 0; y < this.texWidth; ++y) {
                  for(srcOffset = 0; srcOffset < perPixel; ++srcOffset) {
                     this.scratch.put(y + y + srcOffset, this.scratch.get(y + srcOffset));
                     this.scratch.put(x + this.texWidth * perPixel + y + srcOffset, this.scratch.get(x + y + srcOffset));
                  }
               }
            }

            if (this.width < this.texWidth - 1) {
               for(y = 0; y < this.texHeight; ++y) {
                  for(x = 0; x < perPixel; ++x) {
                     this.scratch.put((y + 1) * this.texWidth * perPixel - perPixel + x, this.scratch.get(y * this.texWidth * perPixel + x));
                     this.scratch.put(y * this.texWidth * perPixel + this.width * perPixel + x, this.scratch.get(y * this.texWidth * perPixel + (this.width - 1) * perPixel + x));
                  }
               }
            }

            if (!decoder.hasAlpha() && forceAlpha) {
               ByteBuffer temp = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * 4);

               for(x = 0; x < this.texWidth; ++x) {
                  for(y = 0; y < this.texHeight; ++y) {
                     srcOffset = y * 3 + x * this.texHeight * 3;
                     int dstOffset = y * 4 + x * this.texHeight * 4;
                     temp.put(dstOffset, this.scratch.get(srcOffset));
                     temp.put(dstOffset + 1, this.scratch.get(srcOffset + 1));
                     temp.put(dstOffset + 2, this.scratch.get(srcOffset + 2));
                     if (x < this.getHeight() && y < this.getWidth()) {
                        temp.put(dstOffset + 3, (byte)-1);
                     } else {
                        temp.put(dstOffset + 3, (byte)0);
                     }
                  }
               }

               this.bitDepth = 32;
               this.scratch = temp;
            }

            this.scratch.position(0);
            return this.scratch;
         }
      }
   }

   private int toInt(byte b) {
      return b < 0 ? 256 + b : b;
   }

   private int get2Fold(int fold) {
      int ret;
      for(ret = 2; ret < fold; ret *= 2) {
      }

      return ret;
   }

   public void configureEdging(boolean edging) {
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }
}
