package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.api.event.events.GameJoinedEvent;
import net.caffeinemc.phosphor.common.Phosphor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        Phosphor.EVENTBUS.post(GameJoinedEvent.get());
    }
}
