package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityBreakingFX extends EntityFX {
   protected EntityBreakingFX(World var1, double var2, double var4, double var6, Item var8) {
      this(var1, var2, var4, var6, var8, 0);
   }

   public int getFXLayer() {
      return 1;
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
      float var10 = var9 + 0.015609375F;
      float var11 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
      float var12 = var11 + 0.015609375F;
      float var13 = 0.1F * this.particleScale;
      if (this.particleIcon != null) {
         var9 = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
         var10 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
         var11 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
         var12 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
      }

      float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var3 - interpPosX);
      float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var3 - interpPosY);
      float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var3 - interpPosZ);
      int var17 = this.getBrightnessForRender(var3);
      int var18 = var17 >> 16 & '\uffff';
      int var19 = var17 & '\uffff';
      var1.pos((double)(var14 - var4 * var13 - var7 * var13), (double)(var15 - var5 * var13), (double)(var16 - var6 * var13 - var8 * var13)).tex((double)var9, (double)var12).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
      var1.pos((double)(var14 - var4 * var13 + var7 * var13), (double)(var15 + var5 * var13), (double)(var16 - var6 * var13 + var8 * var13)).tex((double)var9, (double)var11).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
      var1.pos((double)(var14 + var4 * var13 + var7 * var13), (double)(var15 + var5 * var13), (double)(var16 + var6 * var13 + var8 * var13)).tex((double)var10, (double)var11).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
      var1.pos((double)(var14 + var4 * var13 - var7 * var13), (double)(var15 - var5 * var13), (double)(var16 + var6 * var13 - var8 * var13)).tex((double)var10, (double)var12).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(var18, var19).endVertex();
   }

   protected EntityBreakingFX(World var1, double var2, double var4, double var6, Item var8, int var9) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(var8, var9));
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleGravity = Blocks.snow.blockParticleGravity;
      this.particleScale /= 2.0F;
   }

   protected EntityBreakingFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, Item var14, int var15) {
      this(var1, var2, var4, var6, var14, var15);
      this.motionX *= 0.10000000149011612D;
      this.motionY *= 0.10000000149011612D;
      this.motionZ *= 0.10000000149011612D;
      this.motionX += var8;
      this.motionY += var10;
      this.motionZ += var12;
   }

   public static class SlimeFactory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityBreakingFX(var2, var3, var5, var7, Items.slime_ball);
      }
   }

   public static class SnowballFactory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityBreakingFX(var2, var3, var5, var7, Items.snowball);
      }
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         int var16 = var15.length > 1 ? var15[1] : 0;
         return new EntityBreakingFX(var2, var3, var5, var7, var9, var11, var13, Item.getItemById(var15[0]), var16);
      }
   }
}
