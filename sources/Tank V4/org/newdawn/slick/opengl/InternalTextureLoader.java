package org.newdawn.slick.opengl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.ResourceLoader;

public class InternalTextureLoader {
   private static final InternalTextureLoader loader = new InternalTextureLoader();
   private HashMap texturesLinear = new HashMap();
   private HashMap texturesNearest = new HashMap();
   private int dstPixelFormat = 32856;
   private boolean deferred;

   public static InternalTextureLoader get() {
      return loader;
   }

   private InternalTextureLoader() {
   }

   public void setDeferredLoading(boolean var1) {
      this.deferred = var1;
   }

   public boolean isDeferredLoading() {
      return this.deferred;
   }

   public void clear(String var1) {
      this.texturesLinear.remove(var1);
      this.texturesNearest.remove(var1);
   }

   public void clear() {
      this.texturesLinear.clear();
      this.texturesNearest.clear();
   }

   public void set16BitMode() {
      this.dstPixelFormat = 32859;
   }

   public static int createTextureID() {
      IntBuffer var0 = createIntBuffer(1);
      GL11.glGenTextures(var0);
      return var0.get(0);
   }

   public Texture getTexture(File var1, boolean var2, int var3) throws IOException {
      String var4 = var1.getAbsolutePath();
      FileInputStream var5 = new FileInputStream(var1);
      return this.getTexture(var5, var4, var2, var3, (int[])null);
   }

   public Texture getTexture(File var1, boolean var2, int var3, int[] var4) throws IOException {
      String var5 = var1.getAbsolutePath();
      FileInputStream var6 = new FileInputStream(var1);
      return this.getTexture(var6, var5, var2, var3, var4);
   }

   public Texture getTexture(String var1, boolean var2, int var3) throws IOException {
      InputStream var4 = ResourceLoader.getResourceAsStream(var1);
      return this.getTexture(var4, var1, var2, var3, (int[])null);
   }

   public Texture getTexture(String var1, boolean var2, int var3, int[] var4) throws IOException {
      InputStream var5 = ResourceLoader.getResourceAsStream(var1);
      return this.getTexture(var5, var1, var2, var3, var4);
   }

   public Texture getTexture(InputStream var1, String var2, boolean var3, int var4) throws IOException {
      return this.getTexture(var1, var2, var3, var4, (int[])null);
   }

   public TextureImpl getTexture(InputStream var1, String var2, boolean var3, int var4, int[] var5) throws IOException {
      if (this.deferred) {
         return new DeferredTexture(var1, var2, var3, var4, var5);
      } else {
         HashMap var6 = this.texturesLinear;
         if (var4 == 9728) {
            var6 = this.texturesNearest;
         }

         String var7 = var2;
         if (var5 != null) {
            var7 = var2 + ":" + var5[0] + ":" + var5[1] + ":" + var5[2];
         }

         var7 = var7 + ":" + var3;
         SoftReference var8 = (SoftReference)var6.get(var7);
         TextureImpl var9;
         if (var8 != null) {
            var9 = (TextureImpl)var8.get();
            if (var9 != null) {
               return var9;
            }

            var6.remove(var7);
         }

         try {
            GL11.glGetError();
         } catch (NullPointerException var10) {
            throw new RuntimeException("Image based resources must be loaded as part of init() or the game loop. They cannot be loaded before initialisation.");
         }

         var9 = this.getTexture(var1, var2, 3553, var4, var4, var3, var5);
         var9.setCacheName(var7);
         var6.put(var7, new SoftReference(var9));
         return var9;
      }
   }

   private TextureImpl getTexture(InputStream var1, String var2, int var3, int var4, int var5, boolean var6, int[] var7) throws IOException {
      int var8 = createTextureID();
      TextureImpl var9 = new TextureImpl(var2, var3, var8);
      GL11.glBindTexture(var3, var8);
      LoadableImageData var16 = ImageDataFactory.getImageDataFor(var2);
      ByteBuffer var10 = var16.loadImage(new BufferedInputStream(var1), var6, var7);
      int var11 = var16.getWidth();
      int var12 = var16.getHeight();
      boolean var15 = var16.getDepth() == 32;
      var9.setTextureWidth(var16.getTexWidth());
      var9.setTextureHeight(var16.getTexHeight());
      int var13 = var9.getTextureWidth();
      int var14 = var9.getTextureHeight();
      IntBuffer var17 = BufferUtils.createIntBuffer(16);
      GL11.glGetInteger(3379, var17);
      int var18 = var17.get(0);
      if (var13 <= var18 && var14 <= var18) {
         int var19 = var15 ? 6408 : 6407;
         boolean var20 = var15 ? true : true;
         var9.setWidth(var11);
         var9.setHeight(var12);
         var9.setAlpha(var15);
         GL11.glTexParameteri(var3, 10241, var5);
         GL11.glTexParameteri(var3, 10240, var4);
         GL11.glTexImage2D(var3, 0, this.dstPixelFormat, get2Fold(var11), get2Fold(var12), 0, var19, 5121, (ByteBuffer)var10);
         return var9;
      } else {
         throw new IOException("Attempt to allocate a texture to big for the current hardware");
      }
   }

   public Texture createTexture(int var1, int var2) throws IOException {
      return this.createTexture(var1, var2, 9728);
   }

   public Texture createTexture(int var1, int var2, int var3) throws IOException {
      EmptyImageData var4 = new EmptyImageData(var1, var2);
      return this.getTexture(var4, var3);
   }

   public Texture getTexture(ImageData var1, int var2) throws IOException {
      short var3 = 3553;
      int var4 = createTextureID();
      TextureImpl var5 = new TextureImpl("generated:" + var1, var3, var4);
      boolean var8 = false;
      GL11.glBindTexture(var3, var4);
      ByteBuffer var9 = var1.getImageBufferData();
      int var10 = var1.getWidth();
      int var11 = var1.getHeight();
      boolean var14 = var1.getDepth() == 32;
      var5.setTextureWidth(var1.getTexWidth());
      var5.setTextureHeight(var1.getTexHeight());
      int var12 = var5.getTextureWidth();
      int var13 = var5.getTextureHeight();
      int var15 = var14 ? 6408 : 6407;
      boolean var16 = var14 ? true : true;
      var5.setWidth(var10);
      var5.setHeight(var11);
      var5.setAlpha(var14);
      IntBuffer var17 = BufferUtils.createIntBuffer(16);
      GL11.glGetInteger(3379, var17);
      int var18 = var17.get(0);
      if (var12 <= var18 && var13 <= var18) {
         GL11.glTexParameteri(var3, 10241, var2);
         GL11.glTexParameteri(var3, 10240, var2);
         GL11.glTexImage2D(var3, 0, this.dstPixelFormat, get2Fold(var10), get2Fold(var11), 0, var15, 5121, (ByteBuffer)var9);
         return var5;
      } else {
         throw new IOException("Attempt to allocate a texture to big for the current hardware");
      }
   }

   public static int get2Fold(int var0) {
      int var1;
      for(var1 = 2; var1 < var0; var1 *= 2) {
      }

      return var1;
   }

   public static IntBuffer createIntBuffer(int var0) {
      ByteBuffer var1 = ByteBuffer.allocateDirect(4 * var0);
      var1.order(ByteOrder.nativeOrder());
      return var1.asIntBuffer();
   }
}
