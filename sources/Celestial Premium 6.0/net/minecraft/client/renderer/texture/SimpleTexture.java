/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class SimpleTexture
extends AbstractTexture {
    private static final Logger LOG = LogManager.getLogger();
    protected final ResourceLocation textureLocation;

    public SimpleTexture(ResourceLocation textureResourceLocation) {
        this.textureLocation = textureResourceLocation;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        IResource iresource;
        block7: {
            this.deleteGlTexture();
            iresource = null;
            try {
                iresource = resourceManager.getResource(this.textureLocation);
                BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
                boolean flag = false;
                boolean flag1 = false;
                if (iresource.hasMetadata()) {
                    try {
                        TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
                        if (texturemetadatasection != null) {
                            flag = texturemetadatasection.getTextureBlur();
                            flag1 = texturemetadatasection.getTextureClamp();
                        }
                    }
                    catch (RuntimeException runtimeexception1) {
                        LOG.warn("Failed reading metadata of: {}", this.textureLocation, runtimeexception1);
                    }
                }
                if (Config.isShaders()) {
                    ShadersTex.loadSimpleTexture(this.getGlTextureId(), bufferedimage, flag, flag1, resourceManager, this.textureLocation, this.getMultiTexID());
                    break block7;
                }
                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag1);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(iresource);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Closeable)iresource);
    }
}

