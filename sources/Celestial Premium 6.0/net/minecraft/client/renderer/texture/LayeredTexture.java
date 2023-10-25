/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture
extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;

    public LayeredTexture(String ... textureNames) {
        this.layeredTextureNames = Lists.newArrayList(textureNames);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;
        for (String s : this.layeredTextureNames) {
            IResource iresource;
            block6: {
                iresource = null;
                try {
                    if (s == null) break block6;
                    iresource = resourceManager.getResource(new ResourceLocation(s));
                    BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());
                    if (bufferedimage == null) {
                        bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
                    }
                    bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, null);
                }
                catch (IOException ioexception) {
                    try {
                        LOGGER.error("Couldn't load layered image", (Throwable)ioexception);
                    }
                    catch (Throwable throwable) {
                        IOUtils.closeQuietly(iresource);
                        throw throwable;
                    }
                    IOUtils.closeQuietly((Closeable)iresource);
                }
            }
            IOUtils.closeQuietly(iresource);
            continue;
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }
}

