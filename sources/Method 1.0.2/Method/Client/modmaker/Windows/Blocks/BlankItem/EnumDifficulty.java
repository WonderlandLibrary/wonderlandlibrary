package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class EnumDifficulty extends Block{
        public EnumDifficulty() {
            super( "EnumDifficulty", MainBlockType.EnumDifficulty, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
            for (net.minecraft.world.EnumDifficulty value : net.minecraft.world.EnumDifficulty.values()) {
                this.ddOptions.add(value.toString());
            }
        }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.world.EnumDifficulty.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
