package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityVec3d extends Block {

    public EntityVec3d() {
        super("EntityVec3d", MainBlockType.Vec3d, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.TileEntity, MainBlockType.Entity));
        this.description = "Gets position of Entity  " + "\n" + "Entity.getvec3d()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof TileEntity){
            BlockPos blockpos = ((TileEntity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getPos();
            return new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
        else if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof net.minecraft.entity.Entity)
            return ((net.minecraft.entity.Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getPositionVector();
        return null;
    }

}
