package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.EnumConnectionState;

public class ConnectionState extends Block {

    public ConnectionState() {
        super("ConnectionState", MainBlockType.ConnectionState, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (EnumConnectionState value : EnumConnectionState.values()) {
            this.ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return EnumConnectionState.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
