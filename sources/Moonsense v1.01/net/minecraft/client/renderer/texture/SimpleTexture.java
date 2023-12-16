// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResource;
import java.io.InputStream;
import shadersmod.client.ShadersTex;
import optifine.Config;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.IResourceManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture
{
    private static final Logger logger;
    protected final ResourceLocation textureLocation;
    private static final String __OBFID = "CL_00001052";
    
    static {
        logger = LogManager.getLogger();
    }
    
    public SimpleTexture(final ResourceLocation p_i1275_1_) {
        this.textureLocation = p_i1275_1_;
    }
    
    @Override
    public void loadTexture(final IResourceManager p_110551_1_) throws IOException {
        this.deleteGlTexture();
        InputStream var2 = null;
        try {
            final IResource var3 = p_110551_1_.getResource(this.textureLocation);
            var2 = var3.getInputStream();
            final BufferedImage var4 = TextureUtil.func_177053_a(var2);
            boolean var5 = false;
            boolean var6 = false;
            if (var3.hasMetadata()) {
                try {
                    final TextureMetadataSection var7 = (TextureMetadataSection)var3.getMetadata("texture");
                    if (var7 != null) {
                        var5 = var7.getTextureBlur();
                        var6 = var7.getTextureClamp();
                    }
                }
                catch (RuntimeException var8) {
                    SimpleTexture.logger.warn("Failed reading metadata of: " + this.textureLocation, (Throwable)var8);
                }
            }
            if (Config.isShaders()) {
                ShadersTex.loadSimpleTexture(this.getGlTextureId(), var4, var5, var6, p_110551_1_, this.textureLocation, this.getMultiTexID());
            }
            else {
                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), var4, var5, var6);
            }
        }
        finally {
            if (var2 != null) {
                var2.close();
            }
        }
        if (var2 != null) {
            var2.close();
        }
    }
}
