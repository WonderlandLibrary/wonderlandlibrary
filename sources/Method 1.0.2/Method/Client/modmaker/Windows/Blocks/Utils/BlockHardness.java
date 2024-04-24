package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

public class BlockHardness extends Block {

    public BlockHardness() {
        super("BlockHardness", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Returns the hardness of the block at the given position." + "\n" + "getState(pos).getPlayerRelativeBlockHardness(player(), world(), pos)";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return BlockUtils.getHardness((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
    }

}
