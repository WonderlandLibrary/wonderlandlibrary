/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package shadersmod.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import shadersmod.client.MultiTexID;
import shadersmod.client.Shaders;
import shadersmod.common.SMCLog;

public class ShadersTex {
    public static final int initialBufferSize = 0x100000;
    public static ByteBuffer byteBuffer = BufferUtils.createByteBuffer((int)0x400000);
    public static IntBuffer intBuffer = byteBuffer.asIntBuffer();
    public static int[] intArray = new int[0x100000];
    public static final int defBaseTexColor = 0;
    public static final int defNormTexColor = -8421377;
    public static final int defSpecTexColor = 0;
    public static Map<Integer, MultiTexID> multiTexMap = new HashMap<Integer, MultiTexID>();
    public static TextureMap updatingTextureMap = null;
    public static TextureAtlasSprite updatingSprite = null;
    public static MultiTexID updatingTex = null;
    public static MultiTexID boundTex = null;
    public static int updatingPage = 0;
    public static String iconName = null;
    public static IResourceManager resManager = null;
    static ResourceLocation resLocation = null;
    static int imageSize = 0;

    public static IntBuffer getIntBuffer(int size) {
        if (intBuffer.capacity() < size) {
            int bufferSize = ShadersTex.roundUpPOT(size);
            byteBuffer = BufferUtils.createByteBuffer((int)(bufferSize * 4));
            intBuffer = byteBuffer.asIntBuffer();
        }
        return intBuffer;
    }

    public static int[] getIntArray(int size) {
        if (intArray == null) {
            intArray = new int[0x100000];
        }
        if (intArray.length < size) {
            intArray = new int[ShadersTex.roundUpPOT(size)];
        }
        return intArray;
    }

    public static int roundUpPOT(int x) {
        int i = x - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }

    public static int log2(int x) {
        int log = 0;
        if ((x & 0xFFFF0000) != 0) {
            log += 16;
            x >>= 16;
        }
        if ((x & 0xFF00) != 0) {
            log += 8;
            x >>= 8;
        }
        if ((x & 0xF0) != 0) {
            log += 4;
            x >>= 4;
        }
        if ((x & 6) != 0) {
            log += 2;
            x >>= 2;
        }
        if ((x & 2) != 0) {
            ++log;
        }
        return log;
    }

    public static IntBuffer fillIntBuffer(int size, int value) {
        int[] aint = ShadersTex.getIntArray(size);
        IntBuffer intBuf = ShadersTex.getIntBuffer(size);
        Arrays.fill(intArray, 0, size, value);
        intBuffer.put(intArray, 0, size);
        return intBuffer;
    }

    public static int[] createAIntImage(int size) {
        int[] aint = new int[size * 3];
        Arrays.fill(aint, 0, size, 0);
        Arrays.fill(aint, size, size * 2, -8421377);
        Arrays.fill(aint, size * 2, size * 3, 0);
        return aint;
    }

    public static int[] createAIntImage(int size, int color) {
        int[] aint = new int[size * 3];
        Arrays.fill(aint, 0, size, color);
        Arrays.fill(aint, size, size * 2, -8421377);
        Arrays.fill(aint, size * 2, size * 3, 0);
        return aint;
    }

    public static MultiTexID getMultiTexID(AbstractTexture tex) {
        MultiTexID multiTex = tex.multiTex;
        if (multiTex == null) {
            int baseTex = tex.getGlTextureId();
            multiTex = multiTexMap.get(baseTex);
            if (multiTex == null) {
                multiTex = new MultiTexID(baseTex, GL11.glGenTextures(), GL11.glGenTextures());
                multiTexMap.put(baseTex, multiTex);
            }
            tex.multiTex = multiTex;
        }
        return multiTex;
    }

    public static void deleteTextures(AbstractTexture atex, int texid) {
        MultiTexID multiTex = atex.multiTex;
        if (multiTex != null) {
            atex.multiTex = null;
            multiTexMap.remove(multiTex.base);
            GlStateManager.func_179150_h(multiTex.norm);
            GlStateManager.func_179150_h(multiTex.spec);
            if (multiTex.base != texid) {
                SMCLog.warning("Error : MultiTexID.base mismatch: " + multiTex.base + ", texid: " + texid);
                GlStateManager.func_179150_h(multiTex.base);
            }
        }
    }

