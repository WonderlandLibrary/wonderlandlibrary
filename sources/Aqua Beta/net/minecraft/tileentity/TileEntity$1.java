package net.minecraft.tileentity;

import java.util.concurrent.Callable;
import net.minecraft.tileentity.TileEntity;

/*
 * Exception performing whole class analysis ignored.
 */
class TileEntity.1
implements Callable<String> {
    TileEntity.1() {
    }

    public String call() throws Exception {
        return (String)TileEntity.access$000().get((Object)TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
    }
}
