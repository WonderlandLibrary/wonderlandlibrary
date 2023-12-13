package me.theresa.fontRenderer.font.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class EmptyImageData implements ImageData {
   private final int width;
   private final int height;

   public EmptyImageData(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public int getDepth() {
      return 32;
   }

   public int getHeight() {
      return this.height;
   }

   public ByteBuffer getImageBufferData() {
      return BufferUtils.createByteBuffer(this.getTexWidth() * this.getTexHeight() * 4);
   }

   public int getTexHeight() {
      return InternalTextureLoader.get2Fold(this.height);
   }

   public int getTexWidth() {
      return InternalTextureLoader.get2Fold(this.width);
   }

   public int getWidth() {
      return this.width;
   }
}
