package Method.Client.modmaker.Windows.Blocks.Array;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldArray;

public class GetArray extends Block {

    public GetArray() {
        super("getArray", MainBlockType.Array, Tabs.Arrays);
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((heldArray) dragableBlock.blockPointer).array;
    }

}
