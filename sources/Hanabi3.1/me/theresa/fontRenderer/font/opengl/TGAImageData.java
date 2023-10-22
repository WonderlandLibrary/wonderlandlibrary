package me.theresa.fontRenderer.font.opengl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;

public class TGAImageData implements LoadableImageData {
   private int texWidth;
   private int texHeight;
   private int width;
   private int height;
   private short pixelDepth;

   private short flipEndian(short signedShort) {
      int input = signedShort & '\uffff';
      return (short)(input << 8 | (input & '\uff00') >>> 8);
   }

   public int getDepth() {
      return this.pixelDepth;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getTexWidth() {
      return this.texWidth;
   }

   public int getTexHeight() {
      return this.texHeight;
   }

   public ByteBuffer loadImage(InputStream fis) throws IOException {
      return this.loadImage(fis, true, (int[])null);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
      return this.loadImage(fis, flipped, false, transparent);
   }

   public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
      if (transparent != null) {
         forceAlpha = true;
      }

      byte red = false;
      byte green = false;
      byte blue = false;
      byte alpha = false;
      BufferedInputStream bis = new BufferedInputStream(fis, 100000);
      DataInputStream dis = new DataInputStream(bis);
      short idLength = (short)dis.read();
      short colorMapType = (short)dis.read();
      short imageType = (short)dis.read();
      this.flipEndian(dis.readShort());
      this.flipEndian(dis.readShort());
      short cMapDepth = (short)dis.read();
      this.flipEndian(dis.readShort());
      this.flipEndian(dis.readShort());
      if (imageType != 2) {
         throw new IOException("Slick only supports uncompressed RGB(A) TGA images");
      } else {
         this.width = this.flipEndian(dis.readShort());
         this.height = this.flipEndian(dis.readShort());
         this.pixelDepth = (short)dis.read();
         if (this.pixelDepth == 32) {
            forceAlpha = false;
         }

         this.texWidth = this.get2Fold(this.width);
         this.texHeight = this.get2Fold(this.height);
         short imageDescriptor = (short)dis.read();
         if ((imageDescriptor & 32) == 0) {
            flipped = !flipped;
         }

         if (idLength > 0) {
            bis.skip((long)idLength);
         }

         byte[] rawData = null;
         byte[] rawData;
         if (this.pixelDepth != 32 && !forceAlpha) {
            if (this.pixelDepth != 24) {
               throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
            }

            rawData = new byte[this.texWidth * this.texHeight * 3];
         } else {
            this.pixelDepth = 32;
            rawData = new byte[this.texWidth * this.texHeight * 4];
         }

         int i;
         int perPixel;
         int ofs;
         byte red;
         byte green;
         byte blue;
         if (this.pixelDepth == 24) {
            if (flipped) {
               for(i = this.height - 1; i >= 0; --i) {
                  for(perPixel = 0; perPixel < this.width; ++perPixel) {
                     blue = dis.readByte();
                     green = dis.readByte();
                     red = dis.readByte();
                     ofs = (perPixel + i * this.texWidth) * 3;
                     rawData[ofs] = red;
                     rawData[ofs + 1] = green;
                     rawData[ofs + 2] = blue;
                  }
               }
            } else {
               for(i = 0; i < this.height; ++i) {
                  for(perPixel = 0; perPixel < this.width; ++perPixel) {
                     blue = dis.readByte();
                     green = dis.readByte();
                     red = dis.readByte();
                     ofs = (perPixel + i * this.texWidth) * 3;
                     rawData[ofs] = red;
                     rawData[ofs + 1] = green;
                     rawData[ofs + 2] = blue;
                  }
               }
            }
         } else if (this.pixelDepth == 32) {
            byte alpha;
            if (flipped) {
               for(i = this.height - 1; i >= 0; --i) {
                  for(perPixel = 0; perPixel < this.width; ++perPixel) {
                     blue = dis.readByte();
                     green = dis.readByte();
                     red = dis.readByte();
                     if (forceAlpha) {
                        alpha = -1;
                     } else {
                        alpha = dis.readByte();
                     }

                     ofs = (perPixel + i * this.texWidth) * 4;
                     rawData[ofs] = red;
                     rawData[ofs + 1] = green;
                     rawData[ofs + 2] = blue;
                     rawData[ofs + 3] = alpha;
                     if (alpha == 0) {
                        rawData[ofs + 2] = 0;
                        rawData[ofs + 1] = 0;
                        rawData[ofs] = 0;
                     }
                  }
               }
            } else {
               for(i = 0; i < this.height; ++i) {
                  for(perPixel = 0; perPixel < this.width; ++perPixel) {
                     blue = dis.readByte();
                     green = dis.readByte();
                     red = dis.readByte();
                     if (forceAlpha) {
                        alpha = -1;
                     } else {
                        alpha = dis.readByte();
                     }

                     ofs = (perPixel + i * this.texWidth) * 4;
                     if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                        rawData[ofs + 3] = alpha;
                     } else {
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                        rawData[ofs + 3] = alpha;
                     }

                     if (alpha == 0) {
                        rawData[ofs + 2] = 0;
                        rawData[ofs + 1] = 0;
                        rawData[ofs] = 0;
                     }
                  }
               }
            }
         }

         fis.close();
         if (transparent != null) {
            for(i = 0; i < rawData.length; i += 4) {
               boolean match = true;

               for(ofs = 0; ofs < 3; ++ofs) {
                  if (rawData[i + ofs] != transparent[ofs]) {
                     match = false;
                     break;
                  }
               }

               if (match) {
                  rawData[i + 3] = 0;
               }
            }
         }

         ByteBuffer scratch = BufferUtils.createByteBuffer(rawData.length);
         scratch.put(rawData);
         perPixel = this.pixelDepth / 8;
         int i;
         if (this.height < this.texHeight - 1) {
            ofs = (this.texHeight - 1) * this.texWidth * perPixel;
            i = (this.height - 1) * this.texWidth * perPixel;

            for(int x = 0; x < this.texWidth * perPixel; ++x) {
               scratch.put(ofs + x, scratch.get(x));
               scratch.put(i + this.texWidth * perPixel + x, scratch.get(this.texWidth * perPixel + x));
            }
         }

         if (this.width < this.texWidth - 1) {
            for(ofs = 0; ofs < this.texHeight; ++ofs) {
               for(i = 0; i < perPixel; ++i) {
                  scratch.put((ofs + 1) * this.texWidth * perPixel - perPixel + i, scratch.get(ofs * this.texWidth * perPixel + i));
                  scratch.put(ofs * this.texWidth * perPixel + this.width * perPixel + i, scratch.get(ofs * this.texWidth * perPixel + (this.width - 1) * perPixel + i));
               }
            }
         }

         scratch.flip();
         return scratch;
      }
   }

   private int get2Fold(int fold) {
      int ret;
      for(ret = 2; ret < fold; ret *= 2) {
      }

      return ret;
   }

   public ByteBuffer getImageBufferData() {
      throw new RuntimeException("TGAImageData doesn't store it's image.");
   }

   public void configureEdging(boolean edging) {
   }
}
