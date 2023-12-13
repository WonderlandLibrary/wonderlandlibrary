package me.earth.earthhack.impl.core.mixins.minecraft.entity.living.player;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Accessor for {@link PlayerControllerMP}.
 */
@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMP
{
    @Invoker("syncCurrentPlayItem")
    void syncItem();

    @Accessor("currentPlayerItem")
    int getItem();

}
