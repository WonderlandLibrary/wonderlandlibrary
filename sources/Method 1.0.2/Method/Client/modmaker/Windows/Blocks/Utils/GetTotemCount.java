package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GetTotemCount extends Block {

    public GetTotemCount() {
        super("GetTotemCount", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name);
        this.description = "Returns the position of the player's eyes";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        int totem = 0;
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING))
            totem++;

        for (ItemStack itemStack : Minecraft.getMinecraft().player.inventory.mainInventory) {
            if (itemStack.getItem().equals(Items.TOTEM_OF_UNDYING))
                totem += itemStack.getCount();
        }
        return totem;
    }

}
