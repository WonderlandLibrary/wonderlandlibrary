package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.RayTraceResult;

public class RaytraceEntityHit extends Block {

    public RaytraceEntityHit() {
        super("RaytraceEntityHit", MainBlockType.Entity, Tabs.Utils, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.description = "Returns the look vector of the player entityHit";
        this.typesAccepted.add(typesCollapse(MainBlockType.RayTraceResult));

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((RayTraceResult)dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).entityHit;
    }

}