    public static void bindNSTextures(int normTex, int specTex) {
        if (Shaders.isRenderingWorld && GlStateManager.getActiveTextureUnit() == 33984) {
            GlStateManager.setActiveTexture(33986);
            GlStateManager.func_179144_i(normTex);
            GlStateManager.setActiveTexture(33987);
            GlStateManager.func_179144_i(specTex);
            GlStateManager.setActiveTexture(33984);
        }
    }

    public static void bindNSTextures(MultiTexID multiTex) {
        ShadersTex.bindNSTextures(multiTex.norm, multiTex.spec);
    }

    public static void bindTextures(int baseTex, int normTex, int specTex) {
        if (Shaders.isRenderingWorld && GlStateManager.getActiveTextureUnit() == 33984) {
            GlStateManager.setActiveTexture(33986);
            GlStateManager.func_179144_i(normTex);
            GlStateManager.setActiveTexture(33987);
            GlStateManager.func_179144_i(specTex);
            GlStateManager.setActiveTexture(33984);
        }
        GlStateManager.func_179144_i(baseTex);
    }

    public static void bindTextures(MultiTexID multiTex) {
        boundTex = multiTex;
        if (Shaders.isRenderingWorld && GlStateManager.getActiveTextureUnit() == 33984) {
            if (Shaders.configNormalMap) {
                GlStateManager.setActiveTexture(33986);
                GlStateManager.func_179144_i(multiTex.norm);
            }
            if (Shaders.configSpecularMap) {
                GlStateManager.setActiveTexture(33987);
                GlStateManager.func_179144_i(multiTex.spec);
            }
            GlStateManager.setActiveTexture(33984);
        }
        GlStateManager.func_179144_i(multiTex.base);
    }

    public static void bindTexture(ITextureObject tex) {
        int texId = tex.getGlTextureId();
        if (tex instanceof TextureMap) {
            Shaders.atlasSizeX = ((TextureMap)tex).atlasWidth;
            Shaders.atlasSizeY = ((TextureMap)tex).atlasHeight;
            ShadersTex.bindTextures(tex.getMultiTexID());
        } else {
            Shaders.atlasSizeX = 0;
            Shaders.atlasSizeY = 0;
            ShadersTex.bindTextures(tex.getMultiTexID());
        }
    }

    public static void bindTextureMapForUpdateAndRender(TextureManager tm, ResourceLocation resLoc) {
        TextureMap tex = (TextureMap)tm.getTexture(resLoc);
        Shaders.atlasSizeX = tex.atlasWidth;
        Shaders.atlasSizeY = tex.atlasHeight;
        updatingTex = tex.getMultiTexID();
        ShadersTex.bindTextures(updatingTex);
    }

    public static void bindTextures(int baseTex) {
        MultiTexID multiTex = multiTexMap.get(baseTex);
        ShadersTex.bindTextures(multiTex);
    }

    public static void initDynamicTexture(int texID, int width, int height, DynamicTexture tex) {
        MultiTexID multiTex = tex.getMultiTexID();
        int[] aint = tex.getTextureData();
        int size = width * height;
        Arrays.fill(aint, size, size * 2, -8421377);
        Arrays.fill(aint, size * 2, size * 3, 0);
        TextureUtil.allocateTexture(multiTex.base, width, height);
        TextureUtil.func_147954_b(false, false);
        TextureUtil.setTextureClamped(false);
        TextureUtil.allocateTexture(multiTex.norm, width, height);
        TextureUtil.func_147954_b(false, false);
        TextureUtil.setTextureClamped(false);
        TextureUtil.allocateTexture(multiTex.spec, width, height);
        TextureUtil.func_147954_b(false, false);
        TextureUtil.setTextureClamped(false);
        GlStateManager.func_179144_i(multiTex.base);
    }

