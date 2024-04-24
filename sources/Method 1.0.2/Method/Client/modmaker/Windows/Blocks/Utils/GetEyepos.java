package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;

public class GetEyepos extends Block {

    public GetEyepos() {
        super("GetEyepos", MainBlockType.Vec3d, Tabs.Utils, BlockObjects.Name);
        this.description = "Returns the position of the player's eyes";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Utils.getEyesPos();
    }

}
