package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Continue extends Block {

    public Continue() {
        super("Continue loop", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Return/Continue call, Skips current loop code execution";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        super.runCode();
    }

}
