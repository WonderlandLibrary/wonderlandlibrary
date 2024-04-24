package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class GameType extends Block {
    public GameType() {
        super( "GameType", MainBlockType.GameType, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (net.minecraft.world.GameType value : net.minecraft.world.GameType.values()) {
            this.ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.world.GameType.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
