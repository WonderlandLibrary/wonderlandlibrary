package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class GetHoleBlockpos extends Block {

    public GetHoleBlockpos() {
        super("GetHoleBlockpos", MainBlockType.BlockPos, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "Get Hole Blockpos";
        this.typesAccepted.add(typesCollapse(MainBlockType.Hole));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
    }

}
