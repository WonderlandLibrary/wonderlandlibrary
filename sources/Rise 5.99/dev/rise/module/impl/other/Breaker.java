package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.motion.TeleportEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.player.RotationUtil;
import dev.rise.util.world.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;

@ModuleInfo(name = "Breaker", description = "Breaks blocks", category = Category.PLAYER)
public class Breaker extends Module {

    public final BooleanSetting bed = new BooleanSetting("Bed", this, true);

    public final BooleanSetting instant = new BooleanSetting("Instant", this, true);
    public final BooleanSetting throughWalls = new BooleanSetting("Through Walls", this, true);
    public final BooleanSetting rotations = new BooleanSetting("Rotate", this, true);
    public final BooleanSetting whiteListOwnBed = new BooleanSetting("Whitelist Own Bed", this, true);

    private Vec3 block, lastBlock, home;
    private double damage;

    @Override
    public void onPreMotion(PreMotionEvent event) {
        lastBlock = block;
        block = this.block();

        if (block == null) {
            return;
        }

        if (this.rotations.isEnabled()) {
            this.rotate(event);
        }

        if (!equals(lastBlock, block)) {
            damage = 0;
        }

        this.destroy();
    }

    public void rotate(final PreMotionEvent event) {
        /* Rotate to get correct EnumFacing */
        final float[] rotations = BlockUtil.getRotations(block.xCoord, block.yCoord, block.zCoord);

        /* Setting rotations */
        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);
    }

    public Vec3 block() {
        if (home != null && mc.thePlayer.getDistanceSq(home.xCoord, home.yCoord, home.zCoord) < 55 * 55 && whiteListOwnBed.isEnabled()) {
            return null;
        }

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {

                    /* Get block */
                    final Block block = PlayerUtil.getBlockRelativeToPlayer(x, y, z);
                    final Vec3 position = new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);

                    if (!(block instanceof BlockBed && bed.isEnabled())) {
                        continue;
                    }

                    if (!throughWalls.isEnabled()) {

                        /* Rotate to get correct EnumFacing */
                        final float[] rotations = BlockUtil.getRotations(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);

                        /* Grab moving object position */
                        final MovingObjectPosition movingObjectPosition = mc.thePlayer.rayTraceCustom(4.5f, rotations[0], rotations[1]);

                        if (movingObjectPosition == null) {
                            continue;
                        }

                        final BlockPos blockPos = movingObjectPosition.getBlockPos();

                        if (!equals(new Vec3(blockPos), position)) {
                            continue;
                        }
                    }

                    return position;
                }
            }
        }

        return null;
    }

    public void updateDamage(final BlockPos blockPos, final double hardness) {
        damage += hardness;
        mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), blockPos, (int) (damage * 10 - 1));
    }

    public void destroy() {
        final BlockPos blockPos = new BlockPos(block.xCoord, block.yCoord, block.zCoord);
        final double hardness = mc.theWorld.getBlockState(blockPos).getBlock()
                .getPlayerRelativeBlockHardness(mc.thePlayer);

        if (instant.isEnabled()) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
            mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);

        } else {
            mc.thePlayer.swingItem();

            if (damage <= 0) {
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));

                if (hardness >= 1) {
                    mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
                    damage = 0;
                }

                this.updateDamage(blockPos, hardness);
            } else if (damage >= 1) {
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
                damage = 0;
                this.updateDamage(blockPos, hardness);
            } else {
                this.updateDamage(blockPos, hardness);
            }
        }
    }

    @Override
    public void onTeleportEvent(TeleportEvent event) {
        final double distance = mc.thePlayer.getDistance(event.getPosX(), event.getPosY(), event.getPosZ());

        if (distance > 40) {
            home = new Vec3(event.getPosX(), event.getPosY(), event.getPosZ());
        }
    }

    public boolean equals(final Vector3d a, final Vector3d b) {
        return ((Math.floor(a.getX()) == Math.floor(b.getX()) &&
                Math.floor(a.getY()) == Math.floor(b.getY()) &&
                Math.floor(a.getZ()) == Math.floor(b.getZ())));
    }

    public boolean equals(final Vec3 a, final Vec3 b) {
        return (b != null && a != null && (Math.floor(a.xCoord) == Math.floor(b.xCoord) &&
                Math.floor(a.yCoord) == Math.floor(b.yCoord) &&
                Math.floor(a.zCoord) == Math.floor(b.zCoord)));
    }

    public boolean equals(final BlockPos a, final Vector3d b) {
        return ((Math.floor(a.getX()) == Math.floor(b.getX()) &&
                Math.floor(a.getY()) == Math.floor(b.getY()) &&
                Math.floor(a.getZ()) == Math.floor(b.getZ())));
    }
}