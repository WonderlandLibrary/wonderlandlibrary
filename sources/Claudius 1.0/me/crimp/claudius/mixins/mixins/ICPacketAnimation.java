package me.crimp.claudius.mixins.mixins;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketAnimation.class)
public interface ICPacketAnimation {

    @Accessor(value = "hand")
    void setHand(EnumHand hand);
}
