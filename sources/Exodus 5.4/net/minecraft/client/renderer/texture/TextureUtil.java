/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
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
    public static final int[] missingTextureData;
    private static final IntBuffer dataBuffer;
    public static final DynamicTexture missingTexture;
    private static final int[] mipmapBuffer;

    public static void uploadTexture(int n, int[] nArray, int n2, int n3) {
        TextureUtil.bindTexture(n);
        TextureUtil.uploadTextureSub(0, nArray, n2, n3, 0, 0, false, false, false);
    }

    public static int anaglyphColor(int n) {
        int n2 = n >> 24 & 0xFF;
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        int n6 = (n3 * 30 + n4 * 59 + n5 * 11) / 100;
        int n7 = (n3 * 30 + n4 * 70) / 100;
        int n8 = (n3 * 30 + n5 * 70) / 100;
        return n2 << 24 | n6 << 16 | n7 << 8 | n8;
    }

    public static void uploadTextureMipmap(int[][] nArray, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        int n5 = 0;
        while (n5 < nArray.length) {
            int[] nArray2 = nArray[n5];
            TextureUtil.uploadTextureSub(n5, nArray2, n >> n5, n2 >> n5, n3 >> n5, n4 >> n5, bl, bl2, nArray.length > 1);
            ++n5;
        }
    }

    public static int[] readImageData(IResourceManager iResourceManager, ResourceLocation resourceLocation) throws IOException {
        BufferedImage bufferedImage = TextureUtil.readBufferedImage(iResourceManager.getResource(resourceLocation).getInputStream());
        int n = bufferedImage.getWidth();
        int n2 = bufferedImage.getHeight();
        int[] nArray = new int[n * n2];
        bufferedImage.getRGB(0, 0, n, n2, nArray, 0, n);
        return nArray;
    }

    private static void setTextureClamped(boolean bl) {
        if (bl) {
            GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
        } else {
            GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        }
    }

    public static int[][] generateMipmapData(int n, int n2, int[][] nArray) {
        int[][] nArrayArray = new int[n + 1][];
        nArrayArray[0] = nArray[0];
        if (n > 0) {
            boolean bl = false;
            int n3 = 0;
            while (n3 < nArray.length) {
                if (nArray[0][n3] >> 24 == 0) {
                    bl = true;
                    break;
                }
                ++n3;
            }
            n3 = 1;
            while (n3 <= n) {
                if (nArray[n3] != null) {
                    nArrayArray[n3] = nArray[n3];
                } else {
                    int[] nArray2 = nArrayArray[n3 - 1];
                    int[] nArray3 = new int[nArray2.length >> 2];
                    int n4 = n2 >> n3;
                    int n5 = nArray3.length / n4;
                    int n6 = n4 << 1;
                    int n7 = 0;
                    while (n7 < n4) {
                        int n8 = 0;
                        while (n8 < n5) {
                            int n9 = 2 * (n7 + n8 * n6);
                            nArray3[n7 + n8 * n4] = TextureUtil.blendColors(nArray2[n9 + 0], nArray2[n9 + 1], nArray2[n9 + 0 + n6], nArray2[n9 + 1 + n6], bl);
                            ++n8;
                        }
                        ++n7;
                    }
                    nArrayArray[n3] = nArray3;
                }
                ++n3;
            }
        }
        return nArrayArray;
    }

    static void bindTexture(int n) {
        GlStateManager.bindTexture(n);
    }

    public static int[] updateAnaglyph(int[] nArray) {
        int[] nArray2 = new int[nArray.length];
        int n = 0;
        while (n < nArray.length) {
            nArray2[n] = TextureUtil.anaglyphColor(nArray[n]);
            ++n;
        }
        return nArray2;
    }

    private static void uploadTextureSub(int n, int[] nArray, int n2, int n3, int n4, int n5, boolean bl, boolean bl2, boolean bl3) {
        int n6 = 0x400000 / n2;
        TextureUtil.setTextureBlurMipmap(bl, bl3);
        TextureUtil.setTextureClamped(bl2);
        int n7 = 0;
        while (n7 < n2 * n3) {
            int n8 = n7 / n2;
            int n9 = Math.min(n6, n3 - n8);
            int n10 = n2 * n9;
            TextureUtil.copyToBufferPos(nArray, n7, n10);
            GL11.glTexSubImage2D((int)3553, (int)n, (int)n4, (int)(n5 + n8), (int)n2, (int)n9, (int)32993, (int)33639, (IntBuffer)dataBuffer);
            n7 += n2 * n9;
        }
    }

    private static int blendColorComponent(int n, int n2, int n3, int n4, int n5) {
        float f = (float)Math.pow((float)(n >> n5 & 0xFF) / 255.0f, 2.2);
        float f2 = (float)Math.pow((float)(n2 >> n5 & 0xFF) / 255.0f, 2.2);
        float f3 = (float)Math.pow((float)(n3 >> n5 & 0xFF) / 255.0f, 2.2);
        float f4 = (float)Math.pow((float)(n4 >> n5 & 0xFF) / 255.0f, 2.2);
        float f5 = (float)Math.pow((double)(f + f2 + f3 + f4) * 0.25, 0.45454545454545453);
        return (int)((double)f5 * 255.0);
    }

    public static void allocateTextureImpl(int n, int n2, int n3, int n4) {
        TextureUtil.deleteTexture(n);
        TextureUtil.bindTexture(n);
        if (n2 >= 0) {
            GL11.glTexParameteri((int)3553, (int)33085, (int)n2);
            GL11.glTexParameterf((int)3553, (int)33082, (float)0.0f);
            GL11.glTexParameterf((int)3553, (int)33083, (float)n2);
            GL11.glTexParameterf((int)3553, (int)34049, (float)0.0f);
        }
        int n5 = 0;
        while (n5 <= n2) {
            GL11.glTexImage2D((int)3553, (int)n5, (int)6408, (int)(n3 >> n5), (int)(n4 >> n5), (int)0, (int)32993, (int)33639, null);
            ++n5;
        }
    }

    public static int uploadTextureImageAllocate(int n, BufferedImage bufferedImage, boolean bl, boolean bl2) {
        TextureUtil.allocateTexture(n, bufferedImage.getWidth(), bufferedImage.getHeight());
        return TextureUtil.uploadTextureImageSub(n, bufferedImage, 0, 0, bl, bl2);
    }

    public static void allocateTexture(int n, int n2, int n3) {
        TextureUtil.allocateTextureImpl(n, 0, n2, n3);
    }

    public static int uploadTextureImage(int n, BufferedImage bufferedImage) {
        return TextureUtil.uploadTextureImageAllocate(n, bufferedImage, false, false);
    }

    public static int uploadTextureImageSub(int n, BufferedImage bufferedImage, int n2, int n3, boolean bl, boolean bl2) {
        TextureUtil.bindTexture(n);
        TextureUtil.uploadTextureImageSubImpl(bufferedImage, n2, n3, bl, bl2);
        return n;
    }

    public static BufferedImage readBufferedImage(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        IOUtils.closeQuietly((InputStream)inputStream);
        return bufferedImage;
    }

    public static int glGenTextures() {
        return GlStateManager.generateTexture();
    }

    static {
        dataBuffer = GLAllocation.createDirectIntBuffer(0x400000);
        missingTexture = new DynamicTexture(16, 16);
        missingTextureData = missingTexture.getTextureData();
        int n = -16777216;
        int n2 = -524040;
        int[] nArray = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] nArray2 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int n3 = nArray.length;
        int n4 = 0;
        while (n4 < 16) {
            System.arraycopy(n4 < n3 ? nArray : nArray2, 0, missingTextureData, 16 * n4, n3);
            System.arraycopy(n4 < n3 ? nArray2 : nArray, 0, missingTextureData, 16 * n4 + n3, n3);
            ++n4;
        }
        missingTexture.updateDynamicTexture();
        mipmapBuffer = new int[4];
    }

    private static void uploadTextureImageSubImpl(BufferedImage bufferedImage, int n, int n2, boolean bl, boolean bl2) {
        int n3 = bufferedImage.getWidth();
        int n4 = bufferedImage.getHeight();
        int n5 = 0x400000 / n3;
        int[] nArray = new int[n5 * n3];
        TextureUtil.setTextureBlurred(bl);
        TextureUtil.setTextureClamped(bl2);
        int n6 = 0;
        while (n6 < n3 * n4) {
            int n7 = n6 / n3;
            int n8 = Math.min(n5, n4 - n7);
            int n9 = n3 * n8;
            bufferedImage.getRGB(0, n7, n3, n8, nArray, 0, n3);
            TextureUtil.copyToBuffer(nArray, n9);
            GL11.glTexSubImage2D((int)3553, (int)0, (int)n, (int)(n2 + n7), (int)n3, (int)n8, (int)32993, (int)33639, (IntBuffer)dataBuffer);
            n6 += n3 * n5;
        }
    }

    private static void setTextureBlurred(boolean bl) {
        TextureUtil.setTextureBlurMipmap(bl, false);
    }

    private static int blendColors(int n, int n2, int n3, int n4, boolean bl) {
        if (!bl) {
            int n5 = TextureUtil.blendColorComponent(n, n2, n3, n4, 24);
            int n6 = TextureUtil.blendColorComponent(n, n2, n3, n4, 16);
            int n7 = TextureUtil.blendColorComponent(n, n2, n3, n4, 8);
            int n8 = TextureUtil.blendColorComponent(n, n2, n3, n4, 0);
            return n5 << 24 | n6 << 16 | n7 << 8 | n8;
        }
        TextureUtil.mipmapBuffer[0] = n;
        TextureUtil.mipmapBuffer[1] = n2;
        TextureUtil.mipmapBuffer[2] = n3;
        TextureUtil.mipmapBuffer[3] = n4;
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        int n9 = 0;
        while (n9 < 4) {
            if (mipmapBuffer[n9] >> 24 != 0) {
                f += (float)Math.pow((float)(mipmapBuffer[n9] >> 24 & 0xFF) / 255.0f, 2.2);
                f2 += (float)Math.pow((float)(mipmapBuffer[n9] >> 16 & 0xFF) / 255.0f, 2.2);
                f3 += (float)Math.pow((float)(mipmapBuffer[n9] >> 8 & 0xFF) / 255.0f, 2.2);
                f4 += (float)Math.pow((float)(mipmapBuffer[n9] >> 0 & 0xFF) / 255.0f, 2.2);
            }
            ++n9;
        }
        n9 = (int)(Math.pow(f /= 4.0f, 0.45454545454545453) * 255.0);
        int n10 = (int)(Math.pow(f2 /= 4.0f, 0.45454545454545453) * 255.0);
        int n11 = (int)(Math.pow(f3 /= 4.0f, 0.45454545454545453) * 255.0);
        int n12 = (int)(Math.pow(f4 /= 4.0f, 0.45454545454545453) * 255.0);
        if (n9 < 96) {
            n9 = 0;
        }
        return n9 << 24 | n10 << 16 | n11 << 8 | n12;
    }

    private static void setTextureBlurMipmap(boolean bl, boolean bl2) {
        if (bl) {
            GL11.glTexParameteri((int)3553, (int)10241, (int)(bl2 ? 9987 : 9729));
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        } else {
            GL11.glTexParameteri((int)3553, (int)10241, (int)(bl2 ? 9986 : 9728));
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        }
    }

    public static void deleteTexture(int n) {
        GlStateManager.deleteTexture(n);
    }

    private static void copyToBuffer(int[] nArray, int n) {
        TextureUtil.copyToBufferPos(nArray, 0, n);
    }

    public static void processPixelValues(int[] nArray, int n, int n2) {
        int[] nArray2 = new int[n];
        int n3 = n2 / 2;
        int n4 = 0;
        while (n4 < n3) {
            System.arraycopy(nArray, n4 * n, nArray2, 0, n);
            System.arraycopy(nArray, (n2 - 1 - n4) * n, nArray, n4 * n, n);
            System.arraycopy(nArray2, 0, nArray, (n2 - 1 - n4) * n, n);
            ++n4;
        }
    }

    private static void copyToBufferPos(int[] nArray, int n, int n2) {
        int[] nArray2 = nArray;
        Minecraft.getMinecraft();
        if (Minecraft.gameSettings.anaglyph) {
            nArray2 = TextureUtil.updateAnaglyph(nArray);
        }
        dataBuffer.clear();
        dataBuffer.put(nArray2, n, n2);
        dataBuffer.position(0).limit(n2);
    }
}

