package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import org.lwjgl.util.vector.Matrix4f;

public class Shader {
   private final List listAuxHeights = Lists.newArrayList();
   public final Framebuffer framebufferIn;
   private final List listAuxNames = Lists.newArrayList();
   private final List listAuxWidths = Lists.newArrayList();
   private final ShaderManager manager;
   public final Framebuffer framebufferOut;
   private final List listAuxFramebuffers = Lists.newArrayList();
   private Matrix4f projectionMatrix;

   public void deleteShader() {
      this.manager.deleteShader();
   }

   public void setProjectionMatrix(Matrix4f var1) {
      this.projectionMatrix = var1;
   }

   public void loadShader(float var1) {
      this.preLoadShader();
      this.framebufferIn.unbindFramebuffer();
      float var2 = (float)this.framebufferOut.framebufferTextureWidth;
      float var3 = (float)this.framebufferOut.framebufferTextureHeight;
      GlStateManager.viewport(0, 0, (int)var2, (int)var3);
      this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);

      for(int var4 = 0; var4 < this.listAuxFramebuffers.size(); ++var4) {
         this.manager.addSamplerTexture((String)this.listAuxNames.get(var4), this.listAuxFramebuffers.get(var4));
         this.manager.getShaderUniformOrDefault("AuxSize" + var4).set((float)(Integer)this.listAuxWidths.get(var4), (float)(Integer)this.listAuxHeights.get(var4));
      }

      this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
      this.manager.getShaderUniformOrDefault("InSize").set((float)this.framebufferIn.framebufferTextureWidth, (float)this.framebufferIn.framebufferTextureHeight);
      this.manager.getShaderUniformOrDefault("OutSize").set(var2, var3);
      this.manager.getShaderUniformOrDefault("Time").set(var1);
      Minecraft var9 = Minecraft.getMinecraft();
      this.manager.getShaderUniformOrDefault("ScreenSize").set((float)var9.displayWidth, (float)var9.displayHeight);
      this.manager.useShader();
      this.framebufferOut.framebufferClear();
      this.framebufferOut.bindFramebuffer(false);
      GlStateManager.depthMask(false);
      GlStateManager.colorMask(true, true, true, true);
      Tessellator var5 = Tessellator.getInstance();
      WorldRenderer var6 = var5.getWorldRenderer();
      var6.begin(7, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(0.0D, (double)var3, 500.0D).color(255, 255, 255, 255).endVertex();
      var6.pos((double)var2, (double)var3, 500.0D).color(255, 255, 255, 255).endVertex();
      var6.pos((double)var2, 0.0D, 500.0D).color(255, 255, 255, 255).endVertex();
      var6.pos(0.0D, 0.0D, 500.0D).color(255, 255, 255, 255).endVertex();
      var5.draw();
      GlStateManager.depthMask(true);
      GlStateManager.colorMask(true, true, true, true);
      this.manager.endShader();
      this.framebufferOut.unbindFramebuffer();
      this.framebufferIn.unbindFramebufferTexture();
      Iterator var8 = this.listAuxFramebuffers.iterator();

      while(var8.hasNext()) {
         Object var7 = var8.next();
         if (var7 instanceof Framebuffer) {
            ((Framebuffer)var7).unbindFramebufferTexture();
         }
      }

   }

   public Shader(IResourceManager var1, String var2, Framebuffer var3, Framebuffer var4) throws JsonException, IOException {
      this.manager = new ShaderManager(var1, var2);
      this.framebufferIn = var3;
      this.framebufferOut = var4;
   }

   public ShaderManager getShaderManager() {
      return this.manager;
   }

   private void preLoadShader() {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.disableDepth();
      GlStateManager.disableAlpha();
      GlStateManager.disableFog();
      GlStateManager.disableLighting();
      GlStateManager.disableColorMaterial();
      GlStateManager.enableTexture2D();
      GlStateManager.bindTexture(0);
   }

   public void addAuxFramebuffer(String var1, Object var2, int var3, int var4) {
      this.listAuxNames.add(this.listAuxNames.size(), var1);
      this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), var2);
      this.listAuxWidths.add(this.listAuxWidths.size(), var3);
      this.listAuxHeights.add(this.listAuxHeights.size(), var4);
   }
}
