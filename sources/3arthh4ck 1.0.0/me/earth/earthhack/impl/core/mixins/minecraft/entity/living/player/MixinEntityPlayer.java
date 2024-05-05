package me.earth.earthhack.impl.core.mixins.minecraft.entity.living.player;

import me.earth.earthhack.impl.core.mixins.minecraft.entity.living.MixinEntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase
{

}
