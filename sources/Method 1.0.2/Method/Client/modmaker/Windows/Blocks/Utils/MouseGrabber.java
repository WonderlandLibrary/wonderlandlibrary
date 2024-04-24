package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import org.lwjgl.input.Mouse;

public class MouseGrabber extends Block {

    public MouseGrabber() {
        super("MouseGrabber", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.description = "Grab the mouse";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Mouse.setGrabbed(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }


}
