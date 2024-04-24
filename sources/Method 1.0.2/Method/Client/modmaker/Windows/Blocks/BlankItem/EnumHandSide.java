package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class EnumHandSide extends Block{
        public EnumHandSide() {
            super( "EnumHandSide", MainBlockType.EnumHandSide, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
            for (net.minecraft.util.EnumHandSide value : net.minecraft.util.EnumHandSide.values()) {
                this.ddOptions.add(value.toString());
            }
        }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.util.EnumHandSide.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
