package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.Main;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class GetHoles extends Block {

    public GetHoles() {
        super("GetHoles", MainBlockType.Array, Tabs.Utils, BlockObjects.Name);
        this.description = "Get Holes";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Main.holeManager.holes;
    }

}
