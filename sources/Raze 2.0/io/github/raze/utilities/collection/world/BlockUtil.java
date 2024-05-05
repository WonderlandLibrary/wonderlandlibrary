package io.github.raze.utilities.collection.world;

import io.github.raze.utilities.system.Methods;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPosition;
import net.minecraft.util.EnumFacing;

public class BlockUtil implements Methods {

    public static class BlockData {
        public final BlockPosition pos;

        public final EnumFacing facing;

        public BlockData(BlockPosition position, EnumFacing face) {
            this.pos = position;
            this.facing = face;
        }
    }

    public static boolean isBlockUnderPlayer() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {

            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0.0D, -offset, 0.0D);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }

        return false;
    }

}
