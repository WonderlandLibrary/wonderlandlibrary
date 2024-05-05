package net.minecraft.tileentity;

import java.util.concurrent.Callable;
import net.minecraft.block.state.IBlockState;

class TileEntity.3
implements Callable<String> {
    TileEntity.3() {
    }

    public String call() throws Exception {
        IBlockState iblockstate = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
        int i = iblockstate.getBlock().getMetaFromState(iblockstate);
        if (i < 0) {
            return "Unknown? (Got " + i + ")";
        }
        String s = String.format((String)"%4s", (Object[])new Object[]{Integer.toBinaryString((int)i)}).replace((CharSequence)" ", (CharSequence)"0");
        return String.format((String)"%1$d / 0x%1$X / 0b%2$s", (Object[])new Object[]{i, s});
    }
}
