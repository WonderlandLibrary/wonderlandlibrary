package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiStreamIndicator {
   private static final ResourceLocation locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
   private final Minecraft mc;
   private float field_152443_c = 1.0F;
   private int field_152444_d = 1;

   private int func_152438_c() {
      return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
   }

   private int func_152440_b() {
      return this.mc.getTwitchStream().isPaused() ? 16 : 0;
   }

   private void render(int var1, int var2, int var3, int var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * this.field_152443_c);
      this.mc.getTextureManager().bindTexture(locationStreamIndicator);
      float var5 = 150.0F;
      float var6 = 0.0F;
      float var7 = (float)var3 * 0.015625F;
      float var8 = 1.0F;
      float var9 = (float)(var3 + 16) * 0.015625F;
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      var11.begin(7, DefaultVertexFormats.POSITION_TEX);
      var11.pos((double)(var1 - 16 - var4), (double)(var2 + 16), (double)var5).tex((double)var6, (double)var9).endVertex();
      var11.pos((double)(var1 - var4), (double)(var2 + 16), (double)var5).tex((double)var8, (double)var9).endVertex();
      var11.pos((double)(var1 - var4), (double)(var2 + 0), (double)var5).tex((double)var8, (double)var7).endVertex();
      var11.pos((double)(var1 - 16 - var4), (double)(var2 + 0), (double)var5).tex((double)var6, (double)var7).endVertex();
      var10.draw();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public GuiStreamIndicator(Minecraft var1) {
      this.mc = var1;
   }

   public void render(int var1, int var2) {
      if (this.mc.getTwitchStream().isBroadcasting()) {
         GlStateManager.enableBlend();
         int var3 = this.mc.getTwitchStream().func_152920_A();
         if (var3 > 0) {
            String var4 = "" + var3;
            int var5 = Minecraft.fontRendererObj.getStringWidth(var4);
            boolean var6 = true;
            int var7 = var1 - var5 - 1;
            int var8 = var2 + 20 - 1;
            int var9 = var2 + 20 + Minecraft.fontRendererObj.FONT_HEIGHT - 1;
            GlStateManager.disableTexture2D();
            Tessellator var10 = Tessellator.getInstance();
            WorldRenderer var11 = var10.getWorldRenderer();
            GlStateManager.color(0.0F, 0.0F, 0.0F, (0.65F + 0.35000002F * this.field_152443_c) / 2.0F);
            var11.begin(7, DefaultVertexFormats.POSITION);
            var11.pos((double)var7, (double)var9, 0.0D).endVertex();
            var11.pos((double)var1, (double)var9, 0.0D).endVertex();
            var11.pos((double)var1, (double)var8, 0.0D).endVertex();
            var11.pos((double)var7, (double)var8, 0.0D).endVertex();
            var10.draw();
            GlStateManager.enableTexture2D();
            Minecraft.fontRendererObj.drawString(var4, (double)(var1 - var5), (double)(var2 + 20), 16777215);
         }

         this.render(var1, var2, this.func_152440_b(), 0);
         this.render(var1, var2, this.func_152438_c(), 17);
      }

   }

   public void func_152439_a() {
      if (this.mc.getTwitchStream().isBroadcasting()) {
         this.field_152443_c += 0.025F * (float)this.field_152444_d;
         if (this.field_152443_c < 0.0F) {
            this.field_152444_d *= -1;
            this.field_152443_c = 0.0F;
         } else if (this.field_152443_c > 1.0F) {
            this.field_152444_d *= -1;
            this.field_152443_c = 1.0F;
         }
      } else {
         this.field_152443_c = 1.0F;
         this.field_152444_d = 1;
      }

   }
}
