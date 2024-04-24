package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.client.CPacketUseEntity;

public class UseEntity extends Block {

    public UseEntity() {
        super("UseEntity", MainBlockType.UseEntity, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (CPacketUseEntity.Action value : CPacketUseEntity.Action.values()) {
            this.ddOptions.add(value.toString());
        }

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return CPacketUseEntity.Action.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