    public static void updateDynamicTexture(int texID, int[] src, int width, int height, DynamicTexture tex) {
        MultiTexID multiTex = tex.getMultiTexID();
        GlStateManager.func_179144_i(multiTex.base);
        ShadersTex.updateDynTexSubImage1(src, width, height, 0, 0, 0);
        GlStateManager.func_179144_i(multiTex.norm);
        ShadersTex.updateDynTexSubImage1(src, width, height, 0, 0, 1);
        GlStateManager.func_179144_i(multiTex.spec);
        ShadersTex.updateDynTexSubImage1(src, width, height, 0, 0, 2);
        GlStateManager.func_179144_i(multiTex.base);
    }

    public static void updateDynTexSubImage1(int[] src, int width, int height, int posX, int posY, int page) {
        int size = width * height;
        IntBuffer intBuf = ShadersTex.getIntBuffer(size);
        intBuf.clear();
        int offset = page * size;
        if (src.length >= offset + size) {
            intBuf.put(src, offset, size).position(0).limit(size);
            GL11.glTexSubImage2D((int)3553, (int)0, (int)posX, (int)posY, (int)width, (int)height, (int)32993, (int)33639, (IntBuffer)intBuf);
            intBuf.clear();
        }
    }

    public static ITextureObject createDefaultTexture() {
        DynamicTexture tex = new DynamicTexture(1, 1);
        tex.getTextureData()[0] = -1;
        tex.updateDynamicTexture();
        return tex;
    }

    public static void allocateTextureMap(int texID, int mipmapLevels, int width, int height, Stitcher stitcher, TextureMap tex) {
        MultiTexID multiTex;
        SMCLog.info("allocateTextureMap " + mipmapLevels + " " + width + " " + height + " ");
        updatingTextureMap = tex;
        tex.atlasWidth = width;
        tex.atlasHeight = height;
        updatingTex = multiTex = ShadersTex.getMultiTexID(tex);
        TextureUtil.func_180600_a(multiTex.base, mipmapLevels, width, height);
        if (Shaders.configNormalMap) {
            TextureUtil.func_180600_a(multiTex.norm, mipmapLevels, width, height);
        }
        if (Shaders.configSpecularMap) {
            TextureUtil.func_180600_a(multiTex.spec, mipmapLevels, width, height);
        }
        GlStateManager.func_179144_i(texID);
    }

    public static TextureAtlasSprite setSprite(TextureAtlasSprite tas) {
        updatingSprite = tas;
        return tas;
    }

    public static String setIconName(String name) {
        iconName = name;
        return name;
    }

    public static void uploadTexSubForLoadAtlas(int[][] data, int width, int height, int xoffset, int yoffset, boolean linear, boolean clamp) {
        int[][] aaint;
        TextureUtil.uploadTextureMipmap(data, width, height, xoffset, yoffset, linear, clamp);
        boolean border = false;
        if (Shaders.configNormalMap) {
            aaint = ShadersTex.readImageAndMipmaps(String.valueOf(iconName) + "_n", width, height, data.length, border, -8421377);
            GlStateManager.func_179144_i(ShadersTex.updatingTex.norm);
            TextureUtil.uploadTextureMipmap(aaint, width, height, xoffset, yoffset, linear, clamp);
        }
        if (Shaders.configSpecularMap) {
            aaint = ShadersTex.readImageAndMipmaps(String.valueOf(iconName) + "_s", width, height, data.length, border, 0);
            GlStateManager.func_179144_i(ShadersTex.updatingTex.spec);
            TextureUtil.uploadTextureMipmap(aaint, width, height, xoffset, yoffset, linear, clamp);
        }
        GlStateManager.func_179144_i(ShadersTex.updatingTex.base);
    }

    public static int[][] readImageAndMipmaps(String name, int width, int height, int numLevels, boolean border, int defColor) {
        Object aaint = new int[numLevels][];
        int[] aint = new int[width * height];
        aaint[0] = aint;
        boolean goodImage = false;
        BufferedImage image = ShadersTex.readImage(updatingTextureMap.completeResourceLocation(new ResourceLocation(name), 0));
        if (image != null) {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            if (imageWidth + (border ? 16 : 0) == width) {
                goodImage = true;
                image.getRGB(0, 0, imageWidth, imageWidth, aint, 0, imageWidth);
            }
        }
        if (!goodImage) {
            Arrays.fill(aint, defColor);
        }
        GlStateManager.func_179144_i(ShadersTex.updatingTex.spec);
        aaint = ShadersTex.genMipmapsSimple(((int[][])aaint).length - 1, width, aaint);
        return aaint;
    }

