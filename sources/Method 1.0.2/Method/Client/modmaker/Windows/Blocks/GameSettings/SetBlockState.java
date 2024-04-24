package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class SetBlockState extends Block {

    public SetBlockState() {
        super("SetBlockState", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.words[0] = "BlockState";
        this.typesAccepted.add(typesCollapse(MainBlockType.IBlockState));
        this.description = "Change block state, Client side" + "\n" + "mc.world.setBlockState(BlockPos, IBlockState)";


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().world.setBlockState((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
        super.runCode(dragableBlock, event);
    }

}
