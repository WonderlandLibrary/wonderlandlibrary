package team.massacre.api.ui.framework;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

public final class StaticallySizedImage {
   private final int textureId = GL11.glGenTextures();
   private final int width;
   private final int height;

   public StaticallySizedImage(BufferedImage image, int numMinMaps) {
      this.width = image.getWidth();
      this.height = image.getHeight();
      int[] pixels = new int[this.width * this.height];
      image.getRGB(0, 0, this.width, this.height, pixels, 0, this.width);
      ByteBuffer buffer = BufferUtils.createByteBuffer(this.width * this.height * 4);

      for(int y = 0; y < this.height; ++y) {
         for(int x = 0; x < this.width; ++x) {
            int pixel = pixels[y * this.width + x];
            buffer.put((byte)-1);
            buffer.put((byte)-1);
            buffer.put((byte)-1);
            buffer.put((byte)(pixel >> 24 & 255));
         }
      }

      buffer.flip();
      GL11.glBindTexture(3553, this.textureId);
      GL42.glTexStorage2D(3553, numMinMaps, 32856, this.width, this.height);
      GL11.glTexSubImage2D(3553, 0, 0, 0, this.width, this.height, 32993, 5121, buffer);
      GL30.glGenerateMipmap(3553);
      GL11.glTexParameteri(3553, 10242, 10497);
      GL11.glTexParameteri(3553, 10243, 10497);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9987);
      GL11.glTexImage2D(3553, 0, 6408, this.width, this.height, 0, 6408, 5121, buffer);
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void bind() {
      GL11.glBindTexture(3553, this.textureId);
   }

   public void draw(float x, float y) {
      this.draw(x, y, (float)this.width, (float)this.height);
   }

   public void draw(float x, float y, float width, float height) {
      this.bind();
      GL11.glEnable(3042);
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2f(x, y);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2f(x, y + height);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2f(x + width, y + height);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2f(x + width, y);
      GL11.glEnd();
      GL11.glDisable(3042);
   }
}
