package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Not extends Block {

    public Not() {
        super("Not", MainBlockType.Boolean, Tabs.Logic, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Returns the opposite of the boolean value";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return !dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
    }
}
