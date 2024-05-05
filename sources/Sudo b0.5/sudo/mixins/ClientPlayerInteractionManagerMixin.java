package sudo.mixins;


import net.minecraft.client.network.ClientPlayerInteractionManager;
import sudo.Client;
import sudo.events.EventBlockBreakingCooldown;
import sudo.module.ModuleManager;
import sudo.module.combat.Reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    private void DF9_overwriteReach(CallbackInfoReturnable<Float> cir) {
        if (ModuleManager.INSTANCE.getModule(Reach.class).isEnabled()) {
            cir.setReturnValue((float) ModuleManager.INSTANCE.getModule(Reach.class).reach.getValueFloat());
        }
    }

    @Inject(method = "hasExtendedReach", at = @At("HEAD"), cancellable = true)
    private void DF9_setExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.INSTANCE.getModule(Reach.class).isEnabled()) {
            cir.setReturnValue(true);
        }
    }
    
    @Shadow
    private int blockBreakingCooldown;

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", ordinal = 3),
            require = 0)
    private void updateBlockBreakingProgress(ClientPlayerInteractionManager instance, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value);
        Client.EventBus.post(event);
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", ordinal = 4),
            require = 0)
    private void updateBlockBreakingProgress2(ClientPlayerInteractionManager clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value);
        Client.EventBus.post(event);
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Redirect(method = "attackBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I"),
            require = 0)
    private void attackBlock(ClientPlayerInteractionManager clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value);
        Client.EventBus.post(event);
        this.blockBreakingCooldown = event.getCooldown();
    }
}