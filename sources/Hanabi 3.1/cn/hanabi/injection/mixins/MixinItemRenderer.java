package cn.hanabi.injection.mixins;

import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.modules.modules.render.HitAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ItemRenderer.class})
public abstract class MixinItemRenderer {
   public float rotateDirection = 0.0F;
   public float delta;
   public float shaderDelta;
   @Final
   @Shadow
   private Minecraft mc;
   @Shadow
   private float equippedProgress;
   @Shadow
   private float prevEquippedProgress;
   @Shadow
   private ItemStack itemToRender;
   @Final
   @Shadow
   private RenderManager renderManager;

   @Overwrite
   public void renderItemInFirstPerson(float partialTicks) {
      float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
      EntityPlayerSP entityplayersp = this.mc.thePlayer;
      float f1 = entityplayersp.getSwingProgress(partialTicks);
      float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * partialTicks;
      float f3 = entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * partialTicks;
      float var2 = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
      EntityPlayerSP var3 = this.mc.thePlayer;
      var3.getSwingProgress(partialTicks);
      this.func_178101_a(f2, f3);
      this.func_178109_a(entityplayersp);
      this.func_178110_a(entityplayersp, partialTicks);
      GlStateManager.enableRescaleNormal();
      GlStateManager.pushMatrix();
      if (this.itemToRender != null) {
         if (this.itemToRender.getItem() == Items.filled_map) {
            this.renderItemMap(entityplayersp, f2, f, f1);
         } else if (entityplayersp.getItemInUseCount() > 0) {
            EnumAction enumaction = this.itemToRender.getItemUseAction();
            switch (enumaction) {
               case NONE:
                  this.transformFirstPersonItem(f, 0.0F);
                  break;
               case EAT:
               case DRINK:
                  this.func_178104_a(entityplayersp, partialTicks);
                  this.transformFirstPersonItem(f, f1);
                  break;
               case BLOCK:
                  this.renderingBlocked(f, f1);
                  break;
               case BOW:
                  this.transformFirstPersonItem(f, f1);
                  this.func_178098_a(partialTicks, entityplayersp);
            }
         } else if (((Boolean)KillAura.autoBlock.getValueState() && KillAura.target != null || this.mc.gameSettings.keyBindUseItem.isKeyDown()) && ModManager.getModule("EveryThingBlock").isEnabled()) {
            this.renderingBlocked(f, f1);
         } else {
            this.func_178105_d(f1);
            this.transformFirstPersonItem(f, f1);
         }

         this.renderItem(entityplayersp, this.itemToRender, TransformType.FIRST_PERSON);
      } else if (!entityplayersp.isInvisible()) {
         this.func_178095_a(entityplayersp, f, f1);
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
   }

   private void avatar(float equipProgress, float swingProgress) {
      GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
      GlStateManager.translate(0.0F, 0.0F, 0.0F);
      GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
      float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
      float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
      GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(f1 * -40.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.4F, 0.4F, 0.4F);
   }

   private void renderingBlocked(float swingProgress, float equippedProgress) {
      float hand = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927F);
      HitAnimation animations = (HitAnimation)ModManager.getModule(HitAnimation.class);
      if (!animations.isEnabled()) {
         this.transformFirstPersonItem(swingProgress, 0.0F);
         this.func_178103_d();
      } else {
         GL11.glTranslated((Double)animations.posX.getValue(), (Double)animations.posY.getValue(), (Double)animations.posZ.getValue());
         float slide;
         if (animations.mode.isCurrentMode("Sigma")) {
            this.transformFirstPersonItem(equippedProgress, 0.0F);
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(swingProgress) * Math.PI));
            GlStateManager.rotate(-slide * 55.0F / 2.0F, -8.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-slide * 45.0F, 1.0F, slide / 2.0F, -0.0F);
            this.func_178103_d();
            GL11.glTranslated(1.2, 0.3, 0.5);
            GL11.glTranslatef(-1.0F, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
         } else if (animations.mode.isCurrentMode("Debug")) {
            this.transformFirstPersonItem(0.2F, equippedProgress);
            this.func_178103_d();
            GlStateManager.translate(-0.5, 0.2, 0.0);
         } else if (animations.mode.isCurrentMode("Vanilla")) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Luna")) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.func_178103_d();
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
            GlStateManager.translate(-0.2F, 0.45F, 0.25F);
            GlStateManager.rotate(-slide * 20.0F, -5.0F, -5.0F, 9.0F);
         } else if (animations.mode.isCurrentMode("1.7")) {
            this.transformFirstPersonItem(swingProgress - 0.3F, equippedProgress);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Swang")) {
            this.transformFirstPersonItem(swingProgress / 2.0F, equippedProgress);
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GlStateManager.rotate(slide * 30.0F / 2.0F, -slide, -0.0F, 9.0F);
            GlStateManager.rotate(slide * 40.0F, 1.0F, -slide / 2.0F, -0.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Swank")) {
            this.transformFirstPersonItem(swingProgress / 2.0F, equippedProgress);
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(swingProgress) * Math.PI));
            GlStateManager.rotate(slide * 30.0F, -slide, -0.0F, 9.0F);
            GlStateManager.rotate(slide * 40.0F, 1.0F, -slide, -0.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Swong")) {
            this.transformFirstPersonItem(swingProgress / 2.0F, 0.0F);
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GlStateManager.rotate(-slide * 40.0F / 2.0F, slide / 2.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-slide * 30.0F, 1.0F, slide / 2.0F, -0.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Jigsaw")) {
            this.transformFirstPersonItem(0.1F, equippedProgress);
            this.func_178103_d();
            GlStateManager.translate(-0.5, 0.0, 0.0);
         } else if (animations.mode.isCurrentMode("Hanabi")) {
            this.transformFirstPersonItem(0.1F, equippedProgress);
            this.func_178103_d();
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GlStateManager.translate(-0.0F, -0.3F, 0.4F);
            GlStateManager.rotate(-slide * 22.5F, -9.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-slide * 10.0F, 1.0F, -0.4F, -0.5F);
         } else if (animations.mode.isCurrentMode("Jello")) {
            GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
            GlStateManager.translate(0.0F, -0.0F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            slide = MathHelper.sin(0.0F);
            float var4 = MathHelper.sin((float)((double)MathHelper.sqrt_float(0.0F) * Math.PI));
            GlStateManager.rotate(slide * -20.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(0.4F, 0.4F, 0.4F);
            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
            GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
            int alpha = (int)Math.min(255L, (System.currentTimeMillis() % 255L > 127L ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : System.currentTimeMillis() % 255L) * 2L);
            GlStateManager.translate(0.3F, -0.0F, 0.4F);
            GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, -1.0F);
            GlStateManager.translate(0.6F, 0.5F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, -1.0F);
            GlStateManager.rotate(-10.0F, 1.0F, 0.0F, -1.0F);
            GlStateManager.rotate(this.mc.thePlayer.isSwingInProgress ? (float)(-alpha) / 5.0F : 1.0F, 1.0F, -0.0F, 1.0F);
         } else if (animations.mode.isCurrentMode("Chill")) {
            this.transformFirstPersonItem(swingProgress / 2.0F - 0.18F, 0.0F);
            GL11.glRotatef(hand * 60.0F / 2.0F, -hand / 2.0F, -0.0F, -16.0F);
            GL11.glRotatef(-hand * 30.0F, 1.0F, hand / 2.0F, -1.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Tiny Whack")) {
            this.transformFirstPersonItem(swingProgress / 2.0F - 0.18F, 0.0F);
            GL11.glRotatef(-hand * 40.0F / 2.0F, hand / 2.0F, -0.0F, 9.0F);
            GL11.glRotatef(-hand * 30.0F, 1.0F, hand / 2.0F, -0.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Long Hit")) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.func_178103_d();
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GlStateManager.translate(-0.05F, 0.6F, 0.3F);
            GlStateManager.rotate(-slide * 70.0F / 2.0F, -8.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-slide * 70.0F, 1.5F, -0.4F, -0.0F);
         } else if (animations.mode.isCurrentMode("Butter")) {
            this.transformFirstPersonItem(swingProgress * 0.5F, 0.0F);
            GlStateManager.rotate(-hand * -74.0F / 4.0F, -8.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-hand * 15.0F, 1.0F, hand / 2.0F, -0.0F);
            this.func_178103_d();
            GL11.glTranslated(1.2, 0.3, 0.5);
            GL11.glTranslatef(-1.0F, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
         } else if (animations.mode.isCurrentMode("Slide")) {
            this.transformFirstPersonItem(0.0F, 0.0F);
            this.func_178103_d();
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GlStateManager.translate(-0.05F, -0.0F, 0.35F);
            GlStateManager.rotate(-slide * 60.0F / 2.0F, -15.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-slide * 70.0F, 1.0F, -0.4F, -0.0F);
         } else if (animations.mode.isCurrentMode("Lucky")) {
            this.transformFirstPersonItem(0.0F, 0.0F);
            this.func_178103_d();
            slide = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 0.3215927F);
            GlStateManager.translate(-0.05F, -0.0F, 0.3F);
            GlStateManager.rotate(-slide * 60.0F / 2.0F, -15.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-slide * 70.0F, 1.0F, -0.4F, -0.0F);
         } else if (animations.mode.isCurrentMode("Ohare")) {
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI));
            GL11.glTranslated(-0.05, 0.0, -0.25);
            this.transformFirstPersonItem(swingProgress / 2.0F, 0.0F);
            GlStateManager.rotate(-slide * 60.0F, 2.0F, -slide * 2.0F, -0.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Wizzard")) {
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * 3.1));
            this.transformFirstPersonItem(swingProgress / 3.0F, 0.0F);
            GlStateManager.rotate(slide * 30.0F / 1.0F, slide / -1.0F, 1.0F, 0.0F);
            GlStateManager.rotate(slide * 10.0F / 10.0F, -slide / -1.0F, 1.0F, 0.0F);
            GL11.glTranslated(0.0, 0.4, 0.0);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Lennox")) {
            slide = MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * 3.1));
            GL11.glTranslated(0.0, 0.125, -0.1);
            this.transformFirstPersonItem(swingProgress / 3.0F, 0.0F);
            GlStateManager.rotate(-slide * 75.0F / 4.5F, slide / 3.0F, -2.4F, 5.0F);
            GlStateManager.rotate(-slide * 75.0F, 1.5F, slide / 3.0F, -0.0F);
            GlStateManager.rotate(slide * 72.5F / 2.25F, slide / 3.0F, -2.7F, 5.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Leaked")) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.func_178103_d();
            GlStateManager.rotate(-MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI)) * 30.0F, 0.5F, 0.5F, 0.0F);
         } else if (animations.mode.isCurrentMode("Avatar")) {
            this.avatar(swingProgress, equippedProgress);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("Push")) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.func_178103_d();
            GlStateManager.rotate(-MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI)) * 35.0F, -8.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-MathHelper.sin((float)((double)MathHelper.sqrt_float(equippedProgress) * Math.PI)) * 10.0F, 1.0F, -0.4F, -0.5F);
         } else if (animations.mode.isCurrentMode("Skid")) {
            this.transformFirstPersonItem(swingProgress * 0.5F, 0.0F);
            GlStateManager.rotate(-hand * 10.0F, 0.0F, 15.0F, 300.0F);
            GlStateManager.rotate(-hand * 10.0F, 300.0F, hand / 2.0F, 1.0F);
            this.func_178103_d();
            GL11.glTranslated(1.2, 0.2, 0.1);
            GL11.glTranslatef(-2.1F, -0.2F, 0.1F);
         } else if (animations.mode.isCurrentMode("Slide2")) {
            this.transformFirstPersonItem(swingProgress, equippedProgress);
            this.func_178103_d();
            GL11.glTranslatef(0.1F, -0.1F, 0.3F);
            GlStateManager.translate(0.1F, -0.1F, 0.4F);
         } else if (animations.mode.isCurrentMode("Mix")) {
            this.transformFirstPersonItem(swingProgress, equippedProgress / 40.0F);
            this.func_178103_d();
         } else if (animations.mode.isCurrentMode("SlideT")) {
            this.transformFirstPersonItem(swingProgress, 1.0F);
            this.func_178103_d();
            GL11.glTranslatef(0.6F, 0.3F, 0.7F);
            slide = MathHelper.sin(equippedProgress * equippedProgress * 5.1415925F);
            GlStateManager.translate(-0.52F, -0.1F, -0.2F);
            GlStateManager.rotate(slide * -19.0F, 25.0F, -0.4F, -5.0F);
         } else if (animations.mode.isCurrentMode("SlideA")) {
            this.transformFirstPersonItem(swingProgress * 0.5F, 0.0F);
            GlStateManager.rotate(-hand * -74.0F / 4.0F, -8.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-hand * 15.0F, 1.0F, hand / 2.0F, -0.0F);
            this.func_178103_d();
            GL11.glTranslated(1.2, 0.3, 0.5);
            GL11.glTranslatef(-1.0F, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
         } else if (animations.mode.isCurrentMode("Epic")) {
            this.transformFirstPersonItem(swingProgress, equippedProgress);
            this.func_178103_d();
            GlStateManager.translate(0.0F, 0.0F, 0.0F);
            GlStateManager.rotate(5.0F, 50.0F, 100.0F, 50.0F);
         } else if (animations.mode.isCurrentMode("Punch")) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.func_178103_d();
            GlStateManager.translate(-0.0F, 0.4F, 0.1F);
            GlStateManager.rotate(-hand * 35.0F, -8.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-hand * 10.0F, 1.0F, -0.4F, -0.5F);
         }
      }

   }

   public float getRotateDirection() {
      this.rotateDirection += this.delta;
      if (this.rotateDirection > 360.0F) {
         this.rotateDirection = 0.0F;
      }

      return this.rotateDirection;
   }

   @Shadow
   protected abstract void func_178103_d();

   @Shadow
   protected abstract void func_178095_a(AbstractClientPlayer var1, float var2, float var3);

   @Shadow
   public abstract void renderItem(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3);

   @Shadow
   protected abstract void func_178101_a(float var1, float var2);

   @Shadow
   protected abstract void renderItemMap(AbstractClientPlayer var1, float var2, float var3, float var4);

   @Overwrite
   private void transformFirstPersonItem(float equipProgress, float swingProgress) {
      GL11.glTranslatef(0.56F, -0.52F, -0.72F);
      GL11.glTranslatef(0.0F, equipProgress * -0.6F, 0.0F);
      GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      float scale;
      if ((double)swingProgress > 0.0) {
         scale = MathHelper.sin((float)((double)(swingProgress * swingProgress) * Math.PI));
         float f2 = MathHelper.sin((float)((double)MathHelper.sqrt_float(swingProgress) * Math.PI));
         GL11.glRotatef(scale * -20.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(f2 * -20.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(f2 * -80.0F, 1.0F, 0.0F, 0.0F);
      }

      scale = 0.4F;
      if (((HitAnimation)ModManager.getModule(HitAnimation.class)).isEnabled()) {
         scale *= ((Double)((HitAnimation)ModManager.getModule(HitAnimation.class)).itemScale.getValue()).floatValue();
      }

      GL11.glScalef(scale, scale, scale);
   }

   @Shadow
   protected abstract void func_178109_a(AbstractClientPlayer var1);

   @Shadow
   protected abstract void func_178105_d(float var1);

   @Shadow
   protected abstract void func_178098_a(float var1, AbstractClientPlayer var2);

   @Shadow
   protected abstract void func_178110_a(EntityPlayerSP var1, float var2);

   @Shadow
   protected abstract void func_178104_a(AbstractClientPlayer var1, float var2);
}
