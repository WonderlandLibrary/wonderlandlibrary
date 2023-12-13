/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
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
import optifine.Config;
import optifine.Mipmaps;
import optifine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(0x400000);
    public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
    public static final int[] missingTextureData = missingTexture.getTextureData();
    private static final int[] field_147957_g;
    private static final String __OBFID = "CL_00001067";

    static {
        int var0 = -16777216;
        int var1 = -524040;
        int[] var2 = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] var3 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int var4 = var2.length;
        for (int var5 = 0; var5 < 16; ++var5) {
            System.arraycopy(var5 < var4 ? var2 : var3, 0, missingTextureData, 16 * var5, var4);
            System.arraycopy(var5 < var4 ? var3 : var2, 0, missingTextureData, 16 * var5 + var4, var4);
        }
        missingTexture.updateDynamicTexture();
        field_147957_g = new int[4];
    }

    public static int glGenTextures() {
        return GlStateManager.func_179146_y();
    }

    public static void deleteTexture(int p_147942_0_) {
        GlStateManager.func_179150_h(p_147942_0_);
    }

    public static int uploadTextureImage(int p_110987_0_, BufferedImage p_110987_1_) {
        return TextureUtil.uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
    }

    public static void uploadTexture(int p_110988_0_, int[] p_110988_1_, int p_110988_2_, int p_110988_3_) {
        TextureUtil.bindTexture(p_110988_0_);
        TextureUtil.uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
    }

    public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_) {
        int[][] var3 = new int[p_147949_0_ + 1][];
        var3[0] = p_147949_2_[0];
        if (p_147949_0_ > 0) {
            int var5;
            boolean var4 = false;
            for (var5 = 0; var5 < p_147949_2_.length; ++var5) {
                if (p_147949_2_[0][var5] >> 24 != 0) continue;
                var4 = true;
                break;
            }
            for (var5 = 1; var5 <= p_147949_0_; ++var5) {
                if (p_147949_2_[var5] != null) {
                    var3[var5] = p_147949_2_[var5];
                    continue;
                }
                int[] var6 = var3[var5 - 1];
                int[] var7 = new int[var6.length >> 2];
                int var8 = p_147949_1_ >> var5;
                int var9 = var7.length / var8;
                int var10 = var8 << 1;
                for (int var11 = 0; var11 < var8; ++var11) {
                    for (int var12 = 0; var12 < var9; ++var12) {
                        int var13 = 2 * (var11 + var12 * var10);
                        var7[var11 + var12 * var8] = TextureUtil.func_147943_a(var6[var13 + 0], var6[var13 + 1], var6[var13 + 0 + var10], var6[var13 + 1 + var10], var4);
                    }
                }
                var3[var5] = var7;
            }
        }
        return var3;
    }

    private static int func_147943_a(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_) {
        return Mipmaps.alphaBlend(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_);
    }

    private static int func_147944_a(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_) {
        float var5 = (float)Math.pow((float)(p_147944_0_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float var6 = (float)Math.pow((float)(p_147944_1_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float var7 = (float)Math.pow((float)(p_147944_2_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float var8 = (float)Math.pow((float)(p_147944_3_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        float var9 = (float)Math.pow((double)(var5 + var6 + var7 + var8) * 0.25, 0.45454545454545453);
        return (int)((double)var9 * 255.0);
    }

    public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_) {
        for (int var7 = 0; var7 < p_147955_0_.length; ++var7) {
            int[] var8 = p_147955_0_[var7];
            TextureUtil.uploadTextureSub(var7, var8, p_147955_1_ >> var7, p_147955_2_ >> var7, p_147955_3_ >> var7, p_147955_4_ >> var7, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
        }
    }

    private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_) {
        int var12;
        int var9 = 0x400000 / p_147947_2_;
        TextureUtil.func_147954_b(p_147947_6_, p_147947_8_);
        TextureUtil.setTextureClamped(p_147947_7_);
        for (int var10 = 0; var10 < p_147947_2_ * p_147947_3_; var10 += p_147947_2_ * var12) {
            int var11 = var10 / p_147947_2_;
            var12 = Math.min(var9, p_147947_3_ - var11);
            int var13 = p_147947_2_ * var12;
            TextureUtil.copyToBufferPos(p_147947_1_, var10, var13);
            GL11.glTexSubImage2D((int)3553, (int)p_147947_0_, (int)p_147947_4_, (int)(p_147947_5_ + var11), (int)p_147947_2_, (int)var12, (int)32993, (int)33639, (IntBuffer)dataBuffer);
        }
    }

    public static int uploadTextureImageAllocate(int p_110989_0_, BufferedImage p_110989_1_, boolean p_110989_2_, boolean p_110989_3_) {
        TextureUtil.allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
        return TextureUtil.uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
    }

    public static void allocateTexture(int p_110991_0_, int p_110991_1_, int p_110991_2_) {
        TextureUtil.func_180600_a(p_110991_0_, 0, p_110991_1_, p_110991_2_);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void func_180600_a(int p_180600_0_, int p_180600_1_, int p_180600_2_, int p_180600_3_) {
        Class monitor = TextureUtil.class;
        if (Reflector.SplashScreen.exists()) {
            monitor = Reflector.SplashScreen.getTargetClass();
        }
        Class class_ = monitor;
        synchronized (class_) {
            TextureUtil.deleteTexture(p_180600_0_);
            TextureUtil.bindTexture(p_180600_0_);
        }
        if (p_180600_1_ >= 0) {
            GL11.glTexParameteri((int)3553, (int)33085, (int)p_180600_1_);
            GL11.glTexParameterf((int)3553, (int)33082, (float)0.0f);
            GL11.glTexParameterf((int)3553, (int)33083, (float)p_180600_1_);
            GL11.glTexParameterf((int)3553, (int)34049, (float)0.0f);
        }
        for (int var4 = 0; var4 <= p_180600_1_; ++var4) {
            GL11.glTexImage2D((int)3553, (int)var4, (int)6408, (int)(p_180600_2_ >> var4), (int)(p_180600_3_ >> var4), (int)0, (int)32993, (int)33639, null);
        }
    }

    public static int uploadTextureImageSub(int p_110995_0_, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_) {
        TextureUtil.bindTexture(p_110995_0_);
        TextureUtil.uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return p_110995_0_;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_) {
        int var5 = p_110993_0_.getWidth();
        int var6 = p_110993_0_.getHeight();
        int var7 = 0x400000 / var5;
        int[] var8 = new int[var7 * var5];
        TextureUtil.setTextureBlurred(p_110993_3_);
        TextureUtil.setTextureClamped(p_110993_4_);
        for (int var9 = 0; var9 < var5 * var6; var9 += var5 * var7) {
            int var10 = var9 / var5;
            int var11 = Math.min(var7, var6 - var10);
            int var12 = var5 * var11;
            p_110993_0_.getRGB(0, var10, var5, var11, var8, 0, var5);
            TextureUtil.copyToBuffer(var8, var12);
            GL11.glTexSubImage2D((int)3553, (int)0, (int)p_110993_1_, (int)(p_110993_2_ + var10), (int)var5, (int)var11, (int)32993, (int)33639, (IntBuffer)dataBuffer);
        }
    }

    public static void setTextureClamped(boolean p_110997_0_) {
        if (p_110997_0_) {
            GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
            GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        } else {
            GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        }
    }

    private static void setTextureBlurred(boolean p_147951_0_) {
        TextureUtil.func_147954_b(p_147951_0_, false);
    }

    public static void func_147954_b(boolean p_147954_0_, boolean p_147954_1_) {
        if (p_147954_0_) {
            GL11.glTexParameteri((int)3553, (int)10241, (int)(p_147954_1_ ? 9987 : 9729));
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        } else {
            int mipmapType = Config.getMipmapType();
            GL11.glTexParameteri((int)3553, (int)10241, (int)(p_147954_1_ ? mipmapType : 9728));
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        }
    }

    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_) {
        TextureUtil.copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }

    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_) {
        int[] var3 = p_110994_0_;
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            var3 = TextureUtil.updateAnaglyph(p_110994_0_);
        }
        dataBuffer.clear();
        dataBuffer.put(var3, p_110994_1_, p_110994_2_);
        dataBuffer.position(0).limit(p_110994_2_);
    }

    static void bindTexture(int p_94277_0_) {
        GlStateManager.func_179144_i(p_94277_0_);
    }

    public static int[] readImageData(IResourceManager p_110986_0_, ResourceLocation p_110986_1_) throws IOException {
        BufferedImage var2 = TextureUtil.func_177053_a(p_110986_0_.getResource(p_110986_1_).getInputStream());
        if (var2 == null) {
            return null;
        }
        int var3 = var2.getWidth();
        int var4 = var2.getHeight();
        int[] var5 = new int[var3 * var4];
        var2.getRGB(0, 0, var3, var4, var5, 0, var3);
        return var5;
    }

    public static BufferedImage func_177053_a(InputStream p_177053_0_) throws IOException, IOException {
        BufferedImage var1;
        if (p_177053_0_ == null) {
            return null;
        }
        try {
            var1 = ImageIO.read(p_177053_0_);
        }
        finally {
            IOUtils.closeQuietly((InputStream)p_177053_0_);
        }
        return var1;
    }

    public static int[] updateAnaglyph(int[] p_110985_0_) {
        int[] var1 = new int[p_110985_0_.length];
        for (int var2 = 0; var2 < p_110985_0_.length; ++var2) {
            var1[var2] = TextureUtil.func_177054_c(p_110985_0_[var2]);
        }
        return var1;
    }

    public static int func_177054_c(int p_177054_0_) {
        int var1 = p_177054_0_ >> 24 & 0xFF;
        int var2 = p_177054_0_ >> 16 & 0xFF;
        int var3 = p_177054_0_ >> 8 & 0xFF;
        int var4 = p_177054_0_ & 0xFF;
        int var5 = (var2 * 30 + var3 * 59 + var4 * 11) / 100;
        int var6 = (var2 * 30 + var3 * 70) / 100;
        int var7 = (var2 * 30 + var4 * 70) / 100;
        return var1 << 24 | var5 << 16 | var6 << 8 | var7;
    }

    public static void func_177055_a(String name, int textureId, int mipmapLevels, int width, int height) {
        TextureUtil.bindTexture(textureId);
        GL11.glPixelStorei((int)3333, (int)1);
        GL11.glPixelStorei((int)3317, (int)1);
        for (int var5 = 0; var5 <= mipmapLevels; ++var5) {
            File var6 = new File(String.valueOf(name) + "_" + var5 + ".png");
            int var7 = width >> var5;
            int var8 = height >> var5;
            int var9 = var7 * var8;
            IntBuffer var10 = BufferUtils.createIntBuffer((int)var9);
            int[] var11 = new int[var9];
            GL11.glGetTexImage((int)3553, (int)var5, (int)32993, (int)33639, (IntBuffer)var10);
            var10.get(var11);
            BufferedImage var12 = new BufferedImage(var7, var8, 2);
            var12.setRGB(0, 0, var7, var8, var11, 0, var7);
            try {
                ImageIO.write((RenderedImage)var12, "png", var6);
                logger.debug("Exported png to: {}", new Object[]{var6.getAbsolutePath()});
                continue;
            }
            catch (Exception var14) {
                logger.debug("Unable to write: ", (Throwable)var14);
            }
        }
    }

    public static void func_147953_a(int[] p_147953_0_, int p_147953_1_, int p_147953_2_) {
        int[] var3 = new int[p_147953_1_];
        int var4 = p_147953_2_ / 2;
        for (int var5 = 0; var5 < var4; ++var5) {
            System.arraycopy(p_147953_0_, var5 * p_147953_1_, var3, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_0_, var5 * p_147953_1_, p_147953_1_);
            System.arraycopy(var3, 0, p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_1_);
        }
    }
}

