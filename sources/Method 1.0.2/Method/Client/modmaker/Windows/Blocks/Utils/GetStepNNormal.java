package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;

public class GetStepNNormal extends Block {

    public GetStepNNormal() {
        super("GetStepNNormal", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name);
        this.description = "Get Step N Normal";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.stepHeight = 0.5F;
        double max_y = -1.0D;
        AxisAlignedBB grow = Minecraft.getMinecraft().player.getEntityBoundingBox().offset(0.0D, 0.05D, 0.0D).grow(0.05D);
        if (!Minecraft.getMinecraft().world.getCollisionBoxes(Minecraft.getMinecraft().player, grow.offset(0.0D, 2.0D, 0.0D)).isEmpty()) {
            return 100.0D;
        } else {
            for (AxisAlignedBB aabb : Minecraft.getMinecraft().world.getCollisionBoxes(Minecraft.getMinecraft().player, grow)) {
                if (aabb.maxY > max_y) {
                    max_y = aabb.maxY;
                }
            }
            return max_y - Minecraft.getMinecraft().player.posY;
        }
    }

}
