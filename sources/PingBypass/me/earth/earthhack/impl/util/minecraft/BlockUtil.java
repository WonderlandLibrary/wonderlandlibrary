package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockUtil implements Globals {

    public static void placeBlockMainHand(BlockPos pos) {
        placeBlock(EnumHand.MAIN_HAND, pos);
    }

    /**
     * Convenience method.
     * Places blocks using mainhand.
     *
     * @param hand, mainhand used to place blocks.
     * @param pos, the position where the mainhand will be used.
     */

    public static void placeBlock(EnumHand hand, BlockPos pos) {
        Vec3d eyesPos = new Vec3d(mc.player.posX,
                mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ);

        for (EnumFacing side : EnumFacing.values()) {
            BlockPos blockToPlace = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            /**
             * Check if we can place the block on the position.
             */
            if (!mc.world.getBlockState(blockToPlace).getBlock().canCollideCheck(mc.world.getBlockState(blockToPlace), false))
                continue;

            Vec3d hitVec = new Vec3d(blockToPlace).add(0.5, 0.5, 0.5)
                    .add(new Vec3d(side2.getDirectionVec()).scale(0.5));
            /**
             * Check if the place / hit Vector is within 4.25 blocks.
             * If yes, then it continue the function.
             */
            if (eyesPos.squareDistanceTo(hitVec) > 18.0625)
                continue;

            /**
             * Do the magic, and place the block.
             */

            double diffX = hitVec.x - eyesPos.x;
            double diffY = hitVec.y - eyesPos.y;
            double diffZ = hitVec.z - eyesPos.z;

            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

            float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
            float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

            float[] rotations = {
                    mc.player.rotationYaw
                            + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
                    mc.player.rotationPitch + MathHelper
                            .wrapDegrees(pitch - mc.player.rotationPitch)};

            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0],
                    rotations[1], mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.playerController.processRightClickBlock(mc.player,
                    mc.world, blockToPlace, side2, hitVec, hand);
            mc.player.swingArm(hand);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
