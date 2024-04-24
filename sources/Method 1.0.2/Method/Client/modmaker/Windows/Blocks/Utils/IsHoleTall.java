package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.managers.Hole;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;


public class IsHoleTall extends Block {

    public IsHoleTall() {
        super("IsHoleTall", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Hole));
        this.description = "Returns true if the hole is tall";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((Hole) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isTall();
    }

}
