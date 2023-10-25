package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

static final class BlockBeacon.1
implements Runnable {
    final /* synthetic */ World val$worldIn;
    final /* synthetic */ BlockPos val$glassPos;

    BlockBeacon.1(World world, BlockPos blockPos) {
        this.val$worldIn = world;
        this.val$glassPos = blockPos;
    }

    public void run() {
        BlockPos blockpos;
        Chunk chunk = this.val$worldIn.getChunkFromBlockCoords(this.val$glassPos);
        for (int i = this.val$glassPos.getY() - 1; i >= 0 && chunk.canSeeSky(blockpos = new BlockPos(this.val$glassPos.getX(), i, this.val$glassPos.getZ())); --i) {
            IBlockState iblockstate = this.val$worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() != Blocks.beacon) continue;
            ((WorldServer)this.val$worldIn).addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */);
        }
    }
}
