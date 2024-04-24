package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EquipmentSlot extends Block {

    public EquipmentSlot() {
        super("EquipmentSlot", MainBlockType.EquipmentSlot, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (EntityEquipmentSlot value : EntityEquipmentSlot.values()) {
            ddOptions.add(value.toString());
        }

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return EntityEquipmentSlot.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
