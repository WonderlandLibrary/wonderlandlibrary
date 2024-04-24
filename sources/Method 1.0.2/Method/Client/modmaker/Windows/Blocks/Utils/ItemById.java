package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.Item;

public class ItemById extends Block {

    public ItemById() {
        super("ItemById", MainBlockType.Items, Tabs.Utils, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns the item with the id given";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return  Item.getItemById((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
    }

}
