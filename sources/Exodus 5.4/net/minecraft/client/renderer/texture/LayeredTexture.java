/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture
extends AbstractTexture {
    public final List<String> layeredTextureNames;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedImage = null;
        try {
            for (String string : this.layeredTextureNames) {
                if (string == null) continue;
                InputStream inputStream = iResourceManager.getResource(new ResourceLocation(string)).getInputStream();
                BufferedImage bufferedImage2 = TextureUtil.readBufferedImage(inputStream);
                if (bufferedImage == null) {
                    bufferedImage = new BufferedImage(bufferedImage2.getWidth(), bufferedImage2.getHeight(), 2);
                }
                bufferedImage.getGraphics().drawImage(bufferedImage2, 0, 0, null);
            }
        }
        catch (IOException iOException) {
            logger.error("Couldn't load layered image", (Throwable)iOException);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedImage);
    }

    public LayeredTexture(String ... stringArray) {
        this.layeredTextureNames = Lists.newArrayList((Object[])stringArray);
    }
}

