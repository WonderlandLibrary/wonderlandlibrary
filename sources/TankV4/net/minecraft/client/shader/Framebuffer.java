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
   public int framebufferWidth;
   public float[] framebufferColor;
   public boolean useDepth;
   public int framebufferFilter;
   public int framebufferHeight;
   public int framebufferTextureHeight;
   public int framebufferTexture;
   public int depthBuffer;
   public int framebufferObject;
   public int framebufferTextureWidth;

   public void unbindFramebufferTexture() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.bindTexture(0);
      }

   }

   public void unbindFramebuffer() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
      }

   }

   public void checkFramebufferComplete() {
      int var1 = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
      if (var1 != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
         if (var1 == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
         } else if (var1 == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
         } else if (var1 == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
         } else if (var1 == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
         } else {
            throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + var1);
         }
      }
   }

   public void createBindFramebuffer(int var1, int var2) {
      if (!OpenGlHelper.isFramebufferEnabled()) {
         this.framebufferWidth = var1;
         this.framebufferHeight = var2;
      } else {
         GlStateManager.enableDepth();
         if (this.framebufferObject >= 0) {
            this.deleteFramebuffer();
         }

         this.createFramebuffer(var1, var2);
         this.checkFramebufferComplete();
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
      }

   }

   public void deleteFramebuffer() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         this.unbindFramebufferTexture();
         this.unbindFramebuffer();
         if (this.depthBuffer > -1) {
            OpenGlHelper.glDeleteRenderbuffers(this.depthBuffer);
            this.depthBuffer = -1;
         }

         if (this.framebufferTexture > -1) {
            TextureUtil.deleteTexture(this.framebufferTexture);
            this.framebufferTexture = -1;
         }

         if (this.framebufferObject > -1) {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
            OpenGlHelper.glDeleteFramebuffers(this.framebufferObject);
            this.framebufferObject = -1;
         }
      }

   }

   public void setFramebufferFilter(int var1) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         this.framebufferFilter = var1;
         GlStateManager.bindTexture(this.framebufferTexture);
         GL11.glTexParameterf(3553, 10241, (float)var1);
         GL11.glTexParameterf(3553, 10240, (float)var1);
         GL11.glTexParameterf(3553, 10242, 10496.0F);
         GL11.glTexParameterf(3553, 10243, 10496.0F);
         GlStateManager.bindTexture(0);
      }

   }

   public void setFramebufferColor(float var1, float var2, float var3, float var4) {
      this.framebufferColor[0] = var1;
      this.framebufferColor[1] = var2;
      this.framebufferColor[2] = var3;
      this.framebufferColor[3] = var4;
   }

   public void framebufferClear() {
      this.bindFramebuffer(true);
      GlStateManager.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
      int var1 = 16384;
      if (this.useDepth) {
         GlStateManager.clearDepth(1.0D);
         var1 |= 256;
      }

      GlStateManager.clear(var1);
      this.unbindFramebuffer();
   }

   public void framebufferRender(int var1, int var2) {
      this.framebufferRenderExt(var1, var2, true);
   }

   public void framebufferRenderExt(int var1, int var2, boolean var3) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.colorMask(true, true, true, false);
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         GlStateManager.ortho(0.0D, (double)var1, (double)var2, 0.0D, 1000.0D, 3000.0D);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
         GlStateManager.viewport(0, 0, var1, var2);
         GlStateManager.enableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.disableAlpha();
         if (var3) {
            GlStateManager.disableBlend();
            GlStateManager.enableColorMaterial();
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.bindFramebufferTexture();
         float var4 = (float)var1;
         float var5 = (float)var2;
         float var6 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
         float var7 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
         Tessellator var8 = Tessellator.getInstance();
         WorldRenderer var9 = var8.getWorldRenderer();
         var9.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         var9.pos(0.0D, (double)var5, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
         var9.pos((double)var4, (double)var5, 0.0D).tex((double)var6, 0.0D).color(255, 255, 255, 255).endVertex();
         var9.pos((double)var4, 0.0D, 0.0D).tex((double)var6, (double)var7).color(255, 255, 255, 255).endVertex();
         var9.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)var7).color(255, 255, 255, 255).endVertex();
         var8.draw();
         this.unbindFramebufferTexture();
         GlStateManager.depthMask(true);
         GlStateManager.colorMask(true, true, true, true);
      }

   }

   public Framebuffer(int var1, int var2, boolean var3) {
      this.useDepth = var3;
      this.framebufferObject = -1;
      this.framebufferTexture = -1;
      this.depthBuffer = -1;
      this.framebufferColor = new float[4];
      this.framebufferColor[0] = 1.0F;
      this.framebufferColor[1] = 1.0F;
      this.framebufferColor[2] = 1.0F;
      this.framebufferColor[3] = 0.0F;
      this.createBindFramebuffer(var1, var2);
   }

   public void bindFramebuffer(boolean var1) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
         if (var1) {
            GlStateManager.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
         }
      }

   }

   public void createFramebuffer(int var1, int var2) {
      this.framebufferWidth = var1;
      this.framebufferHeight = var2;
      this.framebufferTextureWidth = var1;
      this.framebufferTextureHeight = var2;
      if (!OpenGlHelper.isFramebufferEnabled()) {
         this.framebufferClear();
      } else {
         this.framebufferObject = OpenGlHelper.glGenFramebuffers();
         this.framebufferTexture = TextureUtil.glGenTextures();
         if (this.useDepth) {
            this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
         }

         this.setFramebufferFilter(9728);
         GlStateManager.bindTexture(this.framebufferTexture);
         GL11.glTexImage2D(3553, 0, 32856, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, 6408, 5121, (ByteBuffer)null);
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
         OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, this.framebufferTexture, 0);
         if (this.useDepth) {
            OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
            OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
            OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
         }

         this.framebufferClear();
         this.unbindFramebufferTexture();
      }

   }

   public void bindFramebufferTexture() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.bindTexture(this.framebufferTexture);
      }

   }
}
