package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.api.event.events.AttackEntityEvent;
import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.module.modules.combat.ReachModule;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Inject(at = @At("HEAD"), method = "getReachDistance()F", cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        ReachModule reachModule = Phosphor.moduleManager().getModule(ReachModule.class);
        if (AsteriaMenu.isClientEnabled() && reachModule != null && reachModule.isEnabled() && reachModule.blockReachToggle.isEnabled()) {
            cir.setReturnValue(reachModule.blockReach.getFValue());
        }
    }

    @Inject(at = @At("HEAD"), method = "hasExtendedReach()Z", cancellable = true)
    private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (AsteriaMenu.isClientEnabled() && Phosphor.moduleManager().isModuleEnabled(ReachModule.class)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "attackEntity")
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        Phosphor.EVENTBUS.post(AttackEntityEvent.get(target));
    }
}