package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.TextureUtils;
import shadersmod.client.Shaders;

public class TextureAtlasSprite {
   private float maxV;
   protected int width;
   private AnimationMetadataSection animationMetadata;
   public TextureAtlasSprite spriteSpecular = null;
   public float baseU;
   private static final String __OBFID = "CL_00001062";
   private float maxU;
   protected int[][] interpolatedFrameData;
   public int sheetWidth;
   protected boolean rotated;
   public int mipmapLevels = 0;
   public TextureAtlasSprite spriteNormal = null;
   public boolean isSpriteSingle = false;
   public int glSpriteTextureId = -1;
   public boolean isShadersSprite = false;
   private float minU;
   private static String locationNameCompass = "builtin/compass";
   protected List framesTextureData = Lists.newArrayList();
   public float baseV;
   protected int originX;
   private static String locationNameClock = "builtin/clock";
   protected int height;
   protected int tickCounter;
   protected int originY;
   private float minV;
   protected int frameCounter;
   public TextureAtlasSprite spriteSingle = null;
   public int sheetHeight;
   private final String iconName;
   private int indexInMap = -1;

   private void resetSprite() {
      this.animationMetadata = null;
      this.setFramesTextureData(Lists.newArrayList());
      this.frameCounter = 0;
      this.tickCounter = 0;
      if (this.spriteSingle != null) {
         this.spriteSingle.resetSprite();
      }

   }

   public boolean hasAnimationMetadata() {
      return this.animationMetadata != null;
   }

   public float getMinU() {
      return this.minU;
   }

   private void fixTransparentColor(int[] var1) {
      if (var1 != null) {
         long var2 = 0L;
         long var4 = 0L;
         long var6 = 0L;
         long var8 = 0L;

         int var10;
         int var11;
         int var12;
         int var13;
         int var14;
         int var15;
         for(var10 = 0; var10 < var1.length; ++var10) {
            var11 = var1[var10];
            var12 = var11 >> 24 & 255;
            if (var12 >= 16) {
               var13 = var11 >> 16 & 255;
               var14 = var11 >> 8 & 255;
               var15 = var11 & 255;
               var2 += (long)var13;
               var4 += (long)var14;
               var6 += (long)var15;
               ++var8;
            }
         }

         if (var8 > 0L) {
            var10 = (int)(var2 / var8);
            var11 = (int)(var4 / var8);
            var12 = (int)(var6 / var8);
            var13 = var10 << 16 | var11 << 8 | var12;

            for(var14 = 0; var14 < var1.length; ++var14) {
               var15 = var1[var14];
               int var16 = var15 >> 24 & 255;
               if (var16 <= 16) {
                  var1[var14] = var13;
               }
            }
         }
      }

   }

   private TextureAtlasSprite(TextureAtlasSprite var1) {
      this.iconName = var1.iconName;
      this.isSpriteSingle = true;
   }

   public int getOriginX() {
      return this.originX;
   }

   public AnimationMetadataSection getAnimationMetadata() {
      return this.animationMetadata;
   }

   public float getMinV() {
      return this.minV;
   }

   protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation var0) {
      String var1 = var0.toString();
      return (TextureAtlasSprite)(locationNameClock.equals(var1) ? new TextureClock(var1) : (locationNameCompass.equals(var1) ? new TextureCompass(var1) : new TextureAtlasSprite(var1)));
   }

   public static void setLocationNameCompass(String var0) {
      locationNameCompass = var0;
   }

   public float getInterpolatedU(double var1) {
      float var3 = this.maxU - this.minU;
      return this.minU + var3 * (float)var1 / 16.0F;
   }

   public void generateMipmaps(int var1) {
      ArrayList var2 = Lists.newArrayList();

      for(int var3 = 0; var3 < this.framesTextureData.size(); ++var3) {
         int[][] var4 = (int[][])this.framesTextureData.get(var3);
         if (var4 != null) {
            try {
               var2.add(TextureUtil.generateMipmapData(var1, this.width, var4));
            } catch (Throwable var9) {
               CrashReport var6 = CrashReport.makeCrashReport(var9, "Generating mipmaps for frame");
               CrashReportCategory var7 = var6.makeCategory("Frame being iterated");
               var7.addCrashSection("Frame index", var3);
               var7.addCrashSectionCallable("Frame sizes", new Callable(this, var4) {
                  final TextureAtlasSprite this$0;
                  private final int[][] val$aint;
                  private static final String __OBFID = "CL_00001063";

                  public String call() throws Exception {
                     StringBuilder var1 = new StringBuilder();
                     int[][] var5;
                     int var4 = (var5 = this.val$aint).length;

                     for(int var3 = 0; var3 < var4; ++var3) {
                        int[] var2 = var5[var3];
                        if (var1.length() > 0) {
                           var1.append(", ");
                        }

                        var1.append(var2 == null ? "null" : var2.length);
                     }

                     return String.valueOf(var1);
                  }

                  public Object call() throws Exception {
                     return this.call();
                  }

                  {
                     this.this$0 = var1;
                     this.val$aint = var2;
                  }
               });
               throw new ReportedException(var6);
            }
         }
      }

      this.setFramesTextureData(var2);
      if (this.spriteSingle != null) {
         this.spriteSingle.generateMipmaps(var1);
      }

   }

   public String getIconName() {
      return this.iconName;
   }

   public void copyFrom(TextureAtlasSprite var1) {
      this.originX = var1.originX;
      this.originY = var1.originY;
      this.width = var1.width;
      this.height = var1.height;
      this.rotated = var1.rotated;
      this.minU = var1.minU;
      this.maxU = var1.maxU;
      this.minV = var1.minV;
      this.maxV = var1.maxV;
      if (this.spriteSingle != null) {
         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
      }

   }

   private static int[][] getFrameTextureData(int[][] var0, int var1, int var2, int var3) {
      int[][] var4 = new int[var0.length][];

      for(int var5 = 0; var5 < var0.length; ++var5) {
         int[] var6 = var0[var5];
         if (var6 != null) {
            var4[var5] = new int[(var1 >> var5) * (var2 >> var5)];
            System.arraycopy(var6, var3 * var4[var5].length, var4[var5], 0, var4[var5].length);
         }
      }

      return var4;
   }

   protected TextureAtlasSprite(String var1) {
      this.iconName = var1;
      if (Config.isMultiTexture()) {
         this.spriteSingle = new TextureAtlasSprite(this);
      }

   }

   public float getMaxV() {
      return this.maxV;
   }

   public void setFramesTextureData(List var1) {
      this.framesTextureData = var1;
      if (this.spriteSingle != null) {
         this.spriteSingle.setFramesTextureData(var1);
      }

   }

   public void loadShaderSpriteFrames(ResourceLocation var1, int var2) throws IOException {
      IResource var3 = Config.getResource(var1);
      BufferedImage var4 = TextureUtil.readBufferedImage(var3.getInputStream());
      if (this.width != var4.getWidth()) {
         var4 = TextureUtils.scaleImage(var4, this.width);
      }

      AnimationMetadataSection var5 = (AnimationMetadataSection)var3.getMetadata("animation");
      int[][] var6 = new int[var2][];
      var6[0] = new int[var4.getWidth() * var4.getHeight()];
      var4.getRGB(0, 0, var4.getWidth(), var4.getHeight(), var6[0], 0, var4.getWidth());
      if (var5 == null) {
         this.framesTextureData.add(var6);
      } else {
         int var7 = var4.getHeight() / this.width;
         int var9;
         if (var5.getFrameCount() > 0) {
            Iterator var8 = var5.getFrameIndexSet().iterator();

            while(var8.hasNext()) {
               var9 = (Integer)var8.next();
               if (var9 >= var7) {
                  throw new RuntimeException("invalid frameindex " + var9);
               }

               this.allocateFrameTextureData(var9);
               this.framesTextureData.set(var9, getFrameTextureData(var6, this.width, this.width, var9));
            }

            this.animationMetadata = var5;
         } else {
            ArrayList var10 = Lists.newArrayList();

            for(var9 = 0; var9 < var7; ++var9) {
               this.framesTextureData.add(getFrameTextureData(var6, this.width, this.width, var9));
               var10.add(new AnimationFrame(var9, -1));
            }

            this.animationMetadata = new AnimationMetadataSection(var10, this.width, this.height, var5.getFrameTime(), var5.isInterpolate());
         }
      }

   }

   public void bindSpriteTexture() {
      if (this.glSpriteTextureId < 0) {
         this.glSpriteTextureId = TextureUtil.glGenTextures();
         TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
         TextureUtils.applyAnisotropicLevel();
      }

      TextureUtils.bindTexture(this.glSpriteTextureId);
   }

   public String toString() {
      return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
   }

   public int getIconHeight() {
      return this.height;
   }

   public float getInterpolatedV(double var1) {
      float var3 = this.maxV - this.minV;
      return this.minV + var3 * ((float)var1 / 16.0F);
   }

   public float toSingleV(float var1) {
      var1 -= this.baseV;
      float var2 = (float)this.sheetHeight / (float)this.height;
      var1 *= var2;
      return var1;
   }

   public int getIconWidth() {
      return this.width;
   }

   public void setAnimationMetadata(AnimationMetadataSection var1) {
      this.animationMetadata = var1;
   }

   public void clearFramesTextureData() {
      this.framesTextureData.clear();
      if (this.spriteSingle != null) {
         this.spriteSingle.clearFramesTextureData();
      }

   }

   public void setIconWidth(int var1) {
      this.width = var1;
      if (this.spriteSingle != null) {
         this.spriteSingle.setIconWidth(this.width);
      }

   }

   public boolean hasCustomLoader(IResourceManager var1, ResourceLocation var2) {
      return false;
   }

   public void loadSprite(BufferedImage[] var1, AnimationMetadataSection var2) throws IOException {
      this.resetSprite();
      int var3 = var1[0].getWidth();
      int var4 = var1[0].getHeight();
      this.width = var3;
      this.height = var4;
      int[][] var5 = new int[var1.length][];

      int var6;
      for(var6 = 0; var6 < var1.length; ++var6) {
         BufferedImage var7 = var1[var6];
         if (var7 != null) {
            if (var6 > 0 && (var7.getWidth() != var3 >> var6 || var7.getHeight() != var4 >> var6)) {
               throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", var6, var7.getWidth(), var7.getHeight(), var3 >> var6, var4 >> var6));
            }

            var5[var6] = new int[var7.getWidth() * var7.getHeight()];
            var7.getRGB(0, 0, var7.getWidth(), var7.getHeight(), var5[var6], 0, var7.getWidth());
         }
      }

      int var8;
      if (var2 == null) {
         if (var4 != var3) {
            throw new RuntimeException("broken aspect ratio and not an animation");
         }

         this.framesTextureData.add(var5);
      } else {
         var6 = var4 / var3;
         int var11 = var3;
         var8 = var3;
         this.height = this.width;
         int var10;
         if (var2.getFrameCount() > 0) {
            Iterator var9 = var2.getFrameIndexSet().iterator();

            while(var9.hasNext()) {
               var10 = (Integer)var9.next();
               if (var10 >= var6) {
                  throw new RuntimeException("invalid frameindex " + var10);
               }

               this.allocateFrameTextureData(var10);
               this.framesTextureData.set(var10, getFrameTextureData(var5, var11, var8, var10));
            }

            this.animationMetadata = var2;
         } else {
            ArrayList var13 = Lists.newArrayList();

            for(var10 = 0; var10 < var6; ++var10) {
               this.framesTextureData.add(getFrameTextureData(var5, var11, var8, var10));
               var13.add(new AnimationFrame(var10, -1));
            }

            this.animationMetadata = new AnimationMetadataSection(var13, this.width, this.height, var2.getFrameTime(), var2.isInterpolate());
         }
      }

      if (!this.isShadersSprite) {
         if (Config.isShaders()) {
            this.loadShadersSprites();
         }

         for(var6 = 0; var6 < this.framesTextureData.size(); ++var6) {
            int[][] var12 = (int[][])this.framesTextureData.get(var6);
            if (var12 != null && !this.iconName.startsWith("minecraft:blocks/leaves_")) {
               for(var8 = 0; var8 < var12.length; ++var8) {
                  int[] var14 = var12[var8];
                  this.fixTransparentColor(var14);
               }
            }
         }

         if (this.spriteSingle != null) {
            this.spriteSingle.loadSprite(var1, var2);
         }
      }

   }

   public int getOriginY() {
      return this.originY;
   }

   private void loadShadersSprites() {
      this.mipmapLevels = Config.getTextureMap().getMipmapLevels();
      String var1;
      ResourceLocation var2;
      TextureAtlasSprite var3;
      if (Shaders.configNormalMap) {
         var1 = this.iconName + "_n";
         var2 = new ResourceLocation(var1);
         var2 = Config.getTextureMap().completeResourceLocation(var2, 0);
         if (Config.hasResource(var2)) {
            try {
               var3 = new TextureAtlasSprite(var1);
               var3.isShadersSprite = true;
               var3.copyFrom(this);
               var3.loadShaderSpriteFrames(var2, this.mipmapLevels + 1);
               var3.generateMipmaps(this.mipmapLevels);
               this.spriteNormal = var3;
            } catch (IOException var5) {
               Config.warn("Error loading normal texture: " + var1);
               Config.warn(var5.getClass().getName() + ": " + var5.getMessage());
            }
         }
      }

      if (Shaders.configSpecularMap) {
         var1 = this.iconName + "_s";
         var2 = new ResourceLocation(var1);
         var2 = Config.getTextureMap().completeResourceLocation(var2, 0);
         if (Config.hasResource(var2)) {
            try {
               var3 = new TextureAtlasSprite(var1);
               var3.isShadersSprite = true;
               var3.copyFrom(this);
               var3.loadShaderSpriteFrames(var2, this.mipmapLevels + 1);
               var3.generateMipmaps(this.mipmapLevels);
               this.spriteSpecular = var3;
            } catch (IOException var4) {
               Config.warn("Error loading specular texture: " + var1);
               Config.warn(var4.getClass().getName() + ": " + var4.getMessage());
            }
         }
      }

   }

   private void allocateFrameTextureData(int var1) {
      if (this.framesTextureData.size() <= var1) {
         for(int var2 = this.framesTextureData.size(); var2 <= var1; ++var2) {
            this.framesTextureData.add((Object)null);
         }
      }

      if (this.spriteSingle != null) {
         this.spriteSingle.allocateFrameTextureData(var1);
      }

   }

   public void updateAnimation() {
      if (this.animationMetadata != null) {
         ++this.tickCounter;
         if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
            int var2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % var2;
            this.tickCounter = 0;
            int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
            boolean var4 = false;
            boolean var5 = this.isSpriteSingle;
            if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size()) {
               TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(var3), this.width, this.height, this.originX, this.originY, var4, var5);
            }
         } else if (this.animationMetadata.isInterpolate()) {
            this.updateAnimationInterpolated();
         }
      }

   }

   public void initSprite(int var1, int var2, int var3, int var4, boolean var5) {
      this.originX = var3;
      this.originY = var4;
      this.rotated = var5;
      float var6 = (float)(0.009999999776482582D / (double)var1);
      float var7 = (float)(0.009999999776482582D / (double)var2);
      this.minU = (float)var3 / (float)((double)var1) + var6;
      this.maxU = (float)(var3 + this.width) / (float)((double)var1) - var6;
      this.minV = (float)var4 / (float)var2 + var7;
      this.maxV = (float)(var4 + this.height) / (float)var2 - var7;
      this.baseU = Math.min(this.minU, this.maxU);
      this.baseV = Math.min(this.minV, this.maxV);
      if (this.spriteSingle != null) {
         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
      }

      if (this.spriteNormal != null) {
         this.spriteNormal.initSprite(var1, var2, var3, var4, var5);
      }

      if (this.spriteSpecular != null) {
         this.spriteSpecular.initSprite(var1, var2, var3, var4, var5);
      }

   }

   public double getSpriteV16(float var1) {
      float var2 = this.maxV - this.minV;
      return (double)((var1 - this.minV) / var2 * 16.0F);
   }

   public float toSingleU(float var1) {
      var1 -= this.baseU;
      float var2 = (float)this.sheetWidth / (float)this.width;
      var1 *= var2;
      return var1;
   }

   public void setIconHeight(int var1) {
      this.height = var1;
      if (this.spriteSingle != null) {
         this.spriteSingle.setIconHeight(this.height);
      }

   }

   public boolean load(IResourceManager var1, ResourceLocation var2) {
      return true;
   }

   public void deleteSpriteTexture() {
      if (this.glSpriteTextureId >= 0) {
         TextureUtil.deleteTexture(this.glSpriteTextureId);
         this.glSpriteTextureId = -1;
      }

   }

   private void updateAnimationInterpolated() {
      double var1 = 1.0D - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
      int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
      int var4 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
      int var5 = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % var4);
      if (var3 != var5 && var5 >= 0 && var5 < this.framesTextureData.size()) {
         int[][] var6 = (int[][])this.framesTextureData.get(var3);
         int[][] var7 = (int[][])this.framesTextureData.get(var5);
         if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != var6.length) {
            this.interpolatedFrameData = new int[var6.length][];
         }

         for(int var8 = 0; var8 < var6.length; ++var8) {
            if (this.interpolatedFrameData[var8] == null) {
               this.interpolatedFrameData[var8] = new int[var6[var8].length];
            }

            if (var8 < var7.length && var7[var8].length == var6[var8].length) {
               for(int var9 = 0; var9 < var6[var8].length; ++var9) {
                  int var10 = var6[var8][var9];
                  int var11 = var7[var8][var9];
                  int var12 = (int)((double)((var10 & 16711680) >> 16) * var1 + (double)((var11 & 16711680) >> 16) * (1.0D - var1));
                  int var13 = (int)((double)((var10 & '\uff00') >> 8) * var1 + (double)((var11 & '\uff00') >> 8) * (1.0D - var1));
                  int var14 = (int)((double)(var10 & 255) * var1 + (double)(var11 & 255) * (1.0D - var1));
                  this.interpolatedFrameData[var8][var9] = var10 & -16777216 | var12 << 16 | var13 << 8 | var14;
               }
            }
         }

         TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
      }

   }

   public int getFrameCount() {
      return this.framesTextureData.size();
   }

   public List getFramesTextureData() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.framesTextureData);
      return var1;
   }

   public double getSpriteU16(float var1) {
      float var2 = this.maxU - this.minU;
      return (double)((var1 - this.minU) / var2 * 16.0F);
   }

   public static void setLocationNameClock(String var0) {
      locationNameClock = var0;
   }

   public int[][] getFrameTextureData(int var1) {
      return (int[][])this.framesTextureData.get(var1);
   }

   public int getIndexInMap() {
      return this.indexInMap;
   }

   public float getMaxU() {
      return this.maxU;
   }

   public void setIndexInMap(int var1) {
      this.indexInMap = var1;
   }
}
