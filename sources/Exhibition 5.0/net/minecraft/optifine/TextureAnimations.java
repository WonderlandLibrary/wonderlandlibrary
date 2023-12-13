// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.minecraft.client.settings.GameSettings;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.AbstractResourcePack;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.client.resources.IResourcePack;

public class TextureAnimations
{
    private static TextureAnimation[] textureAnimations;
    
    public static void reset() {
        TextureAnimations.textureAnimations = null;
    }
    
    public static void update() {
        final IResourcePack[] rps = Config.getResourcePacks();
        TextureAnimations.textureAnimations = getTextureAnimations(rps);
        updateAnimations();
    }
    
    public static void updateCustomAnimations() {
        if (TextureAnimations.textureAnimations != null && Config.isAnimatedTextures()) {
            updateAnimations();
        }
    }
    
    public static void updateAnimations() {
        if (TextureAnimations.textureAnimations != null) {
            for (final TextureAnimation anim : TextureAnimations.textureAnimations) {
                anim.updateTexture();
            }
        }
    }
    
    public static TextureAnimation[] getTextureAnimations(final IResourcePack[] rps) {
        final ArrayList list = new ArrayList();
        for (final IResourcePack rp : rps) {
            final TextureAnimation[] tas = getTextureAnimations(rp);
            if (tas != null) {
                list.addAll(Arrays.asList(tas));
            }
        }
        final TextureAnimation[] var5 = list.toArray(new TextureAnimation[list.size()]);
        return var5;
    }
    
