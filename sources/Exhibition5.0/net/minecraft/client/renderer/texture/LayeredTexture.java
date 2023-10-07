// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture
{
    private static final Logger logger;
    public final List layeredTextureNames;
    private static final String __OBFID = "CL_00001051";
    
    public LayeredTexture(final String... p_i1274_1_) {
        this.layeredTextureNames = Lists.newArrayList((Object[])p_i1274_1_);
    }
    
    @Override
    public void loadTexture(final IResourceManager p_110551_1_) throws IOException {
        this.deleteGlTexture();
        BufferedImage var2 = null;
        try {
            for (final String var4 : this.layeredTextureNames) {
                if (var4 != null) {
                    final InputStream var5 = p_110551_1_.getResource(new ResourceLocation(var4)).getInputStream();
                    final BufferedImage var6 = TextureUtil.func_177053_a(var5);
                    if (var2 == null) {
                        var2 = new BufferedImage(var6.getWidth(), var6.getHeight(), 2);
                    }
                    var2.getGraphics().drawImage(var6, 0, 0, null);
                }
            }
        }
        catch (IOException var7) {
            LayeredTexture.logger.error("Couldn't load layered image", (Throwable)var7);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), var2);
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
