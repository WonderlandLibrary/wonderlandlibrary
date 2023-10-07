// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import java.awt.image.BufferedImage;

public class DynamicTexture extends AbstractTexture
{
    private final int[] dynamicTextureData;
    private final int width;
    private final int height;
    private static final String __OBFID = "CL_00001048";
    
    public DynamicTexture(final BufferedImage p_i1270_1_) {
        this(p_i1270_1_.getWidth(), p_i1270_1_.getHeight());
        p_i1270_1_.getRGB(0, 0, p_i1270_1_.getWidth(), p_i1270_1_.getHeight(), this.dynamicTextureData, 0, p_i1270_1_.getWidth());
        this.updateDynamicTexture();
    }
    
    public DynamicTexture(final int p_i1271_1_, final int p_i1271_2_) {
        this.width = p_i1271_1_;
        this.height = p_i1271_2_;
        this.dynamicTextureData = new int[p_i1271_1_ * p_i1271_2_];
        TextureUtil.allocateTexture(this.getGlTextureId(), p_i1271_1_, p_i1271_2_);
    }
    
    @Override
    public void loadTexture(final IResourceManager p_110551_1_) throws IOException {
    }
    
    public void updateDynamicTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }
    
    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}
