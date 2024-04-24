package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.ArrayList;
import java.util.Collections;

public class BinarySearch extends Block {

    public BinarySearch() {
        super( "BinarySearch", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.words[0] = "+";
        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns the index of the value in the array, or -1 if not found.";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Collections.binarySearch((ArrayList) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }


}
