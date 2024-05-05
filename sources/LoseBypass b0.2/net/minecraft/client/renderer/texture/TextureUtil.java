/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class TextureUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(0x400000);
    public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
    public static final int[] missingTextureData = missingTexture.getTextureData();
    private static final int[] mipmapBuffer;

    public static int glGenTextures() {
        return GlStateManager.generateTexture();
    }

    public static void deleteTexture(int textureId) {
        GlStateManager.deleteTexture(textureId);
    }

    public static int uploadTextureImage(int p_110987_0_, BufferedImage p_110987_1_) {
        return TextureUtil.uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
    }

    public static void uploadTexture(int textureId, int[] p_110988_1_, int p_110988_2_, int p_110988_3_) {
        TextureUtil.bindTexture(textureId);
        TextureUtil.uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
    }

    public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_) {
        int[][] aint = new int[p_147949_0_ + 1][];
        aint[0] = p_147949_2_[0];
        if (p_147949_0_ <= 0) return aint;
        boolean flag = false;
        for (int i = 0; i < p_147949_2_.length; ++i) {
            if (p_147949_2_[0][i] >> 24 != 0) continue;
            flag = true;
            break;
        }
        int l1 = 1;
        while (l1 <= p_147949_0_) {
            if (p_147949_2_[l1] != null) {
                aint[l1] = p_147949_2_[l1];
            } else {
                int[] aint1 = aint[l1 - 1];
                int[] aint2 = new int[aint1.length >> 2];
                int j = p_147949_1_ >> l1;
                int k = aint2.length / j;
                int l = j << 1;
                for (int i1 = 0; i1 < j; ++i1) {
                    for (int j1 = 0; j1 < k; ++j1) {
                        int k1 = 2 * (i1 + j1 * l);
                        aint2[i1 + j1 * j] = TextureUtil.blendColors(aint1[k1 + 0], aint1[k1 + 1], aint1[k1 + 0 + l], aint1[k1 + 1 + l], flag);
                    }
                }
                aint[l1] = aint2;
            }
            ++l1;
        }
        return aint;
    }

    private static int blendColors(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_) {
        if (!p_147943_4_) {
            int i1 = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 24);
            int j1 = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 16);
            int k1 = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 8);
            int l1 = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 0);
            return i1 << 24 | j1 << 16 | k1 << 8 | l1;
        }
        TextureUtil.mipmapBuffer[0] = p_147943_0_;
        TextureUtil.mipmapBuffer[1] = p_147943_1_;
        TextureUtil.mipmapBuffer[2] = p_147943_2_;
        TextureUtil.mipmapBuffer[3] = p_147943_3_;
        float f = 0.0f;
        float f1 = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        int i = 0;
        while (true) {
            if (i >= 4) {
                int i2 = (int)(Math.pow(f /= 4.0f, 0.45454545454545453) * 255.0);
                int j = (int)(Math.pow(f1 /= 4.0f, 0.45454545454545453) * 255.0);
                int k = (int)(Math.pow(f2 /= 4.0f, 0.45454545454545453) * 255.0);
                int l = (int)(Math.pow(f3 /= 4.0f, 0.45454545454545453) * 255.0);
                if (i2 >= 96) return i2 << 24 | j << 16 | k << 8 | l;
                i2 = 0;
                return i2 << 24 | j << 16 | k << 8 | l;
            }
            if (mipmapBuffer[i] >> 24 != 0) {
                f += (float)Math.pow((float)(mipmapBuffer[i] >> 24 & 0xFF) / 255.0f, 2.2);
                f1 += (float)Math.pow((float)(mipmapBuffer[i] >> 16 & 0xFF) / 255.0f, 2.2);
                f2 += (float)Math.pow((float)(mipmapBuffer[i] >> 8 & 0xFF) / 255.0f, 2.2);
                f3 += (float)Math.pow((float)(mipmapBuffer[i] >> 0 & 0xFF) / 255.0f, 2.2);
            }
            ++i;
        }
    }

    private static int blendColorComponent(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_) {
        float f = (float)Math.pow((float)(p_147944_0_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float f1 = (float)Math.pow((float)(p_147944_1_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float f2 = (float)Math.pow((float)(p_147944_2_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float f3 = (float)Math.pow((float)(p_147944_3_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float f4 = (float)Math.pow((double)(f + f1 + f2 + f3) * 0.25, 0.45454545454545453);
        return (int)((double)f4 * 255.0);
    }

    public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_) {
        int i = 0;
        while (i < p_147955_0_.length) {
            int[] aint = p_147955_0_[i];
            TextureUtil.uploadTextureSub(i, aint, p_147955_1_ >> i, p_147955_2_ >> i, p_147955_3_ >> i, p_147955_4_ >> i, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
            ++i;
        }
    }

    private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_) {
        int i = 0x400000 / p_147947_2_;
        TextureUtil.setTextureBlurMipmap(p_147947_6_, p_147947_8_);
        TextureUtil.setTextureClamped(p_147947_7_);
        int j = 0;
        while (j < p_147947_2_ * p_147947_3_) {
            int k = j / p_147947_2_;
            int l = Math.min(i, p_147947_3_ - k);
            int i1 = p_147947_2_ * l;
            TextureUtil.copyToBufferPos(p_147947_1_, j, i1);
            GL11.glTexSubImage2D((int)3553, (int)p_147947_0_, (int)p_147947_4_, (int)(p_147947_5_ + k), (int)p_147947_2_, (int)l, (int)32993, (int)33639, (IntBuffer)dataBuffer);
            j += p_147947_2_ * l;
        }
    }

    public static int uploadTextureImageAllocate(int p_110989_0_, BufferedImage p_110989_1_, boolean p_110989_2_, boolean p_110989_3_) {
        TextureUtil.allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
        return TextureUtil.uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
    }

    public static void allocateTexture(int p_110991_0_, int p_110991_1_, int p_110991_2_) {
        TextureUtil.allocateTextureImpl(p_110991_0_, 0, p_110991_1_, p_110991_2_);
    }

    public static void allocateTextureImpl(int p_180600_0_, int p_180600_1_, int p_180600_2_, int p_180600_3_) {
        TextureUtil.deleteTexture(p_180600_0_);
        TextureUtil.bindTexture(p_180600_0_);
        if (p_180600_1_ >= 0) {
            GL11.glTexParameteri((int)3553, (int)33085, (int)p_180600_1_);
            GL11.glTexParameterf((int)3553, (int)33082, (float)0.0f);
            GL11.glTexParameterf((int)3553, (int)33083, (float)p_180600_1_);
            GL11.glTexParameterf((int)3553, (int)34049, (float)0.0f);
        }
        int i = 0;
        while (i <= p_180600_1_) {
            GL11.glTexImage2D((int)3553, (int)i, (int)6408, (int)(p_180600_2_ >> i), (int)(p_180600_3_ >> i), (int)0, (int)32993, (int)33639, (IntBuffer)null);
            ++i;
        }
    }

    public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_) {
        TextureUtil.bindTexture(textureId);
        TextureUtil.uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_) {
        int i = p_110993_0_.getWidth();
        int j = p_110993_0_.getHeight();
        int k = 0x400000 / i;
        int[] aint = new int[k * i];
        TextureUtil.setTextureBlurred(p_110993_3_);
        TextureUtil.setTextureClamped(p_110993_4_);
        int l = 0;
        while (l < i * j) {
            int i1 = l / i;
            int j1 = Math.min(k, j - i1);
            int k1 = i * j1;
            p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
            TextureUtil.copyToBuffer(aint, k1);
            GL11.glTexSubImage2D((int)3553, (int)0, (int)p_110993_1_, (int)(p_110993_2_ + i1), (int)i, (int)j1, (int)32993, (int)33639, (IntBuffer)dataBuffer);
            l += i * k;
        }
    }

    private static void setTextureClamped(boolean p_110997_0_) {
        if (p_110997_0_) {
            GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
            return;
        }
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
    }

    private static void setTextureBlurred(boolean p_147951_0_) {
        TextureUtil.setTextureBlurMipmap(p_147951_0_, false);
    }

    private static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_) {
        if (p_147954_0_) {
            GL11.glTexParameteri((int)3553, (int)10241, (int)(p_147954_1_ ? 9987 : 9729));
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            return;
        }
        GL11.glTexParameteri((int)3553, (int)10241, (int)(p_147954_1_ ? 9986 : 9728));
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
    }

    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_) {
        TextureUtil.copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }

    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_) {
        int[] aint = p_110994_0_;
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            aint = TextureUtil.updateAnaglyph(p_110994_0_);
        }
        dataBuffer.clear();
        dataBuffer.put(aint, p_110994_1_, p_110994_2_);
        dataBuffer.position(0).limit(p_110994_2_);
    }

    static void bindTexture(int p_94277_0_) {
        GlStateManager.bindTexture(p_94277_0_);
    }

    public static int[] readImageData(IResourceManager resourceManager, ResourceLocation imageLocation) throws IOException {
        BufferedImage bufferedimage = TextureUtil.readBufferedImage(resourceManager.getResource(imageLocation).getInputStream());
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        return aint;
    }

    public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException {
        try {
            BufferedImage bufferedimage = ImageIO.read(imageStream);
            return bufferedimage;
        }
        finally {
            IOUtils.closeQuietly(imageStream);
        }
    }

    public static int[] updateAnaglyph(int[] p_110985_0_) {
        int[] aint = new int[p_110985_0_.length];
        int i = 0;
        while (i < p_110985_0_.length) {
            aint[i] = TextureUtil.anaglyphColor(p_110985_0_[i]);
            ++i;
        }
        return aint;
    }

    public static int anaglyphColor(int p_177054_0_) {
        int i = p_177054_0_ >> 24 & 0xFF;
        int j = p_177054_0_ >> 16 & 0xFF;
        int k = p_177054_0_ >> 8 & 0xFF;
        int l = p_177054_0_ & 0xFF;
        int i1 = (j * 30 + k * 59 + l * 11) / 100;
        int j1 = (j * 30 + k * 70) / 100;
        int k1 = (j * 30 + l * 70) / 100;
        return i << 24 | i1 << 16 | j1 << 8 | k1;
    }

    public static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_) {
        int[] aint = new int[p_147953_1_];
        int i = p_147953_2_ / 2;
        int j = 0;
        while (j < i) {
            System.arraycopy(p_147953_0_, j * p_147953_1_, aint, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_0_, j * p_147953_1_, p_147953_1_);
            System.arraycopy(aint, 0, p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_1_);
            ++j;
        }
    }

    static {
        int i = -16777216;
        int j = -524040;
        int[] aint = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] aint1 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int k = aint.length;
        int l = 0;
        while (true) {
            if (l >= 16) {
                missingTexture.updateDynamicTexture();
                mipmapBuffer = new int[4];
                return;
            }
            System.arraycopy(l < k ? aint : aint1, 0, missingTextureData, 16 * l, k);
            System.arraycopy(l < k ? aint1 : aint, 0, missingTextureData, 16 * l + k, k);
            ++l;
        }
    }
}

