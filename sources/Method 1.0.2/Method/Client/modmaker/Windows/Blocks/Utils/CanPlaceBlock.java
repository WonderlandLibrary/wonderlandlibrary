package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

public class CanPlaceBlock extends Block {

    public CanPlaceBlock() {
        super("CanPlaceBlock", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Returns true if the block can be placed at the given position.";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return BlockUtils.CanPlaceBlock((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
    }

}
