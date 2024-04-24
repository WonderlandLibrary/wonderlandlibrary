package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class BlockSearch extends Block {

    public BlockSearch() {
        super("BlockSearch", MainBlockType.Blocks, Tabs.NewItem, BlockObjects.Name, BlockObjects.BlockSearch);
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.manyItems != null) {
            return dragableBlock.manyItems.storedblock;
        }
        return false;
    }
}
