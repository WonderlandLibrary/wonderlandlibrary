package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class GetStackInSlot extends Block {

    public GetStackInSlot() {
        super("GetStackInSlot", MainBlockType.ItemStack, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "Returns the itemstack in the slot";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().player.inventory.getStackInSlot((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
    }

}
