package my.NewSnake.utils;

import java.nio.ByteBuffer;

public class TextureData {
   private int width;
   private ByteBuffer buffer;
   private int height;
   private int textureId;

   public int getWidth() {
      return this.width;
   }

   public int getTextureId() {
      return this.textureId;
   }

   public ByteBuffer getBuffer() {
      return this.buffer;
   }

   public int getHeight() {
      return this.height;
   }

   public TextureData(int var1, int var2, int var3, ByteBuffer var4) {
      this.textureId = var1;
      this.width = var2;
      this.height = var3;
      this.buffer = var4;
   }
}
