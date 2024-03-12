// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import net.minecraft.client.settings.GameSettings;
import java.awt.image.BufferedImage;
import net.optifine.util.TextureUtils;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.ResUtils;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.src.Config;

public class TextureAnimations
{
    private static TextureAnimation[] textureAnimations;
    private static int countAnimationsActive;
    private static int frameCountAnimations;
    
    public static void reset() {
        TextureAnimations.textureAnimations = null;
    }
    
    public static void update() {
        TextureAnimations.textureAnimations = null;
        TextureAnimations.countAnimationsActive = 0;
        final IResourcePack[] airesourcepack = Config.getResourcePacks();
        TextureAnimations.textureAnimations = getTextureAnimations(airesourcepack);
        updateAnimations();
    }
    
    public static void updateAnimations() {
        if (TextureAnimations.textureAnimations != null && Config.isAnimatedTextures()) {
            int i = 0;
            for (int j = 0; j < TextureAnimations.textureAnimations.length; ++j) {
                final TextureAnimation textureanimation = TextureAnimations.textureAnimations[j];
                textureanimation.updateTexture();
                if (textureanimation.isActive()) {
                    ++i;
                }
            }
            final int k = Config.getMinecraft().entityRenderer.frameCount;
            if (k != TextureAnimations.frameCountAnimations) {
                TextureAnimations.countAnimationsActive = i;
                TextureAnimations.frameCountAnimations = k;
            }
            if (SmartAnimations.isActive()) {
                SmartAnimations.resetTexturesRendered();
            }
        }
        else {
            TextureAnimations.countAnimationsActive = 0;
        }
    }
    
    private static TextureAnimation[] getTextureAnimations(final IResourcePack[] rps) {
        final List list = new ArrayList();
        for (int i = 0; i < rps.length; ++i) {
            final IResourcePack iresourcepack = rps[i];
            final TextureAnimation[] atextureanimation = getTextureAnimations(iresourcepack);
            if (atextureanimation != null) {
                list.addAll(Arrays.asList(atextureanimation));
            }
        }
        final TextureAnimation[] atextureanimation2 = list.toArray(new TextureAnimation[list.size()]);
        return atextureanimation2;
    }
    
    private static TextureAnimation[] getTextureAnimations(final IResourcePack rp) {
        final String[] astring = ResUtils.collectFiles(rp, "mcpatcher/anim/", ".properties", null);
        if (astring.length <= 0) {
            return null;
        }
        final List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("Texture animation: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = rp.getInputStream(resourcelocation);
                final Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                final TextureAnimation textureanimation = makeTextureAnimation(properties, resourcelocation);
                if (textureanimation != null) {
                    final ResourceLocation resourcelocation2 = new ResourceLocation(textureanimation.getDstTex());
                    if (Config.getDefiningResourcePack(resourcelocation2) != rp) {
                        Config.dbg("Skipped: " + s + ", target texture not loaded from same resource pack");
                    }
                    else {
                        list.add(textureanimation);
                    }
                }
            }
            catch (FileNotFoundException filenotfoundexception) {
                Config.warn("File not found: " + filenotfoundexception.getMessage());
            }
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        final TextureAnimation[] atextureanimation = list.toArray(new TextureAnimation[list.size()]);
        return atextureanimation;
    }
    
