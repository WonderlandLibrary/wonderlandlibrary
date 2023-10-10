package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityLargeExplodeFX extends EntityFX {
   private TextureManager theRenderEngine;
   private static final VertexFormat field_181549_az;
   private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
   private int field_70581_a;
   private float field_70582_as;
   private int field_70584_aq;

   public int getFXLayer() {
      return 3;
   }

   protected EntityLargeExplodeFX(TextureManager var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      super(var2, var3, var5, var7, 0.0D, 0.0D, 0.0D);
      this.theRenderEngine = var1;
      this.field_70584_aq = 6 + this.rand.nextInt(4);
      this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F;
      this.field_70582_as = 1.0F - (float)var9 * 0.5F;
   }

   static {
      field_181549_az = (new VertexFormat()).func_181721_a(DefaultVertexFormats.POSITION_3F).func_181721_a(DefaultVertexFormats.TEX_2F).func_181721_a(DefaultVertexFormats.COLOR_4UB).func_181721_a(DefaultVertexFormats.TEX_2S).func_181721_a(DefaultVertexFormats.NORMAL_3B).func_181721_a(DefaultVertexFormats.PADDING_1B);
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      int var9 = (int)(((float)this.field_70581_a + var3) * 15.0F / (float)this.field_70584_aq);
      if (var9 <= 15) {
         this.theRenderEngine.bindTexture(EXPLOSION_TEXTURE);
         float var10 = (float)(var9 % 4) / 4.0F;
         float var11 = var10 + 0.24975F;
         float var12 = (float)(var9 / 4) / 4.0F;
         float var13 = var12 + 0.24975F;
         float var14 = 2.0F * this.field_70582_as;
         float var15 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var3 - interpPosX);
         float var16 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var3 - interpPosY);
         float var17 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var3 - interpPosZ);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableLighting();
         RenderHelper.disableStandardItemLighting();
         var1.begin(7, field_181549_az);
         var1.pos((double)(var15 - var4 * var14 - var7 * var14), (double)(var16 - var5 * var14), (double)(var17 - var6 * var14 - var8 * var14)).tex((double)var11, (double)var13).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
         var1.pos((double)(var15 - var4 * var14 + var7 * var14), (double)(var16 + var5 * var14), (double)(var17 - var6 * var14 + var8 * var14)).tex((double)var11, (double)var12).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
         var1.pos((double)(var15 + var4 * var14 + var7 * var14), (double)(var16 + var5 * var14), (double)(var17 + var6 * var14 + var8 * var14)).tex((double)var10, (double)var12).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
         var1.pos((double)(var15 + var4 * var14 - var7 * var14), (double)(var16 - var5 * var14), (double)(var17 + var6 * var14 - var8 * var14)).tex((double)var10, (double)var13).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
         Tessellator.getInstance().draw();
         GlStateManager.enableLighting();
      }

   }

   public int getBrightnessForRender(float var1) {
      return 61680;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      ++this.field_70581_a;
      if (this.field_70581_a == this.field_70584_aq) {
         this.setDead();
      }

   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
