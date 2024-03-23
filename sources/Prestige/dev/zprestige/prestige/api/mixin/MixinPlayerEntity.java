package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.LastAttackedEvent;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntity.class})
public class MixinPlayerEntity {
    @Inject(method={"resetLastAttackedTicks"}, at={@At(value="HEAD")}, cancellable=true)
    void resetLastAttackedTicks(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new LastAttackedEvent().invoke()) {
            callbackInfo.cancel();
        }
    }
}
