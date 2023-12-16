// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import java.util.List;
import net.minecraft.src.Config;
import java.util.ArrayList;
import net.optifine.util.TextureUtils;
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
        final int i = TextureUtils.ceilPowerOfTwo(width);
        final int j = TextureUtils.ceilPowerOfTwo(height);
        if (i == width && j == height) {
            final List list = new ArrayList();
            int k = i;
            int l = j;
            while (true) {
                k /= 2;
                l /= 2;
                if (k <= 0 && l <= 0) {
                    break;
                }
                if (k <= 0) {
                    k = 1;
                }
                if (l <= 0) {
                    l = 1;
                }
                final int i2 = k * l * 4;
                final Dimension dimension = new Dimension(k, l);
                list.add(dimension);
            }
            final Dimension[] adimension = list.toArray(new Dimension[list.size()]);
            return adimension;
        }
        Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: " + iconName + ", dim: " + width + "x" + height);
        return new Dimension[0];
    }
    
    public static int[][] generateMipMapData(final int[] data, final int width, final int height, final Dimension[] mipmapDimensions) {
        int[] aint = data;
        int i = width;
        boolean flag = true;
        final int[][] aint2 = new int[mipmapDimensions.length][];
        for (int j = 0; j < mipmapDimensions.length; ++j) {
            final Dimension dimension = mipmapDimensions[j];
            final int k = dimension.width;
            final int l = dimension.height;
            final int[] aint3 = new int[k * l];
            aint2[j] = aint3;
            final int i2 = j + 1;
            if (flag) {
                for (int j2 = 0; j2 < k; ++j2) {
                    for (int k2 = 0; k2 < l; ++k2) {
                        final int l2 = aint[j2 * 2 + 0 + (k2 * 2 + 0) * i];
                        final int i3 = aint[j2 * 2 + 1 + (k2 * 2 + 0) * i];
                        final int j3 = aint[j2 * 2 + 1 + (k2 * 2 + 1) * i];
                        final int k3 = aint[j2 * 2 + 0 + (k2 * 2 + 1) * i];
                        final int l3 = alphaBlend(l2, i3, j3, k3);
                        aint3[j2 + k2 * k] = l3;
                    }
                }
            }
            aint = aint3;
            i = k;
            if (k <= 1 || l <= 1) {
                flag = false;
            }
        }
        return aint2;
    }
    
    public static int alphaBlend(final int c1, final int c2, final int c3, final int c4) {
        final int i = alphaBlend(c1, c2);
        final int j = alphaBlend(c3, c4);
        final int k = alphaBlend(i, j);
        return k;
    }
    
    private static int alphaBlend(int c1, int c2) {
        int i = (c1 & 0xFF000000) >> 24 & 0xFF;
        int j = (c2 & 0xFF000000) >> 24 & 0xFF;
        int k = (i + j) / 2;
        if (i == 0 && j == 0) {
            i = 1;
            j = 1;
        }
        else {
            if (i == 0) {
                c1 = c2;
                k /= 2;
            }
            if (j == 0) {
                c2 = c1;
                k /= 2;
            }
        }
        final int l = (c1 >> 16 & 0xFF) * i;
        final int i2 = (c1 >> 8 & 0xFF) * i;
        final int j2 = (c1 & 0xFF) * i;
        final int k2 = (c2 >> 16 & 0xFF) * j;
        final int l2 = (c2 >> 8 & 0xFF) * j;
        final int i3 = (c2 & 0xFF) * j;
        final int j3 = (l + k2) / (i + j);
        final int k3 = (i2 + l2) / (i + j);
        final int l3 = (j2 + i3) / (i + j);
        return k << 24 | j3 << 16 | k3 << 8 | l3;
    }
    
    private int averageColor(final int i, final int j) {
        final int k = (i & 0xFF000000) >> 24 & 0xFF;
        final int p = (j & 0xFF000000) >> 24 & 0xFF;
        return (k + j >> 1 << 24) + ((k & 0xFEFEFE) + (p & 0xFEFEFE) >> 1);
    }
    
    public static IntBuffer[] makeMipmapBuffers(final Dimension[] mipmapDimensions, final int[][] mipmapDatas) {
        if (mipmapDimensions == null) {
            return null;
        }
        final IntBuffer[] aintbuffer = new IntBuffer[mipmapDimensions.length];
        for (int i = 0; i < mipmapDimensions.length; ++i) {
            final Dimension dimension = mipmapDimensions[i];
            final int j = dimension.width * dimension.height;
            final IntBuffer intbuffer = GLAllocation.createDirectIntBuffer(j);
            final int[] aint = mipmapDatas[i];
            intbuffer.clear();
            intbuffer.put(aint);
            intbuffer.clear();
            aintbuffer[i] = intbuffer;
        }
        return aintbuffer;
    }
    
    public static void allocateMipmapTextures(final int width, final int height, final String name) {
        final Dimension[] adimension = makeMipmapDimensions(width, height, name);
        for (int i = 0; i < adimension.length; ++i) {
            final Dimension dimension = adimension[i];
            final int j = dimension.width;
            final int k = dimension.height;
            final int l = i + 1;
            GL11.glTexImage2D(3553, l, 6408, j, k, 0, 32993, 33639, (IntBuffer)null);
        }
    }
}
