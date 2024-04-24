package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.Item;

public class GetBlockFromItem extends Block {

    public GetBlockFromItem() {
        super("GetBlockFromItem", MainBlockType.Blocks, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "Get totem pops per player";
        this.typesAccepted.add(typesCollapse(MainBlockType.Items));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.block.Block.getBlockFromItem((Item) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getDefaultState().getBlock();
    }

}
