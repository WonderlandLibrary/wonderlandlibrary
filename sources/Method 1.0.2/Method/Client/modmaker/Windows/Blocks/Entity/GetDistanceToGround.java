package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class GetDistanceToGround extends Block {

    public GetDistanceToGround() {
        super("GetDistanceToGround", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));

        this.description = "Returns the distance to the ground.";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        Entity entity = ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
        for (int blocks = 0; blocks < 256; ++blocks) {
            BlockPos blockPos = new BlockPos(entity.posX, entity.posY - (double) blocks, entity.posZ);
            if (Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.AIR &&
                    Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.GRASS &&
                    Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.TALLGRASS &&
                    Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.RED_FLOWER &&
                    Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.YELLOW_FLOWER) {
                return entity.posY - (double) blockPos.getY() - 1.0D;
            }
        }

        return 256.0D;
    }

}
