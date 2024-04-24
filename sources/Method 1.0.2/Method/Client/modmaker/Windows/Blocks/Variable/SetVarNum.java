package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldObject;

public class SetVarNum extends Block {

    public SetVarNum() {
        super("SetVarNum", MainBlockType.Default, Tabs.Sub, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((heldObject) dragableBlock.blockPointer).value = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
        super.runCode(dragableBlock, event);
    }

}
