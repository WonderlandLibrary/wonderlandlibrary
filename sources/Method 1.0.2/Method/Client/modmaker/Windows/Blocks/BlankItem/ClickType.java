package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ClickType extends Block {
    public ClickType() {
        super("ClickType", MainBlockType.ClickType, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (net.minecraft.inventory.ClickType value : net.minecraft.inventory.ClickType.values()) {
            this.ddOptions.add(value.toString());
        }
    }
    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.inventory.ClickType.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
