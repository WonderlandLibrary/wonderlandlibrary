// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import java.io.InputStream;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;
import net.augustus.utils.interfaces.MC;

public class ResourceUtil implements MC
{
    public static ResourceLocation loadResourceLocation(final String path, final String name) {
        BufferedImage img = null;
        final InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            return null;
        }
        try {
            img = ImageIO.read(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ResourceUtil.mc.getRenderManager().renderEngine.getDynamicTextureLocation(name, new DynamicTexture(img));
    }
}
