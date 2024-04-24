package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class GetDistanceToBlock extends Block {

    public GetDistanceToBlock() {
        super("GetDistanceToBlock", MainBlockType.Numbers, Tabs.Utils, BlockObjects.NumericalTextEnter,BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Returns the distance between two objects";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((Entity)dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getDistanceSq((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }

}
