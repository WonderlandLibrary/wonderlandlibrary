package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.MathHelper;

public class Stitcher {
   private final int mipmapLevelStitcher;
   private final int maxHeight;
   private final int maxTileDimension;
   private static final String __OBFID = "CL_00001054";
   private int currentHeight;
   private final List stitchSlots = Lists.newArrayListWithCapacity(256);
   private final int maxWidth;
   private final Set setStitchHolders = Sets.newHashSetWithExpectedSize(256);
   private int currentWidth;
   private final boolean forcePowerOf2;

   static int access$0(int var0, int var1) {
      return getMipmapDimension(var0, var1);
   }

   private boolean expandAndAllocateSlot(Stitcher.Holder var1) {
      int var2 = Math.min(var1.getWidth(), var1.getHeight());
      boolean var3 = this.currentWidth == 0 && this.currentHeight == 0;
      boolean var4;
      int var5;
      if (this.forcePowerOf2) {
         var5 = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
         int var6 = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
         int var7 = MathHelper.roundUpToPowerOfTwo(this.currentWidth + var2);
         int var8 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + var2);
         boolean var9 = var7 <= this.maxWidth;
         boolean var10 = var8 <= this.maxHeight;
         if (!var9 && !var10) {
            return false;
         }

         boolean var11 = var5 != var7;
         boolean var12 = var6 != var8;
         if (var11 ^ var12) {
            var4 = !var11;
         } else {
            var4 = var9 && var5 <= var6;
         }
      } else {
         boolean var13 = this.currentWidth + var2 <= this.maxWidth;
         boolean var14 = this.currentHeight + var2 <= this.maxHeight;
         if (!var13 && !var14) {
            return false;
         }

         var4 = var13 && (var3 || this.currentWidth <= this.currentHeight);
      }

