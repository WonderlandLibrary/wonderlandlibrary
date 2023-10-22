package me.theresa.fontRenderer.font;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import me.theresa.fontRenderer.font.impl.ImageData;
import org.lwjgl.BufferUtils;

public class ImageBuffer implements ImageData {
   private final int width;
   private final int height;
   private final int texWidth;
   private final int texHeight;
   private final byte[] rawData;

   public ImageBuffer(int width, int height) {
      this.width = width;
      this.height = height;
      this.texWidth = this.get2Fold(width);
      this.texHeight = this.get2Fold(height);
      this.rawData = new byte[this.texWidth * this.texHeight * 4];
   }

   public byte[] getRGBA() {
      return this.rawData;
   }

   public int getDepth() {
      return 32;
   }

   public int getHeight() {
      return this.height;
   }

   public int getTexHeight() {
      return this.texHeight;
   }

   public int getTexWidth() {
      return this.texWidth;
   }

   public int getWidth() {
      return this.width;
   }

   public ByteBuffer getImageBufferData() {
      ByteBuffer scratch = BufferUtils.createByteBuffer(this.rawData.length);
      scratch.put(this.rawData);
      scratch.flip();
      return scratch;
   }

   public void setRGBA(int x, int y, int r, int g, int b, int a) {
      if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
         int ofs = (x + y * this.texWidth) * 4;
         if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            this.rawData[ofs] = (byte)b;
            this.rawData[ofs + 1] = (byte)g;
            this.rawData[ofs + 2] = (byte)r;
            this.rawData[ofs + 3] = (byte)a;
         } else {
            this.rawData[ofs] = (byte)r;
            this.rawData[ofs + 1] = (byte)g;
            this.rawData[ofs + 2] = (byte)b;
            this.rawData[ofs + 3] = (byte)a;
         }

      } else {
         throw new RuntimeException("Specified location: " + x + "," + y + " outside of image");
      }
   }

   public Image getImage() {
      return new Image(this);
   }

   public Image getImage(int filter) {
      return new Image(this, filter);
   }

   private int get2Fold(int fold) {
      int ret;
      for(ret = 2; ret < fold; ret *= 2) {
      }

      return ret;
   }
}
