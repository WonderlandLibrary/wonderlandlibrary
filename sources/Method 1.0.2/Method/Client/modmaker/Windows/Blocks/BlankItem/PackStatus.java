package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.client.CPacketResourcePackStatus;

public class PackStatus extends Block {

    public PackStatus() {
        super("PackStatus", MainBlockType.PackStatus, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (CPacketResourcePackStatus.Action value : CPacketResourcePackStatus.Action.values()) {
            this.ddOptions.add(value.toString());
        }

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return CPacketResourcePackStatus.Action.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
