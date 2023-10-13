/*
 * Decompiled with CFR 0.150.
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
    private static final Logger logger = LogManager.getLogger();
    public final List layeredTextureNames;
    private static final String __OBFID = "CL_00001051";

    public LayeredTexture(String ... p_i1274_1_) {
        this.layeredTextureNames = Lists.newArrayList((Object[])p_i1274_1_);
    }

    @Override
    public void loadTexture(IResourceManager p_110551_1_) throws IOException {
        this.deleteGlTexture();
        BufferedImage var2 = null;
        try {
            for (String var4 : this.layeredTextureNames) {
                if (var4 == null) continue;
                InputStream var5 = p_110551_1_.getResource(new ResourceLocation(var4)).getInputStream();
                BufferedImage var6 = TextureUtil.func_177053_a(var5);
                if (var2 == null) {
                    var2 = new BufferedImage(var6.getWidth(), var6.getHeight(), 2);
                }
                var2.getGraphics().drawImage(var6, 0, 0, null);
            }
        }
        catch (IOException var7) {
            logger.error("Couldn't load layered image", (Throwable)var7);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), var2);
    }
}

