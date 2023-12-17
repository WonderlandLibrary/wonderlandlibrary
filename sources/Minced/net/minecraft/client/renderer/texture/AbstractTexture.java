// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.optifine.shaders.ShadersTex;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.MultiTexID;

public abstract class AbstractTexture implements ITextureObject
{
    protected int glTextureId;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;
    public MultiTexID multiTex;
    
    public AbstractTexture() {
        this.glTextureId = -1;
    }
    
    public void setBlurMipmapDirect(final boolean blurIn, final boolean mipmapIn) {
        this.blur = blurIn;
        this.mipmap = mipmapIn;
        int i;
        int j;
        if (blurIn) {
            i = (mipmapIn ? 9987 : 9729);
            j = 9729;
        }
        else {
            i = (mipmapIn ? 9986 : 9728);
            j = 9728;
        }
        GlStateManager.bindTexture(this.getGlTextureId());
        GlStateManager.glTexParameteri(3553, 10241, i);
        GlStateManager.glTexParameteri(3553, 10240, j);
    }
    
    @Override
    public void setBlurMipmap(final boolean blurIn, final boolean mipmapIn) {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(blurIn, mipmapIn);
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
        ShadersTex.deleteTextures(this, this.glTextureId);
        if (this.glTextureId != -1) {
            TextureUtil.deleteTexture(this.glTextureId);
            this.glTextureId = -1;
        }
    }
    
    @Override
    public MultiTexID getMultiTexID() {
        return ShadersTex.getMultiTexID(this);
    }
}
