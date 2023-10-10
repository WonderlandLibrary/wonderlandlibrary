/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.awt.Dimension;
import java.nio.IntBuffer;
import java.util.ArrayList;
import net.minecraft.client.renderer.GLAllocation;
import optfine.Config;
import optfine.TextureUtils;
import org.lwjgl.opengl.GL11;

public class Mipmaps {
    private final String iconName;
    private final int width;
    private final int height;
    private final int[] data;
    private final boolean direct;
    private int[][] mipmapDatas;
    private IntBuffer[] mipmapBuffers;
    private Dimension[] mipmapDimensions;

    public Mipmaps(String p_i42_1_, int p_i42_2_, int p_i42_3_, int[] p_i42_4_, boolean p_i42_5_) {
        this.iconName = p_i42_1_;
        this.width = p_i42_2_;
        this.height = p_i42_3_;
        this.data = p_i42_4_;
        this.direct = p_i42_5_;
        this.mipmapDimensions = Mipmaps.makeMipmapDimensions(p_i42_2_, p_i42_3_, p_i42_1_);
        this.mipmapDatas = Mipmaps.generateMipMapData(p_i42_4_, p_i42_2_, p_i42_3_, this.mipmapDimensions);
        if (p_i42_5_) {
            this.mipmapBuffers = Mipmaps.makeMipmapBuffers(this.mipmapDimensions, this.mipmapDatas);
        }
    }

    public static Dimension[] makeMipmapDimensions(int p_makeMipmapDimensions_0_, int p_makeMipmapDimensions_1_, String p_makeMipmapDimensions_2_) {
        int i2 = TextureUtils.ceilPowerOfTwo(p_makeMipmapDimensions_0_);
        int j2 = TextureUtils.ceilPowerOfTwo(p_makeMipmapDimensions_1_);
        if (i2 == p_makeMipmapDimensions_0_ && j2 == p_makeMipmapDimensions_1_) {
            ArrayList<Dimension> list = new ArrayList<Dimension>();
            int k2 = i2;
            int l2 = j2;
            while (true) {
                if ((k2 /= 2) <= 0 && (l2 /= 2) <= 0) {
                    Dimension[] adimension = list.toArray(new Dimension[list.size()]);
                    return adimension;
                }
                if (k2 <= 0) {
                    k2 = 1;
                }
                if (l2 <= 0) {
                    l2 = 1;
                }
                int i1 = k2 * l2 * 4;
                Dimension dimension = new Dimension(k2, l2);
                list.add(dimension);
            }
        }
        Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: " + p_makeMipmapDimensions_2_ + ", dim: " + p_makeMipmapDimensions_0_ + "x" + p_makeMipmapDimensions_1_);
        return new Dimension[0];
    }

    public static int[][] generateMipMapData(int[] p_generateMipMapData_0_, int p_generateMipMapData_1_, int p_generateMipMapData_2_, Dimension[] p_generateMipMapData_3_) {
        int[] aint = p_generateMipMapData_0_;
        int i2 = p_generateMipMapData_1_;
        boolean flag = true;
        int[][] aint1 = new int[p_generateMipMapData_3_.length][];
        for (int j2 = 0; j2 < p_generateMipMapData_3_.length; ++j2) {
            Dimension dimension = p_generateMipMapData_3_[j2];
            int k2 = dimension.width;
            int l2 = dimension.height;
            int[] aint2 = new int[k2 * l2];
            aint1[j2] = aint2;
            int i1 = j2 + 1;
            if (flag) {
                for (int j1 = 0; j1 < k2; ++j1) {
                    for (int k1 = 0; k1 < l2; ++k1) {
                        int l22;
                        int l1 = aint[j1 * 2 + 0 + (k1 * 2 + 0) * i2];
                        int i22 = aint[j1 * 2 + 1 + (k1 * 2 + 0) * i2];
                        int j22 = aint[j1 * 2 + 1 + (k1 * 2 + 1) * i2];
                        int k22 = aint[j1 * 2 + 0 + (k1 * 2 + 1) * i2];
                        aint2[j1 + k1 * k2] = l22 = Mipmaps.alphaBlend(l1, i22, j22, k22);
                    }
                }
            }
            aint = aint2;
            i2 = k2;
            if (k2 > 1 && l2 > 1) continue;
            flag = false;
        }
        return aint1;
    }

