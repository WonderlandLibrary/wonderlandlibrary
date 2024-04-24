package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockposToVec3d extends Block {

    public BlockposToVec3d() {
        super("BlockposToVec3d", MainBlockType.Vec3d, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Converts Blockpos to Vec3d";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        BlockPos pos = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
        return new Vec3d(pos);
    }

}
