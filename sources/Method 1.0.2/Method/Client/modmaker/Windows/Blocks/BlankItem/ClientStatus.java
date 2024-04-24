package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.client.CPacketClientStatus;

public class ClientStatus extends Block {

    public ClientStatus() {
        super("ClientStatus", MainBlockType.ClientStatus, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (CPacketClientStatus.State value : CPacketClientStatus.State.values()) {
            ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return CPacketClientStatus.State.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
