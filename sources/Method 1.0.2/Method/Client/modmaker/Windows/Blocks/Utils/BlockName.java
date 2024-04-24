package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class BlockName extends Block {

    public BlockName() {
        super("BlockName", MainBlockType.String, Tabs.Utils, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Blocks));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return "" + net.minecraft.block.Block.REGISTRY.getNameForObject((net.minecraft.block.Block) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
