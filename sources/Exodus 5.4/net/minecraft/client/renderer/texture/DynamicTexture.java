/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class DynamicTexture
extends AbstractTexture {
    private final int height;
    private final int width;
    private final int[] dynamicTextureData;

    public void updateDynamicTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public DynamicTexture(int n, int n2) {
        this.width = n;
        this.height = n2;
        this.dynamicTextureData = new int[n * n2];
        TextureUtil.allocateTexture(this.getGlTextureId(), n, n2);
    }

    public int[] getTextureData() {
        return this.dynamicTextureData;
    }

    public DynamicTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
        this.updateDynamicTexture();
    }

    @Override
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
    }
}

