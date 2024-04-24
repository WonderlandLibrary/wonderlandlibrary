package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.client.CPacketPlayerDigging;

public class PlayerDigging extends Block {

    public PlayerDigging() {
        super("PlayerDigging", MainBlockType.PlayerDigging, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (CPacketPlayerDigging.Action value : CPacketPlayerDigging.Action.values()) {
            ddOptions.add(value.toString());
        }

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return CPacketPlayerDigging.Action.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
