package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.client.CPacketEntityAction;

public class EntityAction extends Block {

    public EntityAction() {
        super("EntityAction", MainBlockType.EntityAction, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (CPacketEntityAction.Action value : CPacketEntityAction.Action.values()) {
            ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return CPacketEntityAction.Action.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
