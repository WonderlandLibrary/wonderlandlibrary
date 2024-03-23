package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.OnJoinEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayNetworkHandler.class})
public class MixinClientPlayNetworkHandler {
    @Unique
    public boolean worldNotNull;
    @Shadow
    public ClientWorld world;

    @Inject(method={"onGameJoin"}, at={@At(value="HEAD")})
    void onGameJoinHead(GameJoinS2CPacket gameJoinS2CPacket, CallbackInfo callbackInfo) {
        worldNotNull = world != null;
    }

    @Inject(method={"onGameJoin"}, at={@At(value="TAIL")})
    void onGameJoinTail(GameJoinS2CPacket gameJoinS2CPacket, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (!this.worldNotNull) {
            new OnJoinEvent().invoke();
        }
    }
}
