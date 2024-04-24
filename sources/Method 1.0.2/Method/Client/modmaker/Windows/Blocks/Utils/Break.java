package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.Contained;

public class Break extends Block {

    public Contained parent;

    public Break() {
        super("Break loop", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Return/Break call, Stops code execution";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        dragableBlock.local.codeExecuter.breakloop = true;
         super.runCode();
    }

}
