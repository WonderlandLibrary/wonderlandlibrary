// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import org.lwjgl.opengl.GL11;

public abstract class AbstractTexture implements ITextureObject
{
    protected int glTextureId;
    protected boolean field_174940_b;
    protected boolean field_174941_c;
    protected boolean field_174938_d;
    protected boolean field_174939_e;
    private static final String __OBFID = "CL_00001047";
    
    public AbstractTexture() {
        this.glTextureId = -1;
    }
    
    public void func_174937_a(final boolean p_174937_1_, final boolean p_174937_2_) {
        this.field_174940_b = p_174937_1_;
        this.field_174941_c = p_174937_2_;
        final boolean var3 = true;
        final boolean var4 = true;
        int var5;
        short var6;
        if (p_174937_1_) {
            var5 = (p_174937_2_ ? 9987 : 9729);
            var6 = 9729;
        }
        else {
            var5 = (p_174937_2_ ? 9986 : 9728);
            var6 = 9728;
        }
        GL11.glTexParameteri(3553, 10241, var5);
        GL11.glTexParameteri(3553, 10240, (int)var6);
    }
    
    @Override
    public void func_174936_b(final boolean p_174936_1_, final boolean p_174936_2_) {
        this.field_174938_d = this.field_174940_b;
        this.field_174939_e = this.field_174941_c;
        this.func_174937_a(p_174936_1_, p_174936_2_);
    }
    
    @Override
    public void func_174935_a() {
        this.func_174937_a(this.field_174938_d, this.field_174939_e);
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
