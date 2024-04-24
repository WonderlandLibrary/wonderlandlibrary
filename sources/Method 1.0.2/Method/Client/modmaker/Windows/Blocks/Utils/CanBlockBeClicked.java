package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

public class CanBlockBeClicked extends Block {

    public CanBlockBeClicked() {
        super("CanBlockBeClicked", MainBlockType.Boolean, Tabs.Utils, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = " returns true if the block can be clicked";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return BlockUtils.canBeClicked((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
