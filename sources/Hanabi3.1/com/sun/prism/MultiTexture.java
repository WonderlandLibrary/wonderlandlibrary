package com.sun.prism;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public final class MultiTexture implements Texture {
   private int width;
   private int height;
   private PixelFormat format;
   private Texture.WrapMode wrapMode;
   private boolean linearFiltering;
   private final ArrayList textures;
   private int lastImageSerial;

   public MultiTexture(PixelFormat var1, Texture.WrapMode var2, int var3, int var4) {
      this.linearFiltering = true;
      this.width = var3;
      this.height = var4;
      this.format = var1;
      this.wrapMode = var2;
      this.textures = new ArrayList(4);
   }

   private MultiTexture(MultiTexture var1, Texture.WrapMode var2) {
      this(var1.format, var2, var1.width, var1.height);

      for(int var3 = 0; var3 < var1.textureCount(); ++var3) {
         Texture var4 = var1.getTexture(var3);
         this.setTexture(var4.getSharedTexture(var2), var3);
      }

      this.linearFiltering = var1.linearFiltering;
      this.lastImageSerial = var1.lastImageSerial;
   }

   public Texture getSharedTexture(Texture.WrapMode var1) {
      this.assertLocked();
      if (this.wrapMode == var1) {
         this.lock();
         return this;
      } else {
         switch (var1) {
            case REPEAT:
               if (this.wrapMode != Texture.WrapMode.CLAMP_TO_EDGE) {
                  return null;
               }
               break;
            case CLAMP_TO_EDGE:
               if (this.wrapMode != Texture.WrapMode.REPEAT) {
                  return null;
               }
               break;
            default:
               return null;
         }

         MultiTexture var2 = new MultiTexture(this, var1);
         var2.lock();
         return var2;
      }
   }

   public int textureCount() {
      return this.textures.size();
   }

   public void setTexture(Texture var1, int var2) {
      if (!var1.getWrapMode().isCompatibleWith(this.wrapMode)) {
         throw new IllegalArgumentException("texture wrap mode must match multi-texture mode");
      } else {
         if (this.textures.size() < var2 + 1) {
            for(int var3 = this.textures.size(); var3 < var2; ++var3) {
               this.textures.add((Object)null);
            }

            this.textures.add(var1);
         } else {
            this.textures.set(var2, var1);
         }

         var1.setLinearFiltering(this.linearFiltering);
      }
   }

   public Texture getTexture(int var1) {
      return (Texture)this.textures.get(var1);
   }

   public Texture[] getTextures() {
      return (Texture[])this.textures.toArray(new Texture[this.textures.size()]);
   }

   public void removeTexture(Texture var1) {
      this.textures.remove(var1);
   }

   public void removeTexture(int var1) {
      this.textures.remove(var1);
   }

   public PixelFormat getPixelFormat() {
      return this.format;
   }

   public int getPhysicalWidth() {
      return this.width;
   }

   public int getPhysicalHeight() {
      return this.height;
   }

   public int getContentX() {
      return 0;
   }

   public int getContentY() {
      return 0;
   }

   public int getContentWidth() {
      return this.width;
   }

   public int getContentHeight() {
      return this.height;
   }

   public int getLastImageSerial() {
      return this.lastImageSerial;
   }

   public void setLastImageSerial(int var1) {
      this.lastImageSerial = var1;
   }

   public void update(Image var1) {
      throw new UnsupportedOperationException("Update from Image not supported");
   }

   public void update(Image var1, int var2, int var3) {
      throw new UnsupportedOperationException("Update from Image not supported");
   }

   public void update(Image var1, int var2, int var3, int var4, int var5) {
      throw new UnsupportedOperationException("Update from Image not supported");
   }

   public void update(Image var1, int var2, int var3, int var4, int var5, boolean var6) {
      throw new UnsupportedOperationException("Update from Image not supported");
   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      throw new UnsupportedOperationException("Update from generic Buffer not supported");
   }

   public void update(MediaFrame var1, boolean var2) {
      if (var1.getPixelFormat() != PixelFormat.MULTI_YCbCr_420) {
         throw new IllegalArgumentException("Invalid pixel format in MediaFrame");
      } else {
         int var4 = var1.getEncodedWidth();
         int var5 = var1.getEncodedHeight();

         for(int var6 = 0; var6 < var1.planeCount(); ++var6) {
            Texture var3 = (Texture)this.textures.get(var6);
            if (null != var3) {
               int var7 = var4;
               int var8 = var5;
               if (var6 == 2 || var6 == 1) {
                  var7 = var4 / 2;
                  var8 = var5 / 2;
               }

               ByteBuffer var9 = var1.getBufferForPlane(var6);
               var3.update(var9, PixelFormat.BYTE_ALPHA, 0, 0, 0, 0, var7, var8, var1.strideForPlane(var6), var2);
            }
         }

      }
   }

   public Texture.WrapMode getWrapMode() {
      return this.wrapMode;
   }

   public boolean getUseMipmap() {
      return false;
   }

   public boolean getLinearFiltering() {
      return this.linearFiltering;
   }

   public void setLinearFiltering(boolean var1) {
      this.linearFiltering = var1;
      Iterator var2 = this.textures.iterator();

      while(var2.hasNext()) {
         Texture var3 = (Texture)var2.next();
         var3.setLinearFiltering(var1);
      }

   }

   public void lock() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.lock();
      }

   }

   public void unlock() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.unlock();
      }

   }

   public boolean isLocked() {
      Iterator var1 = this.textures.iterator();

      Texture var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (Texture)var1.next();
      } while(!var2.isLocked());

      return true;
   }

   public int getLockCount() {
      int var1 = 0;

      Texture var3;
      for(Iterator var2 = this.textures.iterator(); var2.hasNext(); var1 = Math.max(var1, var3.getLockCount())) {
         var3 = (Texture)var2.next();
      }

      return var1;
   }

   public void assertLocked() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.assertLocked();
      }

   }

   public void makePermanent() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.makePermanent();
      }

   }

   public void contentsUseful() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.contentsUseful();
      }

   }

   public void contentsNotUseful() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.contentsNotUseful();
      }

   }

   public boolean isSurfaceLost() {
      Iterator var1 = this.textures.iterator();

      Texture var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (Texture)var1.next();
      } while(!var2.isSurfaceLost());

      return true;
   }

   public void dispose() {
      Iterator var1 = this.textures.iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.dispose();
      }

      this.textures.clear();
   }

   public int getMaxContentWidth() {
      return this.getPhysicalWidth();
   }

   public int getMaxContentHeight() {
      return this.getPhysicalHeight();
   }

   public void setContentWidth(int var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public void setContentHeight(int var1) {
      throw new UnsupportedOperationException("Not supported.");
   }
}
