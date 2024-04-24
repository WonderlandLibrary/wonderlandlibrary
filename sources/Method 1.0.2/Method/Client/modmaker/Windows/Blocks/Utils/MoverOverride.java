package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.proxy.Overrides.MoveOverride;

public class MoverOverride extends Block {

    public MoverOverride() {
        super("MoverOverride", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Toggles the mover override" ;
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        MoveOverride.toggle();
        super.runCode(dragableBlock, event);
    }

}
