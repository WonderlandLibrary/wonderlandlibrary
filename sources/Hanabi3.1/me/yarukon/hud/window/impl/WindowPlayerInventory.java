package me.yarukon.hud.window.impl;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.modules.modules.render.HUD;
import me.yarukon.hud.window.HudWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ForgeHooksClient;

@ObfuscationClass
public class WindowPlayerInventory extends HudWindow {
   public float animation = 0.0F;

   public WindowPlayerInventory() {
      super("PlayerInventory", 5.0F, 100.0F, 180.0F, 80.0F, "Inventory", "", 12.0F, 0.0F, 1.0F);
   }

   public void draw() {
      this.height = 60.0F;
      super.draw();
      float startX = this.x + 2.0F;
      float startY = this.y + this.draggableHeight + 2.0F;
      int curIndex = 0;
      boolean isClassic = HUD.hudMode.isCurrentMode("Classic");
      int col = isClassic ? -1996488705 : -2009055168;

      for(int i = 9; i < 36; ++i) {
         ItemStack slot = this.mc.thePlayer.inventory.mainInventory[i];
         if (slot == null) {
            startX += 20.0F;
            ++curIndex;
            if (curIndex > 8) {
               curIndex = 0;
               startY += 20.0F;
               startX = this.x + 2.0F;
            }
         } else {
            this.drawItemStack(slot, startX, startY);
            startX += 20.0F;
            ++curIndex;
            if (curIndex > 8) {
               curIndex = 0;
               startY += 20.0F;
               startX = this.x + 2.0F;
            }
         }
      }

   }

   private void drawItemStack(ItemStack stack, float x, float y) {
      GlStateManager.pushMatrix();
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.disableAlpha();
      GlStateManager.clear(256);
      this.mc.getRenderItem().zLevel = -150.0F;
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.enableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      this.renderItemIntoGUI(stack, x, y);
      this.renderItemOverlayIntoGUI(this.mc.fontRendererObj, stack, x, y, (String)null);
      this.mc.getRenderItem().zLevel = 0.0F;
      GlStateManager.enableAlpha();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
   }

   public void renderItemIntoGUI(ItemStack stack, float x, float y) {
      IBakedModel ibakedmodel = this.mc.getRenderItem().getItemModelMesher().getItemModel(stack);
      GlStateManager.pushMatrix();
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.setupGuiTransform(x, y, ibakedmodel.isGui3d());
      ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, TransformType.GUI);
      this.mc.getRenderItem().renderItem(stack, ibakedmodel);
      GlStateManager.disableAlpha();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableLighting();
      GlStateManager.popMatrix();
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
   }

   public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, float xPosition, float yPosition, String text) {
      if (stack != null) {
         if (stack.stackSize != 1 || text != null) {
            String s = text == null ? String.valueOf(stack.stackSize) : text;
            if (text == null && stack.stackSize < 1) {
               s = EnumChatFormatting.RED + String.valueOf(stack.stackSize);
            }

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            fr.drawStringWithShadow(s, xPosition + 19.0F - 2.0F - (float)fr.getStringWidth(s), yPosition + 6.0F + 3.0F, 16777215);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }

         if (stack.getItem().showDurabilityBar(stack)) {
            double health = stack.getItem().getDurabilityForDisplay(stack);
            int j = (int)Math.round(13.0 - health * 13.0);
            int i = (int)Math.round(255.0 - health * 255.0);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.draw(worldrenderer, xPosition + 2.0F, yPosition + 13.0F, 13, 2, 0, 0, 0, 255);
            this.draw(worldrenderer, xPosition + 2.0F, yPosition + 13.0F, 12, 1, (255 - i) / 4, 64, 0, 255);
            this.draw(worldrenderer, xPosition + 2.0F, yPosition + 13.0F, j, 1, 255 - i, i, 0, 255);
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

   }

   private void draw(WorldRenderer renderer, float x, float y, int width, int height, int red, int green, int blue, int alpha) {
      renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      renderer.pos((double)(x + 0.0F), (double)(y + 0.0F), 0.0).color(red, green, blue, alpha).endVertex();
      renderer.pos((double)(x + 0.0F), (double)(y + (float)height), 0.0).color(red, green, blue, alpha).endVertex();
      renderer.pos((double)(x + (float)width), (double)(y + (float)height), 0.0).color(red, green, blue, alpha).endVertex();
      renderer.pos((double)(x + (float)width), (double)(y + 0.0F), 0.0).color(red, green, blue, alpha).endVertex();
      Tessellator.getInstance().draw();
   }

   private void setupGuiTransform(float xPosition, float yPosition, boolean isGui3d) {
      GlStateManager.translate(xPosition, yPosition, 100.0F + this.mc.getRenderItem().zLevel);
      GlStateManager.translate(8.0F, 8.0F, 0.0F);
      GlStateManager.scale(1.0F, 1.0F, -1.0F);
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      if (isGui3d) {
         GlStateManager.scale(40.0F, 40.0F, 40.0F);
         GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.enableLighting();
      } else {
         GlStateManager.scale(64.0F, 64.0F, 64.0F);
         GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.disableLighting();
      }

   }
}
