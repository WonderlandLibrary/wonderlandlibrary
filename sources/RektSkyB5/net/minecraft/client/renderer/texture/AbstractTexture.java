/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public abstract class AbstractTexture
implements ITextureObject {
    protected int glTextureId = -1;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;

    public void setBlurMipmapDirect(boolean p_174937_1_, boolean p_174937_2_) {
        this.blur = p_174937_1_;
        this.mipmap = p_174937_2_;
        int i2 = -1;
        int j2 = -1;
        if (p_174937_1_) {
            i2 = p_174937_2_ ? 9987 : 9729;
            j2 = 9729;
        } else {
            i2 = p_174937_2_ ? 9986 : 9728;
            j2 = 9728;
        }
        GL11.glTexParameteri(3553, 10241, i2);
        GL11.glTexParameteri(3553, 10240, j2);
    }

    @Override
    public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_) {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(p_174936_1_, p_174936_2_);
    }

    @Override
    public void restoreLastBlurMipmap() {
        this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
    }

    @Override
    public int getGlTextureId() {
        if (this.glTextureId == -1) {
            this.glTextureId = TextureUtil.glGenTextures();
        }
        return this.glTextureId;
    }

    public void deleteGlTexture() {
        if (this.glTextureId != -1) {
            TextureUtil.deleteTexture(this.glTextureId);
            this.glTextureId = -1;
        }
    }
}