    public static BufferedImage readImage(ResourceLocation resLoc) {
        InputStream istr;
        block5: {
            IResource e;
            block4: {
                try {
                    e = resManager.getResource(resLoc);
                    if (e != null) break block4;
                    return null;
                }
                catch (IOException var4) {
                    return null;
                }
            }
            istr = e.getInputStream();
            if (istr != null) break block5;
            return null;
        }
        BufferedImage image = ImageIO.read(istr);
        istr.close();
        return image;
    }

    public static int[][] genMipmapsSimple(int maxLevel, int width, int[][] data) {
        for (int level = 1; level <= maxLevel; ++level) {
            if (data[level] != null) continue;
            int cw = width >> level;
            int pw = cw * 2;
            int[] aintp = data[level - 1];
            data[level] = new int[cw * cw];
            int[] aintc = data[level];
            for (int y = 0; y < cw; ++y) {
                for (int x = 0; x < cw; ++x) {
                    int ppos = y * 2 * pw + x * 2;
                    aintc[y * cw + x] = ShadersTex.blend4Simple(aintp[ppos], aintp[ppos + 1], aintp[ppos + pw], aintp[ppos + pw + 1]);
                }
            }
        }
        return data;
    }

    public static void uploadTexSub(int[][] data, int width, int height, int xoffset, int yoffset, boolean linear, boolean clamp) {
        TextureUtil.uploadTextureMipmap(data, width, height, xoffset, yoffset, linear, clamp);
        if (Shaders.configNormalMap || Shaders.configSpecularMap) {
            if (Shaders.configNormalMap) {
                GlStateManager.func_179144_i(ShadersTex.updatingTex.norm);
                ShadersTex.uploadTexSub1(data, width, height, xoffset, yoffset, 1);
            }
            if (Shaders.configSpecularMap) {
                GlStateManager.func_179144_i(ShadersTex.updatingTex.spec);
                ShadersTex.uploadTexSub1(data, width, height, xoffset, yoffset, 2);
            }
            GlStateManager.func_179144_i(ShadersTex.updatingTex.base);
        }
    }

    public static void uploadTexSub1(int[][] src, int width, int height, int posX, int posY, int page) {
        int size = width * height;
        IntBuffer intBuf = ShadersTex.getIntBuffer(size);
        int numLevel = src.length;
        int level = 0;
        int lw = width;
        int lh = height;
        int px = posX;
        int py = posY;
        while (lw > 0 && lh > 0 && level < numLevel) {
            int lsize = lw * lh;
            int[] aint = src[level];
            intBuf.clear();
            if (aint.length >= lsize * (page + 1)) {
                intBuf.put(aint, lsize * page, lsize).position(0).limit(lsize);
                GL11.glTexSubImage2D((int)3553, (int)level, (int)px, (int)py, (int)lw, (int)lh, (int)32993, (int)33639, (IntBuffer)intBuf);
            }
            lw >>= 1;
            lh >>= 1;
            px >>= 1;
            py >>= 1;
            ++level;
        }
        intBuf.clear();
    }

    public static int blend4Alpha(int c0, int c1, int c2, int c3) {
        int dv;
        int a0 = c0 >>> 24 & 0xFF;
        int a1 = c1 >>> 24 & 0xFF;
        int a2 = c2 >>> 24 & 0xFF;
        int a3 = c3 >>> 24 & 0xFF;
        int as = a0 + a1 + a2 + a3;
        int an = (as + 2) / 4;
        if (as != 0) {
            dv = as;
        } else {
            dv = 4;
            a0 = 1;
            a1 = 1;
            a2 = 1;
            a3 = 1;
        }
        int frac = (dv + 1) / 2;
        int color = an << 24 | ((c0 >>> 16 & 0xFF) * a0 + (c1 >>> 16 & 0xFF) * a1 + (c2 >>> 16 & 0xFF) * a2 + (c3 >>> 16 & 0xFF) * a3 + frac) / dv << 16 | ((c0 >>> 8 & 0xFF) * a0 + (c1 >>> 8 & 0xFF) * a1 + (c2 >>> 8 & 0xFF) * a2 + (c3 >>> 8 & 0xFF) * a3 + frac) / dv << 8 | ((c0 >>> 0 & 0xFF) * a0 + (c1 >>> 0 & 0xFF) * a1 + (c2 >>> 0 & 0xFF) * a2 + (c3 >>> 0 & 0xFF) * a3 + frac) / dv << 0;
        return color;
    }

