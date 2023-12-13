/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.TextureClock;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optfine.Config;
import optfine.TextureUtils;

public class TextureAtlasSprite {
    private final String iconName;
    protected List framesTextureData = Lists.newArrayList();
    protected int[][] interpolatedFrameData;
    private AnimationMetadataSection animationMetadata;
    protected boolean rotated;
    protected int originX;
    protected int originY;
    protected int width;
    protected int height;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;
    private static String locationNameClock = "builtin/clock";
    private static String locationNameCompass = "builtin/compass";
    private static final String __OBFID = "CL_00001062";
    private int indexInMap = -1;
    public float baseU;
    public float baseV;
    public int sheetWidth;
    public int sheetHeight;
    public int glSpriteTextureId = -1;
    public TextureAtlasSprite spriteSingle = null;
    public boolean isSpriteSingle = false;
    public int mipmapLevels = 0;

    private TextureAtlasSprite(TextureAtlasSprite p_i9_1_) {
        this.iconName = p_i9_1_.iconName;
        this.isSpriteSingle = true;
    }

    protected TextureAtlasSprite(String spriteName) {
        this.iconName = spriteName;
        if (Config.isMultiTexture()) {
            this.spriteSingle = new TextureAtlasSprite(this);
        }
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
        String s2 = spriteResourceLocation.toString();
        return locationNameClock.equals(s2) ? new TextureClock(s2) : (locationNameCompass.equals(s2) ? new TextureCompass(s2) : new TextureAtlasSprite(s2));
    }

    public static void setLocationNameClock(String clockName) {
        locationNameClock = clockName;
    }

    public static void setLocationNameCompass(String compassName) {
        locationNameCompass = compassName;
    }

    public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
        this.originX = originInX;
        this.originY = originInY;
        this.rotated = rotatedIn;
        float f2 = (float)((double)0.01f / (double)inX);
        float f1 = (float)((double)0.01f / (double)inY);
        this.minU = (float)originInX / (float)((double)inX) + f2;
        this.maxU = (float)(originInX + this.width) / (float)((double)inX) - f2;
        this.minV = (float)originInY / (float)inY + f1;
        this.maxV = (float)(originInY + this.height) / (float)inY - f1;
        this.baseU = Math.min(this.minU, this.maxU);
        this.baseV = Math.min(this.minV, this.maxV);
        if (this.spriteSingle != null) {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
    }

    public void copyFrom(TextureAtlasSprite atlasSpirit) {
        this.originX = atlasSpirit.originX;
        this.originY = atlasSpirit.originY;
        this.width = atlasSpirit.width;
        this.height = atlasSpirit.height;
        this.rotated = atlasSpirit.rotated;
        this.minU = atlasSpirit.minU;
        this.maxU = atlasSpirit.maxU;
        this.minV = atlasSpirit.minV;
        this.maxV = atlasSpirit.maxV;
        if (this.spriteSingle != null) {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getOriginY() {
        return this.originY;
    }

    public int getIconWidth() {
        return this.width;
    }

    public int getIconHeight() {
        return this.height;
    }

    public float getMinU() {
        return this.minU;
    }

    public float getMaxU() {
        return this.maxU;
    }

    public float getInterpolatedU(double u2) {
        float f2 = this.maxU - this.minU;
        return this.minU + f2 * (float)u2 / 16.0f;
    }

    public float getMinV() {
        return this.minV;
    }

    public float getMaxV() {
        return this.maxV;
    }

    public float getInterpolatedV(double v2) {
        float f2 = this.maxV - this.minV;
        return this.minV + f2 * ((float)v2 / 16.0f);
    }

    public String getIconName() {
        return this.iconName;
    }

    public void updateAnimation() {
        ++this.tickCounter;
        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            int i2 = this.animationMetadata.getFrameIndex(this.frameCounter);
            int j2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % j2;
            this.tickCounter = 0;
            int k2 = this.animationMetadata.getFrameIndex(this.frameCounter);
            boolean flag = false;
            boolean flag1 = this.isSpriteSingle;
            if (i2 != k2 && k2 >= 0 && k2 < this.framesTextureData.size()) {
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(k2), this.width, this.height, this.originX, this.originY, flag, flag1);
            }
        } else if (this.animationMetadata.isInterpolate()) {
            this.updateAnimationInterpolated();
        }
    }

