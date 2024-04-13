package net.caffeinemc.phosphor.mixin;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface EntityVelocityUpdateS2CPacketAccessor {
    @Mutable
    @Accessor
    void setVelocityX(int velocityX);

    @Mutable
    @Accessor
    void setVelocityY(int velocityY);

    @Mutable
    @Accessor
    void setVelocityZ(int velocityZ);
}