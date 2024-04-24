package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class EnumFacing extends Block {

    public EnumFacing() {
        super("EnumFacing", MainBlockType.Facing, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (net.minecraft.util.EnumFacing value : net.minecraft.util.EnumFacing.values()) {
            this.ddOptions.add(value.toString());
        }

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        for (net.minecraft.util.EnumFacing value : net.minecraft.util.EnumFacing.values()) {
            if (value.toString().equals(dragableBlock.dropDowns.getSelected()))
                return value;
        }
        return null;
    }



}
