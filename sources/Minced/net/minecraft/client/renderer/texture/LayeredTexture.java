// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.optifine.shaders.ShadersTex;
import net.minecraft.src.Config;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture
{
    private static final Logger LOGGER;
    public final List<String> layeredTextureNames;
    private ResourceLocation textureLocation;
    
    public LayeredTexture(final String... textureNames) {
        this.layeredTextureNames = (List<String>)Lists.newArrayList((Object[])textureNames);
        if (textureNames.length > 0 && textureNames[0] != null) {
            this.textureLocation = new ResourceLocation(textureNames[0]);
        }
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;
        for (final String s : this.layeredTextureNames) {
            IResource iresource = null;
            try {
                if (s == null) {
                    continue;
                }
                iresource = resourceManager.getResource(new ResourceLocation(s));
                final BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource.getInputStream());
                if (bufferedimage == null) {
                    bufferedimage = new BufferedImage(bufferedimage2.getWidth(), bufferedimage2.getHeight(), 2);
                }
                bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, null);
                continue;
            }
            catch (IOException ioexception1) {
                LayeredTexture.LOGGER.error("Couldn't load layered image", (Throwable)ioexception1);
            }
            finally {
                IOUtils.closeQuietly((Closeable)iresource);
            }
            return;
        }
        if (Config.isShaders()) {
            ShadersTex.loadSimpleTexture(this.getGlTextureId(), bufferedimage, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
        }
        else {
            TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
