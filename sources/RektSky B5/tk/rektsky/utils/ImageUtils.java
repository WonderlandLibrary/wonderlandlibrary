/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.utils.cryoto.CryptoUtils;

public class ImageUtils {
    public static <T> BufferedImage getImage(T source, ThrowingFunction<T, BufferedImage> readFunction) {
        try {
            return readFunction.apply(source);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResourceLocation loadImage(URL url, File cacheFile) {
        try {
            BufferedImage image;
            if (cacheFile.exists()) {
                image = ImageUtils.getImage(cacheFile, ImageIO::read);
            } else {
                image = ImageUtils.getImage(url, ImageIO::read);
                if (image != null) {
                    try {
                        ImageIO.write((RenderedImage)image, "png", cacheFile);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (image == null) {
                return null;
            }
            DynamicTexture dynamicTexture = new DynamicTexture(image);
            return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(CryptoUtils.bytesToHex(cacheFile.getAbsolutePath().getBytes(StandardCharsets.UTF_8)), dynamicTexture);
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @FunctionalInterface
    public static interface ThrowingFunction<T, R> {
        public R apply(T var1) throws IOException;
    }
}

