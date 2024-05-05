package net.minecraft.client.shader;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Framebuffer {
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

    public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_) {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        this.framebufferColor = new float[4];
        this.framebufferColor[0] = 1.0f;
        this.framebufferColor[1] = 1.0f;
        this.framebufferColor[2] = 1.0f;
        this.framebufferColor[3] = 0.0f;
        this.createBindFramebuffer(p_i45078_1_, p_i45078_2_);
    }

    public void createBindFramebuffer(int width, int height) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferWidth = width;
            this.framebufferHeight = height;
        } else {
            GlStateManager.enableDepth();
            if (this.framebufferObject >= 0) {
                this.deleteFramebuffer();
            }
            this.createFramebuffer(width, height);
            this.checkFramebufferComplete();
            OpenGlHelper.glBindFramebuffer((int)OpenGlHelper.GL_FRAMEBUFFER, (int)0);
        }
    }

    public void deleteFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();
            if (this.depthBuffer > -1) {
                OpenGlHelper.glDeleteRenderbuffers((int)this.depthBuffer);
                this.depthBuffer = -1;
            }
            if (this.framebufferTexture > -1) {
                TextureUtil.deleteTexture((int)this.framebufferTexture);
                this.framebufferTexture = -1;
            }
            if (this.framebufferObject > -1) {
                OpenGlHelper.glBindFramebuffer((int)OpenGlHelper.GL_FRAMEBUFFER, (int)0);
                OpenGlHelper.glDeleteFramebuffers((int)this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }

    public void createFramebuffer(int width, int height) {
        this.framebufferWidth = width;
        this.framebufferHeight = height;
        this.framebufferTextureWidth = width;
        this.framebufferTextureHeight = height;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferClear();
        } else {
            this.framebufferObject = OpenGlHelper.glGenFramebuffers();
            this.framebufferTexture = TextureUtil.glGenTextures();
            if (this.useDepth) {
                this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
            }
            this.setFramebufferFilter(9728);
            GlStateManager.bindTexture((int)this.framebufferTexture);
            GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)this.framebufferTextureWidth, (int)this.framebufferTextureHeight, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
            OpenGlHelper.glBindFramebuffer((int)OpenGlHelper.GL_FRAMEBUFFER, (int)this.framebufferObject);
            OpenGlHelper.glFramebufferTexture2D((int)OpenGlHelper.GL_FRAMEBUFFER, (int)OpenGlHelper.GL_COLOR_ATTACHMENT0, (int)3553, (int)this.framebufferTexture, (int)0);
            if (this.useDepth) {
                OpenGlHelper.glBindRenderbuffer((int)OpenGlHelper.GL_RENDERBUFFER, (int)this.depthBuffer);
                OpenGlHelper.glRenderbufferStorage((int)OpenGlHelper.GL_RENDERBUFFER, (int)33190, (int)this.framebufferTextureWidth, (int)this.framebufferTextureHeight);
                OpenGlHelper.glFramebufferRenderbuffer((int)OpenGlHelper.GL_FRAMEBUFFER, (int)OpenGlHelper.GL_DEPTH_ATTACHMENT, (int)OpenGlHelper.GL_RENDERBUFFER, (int)this.depthBuffer);
            }
            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }

    public void setFramebufferFilter(int p_147607_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferFilter = p_147607_1_;
            GlStateManager.bindTexture((int)this.framebufferTexture);
            GL11.glTexParameterf((int)3553, (int)10241, (float)p_147607_1_);
            GL11.glTexParameterf((int)3553, (int)10240, (float)p_147607_1_);
            GL11.glTexParameterf((int)3553, (int)10242, (float)10496.0f);
            GL11.glTexParameterf((int)3553, (int)10243, (float)10496.0f);
            GlStateManager.bindTexture((int)0);
        }
    }

    public void checkFramebufferComplete() {
        int i = OpenGlHelper.glCheckFramebufferStatus((int)OpenGlHelper.GL_FRAMEBUFFER);
        if (i != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            }
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            }
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            }
            throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
        }
    }

    public void bindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture((int)this.framebufferTexture);
        }
    }

    public void unbindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture((int)0);
        }
    }

    public void bindFramebuffer(boolean p_147610_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.glBindFramebuffer((int)OpenGlHelper.GL_FRAMEBUFFER, (int)this.framebufferObject);
            if (p_147610_1_) {
                GlStateManager.viewport((int)0, (int)0, (int)this.framebufferWidth, (int)this.framebufferHeight);
            }
        }
    }

    public void unbindFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.glBindFramebuffer((int)OpenGlHelper.GL_FRAMEBUFFER, (int)0);
        }
    }

    public void setFramebufferColor(float p_147604_1_, float p_147604_2_, float p_147604_3_, float p_147604_4_) {
        this.framebufferColor[0] = p_147604_1_;
        this.framebufferColor[1] = p_147604_2_;
        this.framebufferColor[2] = p_147604_3_;
        this.framebufferColor[3] = p_147604_4_;
    }

    public void framebufferRender(int p_147615_1_, int p_147615_2_) {
        this.framebufferRenderExt(p_147615_1_, p_147615_2_, true);
    }

    public void framebufferRenderExt(int p_178038_1_, int p_178038_2_, boolean p_178038_3_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
            GlStateManager.disableDepth();
            GlStateManager.depthMask((boolean)false);
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho((double)0.0, (double)p_178038_1_, (double)p_178038_2_, (double)0.0, (double)1000.0, (double)3000.0);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)-2000.0f);
            GlStateManager.viewport((int)0, (int)0, (int)p_178038_1_, (int)p_178038_2_);
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            if (p_178038_3_) {
                GlStateManager.disableBlend();
                GlStateManager.enableColorMaterial();
            }
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.bindFramebufferTexture();
            float f = p_178038_1_;
            float f1 = p_178038_2_;
            float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
            float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(0.0, (double)f1, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos((double)f, (double)f1, 0.0).tex((double)f2, 0.0).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos((double)f, 0.0, 0.0).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, (double)f3).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.unbindFramebufferTexture();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        }
    }

    public void framebufferClear() {
        this.bindFramebuffer(true);
        GlStateManager.clearColor((float)this.framebufferColor[0], (float)this.framebufferColor[1], (float)this.framebufferColor[2], (float)this.framebufferColor[3]);
        int i = 16384;
        if (this.useDepth) {
            GlStateManager.clearDepth((double)1.0);
            i |= 0x100;
        }
        GlStateManager.clear((int)i);
        this.unbindFramebuffer();
    }

    public void checkSetup(int width, int height) {
        if (this.framebufferHeight != height || this.framebufferWidth != width) {
            this.deleteFramebuffer();
            this.createFramebuffer(width, height);
        }
    }
}
