package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.Contained;

public class If extends Block {

    public If() {
        super("If", MainBlockType.Contained, Tabs.Logic, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.description = "If the boolean is true, run the code inside the block";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event))
            ((Contained) dragableBlock).runContainedCode(dragableBlock, event);
         if (!dragableBlock.local.codeExecuter.breakloop)
            super.runCode(dragableBlock, event);
        else {
            dragableBlock.local.codeExecuter.breakloop = false;
            super.runCode();
        }
    }

}
