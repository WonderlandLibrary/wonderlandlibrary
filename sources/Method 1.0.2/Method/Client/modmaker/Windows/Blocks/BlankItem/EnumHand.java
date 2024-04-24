package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class EnumHand extends Block{
        public EnumHand() {
            super( "EnumHand", MainBlockType.EnumHand, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
            for (net.minecraft.util.EnumHand value : net.minecraft.util.EnumHand.values()) {
                this.ddOptions.add(value.toString());
            }
        }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.util.EnumHand.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
