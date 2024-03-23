package dev.zprestige.prestige.api.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ItemStack.class})
public class MixinItemStack {
    @Inject(method={"getBobbingAnimationTime"}, at={@At(value="HEAD")}, cancellable=true)
    void stopBobbingAnimation(CallbackInfoReturnable callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(0);
    }
}
