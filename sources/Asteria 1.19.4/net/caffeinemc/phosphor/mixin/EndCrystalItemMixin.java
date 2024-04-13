package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.modules.combat.AutoCrystalModule;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {
    @Redirect(method = "useOnBlock",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrement(I)V",
                    opcode = Opcodes.INVOKEVIRTUAL))
    private void onItemShrink(ItemStack instance, int i) {
        AutoCrystalModule autoCrystal = Phosphor.moduleManager().getModule(AutoCrystalModule.class);
        if (autoCrystal == null || !autoCrystal.isEnabled() || !autoCrystal.noCountGlitch.isEnabled())
            instance.decrement(i);
    }
}
