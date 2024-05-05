package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture
extends AbstractTexture {
    private static final Logger logger = LogManager.getLogger();
    public final List<String> layeredTextureNames;
    private ResourceLocation textureLocation;

    public LayeredTexture(String ... textureNames) {
        this.layeredTextureNames = Lists.newArrayList((Object[])textureNames);
        if (textureNames.length > 0 && textureNames[0] != null) {
            this.textureLocation = new ResourceLocation(textureNames[0]);
        }
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;
        try {
            for (String s : this.layeredTextureNames) {
                if (s == null) continue;
                InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
                BufferedImage bufferedimage1 = TextureUtil.readBufferedImage((InputStream)inputstream);
                if (bufferedimage == null) {
                    bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
                }
                bufferedimage.getGraphics().drawImage((Image)bufferedimage1, 0, 0, (ImageObserver)null);
            }
        }
        catch (IOException ioexception) {
            logger.error("Couldn't load layered image", (Throwable)ioexception);
            return;
        }
        if (Config.isShaders()) {
            ShadersTex.loadSimpleTexture((int)this.getGlTextureId(), bufferedimage, (boolean)false, (boolean)false, (IResourceManager)resourceManager, (ResourceLocation)this.textureLocation, (MultiTexID)this.getMultiTexID());
        } else {
            TextureUtil.uploadTextureImage((int)this.getGlTextureId(), bufferedimage);
        }
    }
}
