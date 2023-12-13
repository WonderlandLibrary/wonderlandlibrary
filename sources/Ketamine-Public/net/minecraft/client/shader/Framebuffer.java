package net.minecraft.client.shader;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

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

    public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_)
    {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        this.framebufferColor = new float[4];
        this.framebufferColor[0] = 1.0F;
        this.framebufferColor[1] = 1.0F;
        this.framebufferColor[2] = 1.0F;
        this.framebufferColor[3] = 0.0F;
        this.createBindFramebuffer(p_i45078_1_, p_i45078_2_);
    }

    public void createBindFramebuffer(int width, int height)
    {
        if (!OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferWidth = width;
            this.framebufferHeight = height;
        }
        else
        {
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            if (this.framebufferObject >= 0)
            {
                this.deleteFramebuffer();
            }

            this.createFramebuffer(width, height);
            this.checkFramebufferComplete();
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }

    public void deleteFramebuffer()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();

            if (this.depthBuffer > -1)
            {
                OpenGlHelper.glDeleteRenderbuffers(this.depthBuffer);
                this.depthBuffer = -1;
            }

            if (this.framebufferTexture > -1)
            {
                TextureUtil.deleteTexture(this.framebufferTexture);
                this.framebufferTexture = -1;
            }

            if (this.framebufferObject > -1)
            {
                OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
                OpenGlHelper.glDeleteFramebuffers(this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }

    public void createFramebuffer(int width, int height)
    {
        this.framebufferWidth = width;
        this.framebufferHeight = height;
        this.framebufferTextureWidth = width;
        this.framebufferTextureHeight = height;

        if (!OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferClear();
        }
        else
        {
            this.framebufferObject = OpenGlHelper.glGenFramebuffers();
            this.framebufferTexture = TextureUtil.glGenTextures();

            if (this.useDepth)
            {
                this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
            }

            this.setFramebufferFilter(9728);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.framebufferTexture);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)((ByteBuffer)null));
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, this.framebufferTexture, 0);

            if (this.useDepth)
            {
                OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
                OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
            }

            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }

    public void setFramebufferFilter(int p_147607_1_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferFilter = p_147607_1_;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.framebufferTexture);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, (float)p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, (float)p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
    }

    public void checkFramebufferComplete()
    {
        int i = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);

        if (i != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE)
        {
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            else if (i == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            }
            else if (i == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            }
            else if (i == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            }
            else
            {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
            }
        }
    }

    public void bindFramebufferTexture()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.framebufferTexture);
        }
    }

    public void unbindFramebufferTexture()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
    }

    public void bindFramebuffer(boolean p_147610_1_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);

            if (p_147610_1_)
            {
                GL11.glViewport(0, 0, this.framebufferWidth, this.framebufferHeight);
            }
        }
    }

    public void unbindFramebuffer()
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }

    public void setFramebufferColor(float p_147604_1_, float p_147604_2_, float p_147604_3_, float p_147604_4_)
    {
        this.framebufferColor[0] = p_147604_1_;
        this.framebufferColor[1] = p_147604_2_;
        this.framebufferColor[2] = p_147604_3_;
        this.framebufferColor[3] = p_147604_4_;
    }

    public void framebufferRender(int p_147615_1_, int p_147615_2_)
    {
        this.framebufferRenderExt(p_147615_1_, p_147615_2_, true);
    }

    public void framebufferRenderExt(int p_178038_1_, int p_178038_2_, boolean p_178038_3_)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            GL11.glColorMask(true, true, true, false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, p_178038_1_, p_178038_2_, 0.0D, 1000.0D, 3000.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glViewport(0, 0, p_178038_1_, p_178038_2_);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.disableAlpha();

            if (p_178038_3_)
            {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindFramebufferTexture();
            float f = (float)p_178038_1_;
            float f1 = (float)p_178038_2_;
            float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
            float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(0.0D, (double)f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos((double)f, (double)f1, 0.0D).tex((double)f2, 0.0D).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos((double)f, 0.0D, 0.0D).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)f3).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.unbindFramebufferTexture();
            GL11.glDepthMask(true);
            GL11.glColorMask(true, true, true, true);
        }
    }

    public void framebufferClear()
    {
        this.bindFramebuffer(true);
        GL11.glClearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
        int i = 16384;

        if (this.useDepth)
        {
            GL11.glClearDepth(1.0D);
            i |= 256;
        }

        GL11.glClear(i);
        this.unbindFramebuffer();
    }
}
