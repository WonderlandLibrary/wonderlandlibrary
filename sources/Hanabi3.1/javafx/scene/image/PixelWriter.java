package javafx.scene.image;

import java.nio.Buffer;
import javafx.scene.paint.Color;

public interface PixelWriter {
   PixelFormat getPixelFormat();

   void setArgb(int var1, int var2, int var3);

   void setColor(int var1, int var2, Color var3);

   void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, Buffer var6, int var7);

   void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, byte[] var6, int var7, int var8);

   void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, int[] var6, int var7, int var8);

   void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7);
}
