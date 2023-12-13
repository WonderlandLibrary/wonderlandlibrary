/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import shadersmod.client.MultiTexID;
import shadersmod.client.ShadersTex;

public abstract class AbstractTexture
implements ITextureObject {
    protected int glTextureId = -1;
    protected boolean field_174940_b;
    protected boolean field_174941_c;
    protected boolean field_174938_d;
    protected boolean field_174939_e;
    private static final String __OBFID = "CL_00001047";
    public MultiTexID multiTex;

    public void func_174937_a(boolean p_174937_1_, boolean p_174937_2_) {
        int var6;
        int var5;
        this.field_174940_b = p_174937_1_;
        this.field_174941_c = p_174937_2_;
        boolean var3 = true;
        boolean var4 = true;
        if (p_174937_1_) {
            var5 = p_174937_2_ ? 9987 : 9729;
            var6 = 9729;
        } else {
            var5 = p_174937_2_ ? 9986 : 9728;
            var6 = 9728;
        }
        GlStateManager.func_179144_i(this.getGlTextureId());
        GL11.glTexParameteri((int)3553, (int)10241, (int)var5);
        GL11.glTexParameteri((int)3553, (int)10240, (int)var6);
    }

    @Override
    public void func_174936_b(boolean p_174936_1_, boolean p_174936_2_) {
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