      var5 = Math.max(var1.getWidth(), var1.getHeight());
      if (MathHelper.roundUpToPowerOfTwo((!var4 ? this.currentHeight : this.currentWidth) + var5) > (!var4 ? this.maxHeight : this.maxWidth)) {
         return false;
      } else {
         Stitcher.Slot var15;
         if (var4) {
            if (var1.getWidth() > var1.getHeight()) {
               var1.rotate();
            }

            if (this.currentHeight == 0) {
               this.currentHeight = var1.getHeight();
            }

            var15 = new Stitcher.Slot(this.currentWidth, 0, var1.getWidth(), this.currentHeight);
            this.currentWidth += var1.getWidth();
         } else {
            var15 = new Stitcher.Slot(0, this.currentHeight, this.currentWidth, var1.getHeight());
            this.currentHeight += var1.getHeight();
         }

         var15.addSlot(var1);
         this.stitchSlots.add(var15);
         return true;
      }
   }

   public void doStitch() {
      Stitcher.Holder[] var1 = (Stitcher.Holder[])this.setStitchHolders.toArray(new Stitcher.Holder[this.setStitchHolders.size()]);
      Arrays.sort(var1);
      Stitcher.Holder[] var5 = var1;
      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Stitcher.Holder var2 = var5[var3];
         if (var2 < 0) {
            String var6 = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", var2.getAtlasSprite().getIconName(), var2.getAtlasSprite().getIconWidth(), var2.getAtlasSprite().getIconHeight(), this.currentWidth, this.currentHeight, this.maxWidth, this.maxHeight);
            throw new StitcherException(var2, var6);
         }
      }

      if (this.forcePowerOf2) {
         this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
         this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
      }

   }

   public List getStichSlots() {
      ArrayList var1 = Lists.newArrayList();
      Iterator var3 = this.stitchSlots.iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         ((Stitcher.Slot)var2).getAllStitchSlots(var1);
      }

      ArrayList var8 = Lists.newArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Object var9 = var4.next();
         Stitcher.Slot var5 = (Stitcher.Slot)var9;
         Stitcher.Holder var6 = var5.getStitchHolder();
         TextureAtlasSprite var7 = var6.getAtlasSprite();
         var7.initSprite(this.currentWidth, this.currentHeight, var5.getOriginX(), var5.getOriginY(), var6.isRotated());
         var8.add(var7);
      }

      return var8;
   }

   private static int getMipmapDimension(int var0, int var1) {
      return (var0 >> var1) + ((var0 & (1 << var1) - 1) == 0 ? 0 : 1) << var1;
   }

   public Stitcher(int var1, int var2, boolean var3, int var4, int var5) {
      this.mipmapLevelStitcher = var5;
      this.maxWidth = var1;
      this.maxHeight = var2;
      this.forcePowerOf2 = var3;
      this.maxTileDimension = var4;
   }

   public int getCurrentWidth() {
      return this.currentWidth;
   }

   public void addSprite(TextureAtlasSprite var1) {
      Stitcher.Holder var2 = new Stitcher.Holder(var1, this.mipmapLevelStitcher);
      if (this.maxTileDimension > 0) {
         var2.setNewDimension(this.maxTileDimension);
      }

      this.setStitchHolders.add(var2);
   }

   public int getCurrentHeight() {
      return this.currentHeight;
   }

   public static class Slot {
      private final int width;
      private List subSlots;
      private static final String __OBFID = "CL_00001056";
      private Stitcher.Holder holder;
      private final int height;
      private final int originY;
      private final int originX;

      public Stitcher.Holder getStitchHolder() {
         return this.holder;
      }

      public int getOriginX() {
         return this.originX;
      }

      public Slot(int var1, int var2, int var3, int var4) {
         this.originX = var1;
         this.originY = var2;
         this.width = var3;
         this.height = var4;
      }

      public int getOriginY() {
         return this.originY;
      }

      public void getAllStitchSlots(List var1) {
         if (this.holder != null) {
            var1.add(this);
         } else if (this.subSlots != null) {
            Iterator var3 = this.subSlots.iterator();

            while(var3.hasNext()) {
               Object var2 = var3.next();
               ((Stitcher.Slot)var2).getAllStitchSlots(var1);
            }
         }

      }

      public String toString() {
         return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
      }
   }

   public static class Holder implements Comparable {
      private boolean rotated;
      private final TextureAtlasSprite theTexture;
      private final int mipmapLevelHolder;
      private float scaleFactor = 1.0F;
      private final int height;
      private final int width;
      private static final String __OBFID = "CL_00001055";

      public boolean isRotated() {
         return this.rotated;
      }

      public int compareTo(Object var1) {
         return this.compareTo((Stitcher.Holder)var1);
      }

      public void setNewDimension(int var1) {
         if (this.width > var1 && this.height > var1) {
            this.scaleFactor = (float)var1 / (float)Math.min(this.width, this.height);
         }

      }

      public Holder(TextureAtlasSprite var1, int var2) {
         this.theTexture = var1;
         this.width = var1.getIconWidth();
         this.height = var1.getIconHeight();
         this.mipmapLevelHolder = var2;
         this.rotated = Stitcher.access$0(this.height, var2) > Stitcher.access$0(this.width, var2);
      }

      public String toString() {
         return "Holder{width=" + this.width + ", height=" + this.height + ", name=" + this.theTexture.getIconName() + '}';
      }

      public void rotate() {
         this.rotated = !this.rotated;
      }

      public TextureAtlasSprite getAtlasSprite() {
         return this.theTexture;
      }

      public int getWidth() {
         return this.rotated ? Stitcher.access$0((int)((float)this.height * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.access$0((int)((float)this.width * this.scaleFactor), this.mipmapLevelHolder);
      }

      public int getHeight() {
         return this.rotated ? Stitcher.access$0((int)((float)this.width * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.access$0((int)((float)this.height * this.scaleFactor), this.mipmapLevelHolder);
      }

      public int compareTo(Stitcher.Holder var1) {
         int var2;
         if (this.getHeight() == var1.getHeight()) {
            if (this.getWidth() == var1.getWidth()) {
               if (this.theTexture.getIconName() == null) {
                  return var1.theTexture.getIconName() == null ? 0 : -1;
               }

               return this.theTexture.getIconName().compareTo(var1.theTexture.getIconName());
            }

            var2 = this.getWidth() < var1.getWidth() ? 1 : -1;
         } else {
            var2 = this.getHeight() < var1.getHeight() ? 1 : -1;
         }

         return var2;
      }
   }
}
