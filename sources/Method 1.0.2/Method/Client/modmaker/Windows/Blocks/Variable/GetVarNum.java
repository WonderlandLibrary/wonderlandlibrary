package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldObject;

public class GetVarNum extends Block {

    public GetVarNum() {
        super("GetVarNum", MainBlockType.Numbers, Tabs.Sub);
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((heldObject) dragableBlock.blockPointer).value;
    }

}