    public static int alphaBlend(int p_alphaBlend_0_, int p_alphaBlend_1_, int p_alphaBlend_2_, int p_alphaBlend_3_) {
        int i2 = Mipmaps.alphaBlend(p_alphaBlend_0_, p_alphaBlend_1_);
        int j2 = Mipmaps.alphaBlend(p_alphaBlend_2_, p_alphaBlend_3_);
        int k2 = Mipmaps.alphaBlend(i2, j2);
        return k2;
    }

    private static int alphaBlend(int p_alphaBlend_0_, int p_alphaBlend_1_) {
        int i2 = (p_alphaBlend_0_ & 0xFF000000) >> 24 & 0xFF;
        int j2 = (p_alphaBlend_1_ & 0xFF000000) >> 24 & 0xFF;
        int k2 = (i2 + j2) / 2;
        if (i2 == 0 && j2 == 0) {
            i2 = 1;
            j2 = 1;
        } else {
            if (i2 == 0) {
                p_alphaBlend_0_ = p_alphaBlend_1_;
                k2 /= 2;
            }
            if (j2 == 0) {
                p_alphaBlend_1_ = p_alphaBlend_0_;
                k2 /= 2;
            }
        }
        int l2 = (p_alphaBlend_0_ >> 16 & 0xFF) * i2;
        int i1 = (p_alphaBlend_0_ >> 8 & 0xFF) * i2;
        int j1 = (p_alphaBlend_0_ & 0xFF) * i2;
        int k1 = (p_alphaBlend_1_ >> 16 & 0xFF) * j2;
        int l1 = (p_alphaBlend_1_ >> 8 & 0xFF) * j2;
        int i22 = (p_alphaBlend_1_ & 0xFF) * j2;
        int j22 = (l2 + k1) / (i2 + j2);
        int k22 = (i1 + l1) / (i2 + j2);
        int l22 = (j1 + i22) / (i2 + j2);
        return k2 << 24 | j22 << 16 | k22 << 8 | l22;
    }

    private int averageColor(int p_averageColor_1_, int p_averageColor_2_) {
        int i2 = (p_averageColor_1_ & 0xFF000000) >> 24 & 0xFF;
        int j2 = (p_averageColor_2_ & 0xFF000000) >> 24 & 0xFF;
        return (i2 + j2 >> 1 << 24) + ((p_averageColor_1_ & 0xFEFEFE) + (p_averageColor_2_ & 0xFEFEFE) >> 1);
    }

    public static IntBuffer[] makeMipmapBuffers(Dimension[] p_makeMipmapBuffers_0_, int[][] p_makeMipmapBuffers_1_) {
        if (p_makeMipmapBuffers_0_ == null) {
            return null;
        }
        IntBuffer[] aintbuffer = new IntBuffer[p_makeMipmapBuffers_0_.length];
        for (int i2 = 0; i2 < p_makeMipmapBuffers_0_.length; ++i2) {
            Dimension dimension = p_makeMipmapBuffers_0_[i2];
            int j2 = dimension.width * dimension.height;
            IntBuffer intbuffer = GLAllocation.createDirectIntBuffer(j2);
            int[] aint = p_makeMipmapBuffers_1_[i2];
            intbuffer.clear();
            intbuffer.put(aint);
            intbuffer.clear();
            aintbuffer[i2] = intbuffer;
        }
        return aintbuffer;
    }

    public static void allocateMipmapTextures(int p_allocateMipmapTextures_0_, int p_allocateMipmapTextures_1_, String p_allocateMipmapTextures_2_) {
        Dimension[] adimension = Mipmaps.makeMipmapDimensions(p_allocateMipmapTextures_0_, p_allocateMipmapTextures_1_, p_allocateMipmapTextures_2_);
        for (int i2 = 0; i2 < adimension.length; ++i2) {
            Dimension dimension = adimension[i2];
            int j2 = dimension.width;
            int k2 = dimension.height;
            int l2 = i2 + 1;
            GL11.glTexImage2D(3553, l2, 6408, j2, k2, 0, 32993, 33639, (IntBuffer)null);
        }
    }
}

