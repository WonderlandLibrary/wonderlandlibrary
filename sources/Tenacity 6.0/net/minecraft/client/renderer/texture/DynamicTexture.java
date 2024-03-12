// 
// Decompiled by Procyon v0.5.36
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
    
    public DynamicTexture(final BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
        this.updateDynamicTexture();
    }
    
    public DynamicTexture(final int textureWidth, final int textureHeight) {
        this.width = textureWidth;
        this.height = textureHeight;
        this.dynamicTextureData = new int[textureWidth * textureHeight];
        TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
    }
    
    public void updateDynamicTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }
    
    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}