    public static TextureAnimation[] getTextureAnimations(final IResourcePack rp) {
        if (!(rp instanceof AbstractResourcePack)) {
            return null;
        }
        final AbstractResourcePack arp = (AbstractResourcePack)rp;
        final File tpFile = ResourceUtils.getResourcePackFile(arp);
        if (tpFile == null) {
            return null;
        }
        if (!tpFile.exists()) {
            return null;
        }
        String[] animPropNames = null;
        if (tpFile.isFile()) {
            animPropNames = getAnimationPropertiesZip(tpFile);
        }
        else {
            animPropNames = getAnimationPropertiesDir(tpFile);
        }
        if (animPropNames == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        for (final String propName : animPropNames) {
            Config.dbg("Texture animation: " + propName);
            try {
                final ResourceLocation e = new ResourceLocation(propName);
                final InputStream in = rp.getInputStream(e);
                final Properties props = new Properties();
                props.load(in);
                final TextureAnimation anim = makeTextureAnimation(props, e);
                if (anim != null) {
                    final ResourceLocation locDstTex = new ResourceLocation(anim.getDstTex());
                    if (Config.getDefiningResourcePack(locDstTex) != rp) {
                        Config.dbg("Skipped: " + propName + ", target texture not loaded from same resource pack");
                    }
                    else {
                        list.add(anim);
                    }
                }
            }
            catch (FileNotFoundException var12) {
                Config.warn("File not found: " + var12.getMessage());
            }
            catch (IOException var13) {
                var13.printStackTrace();
            }
        }
        final TextureAnimation[] var14 = list.toArray(new TextureAnimation[list.size()]);
        return var14;
    }
    
    public static TextureAnimation makeTextureAnimation(final Properties props, final ResourceLocation propLoc) {
        String texFrom = props.getProperty("from");
        String texTo = props.getProperty("to");
        final int x = Config.parseInt(props.getProperty("x"), -1);
        final int y = Config.parseInt(props.getProperty("y"), -1);
        final int width = Config.parseInt(props.getProperty("w"), -1);
        final int height = Config.parseInt(props.getProperty("h"), -1);
        if (texFrom == null || texTo == null) {
            Config.warn("TextureAnimation: Source or target texture not specified");
            return null;
        }
        if (x < 0 || y < 0 || width < 0 || height < 0) {
            Config.warn("TextureAnimation: Invalid coordinates");
            return null;
        }
        final String basePath = TextureUtils.getBasePath(propLoc.getResourcePath());
        texFrom = TextureUtils.fixResourcePath(texFrom, basePath);
        texTo = TextureUtils.fixResourcePath(texTo, basePath);
        final byte[] imageBytes = getCustomTextureData(texFrom, width);
        if (imageBytes == null) {
            Config.warn("TextureAnimation: Source texture not found: " + texTo);
            return null;
        }
        final ResourceLocation locTexTo = new ResourceLocation(texTo);
        if (!Config.hasResource(locTexTo)) {
            Config.warn("TextureAnimation: Target texture not found: " + texTo);
            return null;
        }
        final ITextureObject destTex = TextureUtils.getTexture(locTexTo);
        if (destTex == null) {
            Config.warn("TextureAnimation: Target texture not found: " + locTexTo);
            return null;
        }
        final int destTexId = destTex.getGlTextureId();
        final TextureAnimation anim = new TextureAnimation(texFrom, imageBytes, texTo, destTexId, x, y, width, height, props, 1);
        return anim;
    }
    
    public static String[] getAnimationPropertiesDir(final File tpDir) {
        final File dirAnim = new File(tpDir, "anim");
        if (!dirAnim.exists()) {
            return null;
        }
        if (!dirAnim.isDirectory()) {
            return null;
        }
        final File[] propFiles = dirAnim.listFiles();
        if (propFiles == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        for (final File file : propFiles) {
            final String name = file.getName();
            if (!name.startsWith("custom_") && name.endsWith(".properties") && file.isFile() && file.canRead()) {
                Config.dbg("TextureAnimation: anim/" + file.getName());
                list.add("/anim/" + name);
            }
        }
        final String[] var7 = list.toArray(new String[list.size()]);
        return var7;
    }
    
    public static String[] getAnimationPropertiesZip(final File tpFile) {
        try {
            final ZipFile e = new ZipFile(tpFile);
            final Enumeration en = e.entries();
            final ArrayList list = new ArrayList();
            while (en.hasMoreElements()) {
                final ZipEntry props = en.nextElement();
                String name = props.getName();
                if (name.startsWith("assets/minecraft/mcpatcher/anim/") && !name.startsWith("assets/minecraft/mcpatcher/anim/custom_") && name.endsWith(".properties")) {
                    final String assetsMcStr = "assets/minecraft/";
                    name = name.substring(assetsMcStr.length());
                    list.add(name);
                }
            }
            e.close();
            final String[] props2 = list.toArray(new String[list.size()]);
            return props2;
        }
        catch (IOException var7) {
            var7.printStackTrace();
            return null;
        }
    }
    
    public static byte[] getCustomTextureData(final String imagePath, final int tileWidth) {
        byte[] imageBytes = loadImage(imagePath, tileWidth);
        if (imageBytes == null) {
            imageBytes = loadImage("/anim" + imagePath, tileWidth);
        }
        return imageBytes;
    }
    
    private static byte[] loadImage(final String name, final int targetWidth) {
        final GameSettings options = Config.getGameSettings();
        try {
            final ResourceLocation e = new ResourceLocation(name);
            final InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return null;
            }
            BufferedImage image = readTextureImage(in);
            if (image == null) {
                return null;
            }
            if (targetWidth > 0 && image.getWidth() != targetWidth) {
                final double width = image.getHeight() / image.getWidth();
                final int ai = (int)(targetWidth * width);
                image = scaleBufferedImage(image, targetWidth, ai);
            }
            final int var20 = image.getWidth();
            final int height = image.getHeight();
            final int[] var21 = new int[var20 * height];
            final byte[] byteBuf = new byte[var20 * height * 4];
            image.getRGB(0, 0, var20, height, var21, 0, var20);
            for (int l = 0; l < var21.length; ++l) {
                final int alpha = var21[l] >> 24 & 0xFF;
                int red = var21[l] >> 16 & 0xFF;
                int green = var21[l] >> 8 & 0xFF;
                int blue = var21[l] & 0xFF;
                if (options != null && options.anaglyph) {
                    final int j3 = (red * 30 + green * 59 + blue * 11) / 100;
                    final int l2 = (red * 30 + green * 70) / 100;
                    final int j4 = (red * 30 + blue * 70) / 100;
                    red = j3;
                    green = l2;
                    blue = j4;
                }
                byteBuf[l * 4 + 0] = (byte)red;
                byteBuf[l * 4 + 1] = (byte)green;
                byteBuf[l * 4 + 2] = (byte)blue;
                byteBuf[l * 4 + 3] = (byte)alpha;
            }
            return byteBuf;
        }
        catch (FileNotFoundException var23) {
            return null;
        }
        catch (Exception var22) {
            var22.printStackTrace();
            return null;
        }
    }
    
    private static BufferedImage readTextureImage(final InputStream par1InputStream) throws IOException {
        final BufferedImage var2 = ImageIO.read(par1InputStream);
        par1InputStream.close();
        return var2;
    }
    
    public static BufferedImage scaleBufferedImage(final BufferedImage image, final int width, final int height) {
        final BufferedImage scaledImage = new BufferedImage(width, height, 2);
        final Graphics2D gr = scaledImage.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gr.drawImage(image, 0, 0, width, height, null);
        return scaledImage;
    }
    
    static {
        TextureAnimations.textureAnimations = null;
    }
}
