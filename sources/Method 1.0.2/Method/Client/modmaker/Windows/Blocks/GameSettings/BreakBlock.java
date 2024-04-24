package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BreakBlock extends Block {

    public BreakBlock() {
        super("BreakBlock", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.typesAccepted.add(typesCollapse(MainBlockType.Facing));
        this.words[0] = "Pos";
        this.words[1] = "Facing";
        this.description = "on Player damage block " + "\n" + "mc.playerController.onPlayerDamageBlock(Pos, Facing)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.onPlayerDamageBlock((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), (EnumFacing) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
        super.runCode(dragableBlock, event);
    }

}
