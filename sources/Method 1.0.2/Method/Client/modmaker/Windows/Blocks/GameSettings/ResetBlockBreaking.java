package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class ResetBlockBreaking extends Block {

    public ResetBlockBreaking() {
        super("ResetBlockBreaking", MainBlockType.Default, Tabs.Game, BlockObjects.Name);
        this.description = "Reset block Breaking" + "\n" + "mc.playerController.resetBlockRemoving()";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.resetBlockRemoving();
        super.runCode(dragableBlock, event);
    }

}
