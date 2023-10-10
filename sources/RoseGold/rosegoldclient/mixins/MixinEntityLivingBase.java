package rosegoldclient.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rosegoldclient.Main;
import rosegoldclient.features.SpellCaster;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {
    @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
    public void swingArm(EnumHand hand, CallbackInfo ci) {
        if(SpellCaster.cancelServerSwing) {
            ci.cancel();
            SpellCaster.cancelServerSwing = false;
        }
    }

    @Inject(method = "isPotionActive", at = @At("RETURN"), cancellable = true)
    public void antiBlind(Potion potionIn, CallbackInfoReturnable<Boolean> cir) {
        if(Main.configFile.antiblind && potionIn == MobEffects.BLINDNESS) {
            cir.setReturnValue(false);
        }
    }
}
