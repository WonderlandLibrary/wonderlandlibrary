package net.minecraft.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

public class LoadingScreenRenderer implements IProgressUpdate {
   private long systemTime = Minecraft.getSystemTime();
   private String message = "";
   private ScaledResolution scaledResolution;
   private String currentlyDisplayedText = "";
   private Minecraft mc;
   private Framebuffer framebuffer;
   private boolean field_73724_e;

   public LoadingScreenRenderer(Minecraft var1) {
      this.mc = var1;
      this.scaledResolution = new ScaledResolution(var1);
      this.framebuffer = new Framebuffer(var1.displayWidth, var1.displayHeight, false);
      this.framebuffer.setFramebufferFilter(9728);
   }

   public void displaySavingString(String var1) {
      this.field_73724_e = true;
      this.displayString(var1);
   }

   public void setLoadingProgress(int var1) {
      if (!this.mc.running) {
         if (!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         long var2 = Minecraft.getSystemTime();
         if (var2 - this.systemTime >= 100L) {
            this.systemTime = var2;
            ScaledResolution var4 = new ScaledResolution(this.mc);
            int var5 = var4.getScaleFactor();
            int var6 = var4.getScaledWidth();
            int var7 = ScaledResolution.getScaledHeight();
            if (OpenGlHelper.isFramebufferEnabled()) {
               this.framebuffer.framebufferClear();
            } else {
               GlStateManager.clear(256);
            }

            this.framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
            if (!OpenGlHelper.isFramebufferEnabled()) {
               GlStateManager.clear(16640);
            }

            Tessellator var8 = Tessellator.getInstance();
            WorldRenderer var9 = var8.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            float var10 = 32.0F;
            var9.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var9.pos(0.0D, (double)var7, 0.0D).tex(0.0D, (double)((float)var7 / var10)).color(64, 64, 64, 255).endVertex();
            var9.pos((double)var6, (double)var7, 0.0D).tex((double)((float)var6 / var10), (double)((float)var7 / var10)).color(64, 64, 64, 255).endVertex();
            var9.pos((double)var6, 0.0D, 0.0D).tex((double)((float)var6 / var10), 0.0D).color(64, 64, 64, 255).endVertex();
            var9.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
            var8.draw();
            if (var1 >= 0) {
               byte var11 = 100;
               byte var12 = 2;
               int var13 = var6 / 2 - var11 / 2;
               int var14 = var7 / 2 + 16;
               GlStateManager.disableTexture2D();
               var9.begin(7, DefaultVertexFormats.POSITION_COLOR);
               var9.pos((double)var13, (double)var14, 0.0D).color(128, 128, 128, 255).endVertex();
               var9.pos((double)var13, (double)(var14 + var12), 0.0D).color(128, 128, 128, 255).endVertex();
               var9.pos((double)(var13 + var11), (double)(var14 + var12), 0.0D).color(128, 128, 128, 255).endVertex();
               var9.pos((double)(var13 + var11), (double)var14, 0.0D).color(128, 128, 128, 255).endVertex();
               var9.pos((double)var13, (double)var14, 0.0D).color(128, 255, 128, 255).endVertex();
               var9.pos((double)var13, (double)(var14 + var12), 0.0D).color(128, 255, 128, 255).endVertex();
               var9.pos((double)(var13 + var1), (double)(var14 + var12), 0.0D).color(128, 255, 128, 255).endVertex();
               var9.pos((double)(var13 + var1), (double)var14, 0.0D).color(128, 255, 128, 255).endVertex();
               var8.draw();
               GlStateManager.enableTexture2D();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Minecraft.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (float)((var6 - Minecraft.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2), (float)(var7 / 2 - 4 - 16), 16777215);
            Minecraft.fontRendererObj.drawStringWithShadow(this.message, (float)((var6 - Minecraft.fontRendererObj.getStringWidth(this.message)) / 2), (float)(var7 / 2 - 4 + 8), 16777215);
            this.framebuffer.unbindFramebuffer();
            if (OpenGlHelper.isFramebufferEnabled()) {
               this.framebuffer.framebufferRender(var6 * var5, var7 * var5);
            }

            this.mc.updateDisplay();

            try {
               Thread.yield();
            } catch (Exception var15) {
            }
         }
      }

   }

   public void resetProgressAndMessage(String var1) {
      this.field_73724_e = false;
      this.displayString(var1);
   }

   private void displayString(String var1) {
      this.currentlyDisplayedText = var1;
      if (!this.mc.running) {
         if (!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         if (OpenGlHelper.isFramebufferEnabled()) {
            int var2 = this.scaledResolution.getScaleFactor();
            GlStateManager.ortho(0.0D, (double)(this.scaledResolution.getScaledWidth() * var2), (double)(ScaledResolution.getScaledHeight() * var2), 0.0D, 100.0D, 300.0D);
         } else {
            ScaledResolution var4 = new ScaledResolution(this.mc);
            GlStateManager.ortho(0.0D, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
         }

         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -200.0F);
      }

   }

   public void displayLoadingString(String var1) {
      if (!this.mc.running) {
         if (!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         this.systemTime = 0L;
         this.message = var1;
         this.setLoadingProgress(-1);
         this.systemTime = 0L;
      }

   }

   public void setDoneWorking() {
   }
}
