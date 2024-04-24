package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.managers.FriendManager;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;


public class IsFriend extends Block {

    public IsFriend() {
        super("IsFriend", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns true if the player is your friend.";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return FriendManager.isFriend((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
