package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SyncCurrentItem extends Block {

    public SyncCurrentItem() {
        super("SyncCurrentItem", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Synchronizes the current item";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.syncCurrentPlayItem();
        super.runCode(dragableBlock, event);
    }

}
