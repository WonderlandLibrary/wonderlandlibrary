package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.module.modules.player.CrystalOptimizerModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {
    private EndCrystalEntityMixin() {
        super(null, null);
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (AsteriaMenu.isClientEnabled() &&
                source.getAttacker() == MinecraftClient.getInstance().player &&
                Phosphor.moduleManager().isModuleEnabled(CrystalOptimizerModule.class)) {
            this.kill();
        }
    }
}