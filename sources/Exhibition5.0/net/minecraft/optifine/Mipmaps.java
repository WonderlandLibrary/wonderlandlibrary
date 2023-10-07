// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import java.util.ArrayList;
import java.awt.Dimension;
import java.nio.IntBuffer;

public class Mipmaps
{
    private final String iconName;
    private final int width;
    private final int height;
    private final int[] data;
    private final boolean direct;
    private int[][] mipmapDatas;
    private IntBuffer[] mipmapBuffers;
    private Dimension[] mipmapDimensions;
    
    public Mipmaps(final String iconName, final int width, final int height, final int[] data, final boolean direct) {
        this.iconName = iconName;
        this.width = width;
        this.height = height;
        this.data = data;
        this.direct = direct;
        this.mipmapDimensions = makeMipmapDimensions(width, height, iconName);
        this.mipmapDatas = generateMipMapData(data, width, height, this.mipmapDimensions);
        if (direct) {
            this.mipmapBuffers = makeMipmapBuffers(this.mipmapDimensions, this.mipmapDatas);
        }
    }
    
    public static Dimension[] makeMipmapDimensions(final int width, final int height, final String iconName) {
        final int texWidth = TextureUtils.ceilPowerOfTwo(width);
        final int texHeight = TextureUtils.ceilPowerOfTwo(height);
        if (texWidth == width && texHeight == height) {
            final ArrayList listDims = new ArrayList();
            int mipWidth = texWidth;
            int mipHeight = texHeight;
            while (true) {
                mipWidth /= 2;
                mipHeight /= 2;
                if (mipWidth <= 0 && mipHeight <= 0) {
                    break;
                }
                if (mipWidth <= 0) {
                    mipWidth = 1;
                }
                if (mipHeight <= 0) {
                    mipHeight = 1;
                }
                final int mipmapDimensions = mipWidth * mipHeight * 4;
                final Dimension dim = new Dimension(mipWidth, mipHeight);
                listDims.add(dim);
            }
            final Dimension[] mipmapDimensions2 = listDims.toArray(new Dimension[listDims.size()]);
            return mipmapDimensions2;
        }
        Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: " + iconName + ", dim: " + width + "x" + height);
        return new Dimension[0];
    }
    
    public static int[][] generateMipMapData(final int[] data, final int width, final int height, final Dimension[] mipmapDimensions) {
        int[] parMipData = data;
        int parWidth = width;
        boolean scale = true;
        final int[][] mipmapDatas = new int[mipmapDimensions.length][];
        for (int i = 0; i < mipmapDimensions.length; ++i) {
            final Dimension dim = mipmapDimensions[i];
            final int mipWidth = dim.width;
            final int mipHeight = dim.height;
            final int[] mipData = new int[mipWidth * mipHeight];
            mipmapDatas[i] = mipData;
            final int level = i + 1;
            if (scale) {
                for (int mipX = 0; mipX < mipWidth; ++mipX) {
                    for (int mipY = 0; mipY < mipHeight; ++mipY) {
                        final int p1 = parMipData[mipX * 2 + 0 + (mipY * 2 + 0) * parWidth];
                        final int p2 = parMipData[mipX * 2 + 1 + (mipY * 2 + 0) * parWidth];
                        final int p3 = parMipData[mipX * 2 + 1 + (mipY * 2 + 1) * parWidth];
                        final int p4 = parMipData[mipX * 2 + 0 + (mipY * 2 + 1) * parWidth];
                        final int pixel = alphaBlend(p1, p2, p3, p4);
                        mipData[mipX + mipY * mipWidth] = pixel;
                    }
                }
            }
            parMipData = mipData;
            parWidth = mipWidth;
            if (mipWidth <= 1 || mipHeight <= 1) {
                scale = false;
            }
        }
        return mipmapDatas;
    }
    
    public static int alphaBlend(final int c1, final int c2, final int c3, final int c4) {
        final int cx1 = alphaBlend(c1, c2);
        final int cx2 = alphaBlend(c3, c4);
        final int cx3 = alphaBlend(cx1, cx2);
        return cx3;
    }
    
    private static int alphaBlend(int c1, int c2) {
        int a1 = (c1 & 0xFF000000) >> 24 & 0xFF;
        int a2 = (c2 & 0xFF000000) >> 24 & 0xFF;
        int ax = (a1 + a2) / 2;
        if (a1 == 0 && a2 == 0) {
            a1 = 1;
            a2 = 1;
        }
        else {
            if (a1 == 0) {
                c1 = c2;
                ax /= 2;
            }
            if (a2 == 0) {
                c2 = c1;
                ax /= 2;
            }
        }
        final int r1 = (c1 >> 16 & 0xFF) * a1;
        final int g1 = (c1 >> 8 & 0xFF) * a1;
        final int b1 = (c1 & 0xFF) * a1;
        final int r2 = (c2 >> 16 & 0xFF) * a2;
        final int g2 = (c2 >> 8 & 0xFF) * a2;
        final int b2 = (c2 & 0xFF) * a2;
        final int rx = (r1 + r2) / (a1 + a2);
        final int gx = (g1 + g2) / (a1 + a2);
        final int bx = (b1 + b2) / (a1 + a2);
        return ax << 24 | rx << 16 | gx << 8 | bx;
    }
    
    private int averageColor(final int i, final int j) {
        final int k = (i & 0xFF000000) >> 24 & 0xFF;
        final int l = (j & 0xFF000000) >> 24 & 0xFF;
        return (k + l >> 1 << 24) + ((i & 0xFEFEFE) + (j & 0xFEFEFE) >> 1);
    }
    
    public static IntBuffer[] makeMipmapBuffers(final Dimension[] mipmapDimensions, final int[][] mipmapDatas) {
        if (mipmapDimensions == null) {
            return null;
        }
        final IntBuffer[] mipmapBuffers = new IntBuffer[mipmapDimensions.length];
        for (int i = 0; i < mipmapDimensions.length; ++i) {
            final Dimension dim = mipmapDimensions[i];
            final int bufLen = dim.width * dim.height;
            final IntBuffer buf = GLAllocation.createDirectIntBuffer(bufLen);
            final int[] data = mipmapDatas[i];
            buf.clear();
            buf.put(data);
            buf.clear();
            mipmapBuffers[i] = buf;
        }
        return mipmapBuffers;
    }
    
    public static void allocateMipmapTextures(final int width, final int height, final String name) {
        final Dimension[] dims = makeMipmapDimensions(width, height, name);
        for (int i = 0; i < dims.length; ++i) {
            final Dimension dim = dims[i];
            final int mipWidth = dim.width;
            final int mipHeight = dim.height;
            final int level = i + 1;
            GL11.glTexImage2D(3553, level, 6408, mipWidth, mipHeight, 0, 32993, 33639, (IntBuffer)null);
        }
    }
}
