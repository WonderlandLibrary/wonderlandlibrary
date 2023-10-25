/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasSprite {
    private final String iconName;
    private static String locationNameCompass;
    private float minU;
    protected int[][] interpolatedFrameData;
    private static String locationNameClock;
    protected int width;
    private float maxU;
    protected boolean rotated;
    protected int originX;
    private AnimationMetadataSection animationMetadata;
    protected int originY;
    protected int frameCounter;
    private float maxV;
    protected int tickCounter;
    private float minV;
    protected List<int[][]> framesTextureData = Lists.newArrayList();
    protected int height;

    public void copyFrom(TextureAtlasSprite textureAtlasSprite) {
        this.originX = textureAtlasSprite.originX;
        this.originY = textureAtlasSprite.originY;
        this.width = textureAtlasSprite.width;
        this.height = textureAtlasSprite.height;
        this.rotated = textureAtlasSprite.rotated;
        this.minU = textureAtlasSprite.minU;
        this.maxU = textureAtlasSprite.maxU;
        this.minV = textureAtlasSprite.minV;
        this.maxV = textureAtlasSprite.maxV;
    }

    public String toString() {
        return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    public int getIconHeight() {
        return this.height;
    }

    protected TextureAtlasSprite(String string) {
        this.iconName = string;
    }

    public float getInterpolatedU(double d) {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)d / 16.0f;
    }

    public int getFrameCount() {
        return this.framesTextureData.size();
    }

    public void initSprite(int n, int n2, int n3, int n4, boolean bl) {
        this.originX = n3;
        this.originY = n4;
        this.rotated = bl;
        float f = (float)((double)0.01f / (double)n);
        float f2 = (float)((double)0.01f / (double)n2);
        this.minU = (float)n3 / (float)((double)n) + f;
        this.maxU = (float)(n3 + this.width) / (float)((double)n) - f;
        this.minV = (float)n4 / (float)n2 + f2;
        this.maxV = (float)(n4 + this.height) / (float)n2 - f2;
    }

    public int getOriginY() {
        return this.originY;
    }

    public void setFramesTextureData(List<int[][]> list) {
        this.framesTextureData = list;
    }

    public int[][] getFrameTextureData(int n) {
        return this.framesTextureData.get(n);
    }

    private void allocateFrameTextureData(int n) {
        if (this.framesTextureData.size() <= n) {
            int n2 = this.framesTextureData.size();
            while (n2 <= n) {
                this.framesTextureData.add(null);
                ++n2;
            }
        }
    }

    private void resetSprite() {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    private void updateAnimationInterpolated() {
        int n;
        int n2;
        double d = 1.0 - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int n3 = this.animationMetadata.getFrameIndex(this.frameCounter);
        if (n3 != (n2 = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % (n = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount()))) && n2 >= 0 && n2 < this.framesTextureData.size()) {
            int[][] nArray = this.framesTextureData.get(n3);
            int[][] nArray2 = this.framesTextureData.get(n2);
            if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != nArray.length) {
                this.interpolatedFrameData = new int[nArray.length][];
            }
            int n4 = 0;
            while (n4 < nArray.length) {
                if (this.interpolatedFrameData[n4] == null) {
                    this.interpolatedFrameData[n4] = new int[nArray[n4].length];
                }
                if (n4 < nArray2.length && nArray2[n4].length == nArray[n4].length) {
                    int n5 = 0;
                    while (n5 < nArray[n4].length) {
                        int n6 = nArray[n4][n5];
                        int n7 = nArray2[n4][n5];
                        int n8 = (int)((double)((n6 & 0xFF0000) >> 16) * d + (double)((n7 & 0xFF0000) >> 16) * (1.0 - d));
                        int n9 = (int)((double)((n6 & 0xFF00) >> 8) * d + (double)((n7 & 0xFF00) >> 8) * (1.0 - d));
                        int n10 = (int)((double)(n6 & 0xFF) * d + (double)(n7 & 0xFF) * (1.0 - d));
                        this.interpolatedFrameData[n4][n5] = n6 & 0xFF000000 | n8 << 16 | n9 << 8 | n10;
                        ++n5;
                    }
                }
                ++n4;
            }
            TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
        }
    }

    public void loadSprite(BufferedImage[] bufferedImageArray, AnimationMetadataSection animationMetadataSection) throws IOException {
        this.resetSprite();
        int n = bufferedImageArray[0].getWidth();
        int n2 = bufferedImageArray[0].getHeight();
        this.width = n;
        this.height = n2;
        int[][] nArrayArray = new int[bufferedImageArray.length][];
        int n3 = 0;
        while (n3 < bufferedImageArray.length) {
            BufferedImage bufferedImage = bufferedImageArray[n3];
            if (bufferedImage != null) {
                if (n3 > 0 && (bufferedImage.getWidth() != n >> n3 || bufferedImage.getHeight() != n2 >> n3)) {
                    throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", n3, bufferedImage.getWidth(), bufferedImage.getHeight(), n >> n3, n2 >> n3));
                }
                nArrayArray[n3] = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
                bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), nArrayArray[n3], 0, bufferedImage.getWidth());
            }
            ++n3;
        }
        if (animationMetadataSection == null) {
            if (n2 != n) {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }
            this.framesTextureData.add(nArrayArray);
        } else {
            n3 = n2 / n;
            int n4 = n;
            int n5 = n;
            this.height = this.width;
            if (animationMetadataSection.getFrameCount() > 0) {
                for (int n6 : animationMetadataSection.getFrameIndexSet()) {
                    if (n6 >= n3) {
                        throw new RuntimeException("invalid frameindex " + n6);
                    }
                    this.allocateFrameTextureData(n6);
                    this.framesTextureData.set(n6, TextureAtlasSprite.getFrameTextureData(nArrayArray, n4, n5, n6));
                }
                this.animationMetadata = animationMetadataSection;
            } else {
                ArrayList arrayList = Lists.newArrayList();
                int n7 = 0;
                while (n7 < n3) {
                    this.framesTextureData.add(TextureAtlasSprite.getFrameTextureData(nArrayArray, n4, n5, n7));
                    arrayList.add(new AnimationFrame(n7, -1));
                    ++n7;
                }
                this.animationMetadata = new AnimationMetadataSection(arrayList, this.width, this.height, animationMetadataSection.getFrameTime(), animationMetadataSection.isInterpolate());
            }
        }
    }

    static {
        locationNameClock = "builtin/clock";
        locationNameCompass = "builtin/compass";
    }

    public static void setLocationNameClock(String string) {
        locationNameClock = string;
    }

    public void updateAnimation() {
        ++this.tickCounter;
        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            int n = this.animationMetadata.getFrameIndex(this.frameCounter);
            int n2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % n2;
            this.tickCounter = 0;
            int n3 = this.animationMetadata.getFrameIndex(this.frameCounter);
            if (n != n3 && n3 >= 0 && n3 < this.framesTextureData.size()) {
                TextureUtil.uploadTextureMipmap(this.framesTextureData.get(n3), this.width, this.height, this.originX, this.originY, false, false);
            }
        } else if (this.animationMetadata.isInterpolate()) {
            this.updateAnimationInterpolated();
        }
    }

    public void generateMipmaps(int n) {
        ArrayList arrayList = Lists.newArrayList();
        int n2 = 0;
        while (n2 < this.framesTextureData.size()) {
            final int[][] nArray = this.framesTextureData.get(n2);
            if (nArray != null) {
                try {
                    arrayList.add(TextureUtil.generateMipmapData(n, this.width, nArray));
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
                    CrashReportCategory crashReportCategory = crashReport.makeCategory("Frame being iterated");
                    crashReportCategory.addCrashSection("Frame index", n2);
                    crashReportCategory.addCrashSectionCallable("Frame sizes", new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            StringBuilder stringBuilder = new StringBuilder();
                            int[][] nArray3 = nArray;
                            int n = nArray.length;
                            int n2 = 0;
                            while (n2 < n) {
                                int[] nArray2 = nArray3[n2];
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(nArray2 == null ? "null" : Integer.valueOf(nArray2.length));
                                ++n2;
                            }
                            return stringBuilder.toString();
                        }
                    });
                    throw new ReportedException(crashReport);
                }
            }
            ++n2;
        }
        this.setFramesTextureData(arrayList);
    }

    public float getMaxV() {
        return this.maxV;
    }

    public float getMinV() {
        return this.minV;
    }

    public float getMaxU() {
        return this.maxU;
    }

    public void setIconWidth(int n) {
        this.width = n;
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation resourceLocation) {
        String string = resourceLocation.toString();
        return locationNameClock.equals(string) ? new TextureClock(string) : (locationNameCompass.equals(string) ? new TextureCompass(string) : new TextureAtlasSprite(string));
    }

    public float getMinU() {
        return this.minU;
    }

    private static int[][] getFrameTextureData(int[][] nArray, int n, int n2, int n3) {
        int[][] nArrayArray = new int[nArray.length][];
        int n4 = 0;
        while (n4 < nArray.length) {
            int[] nArray2 = nArray[n4];
            if (nArray2 != null) {
                nArrayArray[n4] = new int[(n >> n4) * (n2 >> n4)];
                System.arraycopy(nArray2, n3 * nArrayArray[n4].length, nArrayArray[n4], 0, nArrayArray[n4].length);
            }
            ++n4;
        }
        return nArrayArray;
    }

    public String getIconName() {
        return this.iconName;
    }

    public boolean hasAnimationMetadata() {
        return this.animationMetadata != null;
    }

    public static void setLocationNameCompass(String string) {
        locationNameCompass = string;
    }

    public void setIconHeight(int n) {
        this.height = n;
    }

    public void clearFramesTextureData() {
        this.framesTextureData.clear();
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getIconWidth() {
        return this.width;
    }

    public float getInterpolatedV(double d) {
        float f = this.maxV - this.minV;
        return this.minV + f * ((float)d / 16.0f);
    }
}

