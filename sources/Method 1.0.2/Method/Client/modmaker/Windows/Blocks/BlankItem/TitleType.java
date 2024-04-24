package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketTitle;

public class TitleType extends Block {
    public TitleType() {
        super( "TitleType", MainBlockType.TitleType, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (SPacketTitle.Type value : SPacketTitle.Type.values()) {
            this.ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return SPacketTitle.Type.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
