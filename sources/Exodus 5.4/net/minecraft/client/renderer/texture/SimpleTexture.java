/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTexture
extends AbstractTexture {
    private static final Logger logger = LogManager.getLogger();
    protected final ResourceLocation textureLocation;

    public SimpleTexture(ResourceLocation resourceLocation) {
        this.textureLocation = resourceLocation;
    }

    @Override
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        this.deleteGlTexture();
        InputStream inputStream = null;
        IResource iResource = iResourceManager.getResource(this.textureLocation);
        inputStream = iResource.getInputStream();
        BufferedImage bufferedImage = TextureUtil.readBufferedImage(inputStream);
        boolean bl = false;
        boolean bl2 = false;
        if (iResource.hasMetadata()) {
            try {
                TextureMetadataSection textureMetadataSection = (TextureMetadataSection)iResource.getMetadata("texture");
                if (textureMetadataSection != null) {
                    bl = textureMetadataSection.getTextureBlur();
                    bl2 = textureMetadataSection.getTextureClamp();
                }
            }
            catch (RuntimeException runtimeException) {
                logger.warn("Failed reading metadata of: " + this.textureLocation, (Throwable)runtimeException);
            }
        }
        TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedImage, bl, bl2);
        if (inputStream != null) {
            inputStream.close();
        }
    }
}

