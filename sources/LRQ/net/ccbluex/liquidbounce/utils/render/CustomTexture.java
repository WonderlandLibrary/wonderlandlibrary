/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class CustomTexture {
    private final BufferedImage image;
    private boolean unloaded;
    private int textureId = -1;

    public CustomTexture(BufferedImage image2) {
        this.image = image2;
    }

    public int getTextureId() {
        if (this.unloaded) {
            throw new IllegalStateException("Texture unloaded");
        }
        if (this.textureId == -1) {
            this.textureId = TextureUtil.func_110989_a((int)TextureUtil.func_110996_a(), (BufferedImage)this.image, (boolean)true, (boolean)true);
        }
        return this.textureId;
    }

    public void unload() {
        if (!this.unloaded) {
            GL11.glDeleteTextures((int)this.textureId);
            this.unloaded = true;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.unload();
    }
}

