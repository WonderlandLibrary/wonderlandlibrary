package me.theresa.fontRenderer.font.opengl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import me.theresa.fontRenderer.font.log.Log;
import me.theresa.fontRenderer.font.util.ResourceLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;

public class CursorLoader {
   private static final CursorLoader single = new CursorLoader();

   public static CursorLoader get() {
      return single;
   }

   private CursorLoader() {
   }

   public Cursor getCursor(String ref, int x, int y) throws IOException, LWJGLException {
      LoadableImageData imageData = null;
      imageData = ImageDataFactory.getImageDataFor(ref);
      imageData.configureEdging(false);
      ByteBuffer buf = imageData.loadImage(ResourceLoader.getResourceAsStream(ref), true, true, (int[])null);

      int yspot;
      for(yspot = 0; yspot < buf.limit(); yspot += 4) {
         byte red = buf.get(yspot);
         byte green = buf.get(yspot + 1);
         byte blue = buf.get(yspot + 2);
         byte alpha = buf.get(yspot + 3);
         buf.put(yspot + 2, red);
         buf.put(yspot + 1, green);
         buf.put(yspot, blue);
         buf.put(yspot + 3, alpha);
      }

      try {
         yspot = imageData.getHeight() - y - 1;
         if (yspot < 0) {
            yspot = 0;
         }

         return new Cursor(imageData.getTexWidth(), imageData.getTexHeight(), x, yspot, 1, buf.asIntBuffer(), (IntBuffer)null);
      } catch (Throwable var11) {
         Log.info("Chances are you cursor is too small for this platform");
         throw new LWJGLException(var11);
      }
   }

   public Cursor getCursor(ByteBuffer buf, int x, int y, int width, int height) throws IOException, LWJGLException {
      int yspot;
      for(yspot = 0; yspot < buf.limit(); yspot += 4) {
         byte red = buf.get(yspot);
         byte green = buf.get(yspot + 1);
         byte blue = buf.get(yspot + 2);
         byte alpha = buf.get(yspot + 3);
         buf.put(yspot + 2, red);
         buf.put(yspot + 1, green);
         buf.put(yspot, blue);
         buf.put(yspot + 3, alpha);
      }

      try {
         yspot = height - y - 1;
         if (yspot < 0) {
            yspot = 0;
         }

         return new Cursor(width, height, x, yspot, 1, buf.asIntBuffer(), (IntBuffer)null);
      } catch (Throwable var11) {
         Log.info("Chances are you cursor is too small for this platform");
         throw new LWJGLException(var11);
      }
   }

   public Cursor getCursor(ImageData imageData, int x, int y) throws IOException, LWJGLException {
      ByteBuffer buf = imageData.getImageBufferData();

      int yspot;
      for(yspot = 0; yspot < buf.limit(); yspot += 4) {
         byte red = buf.get(yspot);
         byte green = buf.get(yspot + 1);
         byte blue = buf.get(yspot + 2);
         byte alpha = buf.get(yspot + 3);
         buf.put(yspot + 2, red);
         buf.put(yspot + 1, green);
         buf.put(yspot, blue);
         buf.put(yspot + 3, alpha);
      }

      try {
         yspot = imageData.getHeight() - y - 1;
         if (yspot < 0) {
            yspot = 0;
         }

         return new Cursor(imageData.getTexWidth(), imageData.getTexHeight(), x, yspot, 1, buf.asIntBuffer(), (IntBuffer)null);
      } catch (Throwable var10) {
         Log.info("Chances are you cursor is too small for this platform");
         throw new LWJGLException(var10);
      }
   }

   public Cursor getAnimatedCursor(String ref, int x, int y, int width, int height, int[] cursorDelays) throws IOException, LWJGLException {
      IntBuffer cursorDelaysBuffer = ByteBuffer.allocateDirect(cursorDelays.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      int[] var8 = cursorDelays;
      int var9 = cursorDelays.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         int cursorDelay = var8[var10];
         cursorDelaysBuffer.put(cursorDelay);
      }

      cursorDelaysBuffer.flip();
      LoadableImageData imageData = new TGAImageData();
      ByteBuffer buf = imageData.loadImage(ResourceLoader.getResourceAsStream(ref), false, (int[])null);
      return new Cursor(width, height, x, y, cursorDelays.length, buf.asIntBuffer(), cursorDelaysBuffer);
   }
}
