/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.TextureClock;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasSprite {
    private final String iconName;
    protected List<int[][]> framesTextureData = Lists.newArrayList();
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

    protected TextureAtlasSprite(String spriteName) {
        this.iconName = spriteName;
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
        TextureAtlasSprite textureAtlasSprite;
        String s = spriteResourceLocation.toString();
        if (locationNameClock.equals(s)) {
            textureAtlasSprite = new TextureClock(s);
            return textureAtlasSprite;
        }
        if (locationNameCompass.equals(s)) {
            textureAtlasSprite = new TextureCompass(s);
            return textureAtlasSprite;
        }
        textureAtlasSprite = new TextureAtlasSprite(s);
        return textureAtlasSprite;
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
        float f = (float)((double)0.01f / (double)inX);
        float f1 = (float)((double)0.01f / (double)inY);
        this.minU = (float)originInX / (float)((double)inX) + f;
        this.maxU = (float)(originInX + this.width) / (float)((double)inX) - f;
        this.minV = (float)originInY / (float)inY + f1;
        this.maxV = (float)(originInY + this.height) / (float)inY - f1;
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

    public float getInterpolatedU(double u) {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)u / 16.0f;
    }

    public float getMinV() {
        return this.minV;
    }

    public float getMaxV() {
        return this.maxV;
    }

    public float getInterpolatedV(double v) {
        float f = this.maxV - this.minV;
        return this.minV + f * ((float)v / 16.0f);
    }

    public String getIconName() {
        return this.iconName;
    }

    public void updateAnimation() {
        ++this.tickCounter;
        if (this.tickCounter < this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            if (!this.animationMetadata.isInterpolate()) return;
            this.updateAnimationInterpolated();
            return;
        }
        int i = this.animationMetadata.getFrameIndex(this.frameCounter);
        int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
        this.frameCounter = (this.frameCounter + 1) % j;
        this.tickCounter = 0;
        int k = this.animationMetadata.getFrameIndex(this.frameCounter);
        if (i == k) return;
        if (k < 0) return;
        if (k >= this.framesTextureData.size()) return;
        TextureUtil.uploadTextureMipmap(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, false, false);
    }

    private void updateAnimationInterpolated() {
        double d0 = 1.0 - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int i = this.animationMetadata.getFrameIndex(this.frameCounter);
        int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
        int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
        if (i == k) return;
        if (k < 0) return;
        if (k >= this.framesTextureData.size()) return;
        int[][] aint = this.framesTextureData.get(i);
        int[][] aint1 = this.framesTextureData.get(k);
        if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length) {
            this.interpolatedFrameData = new int[aint.length][];
        }
        int l = 0;
        while (true) {
            if (l >= aint.length) {
                TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
                return;
            }
            if (this.interpolatedFrameData[l] == null) {
                this.interpolatedFrameData[l] = new int[aint[l].length];
            }
            if (l < aint1.length && aint1[l].length == aint[l].length) {
                for (int i1 = 0; i1 < aint[l].length; ++i1) {
                    int j1 = aint[l][i1];
                    int k1 = aint1[l][i1];
                    int l1 = (int)((double)((j1 & 0xFF0000) >> 16) * d0 + (double)((k1 & 0xFF0000) >> 16) * (1.0 - d0));
                    int i2 = (int)((double)((j1 & 0xFF00) >> 8) * d0 + (double)((k1 & 0xFF00) >> 8) * (1.0 - d0));
                    int j2 = (int)((double)(j1 & 0xFF) * d0 + (double)(k1 & 0xFF) * (1.0 - d0));
                    this.interpolatedFrameData[l][i1] = j1 & 0xFF000000 | l1 << 16 | i2 << 8 | j2;
                }
            }
            ++l;
        }
    }

    public int[][] getFrameTextureData(int index) {
        return this.framesTextureData.get(index);
    }

    public int getFrameCount() {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int newWidth) {
        this.width = newWidth;
    }

    public void setIconHeight(int newHeight) {
        this.height = newHeight;
    }

    public void loadSprite(BufferedImage[] images, AnimationMetadataSection meta) throws IOException {
        this.resetSprite();
        int i = images[0].getWidth();
        int j = images[0].getHeight();
        this.width = i;
        this.height = j;
        int[][] aint = new int[images.length][];
        for (int k = 0; k < images.length; ++k) {
            BufferedImage bufferedimage = images[k];
            if (bufferedimage == null) continue;
            if (k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k)) {
                throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", k, bufferedimage.getWidth(), bufferedimage.getHeight(), i >> k, j >> k));
            }
            aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
        }
        if (meta == null) {
            if (j != i) {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }
            this.framesTextureData.add(aint);
            return;
        }
        int j1 = j / i;
        int k1 = i;
        int l = i;
        this.height = this.width;
        if (meta.getFrameCount() <= 0) {
            ArrayList<AnimationFrame> list = Lists.newArrayList();
            int l1 = 0;
            while (true) {
                if (l1 >= j1) {
                    this.animationMetadata = new AnimationMetadataSection(list, this.width, this.height, meta.getFrameTime(), meta.isInterpolate());
                    return;
                }
                this.framesTextureData.add(TextureAtlasSprite.getFrameTextureData(aint, k1, l, l1));
                list.add(new AnimationFrame(l1, -1));
                ++l1;
            }
        }
        Iterator<Integer> iterator = meta.getFrameIndexSet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.animationMetadata = meta;
                return;
            }
            int i1 = iterator.next();
            if (i1 >= j1) {
                throw new RuntimeException("invalid frameindex " + i1);
            }
            this.allocateFrameTextureData(i1);
            this.framesTextureData.set(i1, TextureAtlasSprite.getFrameTextureData(aint, k1, l, i1));
        }
    }

    public void generateMipmaps(int level) {
        ArrayList<int[][]> list = Lists.newArrayList();
        int i = 0;
        while (true) {
            if (i >= this.framesTextureData.size()) {
                this.setFramesTextureData(list);
                return;
            }
            final int[][] aint = this.framesTextureData.get(i);
            if (aint != null) {
                try {
                    list.add(TextureUtil.generateMipmapData(level, this.width, aint));
                }
                catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
                    crashreportcategory.addCrashSection("Frame index", i);
                    crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            StringBuilder stringbuilder = new StringBuilder();
                            int[][] nArray = aint;
                            int n = nArray.length;
                            int n2 = 0;
                            while (n2 < n) {
                                int[] aint1 = nArray[n2];
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append(", ");
                                }
                                stringbuilder.append(aint1 == null ? "null" : Integer.valueOf(aint1.length));
                                ++n2;
                            }
                            return stringbuilder.toString();
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
            ++i;
        }
    }

    private void allocateFrameTextureData(int index) {
        if (this.framesTextureData.size() > index) return;
        int i = this.framesTextureData.size();
        while (i <= index) {
            this.framesTextureData.add(null);
            ++i;
        }
    }

    private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_) {
        int[][] aint = new int[data.length][];
        int i = 0;
        while (i < data.length) {
            int[] aint1 = data[i];
            if (aint1 != null) {
                aint[i] = new int[(rows >> i) * (columns >> i)];
                System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
            }
            ++i;
        }
        return aint;
    }

    public void clearFramesTextureData() {
        this.framesTextureData.clear();
    }

    public boolean hasAnimationMetadata() {
        if (this.animationMetadata == null) return false;
        return true;
    }

    public void setFramesTextureData(List<int[][]> newFramesTextureData) {
        this.framesTextureData = newFramesTextureData;
    }

    private void resetSprite() {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.<int[][]>newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    public String toString() {
        return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }
}

