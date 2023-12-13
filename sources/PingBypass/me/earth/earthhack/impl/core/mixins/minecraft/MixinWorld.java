package me.earth.earthhack.impl.core.mixins.minecraft;

import me.earth.earthhack.impl.core.ducks.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public abstract class MixinWorld implements IWorld
{
    @Override
    @Invoker(value = "isChunkLoaded")
    public abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty);

}
