package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.RayTraceResult;

public class RaytraceBlockPos extends Block {

    public RaytraceBlockPos() {
        super("RaytraceBlockPos", MainBlockType.BlockPos, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.RayTraceResult));
        this.description = "Returns the blockpos of the block that was hit by the raytrace";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((RayTraceResult) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getBlockPos();
    }

}
