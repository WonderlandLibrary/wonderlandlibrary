package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketPlayerListItem;

public class ListItem extends Block {
    public ListItem() {
        super("ListItem", MainBlockType.ListItem, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (SPacketPlayerListItem.Action value : SPacketPlayerListItem.Action.values()) {
            this.ddOptions.add(value.toString());
        }


    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return SPacketPlayerListItem.Action.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
