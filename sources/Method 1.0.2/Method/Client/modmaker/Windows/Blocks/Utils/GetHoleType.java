package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.managers.Hole;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;


public class GetHoleType extends Block {

    public GetHoleType() {
        super("GetHoleType", MainBlockType.HoleType, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Hole));
        this.description = "Returns the type of hole";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((Hole) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getHoleType();
    }

}
