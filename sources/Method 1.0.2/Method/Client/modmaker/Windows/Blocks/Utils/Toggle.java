package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Toggle extends Block {

    public Toggle() {
        super("Toggle", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Disable this module!";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        dragableBlock.local.toggle();
        dragableBlock.local.codeExecuter.breakloop = true;
        //  super.runCode(dragableBlock, event);
    }

}
