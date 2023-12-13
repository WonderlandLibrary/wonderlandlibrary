package net.minecraft.client.renderer.entity;

import my.NewSnake.Tank.cosmetics.impl.CosmeticFerramenta;
import my.NewSnake.Tank.cosmetics.impl.CosmeticGolem;
import my.NewSnake.Tank.cosmetics.impl.CosmeticPicareta;
import my.NewSnake.Tank.cosmetics.impl.CosmeticTopHat;
import my.NewSnake.Tank.cosmetics.impl.CosmeticZumbi;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;

public class RenderPlayer extends RendererLivingEntity {
   private boolean smallArms;

   protected ResourceLocation getEntityTexture(AbstractClientPlayer var1) {
      return var1.getLocationSkin();
   }

   protected void renderOffsetLivingLabel(AbstractClientPlayer var1, double var2, double var4, double var6, String var8, float var9, double var10) {
      if (var10 < 100.0D) {
         Scoreboard var12 = var1.getWorldScoreboard();
         ScoreObjective var13 = var12.getObjectiveInDisplaySlot(2);
         if (var13 != null) {
            Score var14 = var12.getValueFromObjective(var1.getName(), var13);
            this.renderLivingLabel(var1, var14.getScorePoints() + " " + var13.getDisplayName(), var2, var4, var6, 64);
            var4 += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * var9);
         }
      }

      super.renderOffsetLivingLabel(var1, var2, var4, var6, var8, var9, var10);
   }

   protected void rotateCorpse(EntityLivingBase var1, float var2, float var3, float var4) {
      this.rotateCorpse((AbstractClientPlayer)var1, var2, var3, var4);
   }

   public void transformHeldFull3DItemLayer() {
      GlStateManager.translate(0.0F, 0.1875F, 0.0F);
   }

   public RenderPlayer(RenderManager var1) {
      this(var1, false);
   }

   public ModelPlayer getMainModel() {
      return (ModelPlayer)super.getMainModel();
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((AbstractClientPlayer)var1, var2);
   }

   public void doRender(EntityLivingBase var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((AbstractClientPlayer)var1, var2, var4, var6, var8, var9);
   }

   protected void renderOffsetLivingLabel(Entity var1, double var2, double var4, double var6, String var8, float var9, double var10) {
      this.renderOffsetLivingLabel((AbstractClientPlayer)var1, var2, var4, var6, var8, var9, var10);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((AbstractClientPlayer)var1);
   }

   public ModelBase getMainModel() {
      return this.getMainModel();
   }

   protected void preRenderCallback(AbstractClientPlayer var1, float var2) {
      float var3 = 0.9375F;
      GlStateManager.scale(var3, var3, var3);
   }

   protected void rotateCorpse(AbstractClientPlayer var1, float var2, float var3, float var4) {
      if (var1.isEntityAlive() && var1.isPlayerSleeping()) {
         GlStateManager.rotate(var1.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(this.getDeathMaxRotation(var1), 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
      } else {
         super.rotateCorpse(var1, var2, var3, var4);
      }

   }

   private void setModelVisibilities(AbstractClientPlayer var1) {
      ModelPlayer var2 = this.getMainModel();
      if (var1.isSpectator()) {
         var2.setInvisible(false);
         var2.bipedHead.showModel = true;
         var2.bipedHeadwear.showModel = true;
      } else {
         ItemStack var3 = var1.inventory.getCurrentItem();
         var2.setInvisible(true);
         var2.bipedHeadwear.showModel = var1.isWearing(EnumPlayerModelParts.HAT);
         var2.bipedBodyWear.showModel = var1.isWearing(EnumPlayerModelParts.JACKET);
         var2.bipedLeftLegwear.showModel = var1.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
         var2.bipedRightLegwear.showModel = var1.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
         var2.bipedLeftArmwear.showModel = var1.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
         var2.bipedRightArmwear.showModel = var1.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
         var2.heldItemLeft = 0;
         var2.aimedBow = false;
         var2.isSneak = var1.isSneaking();
         if (var3 == null) {
            var2.heldItemRight = 0;
         } else {
            var2.heldItemRight = 1;
            if (var1.getItemInUseCount() > 0) {
               EnumAction var4 = var3.getItemUseAction();
               if (var4 == EnumAction.BLOCK) {
                  var2.heldItemRight = 3;
               } else if (var4 == EnumAction.BOW) {
                  var2.aimedBow = true;
               }
            }
         }
      }

   }

   protected void renderLivingAt(EntityLivingBase var1, double var2, double var4, double var6) {
      this.renderLivingAt((AbstractClientPlayer)var1, var2, var4, var6);
   }

   public void doRender(AbstractClientPlayer var1, double var2, double var4, double var6, float var8, float var9) {
      if (!var1.isUser() || this.renderManager.livingPlayer == var1) {
         double var10 = var4;
         if (var1.isSneaking() && !(var1 instanceof EntityPlayerSP)) {
            var10 = var4 - 0.125D;
         }

         this.setModelVisibilities(var1);
         super.doRender((EntityLivingBase)var1, var2, var10, var6, var8, var9);
      }

   }

   public void renderLeftArm(AbstractClientPlayer var1) {
      float var2 = 1.0F;
      GlStateManager.color(var2, var2, var2);
      ModelPlayer var3 = this.getMainModel();
      this.setModelVisibilities(var1);
      var3.isSneak = false;
      var3.swingProgress = 0.0F;
      var3.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, var1);
      var3.renderLeftArm();
   }

   public void renderRightArm(AbstractClientPlayer var1) {
      float var2 = 1.0F;
      GlStateManager.color(var2, var2, var2);
      ModelPlayer var3 = this.getMainModel();
      this.setModelVisibilities(var1);
      var3.swingProgress = 0.0F;
      var3.isSneak = false;
      var3.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, var1);
      var3.renderRightArm();
   }

   public RenderPlayer(RenderManager var1, boolean var2) {
      super(var1, new ModelPlayer(0.0F, var2), 0.5F);
      this.smallArms = var2;
      this.addLayer(new LayerBipedArmor(this));
      this.addLayer(new LayerHeldItem(this));
      this.addLayer(new LayerArrow(this));
      this.addLayer(new LayerDeadmau5Head(this));
      this.addLayer(new LayerCape(this));
      this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
      this.addLayer(new CosmeticGolem(this));
      this.addLayer(new CosmeticFerramenta(this));
      this.addLayer(new CosmeticPicareta(this));
      this.addLayer(new CosmeticTopHat(this));
      this.addLayer(new CosmeticZumbi(this));
   }

   protected void renderLivingAt(AbstractClientPlayer var1, double var2, double var4, double var6) {
      if (var1.isEntityAlive() && var1.isPlayerSleeping()) {
         super.renderLivingAt(var1, var2 + (double)var1.renderOffsetX, var4 + (double)var1.renderOffsetY, var6 + (double)var1.renderOffsetZ);
      } else {
         super.renderLivingAt(var1, var2, var4, var6);
      }

   }
}
