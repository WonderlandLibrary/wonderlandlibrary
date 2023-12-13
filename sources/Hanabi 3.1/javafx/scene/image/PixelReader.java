package javafx.scene.image;

import java.nio.Buffer;
import javafx.scene.paint.Color;

public interface PixelReader {
   PixelFormat getPixelFormat();

   int getArgb(int var1, int var2);

   Color getColor(int var1, int var2);

   void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7);

   void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8);

   void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8);
}
