package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.ArrayList;

public class ArraySize extends Block {

    public ArraySize() {
        super("ArraySize", MainBlockType.Numbers, Tabs.VarHelper, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
        this.description = "Returns the size of the array";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((ArrayList) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).size();
    }

}
