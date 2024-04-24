package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class DSetting extends Block {

    public DSetting(String name, MainBlockType type, BlockObjects... objects) {
        super(name, type, Tabs.Sub, objects);
    }


    // Override
    public void save(DragableBlock activeBlock) {
    }
}
