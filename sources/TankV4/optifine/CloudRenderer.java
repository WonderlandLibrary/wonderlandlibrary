package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class CloudRenderer {
   private Minecraft mc;
   private boolean updated = false;
   private boolean renderFancy = false;
   int cloudTickCounter;
   float partialTicks;
   private int glListClouds = -1;
   private int cloudTickCounterUpdate = 0;
   private double cloudPlayerX = 0.0D;
   private double cloudPlayerY = 0.0D;
   private double cloudPlayerZ = 0.0D;

   public CloudRenderer(Minecraft var1) {
      this.mc = var1;
      this.glListClouds = GLAllocation.generateDisplayLists(1);
   }

   public void prepareToRender(boolean var1, int var2, float var3) {
      if (this.renderFancy != var1) {
         this.updated = false;
      }

      this.renderFancy = var1;
      this.cloudTickCounter = var2;
      this.partialTicks = var3;
   }

   public boolean shouldUpdateGlList() {
      if (!this.updated) {
         return true;
      } else if (this.cloudTickCounter >= this.cloudTickCounterUpdate + 20) {
         return true;
      } else {
         Entity var1 = this.mc.getRenderViewEntity();
         boolean var2 = this.cloudPlayerY + (double)var1.getEyeHeight() < 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F);
         boolean var3 = var1.prevPosY + (double)var1.getEyeHeight() < 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F);
         return var3 ^ var2;
      }
   }

   public void startUpdateGlList() {
      GL11.glNewList(this.glListClouds, 4864);
   }

   public void endUpdateGlList() {
      GL11.glEndList();
      this.cloudTickCounterUpdate = this.cloudTickCounter;
      this.cloudPlayerX = this.mc.getRenderViewEntity().prevPosX;
      this.cloudPlayerY = this.mc.getRenderViewEntity().prevPosY;
      this.cloudPlayerZ = this.mc.getRenderViewEntity().prevPosZ;
      this.updated = true;
      GlStateManager.resetColor();
   }

   public void renderGlList() {
      Entity var1 = this.mc.getRenderViewEntity();
      double var2 = var1.prevPosX + (var1.posX - var1.prevPosX) * (double)this.partialTicks;
      double var4 = var1.prevPosY + (var1.posY - var1.prevPosY) * (double)this.partialTicks;
      double var6 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)this.partialTicks;
      double var8 = (double)((float)(this.cloudTickCounter - this.cloudTickCounterUpdate) + this.partialTicks);
      float var10 = (float)(var2 - this.cloudPlayerX + var8 * 0.03D);
      float var11 = (float)(var4 - this.cloudPlayerY);
      float var12 = (float)(var6 - this.cloudPlayerZ);
      GlStateManager.pushMatrix();
      if (this.renderFancy) {
         GlStateManager.translate(-var10 / 12.0F, -var11, -var12 / 12.0F);
      } else {
         GlStateManager.translate(-var10, -var11, -var12);
      }

      GlStateManager.callList(this.glListClouds);
      GlStateManager.popMatrix();
      GlStateManager.resetColor();
   }

   public void reset() {
      this.updated = false;
   }
}
