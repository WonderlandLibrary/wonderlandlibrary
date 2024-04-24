package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class chatVisibility extends Block {

    public chatVisibility() {
        super("chatVisibility", MainBlockType.ChatVisibility, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (EntityPlayer.EnumChatVisibility value : EntityPlayer.EnumChatVisibility.values()) {
            this.ddOptions.add(value.toString());
        }

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return  EntityPlayer.EnumChatVisibility.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
