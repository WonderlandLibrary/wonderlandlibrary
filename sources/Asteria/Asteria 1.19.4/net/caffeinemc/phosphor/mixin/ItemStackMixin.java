package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.modules.combat.AutoCrystalModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean isOf(Item item);

    @Inject(method = "getBobbingAnimationTime", at = @At("HEAD"), cancellable = true)
    private void onGetBobbingAnimationTime(CallbackInfoReturnable<Integer> cir) {
        if (this.isOf(Items.END_CRYSTAL)) {
            AutoCrystalModule autoCrystal = Phosphor.moduleManager().getModule(AutoCrystalModule.class);
            if (autoCrystal != null && autoCrystal.noBounce.isEnabled())
                cir.setReturnValue(0);
        }
    }
}
