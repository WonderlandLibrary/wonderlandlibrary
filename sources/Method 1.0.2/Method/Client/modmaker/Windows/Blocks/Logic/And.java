package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class And extends Block {
    public And() {
        super( "And", MainBlockType.Boolean, Tabs.Logic, BlockObjects.BooleanTextEnter, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.description = "Returns true if both sides are true";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event) && dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,1, event);
    }
}
