package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;

public class Quit extends Block {

    public Quit() {
        super("Quit", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Quits the game (Soft)" ;
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Utils.Quit();
        super.runCode(dragableBlock, event);
    }

}