    private void updateAnimationInterpolated() {
        int j2;
        int k2;
        double d0 = 1.0 - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int i2 = this.animationMetadata.getFrameIndex(this.frameCounter);
        if (i2 != (k2 = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % (j2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount()))) && k2 >= 0 && k2 < this.framesTextureData.size()) {
            int[][] aint = (int[][])this.framesTextureData.get(i2);
            int[][] aint1 = (int[][])this.framesTextureData.get(k2);
            if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length) {
                this.interpolatedFrameData = new int[aint.length][];
            }
            for (int l2 = 0; l2 < aint.length; ++l2) {
                if (this.interpolatedFrameData[l2] == null) {
                    this.interpolatedFrameData[l2] = new int[aint[l2].length];
                }
                if (l2 >= aint1.length || aint1[l2].length != aint[l2].length) continue;
                for (int i1 = 0; i1 < aint[l2].length; ++i1) {
                    int j1 = aint[l2][i1];
                    int k1 = aint1[l2][i1];
                    int l1 = (int)((double)((j1 & 0xFF0000) >> 16) * d0 + (double)((k1 & 0xFF0000) >> 16) * (1.0 - d0));
                    int i22 = (int)((double)((j1 & 0xFF00) >> 8) * d0 + (double)((k1 & 0xFF00) >> 8) * (1.0 - d0));
                    int j22 = (int)((double)(j1 & 0xFF) * d0 + (double)(k1 & 0xFF) * (1.0 - d0));
                    this.interpolatedFrameData[l2][i1] = j1 & 0xFF000000 | l1 << 16 | i22 << 8 | j22;
                }
            }
            TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
        }
    }

    public int[][] getFrameTextureData(int index) {
        return (int[][])this.framesTextureData.get(index);
    }

    public int getFrameCount() {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int newWidth) {
        this.width = newWidth;
        if (this.spriteSingle != null) {
            this.spriteSingle.setIconWidth(this.width);
        }
    }

    public void setIconHeight(int newHeight) {
        this.height = newHeight;
        if (this.spriteSingle != null) {
            this.spriteSingle.setIconHeight(this.height);
        }
    }

    public void loadSprite(BufferedImage[] images, AnimationMetadataSection meta) throws IOException {
        this.resetSprite();
        int i2 = images[0].getWidth();
        int j2 = images[0].getHeight();
        this.width = i2;
        this.height = j2;
        int[][] aint = new int[images.length][];
        for (int k2 = 0; k2 < images.length; ++k2) {
            BufferedImage bufferedimage = images[k2];
            if (bufferedimage == null) continue;
            if (k2 > 0 && (bufferedimage.getWidth() != i2 >> k2 || bufferedimage.getHeight() != j2 >> k2)) {
                throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", k2, bufferedimage.getWidth(), bufferedimage.getHeight(), i2 >> k2, j2 >> k2));
            }
            aint[k2] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k2], 0, bufferedimage.getWidth());
        }
        if (meta == null) {
            if (j2 != i2) {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }
            this.framesTextureData.add(aint);
        } else {
            int j1 = j2 / i2;
            int k1 = i2;
            int l2 = i2;
            this.height = this.width;
            if (meta.getFrameCount() > 0) {
                for (int i1 : meta.getFrameIndexSet()) {
                    if (i1 >= j1) {
                        throw new RuntimeException("invalid frameindex " + i1);
                    }
                    this.allocateFrameTextureData(i1);
                    this.framesTextureData.set(i1, TextureAtlasSprite.getFrameTextureData(aint, k1, l2, i1));
                }
                this.animationMetadata = meta;
            } else {
                ArrayList<AnimationFrame> arraylist = Lists.newArrayList();
                for (int i22 = 0; i22 < j1; ++i22) {
                    this.framesTextureData.add(TextureAtlasSprite.getFrameTextureData(aint, k1, l2, i22));
                    arraylist.add(new AnimationFrame(i22, -1));
                }
                this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height, meta.getFrameTime(), meta.isInterpolate());
            }
        }
        for (int l1 = 0; l1 < this.framesTextureData.size(); ++l1) {
            int[][] aint1 = (int[][])this.framesTextureData.get(l1);
            if (aint1 == null || this.iconName.startsWith("minecraft:blocks/leaves_")) continue;
            for (int j22 = 0; j22 < aint1.length; ++j22) {
                int[] aint2 = aint1[j22];
                this.fixTransparentColor(aint2);
            }
        }
        if (this.spriteSingle != null) {
            this.spriteSingle.loadSprite(images, meta);
        }
    }

    public void generateMipmaps(int level) {
        ArrayList<int[][]> arraylist = Lists.newArrayList();
        for (int i2 = 0; i2 < this.framesTextureData.size(); ++i2) {
            final int[][] aint = (int[][])this.framesTextureData.get(i2);
            if (aint == null) continue;
            try {
                arraylist.add(TextureUtil.generateMipmapData(level, this.width, aint));
                continue;
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
                crashreportcategory.addCrashSection("Frame index", i2);
                crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable(){
                    private static final String __OBFID = "CL_00001063";

                    public String call() throws Exception {
                        StringBuilder stringbuilder = new StringBuilder();
                        for (int[] aint1 : aint) {
                            if (stringbuilder.length() > 0) {
                                stringbuilder.append(", ");
                            }
                            stringbuilder.append(aint1 == null ? "null" : Integer.valueOf(aint1.length));
                        }
                        return stringbuilder.toString();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        this.setFramesTextureData(arraylist);
        if (this.spriteSingle != null) {
            this.spriteSingle.generateMipmaps(level);
        }
    }

    private void allocateFrameTextureData(int index) {
        if (this.framesTextureData.size() <= index) {
            for (int i2 = this.framesTextureData.size(); i2 <= index; ++i2) {
                this.framesTextureData.add(null);
            }
        }
        if (this.spriteSingle != null) {
            this.spriteSingle.allocateFrameTextureData(index);
        }
    }

    private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_) {
        int[][] aint = new int[data.length][];
        for (int i2 = 0; i2 < data.length; ++i2) {
            int[] aint1 = data[i2];
            if (aint1 == null) continue;
            aint[i2] = new int[(rows >> i2) * (columns >> i2)];
            System.arraycopy(aint1, p_147962_3_ * aint[i2].length, aint[i2], 0, aint[i2].length);
        }
        return aint;
    }

    public void clearFramesTextureData() {
        this.framesTextureData.clear();
        if (this.spriteSingle != null) {
            this.spriteSingle.clearFramesTextureData();
        }
    }

    public boolean hasAnimationMetadata() {
        return this.animationMetadata != null;
    }

    public void setFramesTextureData(List newFramesTextureData) {
        this.framesTextureData = newFramesTextureData;
        if (this.spriteSingle != null) {
            this.spriteSingle.setFramesTextureData(newFramesTextureData);
        }
    }

    private void resetSprite() {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
        if (this.spriteSingle != null) {
            this.spriteSingle.resetSprite();
        }
    }

    public String toString() {
        return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    public boolean hasCustomLoader(IResourceManager p_hasCustomLoader_1_, ResourceLocation p_hasCustomLoader_2_) {
        return false;
    }

    public boolean load(IResourceManager p_load_1_, ResourceLocation p_load_2_) {
        return true;
    }

    public int getIndexInMap() {
        return this.indexInMap;
    }

    public void setIndexInMap(int p_setIndexInMap_1_) {
        this.indexInMap = p_setIndexInMap_1_;
    }

    private void fixTransparentColor(int[] p_fixTransparentColor_1_) {
        if (p_fixTransparentColor_1_ != null) {
            long i2 = 0L;
            long j2 = 0L;
            long k2 = 0L;
            long l2 = 0L;
            for (int i1 = 0; i1 < p_fixTransparentColor_1_.length; ++i1) {
                int j1 = p_fixTransparentColor_1_[i1];
                int k1 = j1 >> 24 & 0xFF;
                if (k1 < 16) continue;
                int l1 = j1 >> 16 & 0xFF;
                int i22 = j1 >> 8 & 0xFF;
                int j22 = j1 & 0xFF;
                i2 += (long)l1;
                j2 += (long)i22;
                k2 += (long)j22;
                ++l2;
            }
            if (l2 > 0L) {
                int l22 = (int)(i2 / l2);
                int i3 = (int)(j2 / l2);
                int j3 = (int)(k2 / l2);
                int k3 = l22 << 16 | i3 << 8 | j3;
                for (int l3 = 0; l3 < p_fixTransparentColor_1_.length; ++l3) {
                    int i4 = p_fixTransparentColor_1_[l3];
                    int k22 = i4 >> 24 & 0xFF;
                    if (k22 > 16) continue;
                    p_fixTransparentColor_1_[l3] = k3;
                }
            }
        }
    }

    public double getSpriteU16(float p_getSpriteU16_1_) {
        float f2 = this.maxU - this.minU;
        return (p_getSpriteU16_1_ - this.minU) / f2 * 16.0f;
    }

    public double getSpriteV16(float p_getSpriteV16_1_) {
        float f2 = this.maxV - this.minV;
        return (p_getSpriteV16_1_ - this.minV) / f2 * 16.0f;
    }

    public void bindSpriteTexture() {
        if (this.glSpriteTextureId < 0) {
            this.glSpriteTextureId = TextureUtil.glGenTextures();
            TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
            TextureUtils.applyAnisotropicLevel();
        }
        TextureUtils.bindTexture(this.glSpriteTextureId);
    }

    public void deleteSpriteTexture() {
        if (this.glSpriteTextureId >= 0) {
            TextureUtil.deleteTexture(this.glSpriteTextureId);
            this.glSpriteTextureId = -1;
        }
    }

    public float toSingleU(float p_toSingleU_1_) {
        p_toSingleU_1_ -= this.baseU;
        float f2 = (float)this.sheetWidth / (float)this.width;
        return p_toSingleU_1_ *= f2;
    }

    public float toSingleV(float p_toSingleV_1_) {
        p_toSingleV_1_ -= this.baseV;
        float f2 = (float)this.sheetHeight / (float)this.height;
        return p_toSingleV_1_ *= f2;
    }
}

