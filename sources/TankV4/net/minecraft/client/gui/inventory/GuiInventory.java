package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class GuiInventory extends InventoryEffectRenderer {
   private float oldMouseY;
   private float oldMouseX;

   public void updateScreen() {
      if (Minecraft.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(Minecraft.thePlayer));
      }

      this.updateActivePotionEffects();
   }

   public void initGui() {
      this.buttonList.clear();
      if (Minecraft.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(Minecraft.thePlayer));
      } else {
         super.initGui();
      }

   }

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(inventoryBackground);
      int var4 = this.guiLeft;
      int var5 = this.guiTop;
      drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
      drawEntityOnScreen((float)(var4 + 51), (double)(var5 + 75), 30, (float)(var4 + 51) - this.oldMouseX, (float)(var5 + 75 - 50) - this.oldMouseY, Minecraft.thePlayer);
   }

   public static void drawEntityOnScreen(float var0, double var1, int var3, float var4, float var5, EntityLivingBase var6) {
      GlStateManager.enableColorMaterial();
      GlStateManager.pushMatrix();
      GlStateManager.translate(var0, (float)var1, 50.0F);
      GlStateManager.scale((float)(-var3), (float)var3, (float)var3);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      float var7 = var6.renderYawOffset;
      float var8 = var6.rotationYaw;
      float var9 = var6.rotationPitch;
      float var10 = var6.prevRotationYawHead;
      float var11 = var6.rotationYawHead;
      GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-((float)Math.atan((double)(var5 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      var6.renderYawOffset = (float)Math.atan((double)(var4 / 40.0F)) * 20.0F;
      var6.rotationYaw = (float)Math.atan((double)(var4 / 40.0F)) * 40.0F;
      var6.rotationPitch = -((float)Math.atan((double)(var5 / 40.0F))) * 20.0F;
      var6.rotationYawHead = var6.rotationYaw;
      var6.prevRotationYawHead = var6.rotationYaw;
      GlStateManager.translate(0.0F, 0.0F, 0.0F);
      RenderManager var12 = Minecraft.getMinecraft().getRenderManager();
      var12.setPlayerViewY(180.0F);
      var12.setRenderShadow(false);
      var12.renderEntityWithPosYaw(var6, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      var12.setRenderShadow(true);
      var6.renderYawOffset = var7;
      var6.rotationYaw = var8;
      var6.rotationPitch = var9;
      var6.prevRotationYawHead = var10;
      var6.rotationYawHead = var11;
      GlStateManager.popMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
      }

      if (var1.id == 1) {
         this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      this.oldMouseX = (float)var1;
      this.oldMouseY = (float)var2;
   }

   public GuiInventory(EntityPlayer var1) {
      super(var1.inventoryContainer);
      this.allowUserInput = true;
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRendererObj.drawString(I18n.format("container.crafting"), 86.0D, 16.0D, 4210752);
   }
}
