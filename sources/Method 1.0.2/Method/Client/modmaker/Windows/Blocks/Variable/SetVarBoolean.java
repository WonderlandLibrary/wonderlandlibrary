package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldObject;

public class SetVarBoolean extends Block {

    public SetVarBoolean() {
        super("SetVarBoolean", MainBlockType.Default, Tabs.Sub,  BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((heldObject) dragableBlock.blockPointer).aBoolean = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event);
        super.runCode(dragableBlock, event);
    }

}
