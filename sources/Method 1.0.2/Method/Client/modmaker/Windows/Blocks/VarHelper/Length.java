package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Length extends Block {

    public Length() {
        super("Length", MainBlockType.Numbers, Tabs.VarHelper, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns the length of the string";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).length();
    }


}
