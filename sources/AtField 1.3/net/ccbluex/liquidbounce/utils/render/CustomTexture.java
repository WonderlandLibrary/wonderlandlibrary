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
    private boolean unloaded;
    private int textureId = -1;
    private final BufferedImage image;

    public CustomTexture(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.unload();
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
}

