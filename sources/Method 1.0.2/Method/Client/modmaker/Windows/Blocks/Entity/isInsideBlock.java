package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class isInsideBlock extends Block {

    public isInsideBlock() {
        super("isInsideBlock", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns true if Entity is Insideblock   " + "\n" + "Note not the same as isInsideBlock"
                + "\n" + "Simply checks if the entity is inside the block via the bounding box";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        for (int x = (int) Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).minX; x <
                Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).maxX + 1; ++x) {
            for (int y = (int) Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).minY; y <
                    Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).maxY + 1; ++y) {
                for (int z = (int) Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).minZ; z <
                        Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).maxZ + 1; ++z) {
                    net.minecraft.block.Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (!(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox;
                        if ((boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)),
                                Minecraft.getMinecraft().world, new BlockPos(x, y, z))) != null &&
                                Objects.requireNonNull(((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCollisionBoundingBox()).intersects(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
