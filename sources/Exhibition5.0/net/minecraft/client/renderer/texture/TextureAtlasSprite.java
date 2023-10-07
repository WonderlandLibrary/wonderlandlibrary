// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.resources.data.AnimationFrame;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import java.util.List;

public class TextureAtlasSprite
{
    private final String iconName;
    protected List framesTextureData;
    protected int[][] field_176605_b;
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
    private static String field_176607_p;
    private static String field_176606_q;
    private static final String __OBFID = "CL_00001062";
    private int indexInMap;
    
    protected TextureAtlasSprite(final String p_i1282_1_) {
        this.framesTextureData = Lists.newArrayList();
        this.indexInMap = -1;
        this.iconName = p_i1282_1_;
    }
    
    protected static TextureAtlasSprite func_176604_a(final ResourceLocation p_176604_0_) {
        final String var1 = p_176604_0_.toString();
        return TextureAtlasSprite.field_176607_p.equals(var1) ? new TextureClock(var1) : (TextureAtlasSprite.field_176606_q.equals(var1) ? new TextureCompass(var1) : new TextureAtlasSprite(var1));
    }
    
    public static void func_176602_a(final String p_176602_0_) {
        TextureAtlasSprite.field_176607_p = p_176602_0_;
    }
    
    public static void func_176603_b(final String p_176603_0_) {
        TextureAtlasSprite.field_176606_q = p_176603_0_;
    }
    
    public void initSprite(final int p_110971_1_, final int p_110971_2_, final int p_110971_3_, final int p_110971_4_, final boolean p_110971_5_) {
        this.originX = p_110971_3_;
        this.originY = p_110971_4_;
        this.rotated = p_110971_5_;
        final float var6 = (float)(0.009999999776482582 / p_110971_1_);
        final float var7 = (float)(0.009999999776482582 / p_110971_2_);
        this.minU = p_110971_3_ / p_110971_1_ + var6;
        this.maxU = (p_110971_3_ + this.width) / p_110971_1_ - var6;
        this.minV = p_110971_4_ / p_110971_2_ + var7;
        this.maxV = (p_110971_4_ + this.height) / p_110971_2_ - var7;
    }
    
