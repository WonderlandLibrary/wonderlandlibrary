package me.earth.earthhack.impl.core.mixins.minecraft.entity.living.player;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer
{

}
