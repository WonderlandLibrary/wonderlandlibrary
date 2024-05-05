package sudo.mixins.accessors;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

@Mixin(Chunk.class)
public interface ChunkAccessor {
    @Accessor("blockEntities")
    Map<BlockPos, BlockEntity> getBlockEntities();
}
