package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.ItemBlock;


public class IsItemBlock extends Block {

    public IsItemBlock() {
        super("IsItemBlock", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Items));
        this.description = "Returns true if the item is a block.";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBlock;
    }

}
