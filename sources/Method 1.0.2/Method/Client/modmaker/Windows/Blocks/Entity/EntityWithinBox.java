package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityWithinBox extends Block {

    public EntityWithinBox() {
        super("EntityWithinBox", MainBlockType.Array, Tabs.Entity, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BoundingBox));
        this.description = "Returns Array of Entitys in a BoundingBox" + "\n" + "mc.world.getEntitiesWithinAABBExcludingEntity(BoundingBox)";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().world.getEntitiesWithinAABBExcludingEntity(null, (AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
