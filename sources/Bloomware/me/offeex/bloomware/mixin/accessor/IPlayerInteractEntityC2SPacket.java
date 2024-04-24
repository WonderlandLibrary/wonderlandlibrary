package me.offeex.bloomware.mixin.accessor;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInteractEntityC2SPacket.class)
public interface IPlayerInteractEntityC2SPacket {
    @Accessor("type")
    PlayerInteractEntityC2SPacket.InteractTypeHandler getType();

    @Accessor("entityId")
    int getEntityId();
}
