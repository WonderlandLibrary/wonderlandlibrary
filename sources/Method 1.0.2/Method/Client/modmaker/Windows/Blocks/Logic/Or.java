package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Or extends Block {

    public Or() {
        super("Or", MainBlockType.Boolean, Tabs.Logic, BlockObjects.BooleanTextEnter, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Returns true if either of the two inputs are true.";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event) || dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,1, event);
    }
}
