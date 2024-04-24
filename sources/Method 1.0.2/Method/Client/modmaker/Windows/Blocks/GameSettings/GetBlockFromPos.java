package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GetBlockFromPos extends Block {

    public GetBlockFromPos() {
        super("GetBlockFromPos", MainBlockType.Blocks, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Given block Pos return block" + "\n" + "mc.world.getBlockState(pos).getBlock()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().world.getBlockState((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getBlock();
    }

}
