package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LayeredTexture extends AbstractTexture {

    private static final Logger LOGGER = LogManager.getLogger();

    public final List<String> layeredTextureNames;

    public LayeredTexture(String... textureNames) {
        this.layeredTextureNames = Lists.newArrayList(textureNames);
    }

    public void loadTexture(IResourceManager resourceManager) {
        deleteGlTexture();

        BufferedImage bufferedimage = null;

        try {
            for (String s : this.layeredTextureNames) {
                if (s != null) {
                    final InputStream inputStream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
                    final BufferedImage bufferedImage = TextureUtil.readBufferedImage(inputStream);

                    if (bufferedimage == null)
                        bufferedimage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 2);

                    bufferedimage.getGraphics().drawImage(bufferedImage, 0, 0, null);
                }
            }
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't load layered image", ioexception);
            return;
        }

        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }

}
