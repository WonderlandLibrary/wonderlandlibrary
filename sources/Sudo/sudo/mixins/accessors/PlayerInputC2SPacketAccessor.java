package sudo.mixins.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;

@Mixin(PlayerInputC2SPacket.class)
public interface PlayerInputC2SPacketAccessor {

	@Accessor
    @Mutable
    void setForward(float forward);
	
	@Accessor
    @Mutable
    void setSideways(float sideways);
	
	@Accessor
    @Mutable
    void setJumping(boolean jumping);
	
	@Accessor
    @Mutable
    void setSneaking(boolean sneaking);
}