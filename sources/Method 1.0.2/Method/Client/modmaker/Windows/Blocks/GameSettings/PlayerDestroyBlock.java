package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class PlayerDestroyBlock extends Block {

    public PlayerDestroyBlock() {
        super("PlayerDestroyBlock", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Minecraft.playerController.onPlayerDestroyBlock(BlockPos pos)";


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.onPlayerDestroyBlock((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
