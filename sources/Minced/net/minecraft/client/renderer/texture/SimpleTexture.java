// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResource;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import net.optifine.EmissiveTextures;
import net.optifine.shaders.ShadersTex;
import net.minecraft.src.Config;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture
{
    private static final Logger LOGGER;
    public final ResourceLocation textureLocation;
    public ResourceLocation locationEmissive;
    public boolean isEmissive;
    
    public SimpleTexture(final ResourceLocation textureResourceLocation) {
        this.textureLocation = textureResourceLocation;
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        IResource iresource = null;
        try {
            iresource = resourceManager.getResource(this.textureLocation);
            final BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
            boolean flag = false;
            boolean flag2 = false;
            if (iresource.hasMetadata()) {
                try {
                    final TextureMetadataSection texturemetadatasection = iresource.getMetadata("texture");
                    if (texturemetadatasection != null) {
                        flag = texturemetadatasection.getTextureBlur();
                        flag2 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception1) {
                    SimpleTexture.LOGGER.warn("Failed reading metadata of: {}", (Object)this.textureLocation, (Object)runtimeexception1);
                }
            }
            if (Config.isShaders()) {
                ShadersTex.loadSimpleTexture(this.getGlTextureId(), bufferedimage, flag, flag2, resourceManager, this.textureLocation, this.getMultiTexID());
            }
            else {
                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag2);
            }
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.loadTexture(this.textureLocation, this);
            }
        }
        finally {
            IOUtils.closeQuietly((Closeable)iresource);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
