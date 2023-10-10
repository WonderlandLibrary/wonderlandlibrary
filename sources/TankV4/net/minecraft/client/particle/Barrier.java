package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier extends EntityFX {
   public int getFXLayer() {
      return 1;
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = this.particleIcon.getMinU();
      float var10 = this.particleIcon.getMaxU();
      float var11 = this.particleIcon.getMinV();
      float var12 = this.particleIcon.getMaxV();
      float var13 = 0.5F;
      float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var3 - interpPosX);
      float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var3 - interpPosY);
      float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var3 - interpPosZ);
      int var17 = this.getBrightnessForRender(var3);
      int var18 = var17 >> 16 & '\uffff';
      int var19 = var17 & '\uffff';
      var1.pos((double)(var14 - var4 * 0.5F - var7 * 0.5F), (double)(var15 - var5 * 0.5F), (double)(var16 - var6 * 0.5F - var8 * 0.5F)).tex((double)var10, (double)var12).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
      var1.pos((double)(var14 - var4 * 0.5F + var7 * 0.5F), (double)(var15 + var5 * 0.5F), (double)(var16 - var6 * 0.5F + var8 * 0.5F)).tex((double)var10, (double)var11).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
      var1.pos((double)(var14 + var4 * 0.5F + var7 * 0.5F), (double)(var15 + var5 * 0.5F), (double)(var16 + var6 * 0.5F + var8 * 0.5F)).tex((double)var9, (double)var11).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
      var1.pos((double)(var14 + var4 * 0.5F - var7 * 0.5F), (double)(var15 - var5 * 0.5F), (double)(var16 + var6 * 0.5F - var8 * 0.5F)).tex((double)var9, (double)var12).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
   }

   protected Barrier(World var1, double var2, double var4, double var6, Item var8) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(var8));
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.motionX = this.motionY = this.motionZ = 0.0D;
      this.particleGravity = 0.0F;
      this.particleMaxAge = 80;
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new Barrier(var2, var3, var5, var7, Item.getItemFromBlock(Blocks.barrier));
      }
   }
}
