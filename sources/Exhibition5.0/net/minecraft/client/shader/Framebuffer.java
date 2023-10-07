// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.shader;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

public class Framebuffer
{
    public int framebufferTextureWidth;
    public int framebufferTextureHeight;
    public int framebufferWidth;
    public int framebufferHeight;
    public boolean useDepth;
    public int framebufferObject;
    public int framebufferTexture;
    public int depthBuffer;
    public float[] framebufferColor;
    public int framebufferFilter;
    private static final String __OBFID = "CL_00000959";
    
    public Framebuffer(final int p_i45078_1_, final int p_i45078_2_, final boolean p_i45078_3_) {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        (this.framebufferColor = new float[4])[0] = 1.0f;
        this.framebufferColor[1] = 1.0f;
        this.framebufferColor[2] = 1.0f;
        this.framebufferColor[3] = 0.0f;
        this.createBindFramebuffer(p_i45078_1_, p_i45078_2_);
    }
    
    public void createBindFramebuffer(final int p_147613_1_, final int p_147613_2_) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferWidth = p_147613_1_;
            this.framebufferHeight = p_147613_2_;
        }
        else {
            GlStateManager.enableDepth();
            if (this.framebufferObject >= 0) {
                this.deleteFramebuffer();
            }
            this.createFramebuffer(p_147613_1_, p_147613_2_);
            this.checkFramebufferComplete();
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
        }
    }
    
    public void deleteFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();
            if (this.depthBuffer > -1) {
                OpenGlHelper.func_153184_g(this.depthBuffer);
                this.depthBuffer = -1;
            }
            if (this.framebufferTexture > -1) {
                TextureUtil.deleteTexture(this.framebufferTexture);
                this.framebufferTexture = -1;
            }
            if (this.framebufferObject > -1) {
                OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
                OpenGlHelper.func_153174_h(this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }
    
    public void createFramebuffer(final int p_147605_1_, final int p_147605_2_) {
        this.framebufferWidth = p_147605_1_;
        this.framebufferHeight = p_147605_2_;
        this.framebufferTextureWidth = p_147605_1_;
        this.framebufferTextureHeight = p_147605_2_;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferClear();
        }
        else {
            this.framebufferObject = OpenGlHelper.func_153165_e();
            this.framebufferTexture = TextureUtil.glGenTextures();
            if (this.useDepth) {
                this.depthBuffer = OpenGlHelper.func_153185_f();
            }
            this.setFramebufferFilter(9728);
            GlStateManager.func_179144_i(this.framebufferTexture);
            GL11.glTexImage2D(3553, 0, 32856, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, 6408, 5121, (ByteBuffer)null);
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);
            OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, this.framebufferTexture, 0);
            if (this.useDepth) {
                OpenGlHelper.func_153176_h(OpenGlHelper.field_153199_f, this.depthBuffer);
                OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e, OpenGlHelper.field_153201_h, OpenGlHelper.field_153199_f, this.depthBuffer);
            }
            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }
    
    public void setFramebufferFilter(final int p_147607_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferFilter = p_147607_1_;
            GlStateManager.func_179144_i(this.framebufferTexture);
            GL11.glTexParameterf(3553, 10241, (float)p_147607_1_);
            GL11.glTexParameterf(3553, 10240, (float)p_147607_1_);
            GL11.glTexParameterf(3553, 10242, 10496.0f);
            GL11.glTexParameterf(3553, 10243, 10496.0f);
            GlStateManager.func_179144_i(0);
        }
    }
    
    public void checkFramebufferComplete() {
        final int var1 = OpenGlHelper.func_153167_i(OpenGlHelper.field_153198_e);
        if (var1 == OpenGlHelper.field_153202_i) {
            return;
        }
        if (var1 == OpenGlHelper.field_153203_j) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
        }
        if (var1 == OpenGlHelper.field_153204_k) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
        }
        if (var1 == OpenGlHelper.field_153205_l) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
        }
        if (var1 == OpenGlHelper.field_153206_m) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
        }
        throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + var1);
    }
    
    public void bindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.func_179144_i(this.framebufferTexture);
        }
    }
    
    public void unbindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.func_179144_i(0);
        }
    }
    
    public void bindFramebuffer(final boolean p_147610_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);
            if (p_147610_1_) {
                GlStateManager.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
            }
        }
    }
    
    public void unbindFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
        }
    }
    
    public void setFramebufferColor(final float p_147604_1_, final float p_147604_2_, final float p_147604_3_, final float p_147604_4_) {
        this.framebufferColor[0] = p_147604_1_;
        this.framebufferColor[1] = p_147604_2_;
        this.framebufferColor[2] = p_147604_3_;
        this.framebufferColor[3] = p_147604_4_;
    }
    
    public void framebufferRender(final int p_147615_1_, final int p_147615_2_) {
        this.func_178038_a(p_147615_1_, p_147615_2_, true);
    }
    
    public void func_178038_a(final int p_178038_1_, final int p_178038_2_, final boolean p_178038_3_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, p_178038_1_, p_178038_2_, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GlStateManager.viewport(0, 0, p_178038_1_, p_178038_2_);
            GlStateManager.enableTextures();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            if (p_178038_3_) {
                GlStateManager.disableBlend();
                GlStateManager.enableColorMaterial();
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindFramebufferTexture();
            final float var4 = p_178038_1_;
            final float var5 = p_178038_2_;
            final float var6 = this.framebufferWidth / this.framebufferTextureWidth;
            final float var7 = this.framebufferHeight / this.framebufferTextureHeight;
            final Tessellator var8 = Tessellator.getInstance();
            final WorldRenderer var9 = var8.getWorldRenderer();
            var9.startDrawingQuads();
            final int alpha = 255;
            final int red = 255;
            final int blue = 255;
            final int green = 255;
            int color = 0;
            color |= alpha << 24;
            color |= red << 16;
            color |= green << 8;
            color |= blue;
            var9.drawColor(color, alpha);
            var9.addVertexWithUV(0.0, var5, 0.0, 0.0, 0.0);
            var9.addVertexWithUV(var4, var5, 0.0, var6, 0.0);
            var9.addVertexWithUV(var4, 0.0, 0.0, var6, var7);
            var9.addVertexWithUV(0.0, 0.0, 0.0, 0.0, var7);
            var8.draw();
            this.unbindFramebufferTexture();
            GlStateManager.depthMask(true);
        }
    }
    
    public void framebufferClear() {
        this.bindFramebuffer(true);
        GlStateManager.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
        int var1 = 16384;
        if (this.useDepth) {
            GlStateManager.clearDepth(1.0);
            var1 |= 0x100;
        }
        GlStateManager.clear(var1);
        this.unbindFramebuffer();
    }
}
