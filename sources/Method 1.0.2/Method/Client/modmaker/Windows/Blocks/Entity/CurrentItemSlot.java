package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class CurrentItemSlot extends Block {

    public CurrentItemSlot() {
        super("CurrentItemSlot", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name);
        this.description = "Returns the current item slot" + "\n" + "mc.player.inventory.currentItem";


    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().player.inventory.currentItem;
    }

}
