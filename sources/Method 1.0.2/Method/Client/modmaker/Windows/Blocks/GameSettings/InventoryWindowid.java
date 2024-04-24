package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class InventoryWindowid extends Block {

    public InventoryWindowid() {
        super("InventoryWindowid", MainBlockType.Numbers, Tabs.Game, BlockObjects.Name);
        this.description = "Return current Inventory (Window) id" + "\n" + "mc.player.inventoryContainer.windowId";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().player.inventoryContainer.windowId;
    }

}
