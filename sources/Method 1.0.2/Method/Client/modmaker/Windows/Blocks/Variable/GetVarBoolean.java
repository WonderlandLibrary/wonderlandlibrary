package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldObject;

public class GetVarBoolean extends Block {

    public GetVarBoolean() {
        super("GetVarBoolean", MainBlockType.Boolean, Tabs.Sub);
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((heldObject) dragableBlock.blockPointer).aBoolean;
    }
}
