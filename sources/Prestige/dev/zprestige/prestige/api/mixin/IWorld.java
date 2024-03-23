package dev.zprestige.prestige.api.mixin;

import java.util.List;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={World.class})
public interface IWorld {
    @Accessor(value="blockEntityTickers")
    List<BlockEntityTickInvoker> getBlockEntityTickers();
}