    public void copyFrom(final TextureAtlasSprite p_94217_1_) {
        this.originX = p_94217_1_.originX;
        this.originY = p_94217_1_.originY;
        this.width = p_94217_1_.width;
        this.height = p_94217_1_.height;
        this.rotated = p_94217_1_.rotated;
        this.minU = p_94217_1_.minU;
        this.maxU = p_94217_1_.maxU;
        this.minV = p_94217_1_.minV;
        this.maxV = p_94217_1_.maxV;
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
    
    public float getInterpolatedU(final double p_94214_1_) {
        final float var3 = this.maxU - this.minU;
        return this.minU + var3 * (float)p_94214_1_ / 16.0f;
    }
    
    public float getMinV() {
        return this.minV;
    }
    
    public float getMaxV() {
        return this.maxV;
    }
    
    public float getInterpolatedV(final double p_94207_1_) {
        final float var3 = this.maxV - this.minV;
        return this.minV + var3 * ((float)p_94207_1_ / 16.0f);
    }
    
    public String getIconName() {
        return this.iconName;
    }
    
    public void updateAnimation() {
        ++this.tickCounter;
        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            final int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
            final int var2 = (this.animationMetadata.getFrameCount() == 0) ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % var2;
            this.tickCounter = 0;
            final int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
            if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size()) {
                TextureUtil.uploadTextureMipmap(this.framesTextureData.get(var3), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
        else if (this.animationMetadata.func_177219_e()) {
            this.func_180599_n();
        }
    }
    
    private void func_180599_n() {
        final double var1 = 1.0 - this.tickCounter / this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        final int var2 = this.animationMetadata.getFrameIndex(this.frameCounter);
        final int var3 = (this.animationMetadata.getFrameCount() == 0) ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
        final int var4 = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % var3);
        if (var2 != var4 && var4 >= 0 && var4 < this.framesTextureData.size()) {
            final int[][] var5 = this.framesTextureData.get(var2);
            final int[][] var6 = this.framesTextureData.get(var4);
            if (this.field_176605_b == null || this.field_176605_b.length != var5.length) {
                this.field_176605_b = new int[var5.length][];
            }
            for (int var7 = 0; var7 < var5.length; ++var7) {
                if (this.field_176605_b[var7] == null) {
                    this.field_176605_b[var7] = new int[var5[var7].length];
                }
                if (var7 < var6.length && var6[var7].length == var5[var7].length) {
                    for (int var8 = 0; var8 < var5[var7].length; ++var8) {
                        final int var9 = var5[var7][var8];
                        final int var10 = var6[var7][var8];
                        final int var11 = (int)(((var9 & 0xFF0000) >> 16) * var1 + ((var10 & 0xFF0000) >> 16) * (1.0 - var1));
                        final int var12 = (int)(((var9 & 0xFF00) >> 8) * var1 + ((var10 & 0xFF00) >> 8) * (1.0 - var1));
                        final int var13 = (int)((var9 & 0xFF) * var1 + (var10 & 0xFF) * (1.0 - var1));
                        this.field_176605_b[var7][var8] = ((var9 & 0xFF000000) | var11 << 16 | var12 << 8 | var13);
                    }
                }
            }
            TextureUtil.uploadTextureMipmap(this.field_176605_b, this.width, this.height, this.originX, this.originY, false, false);
        }
    }
    
    public int[][] getFrameTextureData(final int p_147965_1_) {
        return this.framesTextureData.get(p_147965_1_);
    }
    
    public int getFrameCount() {
        return this.framesTextureData.size();
    }
    
    public void setIconWidth(final int p_110966_1_) {
        this.width = p_110966_1_;
    }
    
    public void setIconHeight(final int p_110969_1_) {
        this.height = p_110969_1_;
    }
    
    public void func_180598_a(final BufferedImage[] p_180598_1_, final AnimationMetadataSection p_180598_2_) {
        this.resetSprite();
        final int var3 = p_180598_1_[0].getWidth();
        final int var4 = p_180598_1_[0].getHeight();
        this.width = var3;
        this.height = var4;
        final int[][] var5 = new int[p_180598_1_.length][];
        for (int var6 = 0; var6 < p_180598_1_.length; ++var6) {
            final BufferedImage i = p_180598_1_[var6];
            if (i != null) {
                if (var6 > 0 && (i.getWidth() != var3 >> var6 || i.getHeight() != var4 >> var6)) {
                    throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", var6, i.getWidth(), i.getHeight(), var3 >> var6, var4 >> var6));
                }
                var5[var6] = new int[i.getWidth() * i.getHeight()];
                i.getRGB(0, 0, i.getWidth(), i.getHeight(), var5[var6], 0, i.getWidth());
            }
        }
        if (p_180598_2_ == null) {
            if (var4 != var3) {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }
            this.framesTextureData.add(var5);
        }
        else {
            final int var6 = var4 / var3;
            final int var7 = var3;
            final int datas = var3;
            this.height = this.width;
            if (p_180598_2_.getFrameCount() > 0) {
                for (final int di : p_180598_2_.getFrameIndexSet()) {
                    if (di >= var6) {
                        throw new RuntimeException("invalid frameindex " + di);
                    }
                    this.allocateFrameTextureData(di);
                    this.framesTextureData.set(di, getFrameTextureData(var5, var7, datas, di));
                }
                this.animationMetadata = p_180598_2_;
            }
            else {
                final ArrayList var8 = Lists.newArrayList();
                for (int di = 0; di < var6; ++di) {
                    this.framesTextureData.add(getFrameTextureData(var5, var7, datas, di));
                    var8.add(new AnimationFrame(di, -1));
                }
                this.animationMetadata = new AnimationMetadataSection(var8, this.width, this.height, p_180598_2_.getFrameTime(), p_180598_2_.func_177219_e());
            }
        }
        for (int var7 = 0; var7 < this.framesTextureData.size(); ++var7) {
            final int[][] var9 = this.framesTextureData.get(var7);
            if (var9 != null) {
                for (int di = 0; di < var9.length; ++di) {
                    final int[] var10 = var9[di];
                    this.fixTransparentColor(var10);
                }
            }
        }
    }
    
    public void generateMipmaps(final int p_147963_1_) {
        final ArrayList var2 = Lists.newArrayList();
        for (int var3 = 0; var3 < this.framesTextureData.size(); ++var3) {
            final int[][] var4 = this.framesTextureData.get(var3);
            if (var4 != null) {
                try {
                    var2.add(TextureUtil.generateMipmapData(p_147963_1_, this.width, var4));
                }
                catch (Throwable var6) {
                    final CrashReport var5 = CrashReport.makeCrashReport(var6, "Generating mipmaps for frame");
                    final CrashReportCategory var7 = var5.makeCategory("Frame being iterated");
                    var7.addCrashSection("Frame index", var3);
                    var7.addCrashSectionCallable("Frame sizes", new Callable() {
                        private static final String __OBFID = "CL_00001063";
                        
                        @Override
                        public String call() {
                            final StringBuilder var1 = new StringBuilder();
                            for (final int[] var4 : var4) {
                                if (var1.length() > 0) {
                                    var1.append(", ");
                                }
                                var1.append((var4 == null) ? "null" : var4.length);
                            }
                            return var1.toString();
                        }
                    });
                    throw new ReportedException(var5);
                }
            }
        }
        this.setFramesTextureData(var2);
    }
    
    private void allocateFrameTextureData(final int p_130099_1_) {
        if (this.framesTextureData.size() <= p_130099_1_) {
            for (int var2 = this.framesTextureData.size(); var2 <= p_130099_1_; ++var2) {
                this.framesTextureData.add(null);
            }
        }
    }
    
    private static int[][] getFrameTextureData(final int[][] p_147962_0_, final int p_147962_1_, final int p_147962_2_, final int p_147962_3_) {
        final int[][] var4 = new int[p_147962_0_.length][];
        for (int var5 = 0; var5 < p_147962_0_.length; ++var5) {
            final int[] var6 = p_147962_0_[var5];
            if (var6 != null) {
                var4[var5] = new int[(p_147962_1_ >> var5) * (p_147962_2_ >> var5)];
                System.arraycopy(var6, p_147962_3_ * var4[var5].length, var4[var5], 0, var4[var5].length);
            }
        }
        return var4;
    }
    
    public void clearFramesTextureData() {
        this.framesTextureData.clear();
    }
    
    public boolean hasAnimationMetadata() {
        return this.animationMetadata != null;
    }
    
    public void setFramesTextureData(final List p_110968_1_) {
        this.framesTextureData = p_110968_1_;
    }
    
    private void resetSprite() {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }
    
    @Override
    public String toString() {
        return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }
    
    public boolean hasCustomLoader(final IResourceManager manager, final ResourceLocation location) {
        return false;
    }
    
    public boolean load(final IResourceManager manager, final ResourceLocation location) {
        return true;
    }
    
    public int getIndexInMap() {
        return this.indexInMap;
    }
    
    public void setIndexInMap(final int indexInMap) {
        this.indexInMap = indexInMap;
    }
    
    private void fixTransparentColor(final int[] data) {
        if (data != null) {
            long redSum = 0L;
            long greenSum = 0L;
            long blueSum = 0L;
            long count = 0L;
            for (int redAvg = 0; redAvg < data.length; ++redAvg) {
                final int greenAvg = data[redAvg];
                final int blueAvg = greenAvg >> 24 & 0xFF;
                if (blueAvg != 0) {
                    final int i = greenAvg >> 16 & 0xFF;
                    final int col = greenAvg >> 8 & 0xFF;
                    final int alpha = greenAvg & 0xFF;
                    redSum += i;
                    greenSum += col;
                    blueSum += alpha;
                    ++count;
                }
            }
            if (count > 0L) {
                final int redAvg = (int)(redSum / count);
                final int greenAvg = (int)(greenSum / count);
                final int blueAvg = (int)(blueSum / count);
                for (int i = 0; i < data.length; ++i) {
                    final int col = data[i];
                    final int alpha = col >> 24 & 0xFF;
                    if (alpha == 0) {
                        data[i] = (redAvg << 16 | greenAvg << 8 | blueAvg);
                    }
                }
            }
        }
    }
    
    public double getSpriteU16(final float atlasU) {
        final float dU = this.maxU - this.minU;
        return (atlasU - this.minU) / dU * 16.0f;
    }
    
    public double getSpriteV16(final float atlasV) {
        final float dV = this.maxV - this.minV;
        return (atlasV - this.minV) / dV * 16.0f;
    }
    
    static {
        TextureAtlasSprite.field_176607_p = "builtin/clock";
        TextureAtlasSprite.field_176606_q = "builtin/compass";
    }
}
