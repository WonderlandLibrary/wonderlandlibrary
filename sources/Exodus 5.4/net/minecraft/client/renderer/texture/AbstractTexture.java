/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public abstract class AbstractTexture
implements ITextureObject {
    protected boolean mipmapLast;
    protected boolean blurLast;
    protected boolean blur;
    protected boolean mipmap;
    protected int glTextureId = -1;

    public void setBlurMipmapDirect(boolean bl, boolean bl2) {
        this.blur = bl;
        this.mipmap = bl2;
        int n = -1;
        int n2 = -1;
        if (bl) {
            n = bl2 ? 9987 : 9729;
            n2 = 9729;
        } else {
            n = bl2 ? 9986 : 9728;
            n2 = 9728;
        }
        GL11.glTexParameteri((int)3553, (int)10241, (int)n);
        GL11.glTexParameteri((int)3553, (int)10240, (int)n2);
    }

    @Override
    public void setBlurMipmap(boolean bl, boolean bl2) {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(bl, bl2);
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

