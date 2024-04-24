package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class UpdateController extends Block {

    public UpdateController() {
        super("UpdateController", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);

        this.description = "Updates the player controller";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.updateController();
        super.runCode(dragableBlock, event);
    }

}
