package net.minecraft.world;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;

class World.2
implements Callable<String> {
    final /* synthetic */ Block val$blockIn;

    World.2(Block block) {
        this.val$blockIn = block;
    }

    public String call() throws Exception {
        try {
            return String.format((String)"ID #%d (%s // %s)", (Object[])new Object[]{Block.getIdFromBlock((Block)this.val$blockIn), this.val$blockIn.getUnlocalizedName(), this.val$blockIn.getClass().getCanonicalName()});
        }
        catch (Throwable var2) {
            return "ID #" + Block.getIdFromBlock((Block)this.val$blockIn);
        }
    }
}
