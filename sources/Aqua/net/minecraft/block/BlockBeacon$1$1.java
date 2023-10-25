package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;

class BlockBeacon.1
implements Runnable {
    final /* synthetic */ BlockPos val$blockpos;

    BlockBeacon.1(BlockPos blockPos) {
        this.val$blockpos = blockPos;
    }

    public void run() {
        TileEntity tileentity = val$worldIn.getTileEntity(this.val$blockpos);
        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileentity).updateBeacon();
            val$worldIn.addBlockEvent(this.val$blockpos, (Block)Blocks.beacon, 1, 0);
        }
    }
}
