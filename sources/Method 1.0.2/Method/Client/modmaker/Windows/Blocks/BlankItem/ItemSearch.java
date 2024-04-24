package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ItemSearch extends Block {

    public ItemSearch() {
        super("ItemSearch", MainBlockType.Items, Tabs.NewItem,BlockObjects.Name, BlockObjects.ItemSearch);
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.manyItems != null) {
            return dragableBlock.manyItems.storeditem;
        }
        return false;
    }

}
