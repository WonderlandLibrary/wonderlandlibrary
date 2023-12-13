package org.newdawn.slick.opengl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class CursorLoader {
   private static CursorLoader single = new CursorLoader();

   public static CursorLoader get() {
      return single;
   }

   private CursorLoader() {
   }

   public Cursor getCursor(String var1, int var2, int var3) throws IOException, LWJGLException {
      LoadableImageData var4 = null;
      var4 = ImageDataFactory.getImageDataFor(var1);
      var4.configureEdging(false);
      ByteBuffer var5 = var4.loadImage(ResourceLoader.getResourceAsStream(var1), true, true, (int[])null);

      int var6;
      for(var6 = 0; var6 < var5.limit(); var6 += 4) {
         byte var7 = var5.get(var6);
         byte var8 = var5.get(var6 + 1);
         byte var9 = var5.get(var6 + 2);
         byte var10 = var5.get(var6 + 3);
         var5.put(var6 + 2, var7);
         var5.put(var6 + 1, var8);
         var5.put(var6, var9);
         var5.put(var6 + 3, var10);
      }

      try {
         var6 = var4.getHeight() - var3 - 1;
         if (var6 < 0) {
            var6 = 0;
         }

         return new Cursor(var4.getTexWidth(), var4.getTexHeight(), var2, var6, 1, var5.asIntBuffer(), (IntBuffer)null);
      } catch (Throwable var11) {
         Log.info("Chances are you cursor is too small for this platform");
         throw new LWJGLException(var11);
      }
   }

   public Cursor getCursor(ByteBuffer var1, int var2, int var3, int var4, int var5) throws IOException, LWJGLException {
      int var6;
      for(var6 = 0; var6 < var1.limit(); var6 += 4) {
         byte var7 = var1.get(var6);
         byte var8 = var1.get(var6 + 1);
         byte var9 = var1.get(var6 + 2);
         byte var10 = var1.get(var6 + 3);
         var1.put(var6 + 2, var7);
         var1.put(var6 + 1, var8);
         var1.put(var6, var9);
         var1.put(var6 + 3, var10);
      }

      try {
         var6 = var5 - var3 - 1;
         if (var6 < 0) {
            var6 = 0;
         }

         return new Cursor(var4, var5, var2, var6, 1, var1.asIntBuffer(), (IntBuffer)null);
      } catch (Throwable var11) {
         Log.info("Chances are you cursor is too small for this platform");
         throw new LWJGLException(var11);
      }
   }

   public Cursor getCursor(ImageData var1, int var2, int var3) throws IOException, LWJGLException {
      ByteBuffer var4 = var1.getImageBufferData();

      int var5;
      for(var5 = 0; var5 < var4.limit(); var5 += 4) {
         byte var6 = var4.get(var5);
         byte var7 = var4.get(var5 + 1);
         byte var8 = var4.get(var5 + 2);
         byte var9 = var4.get(var5 + 3);
         var4.put(var5 + 2, var6);
         var4.put(var5 + 1, var7);
         var4.put(var5, var8);
         var4.put(var5 + 3, var9);
      }

      try {
         var5 = var1.getHeight() - var3 - 1;
         if (var5 < 0) {
            var5 = 0;
         }

         return new Cursor(var1.getTexWidth(), var1.getTexHeight(), var2, var5, 1, var4.asIntBuffer(), (IntBuffer)null);
      } catch (Throwable var10) {
         Log.info("Chances are you cursor is too small for this platform");
         throw new LWJGLException(var10);
      }
   }

   public Cursor getAnimatedCursor(String var1, int var2, int var3, int var4, int var5, int[] var6) throws IOException, LWJGLException {
      IntBuffer var7 = ByteBuffer.allocateDirect(var6.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();

      for(int var8 = 0; var8 < var6.length; ++var8) {
         var7.put(var6[var8]);
      }

      var7.flip();
      TGAImageData var10 = new TGAImageData();
      ByteBuffer var9 = var10.loadImage(ResourceLoader.getResourceAsStream(var1), false, (int[])null);
      return new Cursor(var4, var5, var2, var3, var6.length, var9.asIntBuffer(), var7);
   }
}
