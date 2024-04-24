package Method.Client.utils.proxy.Overrides;

import net.minecraft.util.math.BlockPos;


public class VisGraphOverride extends net.minecraft.client.renderer.chunk.VisGraph {

    public static boolean stop = false;

    @Override
    public void setOpaqueCube(BlockPos blockPos) {
        if (stop)
            return;
        this.bitSet.set(getIndex(blockPos), true);
        --this.empty;
    }




}
