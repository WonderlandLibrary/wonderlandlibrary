package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ParseNumber extends Block {

    public ParseNumber() {
        super("ParseNumber", MainBlockType.Numbers, Tabs.VarHelper, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns the number in the string as a double";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Double.parseDouble((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }


}
