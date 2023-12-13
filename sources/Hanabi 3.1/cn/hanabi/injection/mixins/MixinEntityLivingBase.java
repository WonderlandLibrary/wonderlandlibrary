package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IEntityLivingBase;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.movement.NoJumpDelay;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.modules.modules.render.HitAnimation;
import cn.hanabi.utils.SoundFxPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityLivingBase.class})
public abstract class MixinEntityLivingBase implements IEntityLivingBase {
   @Shadow
   private int jumpTicks;

   @Shadow
   public abstract boolean isPotionActive(Potion var1);

   @Shadow
   public abstract PotionEffect getActivePotionEffect(Potion var1);

   @Overwrite
   private int getArmSwingAnimationEnd() {
      int speed = this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
      if (this.equals(Minecraft.getMinecraft().thePlayer) && ModManager.getModule("HitAnimation").getState()) {
         speed = (int)((double)speed * (Double)((HitAnimation)ModManager.getModule(HitAnimation.class)).swingSpeed.getValue());
      }

      return speed;
   }

   @Inject(
      method = {"onLivingUpdate"},
      at = {@At("HEAD")}
   )
   private void headLiving(CallbackInfo callbackInfo) {
      if (((NoJumpDelay)ModManager.getModule(NoJumpDelay.class)).isEnabled()) {
         this.jumpTicks = 0;
      }

   }

   @Inject(
      method = {"handleStatusUpdate"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/EntityLivingBase;playSound(Ljava/lang/String;FF)V"
)},
      cancellable = true
   )
   private void handleSound(CallbackInfo callbackInfo) {
      if (!((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Minecraft")) {
         if (((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Ding")) {
            (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Ding, -7.0F);
         } else {
            (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Crack, -7.0F);
         }

         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"attackEntityFrom"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/EntityLivingBase;playSound(Ljava/lang/String;FF)V"
)},
      cancellable = true
   )
   private void handleSound(DamageSource source, float amount, CallbackInfoReturnable cir) {
      if (!((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Minecraft")) {
         if (((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Ding")) {
            (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Ding, -7.0F);
         } else {
            (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Crack, -7.0F);
         }

         cir.cancel();
      }

   }

   public int runGetArmSwingAnimationEnd() {
      return this.getArmSwingAnimationEnd();
   }

   public int getJumpTicks() {
      return this.jumpTicks;
   }

   public void setJumpTicks(int a) {
      this.jumpTicks = a;
   }
}
