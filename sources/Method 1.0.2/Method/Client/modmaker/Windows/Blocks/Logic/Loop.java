package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.Contained;

public class Loop extends Block {

    public Loop() {
        super("Loop", MainBlockType.Contained, Tabs.Logic, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "A loop that runs a block a set amount of times";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }


    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        for (int i = 0; i < dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event); i++) {
            ((Contained) dragableBlock).runContainedCode(dragableBlock, event);
        }
        super.runCode(dragableBlock, event);
    }

}
