package wtf.evolution.module.impl.Movement;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(name = "Spider", type = Category.Movement)
public class Spider extends Module {

    public Counter c = new Counter();

    public ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Sunrise").call(this);

    @EventTarget
    public void onMotion(EventMotion e) {
        if (mode.is("Matrix")) {
            if (mc.player.collidedHorizontally) {
                if (c.hasReached(100)) {
                    e.setOnGround(true);
                    mc.player.onGround = true;
                    mc.player.jump();
                    c.reset();
                }
            }
        }
        if (mode.is("Sunrise")) {
            if (mc.player.collidedHorizontally) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                int find = -2;
                for (int i = 0; i <= 8; i++)
                    if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)
                        find = i;

                if (find == -2)
                    return;

                BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
                EnumFacing side = getPlaceableSide(pos);
                if (side != null) {
                    mc.getConnection().sendPacket(new CPacketHeldItemChange(find));

                    BlockPos neighbour = new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)
                            .offset(side);
                    EnumFacing opposite = side.getOpposite();

                    Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5)
                            .add(new Vec3d(opposite.getDirectionVec()).scale(0.5));

                    Vec2f rotation = getRotationTo(hitVec);
                    e.setPitch(rotation.y);
                    e.setYaw(rotation.x);

                    float x = (float) (hitVec.x - (double) neighbour.getX());
                    float y = (float) (hitVec.y - (double) neighbour.getY());
                    float z = (float) (hitVec.z - (double) neighbour.getZ());

                    mc.player.connection.sendPacket(
                            new CPacketPlayerTryUseItemOnBlock(neighbour, opposite, EnumHand.MAIN_HAND, x, y, z));
                    mc.player.connection.sendPacket(
                            new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                                neighbour, opposite));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                neighbour, opposite));
                    mc.player.connection.sendPacket(
                            new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                }
            }
            if (mc.player.collidedHorizontally) {
                if (c.hasReached(100)) {
                    e.setOnGround(true);
                    mc.player.onGround = true;
                    mc.player.jump();
                    c.reset();
                }
            }
        }
    }

    public EnumFacing getPlaceableSide(BlockPos pos) {

        for (EnumFacing side : EnumFacing.values()) {

            BlockPos neighbour = pos.offset(side);

            if (mc.world.isAirBlock(neighbour)) {
                continue;
            }

            IBlockState blockState = getState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
                return side;
            }
        }

        return null;
    }


    private Vec2f getRotationTo(Vec3d posTo) {
        EntityPlayerSP player = mc.player;
        return player != null ? getRotationTo(player.getPositionEyes(1.0f), posTo) : Vec2f.ZERO;
    }
    public  Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public  IBlockState getState(BlockPos blockPos) {
        return mc.world.getBlockState(blockPos);
    }

    private Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        return getRotationFromVec(posTo.subtract(posFrom));
    }

    private Vec2f getRotationFromVec(Vec3d vec) {
        double lengthXZ = Math.hypot(vec.x, vec.z);
        double yaw = normalizeAngle(
                getFixedRotation((float) (Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0)));
        double pitch = normalizeAngle(
                getFixedRotation((float) (Math.toDegrees(-Math.atan2(vec.y, lengthXZ)))));

        return new Vec2f((float) yaw, (float) pitch);
    }

    public float getFixedRotation(float value) {
        // using setting sensitivity, but matrix integrated sensitive time checker (to
        // machine learning)
        float f1 = (f1 = (float) (mc.gameSettings.mouseSensitivity * .6 + .2)) * f1 * f1 * 8;
        return Math.round(value / (float) (f1 * .15)) * (float) (f1 * .15);
    }

    public double normalizeAngle(double angle) {
        angle %= 360.0;

        if (angle >= 180.0) {
            angle -= 360.0;
        }

        if (angle < -180.0) {
            angle += 360.0;
        }

        return angle;
    }

}
