package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayShuffle extends Block {

    public ArrayShuffle() {
        super("ArrayShuffle", MainBlockType.Default, Tabs.VarHelper,BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
        this.description = " Shuffles the array";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Collections.shuffle((ArrayList) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
