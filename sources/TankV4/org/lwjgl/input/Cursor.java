package org.lwjgl.input;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

public class Cursor {
   public static final int CURSOR_ONE_BIT_TRANSPARENCY = 1;
   public static final int CURSOR_8_BIT_ALPHA = 2;
   public static final int CURSOR_ANIMATION = 4;
   private final Cursor.CursorElement[] cursors;
   private int index;
   private boolean destroyed;

   public Cursor(int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) throws LWJGLException {
      Object var8;
      synchronized(var8 = OpenGLPackageAccess.global_lock){}
      if ((getCapabilities() & 1) == 0) {
         throw new LWJGLException("Native cursors not supported");
      } else {
         BufferChecks.checkBufferSize(var6, var1 * var2 * var5);
         if (var7 != null) {
            BufferChecks.checkBufferSize(var7, var5);
         }

         if (!Mouse.isCreated()) {
            throw new IllegalStateException("Mouse must be created before creating cursor objects");
         } else if (var1 * var2 * var5 > var6.remaining()) {
            throw new IllegalArgumentException("width*height*numImages > images.remaining()");
         } else if (var3 < var1 && var3 >= 0) {
            if (var4 < var2 && var4 >= 0) {
               Sys.initialize();
               var4 = var2 - 1 - var4;
               this.cursors = createCursors(var1, var2, var3, var4, var5, var6, var7);
            } else {
               throw new IllegalArgumentException("yHotspot > height || yHotspot < 0");
            }
         } else {
            throw new IllegalArgumentException("xHotspot > width || xHotspot < 0");
         }
      }
   }

   public static int getMinCursorSize() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (!Mouse.isCreated()) {
         throw new IllegalStateException("Mouse must be created.");
      } else {
         return Mouse.getImplementation().getMinCursorSize();
      }
   }

   public static int getMaxCursorSize() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (!Mouse.isCreated()) {
         throw new IllegalStateException("Mouse must be created.");
      } else {
         return Mouse.getImplementation().getMaxCursorSize();
      }
   }

   public static int getCapabilities() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return Mouse.getImplementation() != null ? Mouse.getImplementation().getNativeCursorCapabilities() : OpenGLPackageAccess.createImplementation().getNativeCursorCapabilities();
   }

   private static Cursor.CursorElement[] createCursors(int var0, int var1, int var2, int var3, int var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      IntBuffer var7 = BufferUtils.createIntBuffer(var5.remaining());
      flipImages(var0, var1, var4, var5, var7);
      Cursor.CursorElement[] var8;
      int var9;
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         Object var16 = Mouse.getImplementation().createCursor(var0, var1, var2, var3, var4, var7, var6);
         Cursor.CursorElement var18 = new Cursor.CursorElement(var16, -1L, -1L);
         var8 = new Cursor.CursorElement[]{var18};
         return var8;
      case 2:
         convertARGBtoABGR(var7);
         var8 = new Cursor.CursorElement[var4];

         for(var9 = 0; var9 < var4; ++var9) {
            Object var17 = Mouse.getImplementation().createCursor(var0, var1, var2, var3, 1, var7, (IntBuffer)null);
            long var20 = var6 != null ? (long)var6.get(var9) : 0L;
            long var22 = System.currentTimeMillis();
            var8[var9] = new Cursor.CursorElement(var17, var20, var22);
            var7.position(var0 * var1 * (var9 + 1));
         }

         return var8;
      case 3:
         var8 = new Cursor.CursorElement[var4];

         for(var9 = 0; var9 < var4; ++var9) {
            int var10 = var0 * var1;

            for(int var11 = 0; var11 < var10; ++var11) {
               int var12 = var11 + var9 * var10;
               int var13 = var7.get(var12) >> 24 & 255;
               if (var13 != 255) {
                  var7.put(var12, 0);
               }
            }

            Object var19 = Mouse.getImplementation().createCursor(var0, var1, var2, var3, 1, var7, (IntBuffer)null);
            long var21 = var6 != null ? (long)var6.get(var9) : 0L;
            long var14 = System.currentTimeMillis();
            var8[var9] = new Cursor.CursorElement(var19, var21, var14);
            var7.position(var0 * var1 * (var9 + 1));
         }

         return var8;
      default:
         throw new RuntimeException("Unknown OS");
      }
   }

   private static void convertARGBtoABGR(IntBuffer var0) {
      for(int var1 = 0; var1 < var0.limit(); ++var1) {
         int var2 = var0.get(var1);
         byte var3 = (byte)(var2 >>> 24);
         byte var4 = (byte)(var2 >>> 16);
         byte var5 = (byte)(var2 >>> 8);
         byte var6 = (byte)var2;
         int var7 = ((var3 & 255) << 24) + ((var6 & 255) << 16) + ((var5 & 255) << 8) + (var4 & 255);
         var0.put(var1, var7);
      }

   }

   private static void flipImages(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      for(int var5 = 0; var5 < var2; ++var5) {
         int var6 = var5 * var0 * var1;
         flipImage(var0, var1, var6, var3, var4);
      }

   }

   private static void flipImage(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      for(int var5 = 0; var5 < var1 >> 1; ++var5) {
         int var6 = var5 * var0 + var2;
         int var7 = (var1 - var5 - 1) * var0 + var2;

         for(int var8 = 0; var8 < var0; ++var8) {
            int var9 = var6 + var8;
            int var10 = var7 + var8;
            int var11 = var3.get(var9 + var3.position());
            var4.put(var9, var3.get(var10 + var3.position()));
            var4.put(var10, var11);
         }
      }

   }

   Object getHandle() {
      this.checkValid();
      return this.cursors[this.index].cursorHandle;
   }

   private void checkValid() {
      if (this.destroyed) {
         throw new IllegalStateException("The cursor is destroyed");
      }
   }

   public void destroy() {
      Object var1;
      synchronized(var1 = OpenGLPackageAccess.global_lock){}
      if (!this.destroyed) {
         if (Mouse.getNativeCursor() == this) {
            try {
               Mouse.setNativeCursor((Cursor)null);
            } catch (LWJGLException var7) {
            }
         }

         Cursor.CursorElement[] var2 = this.cursors;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Cursor.CursorElement var5 = var2[var4];
            Mouse.getImplementation().destroyCursor(var5.cursorHandle);
         }

         this.destroyed = true;
      }
   }

   protected void setTimeout() {
      this.checkValid();
      this.cursors[this.index].timeout = System.currentTimeMillis() + this.cursors[this.index].delay;
   }

   protected boolean hasTimedOut() {
      this.checkValid();
      return this.cursors.length > 1 && this.cursors[this.index].timeout < System.currentTimeMillis();
   }

   protected void nextCursor() {
      this.checkValid();
      this.index = ++this.index % this.cursors.length;
   }

   private static class CursorElement {
      final Object cursorHandle;
      final long delay;
      long timeout;

      CursorElement(Object var1, long var2, long var4) {
         this.cursorHandle = var1;
         this.delay = var2;
         this.timeout = var4;
      }
   }
}
