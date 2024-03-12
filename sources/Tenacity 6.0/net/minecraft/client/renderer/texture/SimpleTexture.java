// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResource;
import java.io.InputStream;
import net.optifine.EmissiveTextures;
import net.optifine.shaders.ShadersTex;
import net.minecraft.src.Config;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture
{
    private static final Logger logger;
    protected final ResourceLocation textureLocation;
    public ResourceLocation locationEmissive;
    public boolean isEmissive;
    
    public SimpleTexture(final ResourceLocation textureResourceLocation) {
        this.textureLocation = textureResourceLocation;
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        InputStream inputstream = null;
        try {
            final IResource iresource = resourceManager.getResource(this.textureLocation);
            inputstream = iresource.getInputStream();
            final BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
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
                catch (RuntimeException runtimeexception) {
                    SimpleTexture.logger.warn("Failed reading metadata of: " + this.textureLocation, (Throwable)runtimeexception);
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
            if (inputstream != null) {
                inputstream.close();
            }
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
