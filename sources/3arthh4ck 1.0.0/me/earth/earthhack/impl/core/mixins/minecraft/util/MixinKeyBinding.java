package me.earth.earthhack.impl.core.mixins.minecraft.util;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding
{
    @Shadow
    private boolean pressed;

    @Inject(method = "isKeyDown", at = @At("RETURN"), cancellable = true)
    private void isKeyDownHook(CallbackInfoReturnable<Boolean> info)
    {
        if (!info.getReturnValue())
        {
            info.setReturnValue(this.pressed);
        }
    }

}