    private static TextureAnimation makeTextureAnimation(final Properties props, final ResourceLocation propLoc) {
        String s = props.getProperty("from");
        String s2 = props.getProperty("to");
        final int i = Config.parseInt(props.getProperty("x"), -1);
        final int j = Config.parseInt(props.getProperty("y"), -1);
        final int k = Config.parseInt(props.getProperty("w"), -1);
        final int l = Config.parseInt(props.getProperty("h"), -1);
        if (s != null && s2 != null) {
            if (i >= 0 && j >= 0 && k >= 0 && l >= 0) {
                s = s.trim();
                s2 = s2.trim();
                final String s3 = TextureUtils.getBasePath(propLoc.getResourcePath());
                s = TextureUtils.fixResourcePath(s, s3);
                s2 = TextureUtils.fixResourcePath(s2, s3);
                final byte[] abyte = getCustomTextureData(s, k);
                if (abyte == null) {
                    Config.warn("TextureAnimation: Source texture not found: " + s2);
                    return null;
                }
                final int i2 = abyte.length / 4;
                final int j2 = i2 / (k * l);
                final int k2 = j2 * k * l;
                if (i2 != k2) {
                    Config.warn("TextureAnimation: Source texture has invalid number of frames: " + s + ", frames: " + i2 / (float)(k * l));
                    return null;
                }
                final ResourceLocation resourcelocation = new ResourceLocation(s2);
                try {
                    final InputStream inputstream = Config.getResourceStream(resourcelocation);
                    if (inputstream == null) {
                        Config.warn("TextureAnimation: Target texture not found: " + s2);
                        return null;
                    }
                    final BufferedImage bufferedimage = readTextureImage(inputstream);
                    if (i + k <= bufferedimage.getWidth() && j + l <= bufferedimage.getHeight()) {
                        final TextureAnimation textureanimation = new TextureAnimation(s, abyte, s2, resourcelocation, i, j, k, l, props);
                        return textureanimation;
                    }
                    Config.warn("TextureAnimation: Animation coordinates are outside the target texture: " + s2);
                    return null;
                }
                catch (IOException var17) {
                    Config.warn("TextureAnimation: Target texture not found: " + s2);
                    return null;
                }
            }
            Config.warn("TextureAnimation: Invalid coordinates");
            return null;
        }
        Config.warn("TextureAnimation: Source or target texture not specified");
        return null;
    }
    
    private static byte[] getCustomTextureData(final String imagePath, final int tileWidth) {
        byte[] abyte = loadImage(imagePath, tileWidth);
        if (abyte == null) {
            abyte = loadImage("/anim" + imagePath, tileWidth);
        }
        return abyte;
    }
    
    private static byte[] loadImage(final String name, final int targetWidth) {
        final GameSettings gamesettings = Config.getGameSettings();
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(name);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return null;
            }
            BufferedImage bufferedimage = readTextureImage(inputstream);
            inputstream.close();
            if (bufferedimage == null) {
                return null;
            }
            if (targetWidth > 0 && bufferedimage.getWidth() != targetWidth) {
                final double d0 = bufferedimage.getHeight() / bufferedimage.getWidth();
                final int j = (int)(targetWidth * d0);
                bufferedimage = scaleBufferedImage(bufferedimage, targetWidth, j);
            }
            final int k2 = bufferedimage.getWidth();
            final int i = bufferedimage.getHeight();
            final int[] aint = new int[k2 * i];
            final byte[] abyte = new byte[k2 * i * 4];
            bufferedimage.getRGB(0, 0, k2, i, aint, 0, k2);
            for (int l = 0; l < aint.length; ++l) {
                final int m = aint[l] >> 24 & 0xFF;
                int i2 = aint[l] >> 16 & 0xFF;
                int j2 = aint[l] >> 8 & 0xFF;
                int k3 = aint[l] & 0xFF;
                if (gamesettings != null && gamesettings.anaglyph) {
                    final int l2 = (i2 * 30 + j2 * 59 + k3 * 11) / 100;
                    final int i3 = (i2 * 30 + j2 * 70) / 100;
                    final int j3 = (i2 * 30 + k3 * 70) / 100;
                    i2 = l2;
                    j2 = i3;
                    k3 = j3;
                }
                abyte[l * 4 + 0] = (byte)i2;
                abyte[l * 4 + 1] = (byte)j2;
                abyte[l * 4 + 2] = (byte)k3;
                abyte[l * 4 + 3] = (byte)m;
            }
            return abyte;
        }
        catch (FileNotFoundException var18) {
            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    private static BufferedImage readTextureImage(final InputStream par1InputStream) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(par1InputStream);
        par1InputStream.close();
        return bufferedimage;
    }
    
    private static BufferedImage scaleBufferedImage(final BufferedImage image, final int width, final int height) {
        final BufferedImage bufferedimage = new BufferedImage(width, height, 2);
        final Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.drawImage(image, 0, 0, width, height, null);
        return bufferedimage;
    }
    
    public static int getCountAnimations() {
        return (TextureAnimations.textureAnimations == null) ? 0 : TextureAnimations.textureAnimations.length;
    }
    
    public static int getCountAnimationsActive() {
        return TextureAnimations.countAnimationsActive;
    }
    
    static {
        TextureAnimations.textureAnimations = null;
        TextureAnimations.countAnimationsActive = 0;
        TextureAnimations.frameCountAnimations = 0;
    }
}
