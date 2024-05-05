package best.azura.client.util.textures;

import best.azura.client.impl.ui.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;

public class JordanTextureUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final HashMap<ResourceLocation, Texture> cache = new HashMap<>();
    private static final HashMap<BufferedImage, ResourceLocation> resourceLocationCache = new HashMap<>();
    private static final HashMap<ResourceLocation, BufferedImage> bufferedImageCache = new HashMap<>();
    private static final HashMap<BufferedImage, String> base64ImageCache = new HashMap<>();

    public static Texture createTexture(ResourceLocation location) {
        if (cache.containsKey(location)) return cache.get(location);
        Texture texture = new Texture(getImageFromResource(location));
        cache.put(location, texture);
        return texture;
    }

    public static ResourceLocation getResourceFromImage(BufferedImage image) {
        if (resourceLocationCache.containsKey(image)) return resourceLocationCache.get(image);
        final ResourceLocation location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("", new DynamicTexture(image));
        resourceLocationCache.put(image, location);
        return location;
    }

    public static BufferedImage getResourceFromImage(ResourceLocation location) {
        if (resourceLocationCache.containsValue(location))
            return resourceLocationCache.keySet().stream().filter(i -> resourceLocationCache.get(i).equals(location)).findFirst().orElse(null);
        return null;
    }

    public static BufferedImage getImageFromResource(ResourceLocation location) {
        if (bufferedImageCache.containsKey(location)) return bufferedImageCache.get(location);
        BufferedImage image = null;
        try {
            image = ImageIO.read(mc.getResourceManager().getResource(location).getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        bufferedImageCache.put(location, image);
        return image;
    }

    /*code from stack overflow, too lazy to make myself*/
    public static String getBase64FromImage(final BufferedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
            ImageIO.write(img, formatName, b64os);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return os.toString();
    }

    public static byte[] getBytesFromImage(final BufferedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, formatName, os);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }
}