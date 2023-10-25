package net.minecraft.tileentity;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;

class TileEntity.2
implements Callable<String> {
    TileEntity.2() {
    }

    public String call() throws Exception {
        int i = Block.getIdFromBlock((Block)TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());
        try {
            return String.format((String)"ID #%d (%s // %s)", (Object[])new Object[]{i, Block.getBlockById((int)i).getUnlocalizedName(), Block.getBlockById((int)i).getClass().getCanonicalName()});
        }
        catch (Throwable var3) {
            return "ID #" + i;
        }
    }
}
