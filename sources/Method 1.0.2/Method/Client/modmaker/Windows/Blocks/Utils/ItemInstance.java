package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.Item;

public class ItemInstance extends Block {

    public ItemInstance() {
        super("ItemInstance", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter,BlockObjects.Words,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Items));
        this.typesAccepted.add(typesCollapse(MainBlockType.Items));
        this.description = "Returns true if input item is an instance of the item";
    this.words[0] = "instanceof";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {

        return ((Item) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getClass().isInstance(dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));

    }

}
