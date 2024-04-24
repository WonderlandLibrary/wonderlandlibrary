package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.ArrayList;

public class ArrayContains extends Block {

    public ArrayContains() {
        super("ArrayContains", MainBlockType.Boolean, Tabs.VarHelper, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
        this.words[0] = "contains";
        this.description = "Returns if the array contains the object";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((ArrayList) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).contains(dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }

}
