package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Skip extends Block {


    public Skip() {
        super("Skip", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Skip code by jumping ahead: MUST HAVE A NUMBER";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        double skips = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
        if (dragableBlock.nextBlock != null) {
            DragableBlock block = dragableBlock.nextBlock;
            for (int i = 0; i < skips; i++) {
                block = block.nextBlock;
            }
            if (block != null)
                block.runCode(event);
        }
        //   super.runCode(dragableBlock);
    }


}
