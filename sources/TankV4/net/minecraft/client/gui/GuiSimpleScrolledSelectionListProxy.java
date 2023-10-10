package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.util.MathHelper;

public class GuiSimpleScrolledSelectionListProxy extends GuiSlot {
   private final RealmsSimpleScrolledSelectionList field_178050_u;

   public void drawScreen(int var1, int var2, float var3) {
      if (this.field_178041_q) {
         this.mouseX = var1;
         this.mouseY = var2;
         this.drawBackground();
         int var4 = this.getScrollBarX();
         int var5 = var4 + 6;
         this.bindAmountScrolled();
         GlStateManager.disableLighting();
         GlStateManager.disableFog();
         Tessellator var6 = Tessellator.getInstance();
         WorldRenderer var7 = var6.getWorldRenderer();
         int var8 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
         int var9 = this.top + 4 - (int)this.amountScrolled;
         if (this.hasListHeader) {
            this.drawListHeader(var8, var9, var6);
         }

         this.drawSelectionBox(var8, var9, var1, var2);
         GlStateManager.disableDepth();
         boolean var10 = true;
         this.overlayBackground(0, this.top, 255, 255);
         this.overlayBackground(this.bottom, this.height, 255, 255);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
         GlStateManager.disableAlpha();
         GlStateManager.shadeModel(7425);
         GlStateManager.disableTexture2D();
         int var11 = this.func_148135_f();
         if (var11 > 0) {
            int var12 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            var12 = MathHelper.clamp_int(var12, 32, this.bottom - this.top - 8);
            int var13 = (int)this.amountScrolled * (this.bottom - this.top - var12) / var11 + this.top;
            if (var13 < this.top) {
               var13 = this.top;
            }

            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var4, (double)this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)var5, (double)this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)var5, (double)this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)var4, (double)this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            var6.draw();
            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var4, (double)(var13 + var12), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var5, (double)(var13 + var12), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var5, (double)var13, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var4, (double)var13, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
            var6.draw();
            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var4, (double)(var13 + var12 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
            var7.pos((double)(var5 - 1), (double)(var13 + var12 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
            var7.pos((double)(var5 - 1), (double)var13, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
            var7.pos((double)var4, (double)var13, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
            var6.draw();
         }

         this.func_148142_b(var1, var2);
         GlStateManager.enableTexture2D();
         GlStateManager.shadeModel(7424);
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
      }

   }

   public int getMouseY() {
      return super.mouseY;
   }

   public int getWidth() {
      return super.width;
   }

   public void handleMouseInput() {
      super.handleMouseInput();
   }

   protected int getScrollBarX() {
      return this.field_178050_u.getScrollbarPosition();
   }

   protected int getSize() {
      return this.field_178050_u.getItemCount();
   }

   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.field_178050_u.renderItem(var1, var2, var3, var4, var5, var6);
   }

   public int getMouseX() {
      return super.mouseX;
   }

   public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList var1, int var2, int var3, int var4, int var5, int var6) {
      super(Minecraft.getMinecraft(), var2, var3, var4, var5, var6);
      this.field_178050_u = var1;
   }

   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      this.field_178050_u.selectItem(var1, var2, var3, var4);
   }

   protected boolean isSelected(int var1) {
      return this.field_178050_u.isSelectedItem(var1);
   }

   protected int getContentHeight() {
      return this.field_178050_u.getMaxPosition();
   }

   protected void drawBackground() {
      this.field_178050_u.renderBackground();
   }
}
