package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Vec3dToBlockpos extends Block {

    public Vec3dToBlockpos() {
        super("Vec3dToBlockpos", MainBlockType.BlockPos, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.description = "Converts Vec3d to Blockpos";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        Vec3d pos = (Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);

        return new BlockPos(pos);
    }

}
