package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityPickupFX extends EntityFX {
   private RenderManager field_174842_aB = Minecraft.getMinecraft().getRenderManager();
   private Entity field_174843_ax;
   private float field_174841_aA;
   private int age;
   private int maxAge;
   private Entity field_174840_a;

   public void onUpdate() {
      ++this.age;
      if (this.age == this.maxAge) {
         this.setDead();
      }

   }

   public int getFXLayer() {
      return 3;
   }

   public EntityPickupFX(World var1, Entity var2, Entity var3, float var4) {
      super(var1, var2.posX, var2.posY, var2.posZ, var2.motionX, var2.motionY, var2.motionZ);
      this.field_174840_a = var2;
      this.field_174843_ax = var3;
      this.maxAge = 3;
      this.field_174841_aA = var4;
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = ((float)this.age + var3) / (float)this.maxAge;
      var9 *= var9;
      double var10 = this.field_174840_a.posX;
      double var12 = this.field_174840_a.posY;
      double var14 = this.field_174840_a.posZ;
      double var16 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.posX - this.field_174843_ax.lastTickPosX) * (double)var3;
      double var18 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.posY - this.field_174843_ax.lastTickPosY) * (double)var3 + (double)this.field_174841_aA;
      double var20 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.posZ - this.field_174843_ax.lastTickPosZ) * (double)var3;
      double var22 = var10 + (var16 - var10) * (double)var9;
      double var24 = var12 + (var18 - var12) * (double)var9;
      double var26 = var14 + (var20 - var14) * (double)var9;
      int var28 = this.getBrightnessForRender(var3);
      int var29 = var28 % 65536;
      int var30 = var28 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var29 / 1.0F, (float)var30 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      var22 -= interpPosX;
      var24 -= interpPosY;
      var26 -= interpPosZ;
      this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (double)((float)var22), (double)((float)var24), (double)((float)var26), this.field_174840_a.rotationYaw, var3);
   }
}