    public static int blend4Simple(int c0, int c1, int c2, int c3) {
        int color = ((c0 >>> 24 & 0xFF) + (c1 >>> 24 & 0xFF) + (c2 >>> 24 & 0xFF) + (c3 >>> 24 & 0xFF) + 2) / 4 << 24 | ((c0 >>> 16 & 0xFF) + (c1 >>> 16 & 0xFF) + (c2 >>> 16 & 0xFF) + (c3 >>> 16 & 0xFF) + 2) / 4 << 16 | ((c0 >>> 8 & 0xFF) + (c1 >>> 8 & 0xFF) + (c2 >>> 8 & 0xFF) + (c3 >>> 8 & 0xFF) + 2) / 4 << 8 | ((c0 >>> 0 & 0xFF) + (c1 >>> 0 & 0xFF) + (c2 >>> 0 & 0xFF) + (c3 >>> 0 & 0xFF) + 2) / 4 << 0;
        return color;
    }

    public static void genMipmapAlpha(int[] aint, int offset, int width, int height) {
        int x;
        int y;
        int p2;
        Math.min(width, height);
        int o2 = offset;
        int w2 = width;
        int h2 = height;
        int o1 = 0;
        int w1 = 0;
        boolean h1 = false;
        int level = 0;
        while (w2 > 1 && h2 > 1) {
            o1 = o2 + w2 * h2;
            w1 = w2 / 2;
            int var16 = h2 / 2;
            for (p2 = 0; p2 < var16; ++p2) {
                y = o1 + p2 * w1;
                x = o2 + p2 * 2 * w2;
                for (int x1 = 0; x1 < w1; ++x1) {
                    aint[y + x1] = ShadersTex.blend4Alpha(aint[x + x1 * 2], aint[x + x1 * 2 + 1], aint[x + w2 + x1 * 2], aint[x + w2 + x1 * 2 + 1]);
                }
            }
            ++level;
            w2 = w1;
            h2 = var16;
            o2 = o1;
        }
        while (level > 0) {
            w2 = width >> --level;
            h2 = height >> level;
            p2 = o2 = o1 - w2 * h2;
            for (y = 0; y < h2; ++y) {
                for (x = 0; x < w2; ++x) {
                    if (aint[p2] == 0) {
                        aint[p2] = aint[o1 + y / 2 * w1 + x / 2] & 0xFFFFFF;
                    }
                    ++p2;
                }
            }
            o1 = o2;
            w1 = w2;
        }
    }

    public static void genMipmapSimple(int[] aint, int offset, int width, int height) {
        int x;
        int y;
        int p2;
        Math.min(width, height);
        int o2 = offset;
        int w2 = width;
        int h2 = height;
        int o1 = 0;
        int w1 = 0;
        boolean h1 = false;
        int level = 0;
        while (w2 > 1 && h2 > 1) {
            o1 = o2 + w2 * h2;
            w1 = w2 / 2;
            int var16 = h2 / 2;
            for (p2 = 0; p2 < var16; ++p2) {
                y = o1 + p2 * w1;
                x = o2 + p2 * 2 * w2;
                for (int x1 = 0; x1 < w1; ++x1) {
                    aint[y + x1] = ShadersTex.blend4Simple(aint[x + x1 * 2], aint[x + x1 * 2 + 1], aint[x + w2 + x1 * 2], aint[x + w2 + x1 * 2 + 1]);
                }
            }
            ++level;
            w2 = w1;
            h2 = var16;
            o2 = o1;
        }
        while (level > 0) {
            w2 = width >> --level;
            h2 = height >> level;
            p2 = o2 = o1 - w2 * h2;
            for (y = 0; y < h2; ++y) {
                for (x = 0; x < w2; ++x) {
                    if (aint[p2] == 0) {
                        aint[p2] = aint[o1 + y / 2 * w1 + x / 2] & 0xFFFFFF;
                    }
                    ++p2;
                }
            }
            o1 = o2;
            w1 = w2;
        }
    }

