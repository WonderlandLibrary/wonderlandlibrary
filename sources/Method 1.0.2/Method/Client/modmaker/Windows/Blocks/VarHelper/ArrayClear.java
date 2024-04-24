package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.ArrayList;

public class ArrayClear extends Block {

    public ArrayClear() {
        super("ArrayClear", MainBlockType.Default, Tabs.VarHelper,BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
        this.description = "Clears the array";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((ArrayList) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).clear();
        super.runCode(dragableBlock, event);
    }

}
