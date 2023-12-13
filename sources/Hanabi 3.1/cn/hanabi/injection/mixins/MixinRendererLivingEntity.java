package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventRenderLivingEntity;
import cn.hanabi.injection.interfaces.IRendererLivingEntity;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.ESP;
import cn.hanabi.modules.modules.render.Thermal;
import cn.hanabi.utils.OutlineUtils;
import cn.hanabi.utils.RenderUtil;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRendererLivingEntity extends MixinRender implements IRendererLivingEntity {
   @Shadow
   @Final
   private static Logger logger;
   @Shadow
   protected boolean renderOutlines = false;
   @Shadow
   protected ModelBase mainModel;

   @Inject(
      method = {"renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onChat(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
      if (ModManager.getModule("Nametags").isEnabled() && entity instanceof EntityPlayer) {
         ci.cancel();
      }

   }

   @Shadow
   protected abstract float interpolateRotation(float var1, float var2, float var3);

   @Shadow
   protected abstract float getSwingProgress(EntityLivingBase var1, float var2);

   @Shadow
   protected abstract void renderLivingAt(EntityLivingBase var1, double var2, double var4, double var6);

   @Shadow
   protected abstract void rotateCorpse(EntityLivingBase var1, float var2, float var3, float var4);

   @Shadow
   protected abstract float handleRotationFloat(EntityLivingBase var1, float var2);

   @Shadow
   protected abstract void preRenderCallback(EntityLivingBase var1, float var2);

   @Shadow
   protected abstract boolean setScoreTeamColor(EntityLivingBase var1);

   @Shadow
   protected abstract void unsetScoreTeamColor();

   @Shadow
   protected abstract void renderLayers(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

   @Shadow
   protected abstract boolean setDoRenderBrightness(EntityLivingBase var1, float var2);

   @Shadow
   protected abstract void unsetBrightness();

   public void doRenderModel(Object entitylivingbaseIn, float a, float b, float c, float d, float e, float scaleFactor) {
      this.renderModel((EntityLivingBase)entitylivingbaseIn, a, b, c, d, e, scaleFactor);
   }

   public void doRenderLayers(Object entitylivingbaseIn, float a, float b, float partialTicks, float d, float e, float f, float g) {
      this.renderLayers((EntityLivingBase)entitylivingbaseIn, a, b, partialTicks, d, e, f, g);
   }

   @Overwrite
   protected void renderModel(EntityLivingBase entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
      boolean flag = !entitylivingbaseIn.isInvisible();
      boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
      if (flag || flag1) {
         if (!this.bindEntityTexture(entitylivingbaseIn)) {
            return;
         }

         if (flag1) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569F);
         }

         if (((Thermal)ModManager.getModule(Thermal.class)).isEnabled() && entitylivingbaseIn != Minecraft.getMinecraft().thePlayer) {
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glPolygonMode(1028, 6913);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 32772);
            RenderUtil.color(Thermal.renderColor.getColor());
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
         }

         try {
            if (((ESP)ModManager.getModule(ESP.class)).isEnabled() && entitylivingbaseIn != Minecraft.getMinecraft().thePlayer && entitylivingbaseIn instanceof EntityPlayer) {
               ESP var10000 = (ESP)ModManager.getModule(ESP.class);
               if (ESP.mode.isCurrentMode("OutLine")) {
                  GL11.glPushMatrix();
                  GlStateManager.depthMask(true);
                  if (Minecraft.getMinecraft().theWorld != null) {
                     RenderUtil.color(ESP.esp.getColor());
                     this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                     OutlineUtils.renderOne();
                     RenderUtil.color(ESP.esp.getColor());
                     this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                     OutlineUtils.renderTwo();
                     RenderUtil.color(ESP.esp.getColor());
                     this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                     RenderUtil.color(ESP.esp.getColor());
                     OutlineUtils.renderThree();
                     OutlineUtils.renderFour();
                     RenderUtil.color(ESP.esp.getColor());
                     this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                     OutlineUtils.renderFive();
                  }

                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  GL11.glPopMatrix();
               }
            }
         } catch (Exception var11) {
            var11.printStackTrace();
         }

         this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
         if (flag1) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
         }
      }

   }

   @Overwrite
   public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
      this.mainModel.isRiding = entity.isRiding();
      this.mainModel.isChild = entity.isChild();

      try {
         float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
         float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
         float f2 = f1 - f;
         float f7;
         if (this.mainModel.isRiding && entity.ridingEntity instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
            f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
            f2 = f1 - f;
            f7 = MathHelper.wrapAngleTo180_float(f2);
            if (f7 < -85.0F) {
               f7 = -85.0F;
            }

            if (f7 >= 85.0F) {
               f7 = 85.0F;
            }

            f = f1 - f7;
            if (f7 * f7 > 2500.0F) {
               f += f7 * 0.2F;
            }
         }

         float f8 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
         if (entity instanceof EntityPlayerSP && entityYaw != 0.0F) {
            f8 = this.interpolateRotation(EventPreMotion.RPPITCH, EventPreMotion.RPITCH, partialTicks);
         }

         this.renderLivingAt(entity, x, y, z);
         f7 = this.handleRotationFloat(entity, partialTicks);
         this.rotateCorpse(entity, f7, f, partialTicks);
         GlStateManager.enableRescaleNormal();
         GlStateManager.scale(-1.0F, -1.0F, 1.0F);
         this.preRenderCallback(entity, partialTicks);
         float f4 = 0.0625F;
         GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
         float f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
         float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
         if (entity instanceof EntityPlayer) {
            EventRenderLivingEntity pre = new EventRenderLivingEntity(entity, true, f6, f5, f7, f2, f8, f, f4);
            EventManager.call(pre);
            if (pre.isCancelled()) {
               return;
            }
         }

         if (entity.isChild()) {
            f6 *= 3.0F;
         }

         if (f5 > 1.0F) {
            f5 = 1.0F;
         }

         GlStateManager.enableAlpha();
         this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
         this.mainModel.setRotationAngles(f6, f5, f7, f2, f8, 0.0625F, entity);
         boolean flag;
         if (this.renderOutlines) {
            flag = this.setScoreTeamColor(entity);
            this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
            if (flag) {
               this.unsetScoreTeamColor();
            }
         } else {
            flag = this.setDoRenderBrightness(entity, partialTicks);
            this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
            if (flag) {
               this.unsetBrightness();
            }

            GlStateManager.depthMask(true);
            if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
               this.renderLayers(entity, f6, f5, partialTicks, f7, f2, f8, 0.0625F);
            }
         }

         GlStateManager.disableRescaleNormal();
      } catch (Exception var19) {
         logger.error("Couldn't render entity", var19);
      }

      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableCull();
      GlStateManager.popMatrix();
      if (!this.renderOutlines) {
         super.doRender(entity, x, y, z, entityYaw, partialTicks);
      }

      EventRenderLivingEntity post = new EventRenderLivingEntity(entity, false);
      EventManager.call(post);
   }
}
