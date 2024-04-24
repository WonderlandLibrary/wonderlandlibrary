package Method.Client.utils.proxy.Overrides;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Set;

public class RenderGlobalOverride extends net.minecraft.client.renderer.RenderGlobal {

    public RenderGlobalOverride(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public Set<EnumFacing> getVisibleFacings(BlockPos pos) {
        VisGraphOverride VisGraphOverride = new VisGraphOverride();
        BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
        Chunk chunk = this.world.getChunk(blockpos);

        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15))) {
            if (chunk.getBlockState(blockpos$mutableblockpos).isOpaqueCube()) {
                VisGraphOverride.setOpaqueCube(blockpos$mutableblockpos);
            }
        }

        return VisGraphOverride.getVisibleFacings(pos);


    }



}