    public static boolean isSemiTransparent(int[] aint, int width, int height) {
        int size = width * height;
        if (aint[0] >>> 24 == 255 && aint[size - 1] == 0) {
            return true;
        }
        for (int i = 0; i < size; ++i) {
            int alpha = aint[i] >>> 24;
            if (alpha == 0 || alpha == 255) continue;
            return true;
        }
        return false;
    }

    public static void updateSubTex1(int[] src, int width, int height, int posX, int posY) {
        int level = 0;
        int cw = width;
        int ch = height;
        int cx = posX;
        int cy = posY;
        while (cw > 0 && ch > 0) {
            GL11.glCopyTexSubImage2D((int)3553, (int)level, (int)cx, (int)cy, (int)0, (int)0, (int)cw, (int)ch);
            ++level;
            cw /= 2;
            ch /= 2;
            cx /= 2;
            cy /= 2;
        }
    }

    public static void setupTexture(MultiTexID multiTex, int[] src, int width, int height, boolean linear, boolean clamp) {
        int mmfilter = linear ? 9729 : 9728;
        int wraptype = clamp ? 10496 : 10497;
        int size = width * height;
        IntBuffer intBuf = ShadersTex.getIntBuffer(size);
        intBuf.clear();
        intBuf.put(src, 0, size).position(0).limit(size);
        GlStateManager.func_179144_i(multiTex.base);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)width, (int)height, (int)0, (int)32993, (int)33639, (IntBuffer)intBuf);
        GL11.glTexParameteri((int)3553, (int)10241, (int)mmfilter);
        GL11.glTexParameteri((int)3553, (int)10240, (int)mmfilter);
        GL11.glTexParameteri((int)3553, (int)10242, (int)wraptype);
        GL11.glTexParameteri((int)3553, (int)10243, (int)wraptype);
        intBuf.put(src, size, size).position(0).limit(size);
        GlStateManager.func_179144_i(multiTex.norm);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)width, (int)height, (int)0, (int)32993, (int)33639, (IntBuffer)intBuf);
        GL11.glTexParameteri((int)3553, (int)10241, (int)mmfilter);
        GL11.glTexParameteri((int)3553, (int)10240, (int)mmfilter);
        GL11.glTexParameteri((int)3553, (int)10242, (int)wraptype);
        GL11.glTexParameteri((int)3553, (int)10243, (int)wraptype);
        intBuf.put(src, size * 2, size).position(0).limit(size);
        GlStateManager.func_179144_i(multiTex.spec);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)width, (int)height, (int)0, (int)32993, (int)33639, (IntBuffer)intBuf);
        GL11.glTexParameteri((int)3553, (int)10241, (int)mmfilter);
        GL11.glTexParameteri((int)3553, (int)10240, (int)mmfilter);
        GL11.glTexParameteri((int)3553, (int)10242, (int)wraptype);
        GL11.glTexParameteri((int)3553, (int)10243, (int)wraptype);
        GlStateManager.func_179144_i(multiTex.base);
    }

    public static void updateSubImage(MultiTexID multiTex, int[] src, int width, int height, int posX, int posY, boolean linear, boolean clamp) {
        int size = width * height;
        IntBuffer intBuf = ShadersTex.getIntBuffer(size);
        intBuf.clear();
        intBuf.put(src, 0, size);
        intBuf.position(0).limit(size);
        GlStateManager.func_179144_i(multiTex.base);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        GL11.glTexSubImage2D((int)3553, (int)0, (int)posX, (int)posY, (int)width, (int)height, (int)32993, (int)33639, (IntBuffer)intBuf);
        if (src.length == size * 3) {
            intBuf.clear();
            intBuf.put(src, size, size).position(0);
            intBuf.position(0).limit(size);
        }
        GlStateManager.func_179144_i(multiTex.norm);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        GL11.glTexSubImage2D((int)3553, (int)0, (int)posX, (int)posY, (int)width, (int)height, (int)32993, (int)33639, (IntBuffer)intBuf);
        if (src.length == size * 3) {
            intBuf.clear();
            intBuf.put(src, size * 2, size);
            intBuf.position(0).limit(size);
        }
        GlStateManager.func_179144_i(multiTex.spec);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        GL11.glTexSubImage2D((int)3553, (int)0, (int)posX, (int)posY, (int)width, (int)height, (int)32993, (int)33639, (IntBuffer)intBuf);
        GlStateManager.setActiveTexture(33984);
    }

    public static ResourceLocation getNSMapLocation(ResourceLocation location, String mapName) {
        String basename = location.getResourcePath();
        String[] basenameParts = basename.split(".png");
        String basenameNoFileType = basenameParts[0];
        return new ResourceLocation(location.getResourceDomain(), String.valueOf(basenameNoFileType) + "_" + mapName + ".png");
    }

    public static void loadNSMap(IResourceManager manager, ResourceLocation location, int width, int height, int[] aint) {
        if (Shaders.configNormalMap) {
            ShadersTex.loadNSMap1(manager, ShadersTex.getNSMapLocation(location, "n"), width, height, aint, width * height, -8421377);
        }
        if (Shaders.configSpecularMap) {
            ShadersTex.loadNSMap1(manager, ShadersTex.getNSMapLocation(location, "s"), width, height, aint, width * height * 2, 0);
        }
    }

    public static void loadNSMap1(IResourceManager manager, ResourceLocation location, int width, int height, int[] aint, int offset, int defaultColor) {
        boolean good = false;
        try {
            IResource ex = manager.getResource(location);
            BufferedImage bufferedimage = ImageIO.read(ex.getInputStream());
            if (bufferedimage != null && bufferedimage.getWidth() == width && bufferedimage.getHeight() == height) {
                bufferedimage.getRGB(0, 0, width, height, aint, offset, width);
                good = true;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (!good) {
            Arrays.fill(aint, offset, offset + width * height, defaultColor);
        }
    }

    public static int loadSimpleTexture(int textureID, BufferedImage bufferedimage, boolean linear, boolean clamp, IResourceManager resourceManager, ResourceLocation location, MultiTexID multiTex) {
        int width = bufferedimage.getWidth();
        int height = bufferedimage.getHeight();
        int size = width * height;
        int[] aint = ShadersTex.getIntArray(size * 3);
        bufferedimage.getRGB(0, 0, width, height, aint, 0, width);
        ShadersTex.loadNSMap(resourceManager, location, width, height, aint);
        ShadersTex.setupTexture(multiTex, aint, width, height, linear, clamp);
        return textureID;
    }

    public static void mergeImage(int[] aint, int dstoff, int srcoff, int size) {
    }

    public static int blendColor(int color1, int color2, int factor1) {
        int factor2 = 255 - factor1;
        return ((color1 >>> 24 & 0xFF) * factor1 + (color2 >>> 24 & 0xFF) * factor2) / 255 << 24 | ((color1 >>> 16 & 0xFF) * factor1 + (color2 >>> 16 & 0xFF) * factor2) / 255 << 16 | ((color1 >>> 8 & 0xFF) * factor1 + (color2 >>> 8 & 0xFF) * factor2) / 255 << 8 | ((color1 >>> 0 & 0xFF) * factor1 + (color2 >>> 0 & 0xFF) * factor2) / 255 << 0;
    }

    public static void loadLayeredTexture(LayeredTexture tex, IResourceManager manager, List list) {
        int width = 0;
        int height = 0;
        int size = 0;
        int[] image = null;
        for (String s : list) {
            if (s == null) continue;
            try {
                ResourceLocation ex = new ResourceLocation(s);
                InputStream inputstream = manager.getResource(ex).getInputStream();
                BufferedImage bufimg = ImageIO.read(inputstream);
                if (size == 0) {
                    width = bufimg.getWidth();
                    height = bufimg.getHeight();
                    size = width * height;
                    image = ShadersTex.createAIntImage(size, 0);
                }
                int[] aint = ShadersTex.getIntArray(size * 3);
                bufimg.getRGB(0, 0, width, height, aint, 0, width);
                ShadersTex.loadNSMap(manager, ex, width, height, aint);
                for (int i = 0; i < size; ++i) {
                    int alpha = aint[i] >>> 24 & 0xFF;
                    image[size * 0 + i] = ShadersTex.blendColor(aint[size * 0 + i], image[size * 0 + i], alpha);
                    image[size * 1 + i] = ShadersTex.blendColor(aint[size * 1 + i], image[size * 1 + i], alpha);
                    image[size * 2 + i] = ShadersTex.blendColor(aint[size * 2 + i], image[size * 2 + i], alpha);
                }
            }
            catch (IOException var15) {
                var15.printStackTrace();
            }
        }
        ShadersTex.setupTexture(tex.getMultiTexID(), image, width, height, false, false);
    }

    static void updateTextureMinMagFilter() {
        TextureManager texman = Minecraft.getMinecraft().getTextureManager();
        ITextureObject texObj = texman.getTexture(TextureMap.locationBlocksTexture);
        if (texObj != null) {
            MultiTexID multiTex = texObj.getMultiTexID();
            GlStateManager.func_179144_i(multiTex.base);
            GL11.glTexParameteri((int)3553, (int)10241, (int)Shaders.texMinFilValue[Shaders.configTexMinFilB]);
            GL11.glTexParameteri((int)3553, (int)10240, (int)Shaders.texMagFilValue[Shaders.configTexMagFilB]);
            GlStateManager.func_179144_i(multiTex.norm);
            GL11.glTexParameteri((int)3553, (int)10241, (int)Shaders.texMinFilValue[Shaders.configTexMinFilN]);
            GL11.glTexParameteri((int)3553, (int)10240, (int)Shaders.texMagFilValue[Shaders.configTexMagFilN]);
            GlStateManager.func_179144_i(multiTex.spec);
            GL11.glTexParameteri((int)3553, (int)10241, (int)Shaders.texMinFilValue[Shaders.configTexMinFilS]);
            GL11.glTexParameteri((int)3553, (int)10240, (int)Shaders.texMagFilValue[Shaders.configTexMagFilS]);
            GlStateManager.func_179144_i(0);
        }
    }

    public static IResource loadResource(IResourceManager manager, ResourceLocation location) throws IOException {
        resManager = manager;
        resLocation = location;
        return manager.getResource(location);
    }

    public static int[] loadAtlasSprite(BufferedImage bufferedimage, int startX, int startY, int w, int h, int[] aint, int offset, int scansize) {
        imageSize = w * h;
        bufferedimage.getRGB(startX, startY, w, h, aint, offset, scansize);
        ShadersTex.loadNSMap(resManager, resLocation, w, h, aint);
        return aint;
    }

    public static int[][] getFrameTexData(int[][] src, int width, int height, int frameIndex) {
        int numLevel = src.length;
        int[][] dst = new int[numLevel][];
        for (int level = 0; level < numLevel; ++level) {
            int[] sr1 = src[level];
            if (sr1 == null) continue;
            int frameSize = (width >> level) * (height >> level);
            int[] ds1 = new int[frameSize * 3];
            dst[level] = ds1;
            int srcSize = sr1.length / 3;
            int srcPos = frameSize * frameIndex;
            int dstPos = 0;
            System.arraycopy(sr1, srcPos, ds1, dstPos, frameSize);
            int var13 = dstPos + frameSize;
            System.arraycopy(sr1, srcPos += srcSize, ds1, var13, frameSize);
            System.arraycopy(sr1, srcPos += srcSize, ds1, var13 += frameSize, frameSize);
        }
        return dst;
    }

    public static int[][] prepareAF(TextureAtlasSprite tas, int[][] src, int width, int height) {
        boolean skip = true;
        return src;
    }

    public static void fixTransparentColor(TextureAtlasSprite tas, int[] aint) {
    }
}

