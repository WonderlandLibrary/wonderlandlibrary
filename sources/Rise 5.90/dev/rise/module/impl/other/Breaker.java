/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.world.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Objects;

@ModuleInfo(name = "Breaker", description = "Breaks blocks for you", category = Category.OTHER)
public final class Breaker extends Module {

    private float dmg;
    private BlockPos blockPos;
    private Vec3 respawnPoint;

    private final NumberSetting range = new NumberSetting("Range", this, 4, 1, 7, 0.5);
    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);
    private final BooleanSetting cake = new BooleanSetting("Cake", this, true);
    private final BooleanSetting bed = new BooleanSetting("Bed", this, true);
    private final BooleanSetting whitelistOwnBase = new BooleanSetting("Whitelist own bed", this, true);
    private final BooleanSetting breakAbove = new BooleanSetting("Mineplex through walls", this, false);
    private final BooleanSetting rotations = new BooleanSetting("Rotations", this, true);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        blockPos = null;
        if (mc.gameSettings.keyBindAttack.isKeyDown()) return;
        //Cake
        ArrayList<Integer> pos = getBlock(Blocks.cake, (int) range.getValue());

        if (cake.isEnabled()) {
            if (pos != null && mc.thePlayer != null && (respawnPoint == null || mc.thePlayer.getDistance(respawnPoint.xCoord, respawnPoint.yCoord, respawnPoint.zCoord) > 25 || !whitelistOwnBase.isEnabled())) {
                final BlockPos pos2 = new BlockPos(mc.thePlayer.posX + pos.get(0), mc.thePlayer.posY + pos.get(1), mc.thePlayer.posZ + pos.get(2));
                final BlockPos posAbove = new BlockPos(mc.thePlayer.posX + pos.get(0), mc.thePlayer.posY + pos.get(1) + 1, mc.thePlayer.posZ + pos.get(2));
                final BlockPos currentPos = pos2.add(pos.get(0), pos.get(1), pos.get(2));

                if (rotations.isEnabled()) {
                    final float[] rotations = BlockUtil.getRotations(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                }

                if (breakAbove.isEnabled()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                }

                if (!Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Fly")).isEnabled())
                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos2, EnumFacing.UP, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()));

                if (swing.isEnabled()) mc.thePlayer.swingItem();
            }
        }

        //Bed
        if (blockPos == null || mc.theWorld.getBlockState(blockPos).getBlock() != Block.getBlockById(26) && mc.thePlayer != null) {
            pos = getBlock(Blocks.bed, (int) range.getValue());

            if (pos != null)
                blockPos = new BlockPos(mc.thePlayer.posX + Objects.requireNonNull(pos).get(0), mc.thePlayer.posY + Objects.requireNonNull(pos).get(1), mc.thePlayer.posZ + Objects.requireNonNull(pos).get(2));
        }

        if (bed.isEnabled()) {
            if (blockPos != null && (respawnPoint == null || mc.thePlayer.getDistance(respawnPoint.xCoord, respawnPoint.yCoord, respawnPoint.zCoord) > 25 || !whitelistOwnBase.isEnabled())) {
                if (breakAbove.isEnabled()) {
                    final BlockPos posAbove = new BlockPos(mc.thePlayer.posX + blockPos.getX(), mc.thePlayer.posY + blockPos.getY() + 1, mc.thePlayer.posZ + blockPos.getZ());
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                }

                if (rotations.isEnabled()) {
                    final float[] rotations = BlockUtil.getRotations(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                } else if (PlayerUtil.isOnServer("hypixel")) {
                    event.setYaw((float) (Math.random() * 360));
                    event.setPitch((float) (-90 + Math.random() * 180));
                }

                if (dmg == 0) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));

                    if (PlayerUtil.isOnServer("hypixel")) {
                        mc.getNetHandler().addToSendQueueWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                        mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
                    }

                    if (mc.theWorld.getBlockState(blockPos).getBlock().getPlayerRelativeBlockHardness(mc.thePlayer) >= 1) {
                        if (swing.isEnabled()) mc.thePlayer.swingItem();

                        mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);

                        dmg = 0;
                        blockPos = null;
                        return;
                    }
                }

                if (swing.isEnabled()) mc.thePlayer.swingItem();

                dmg += mc.theWorld.getBlockState(blockPos).getBlock().getPlayerRelativeBlockHardness(mc.thePlayer);
                mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), blockPos, (int) (dmg * 10) - 1);

                if (dmg >= 1) {
                    mc.getNetHandler().addToSendQueueWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                    mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);

                    dmg = 0;
                    blockPos = null;
                }
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (!whitelistOwnBase.isEnabled()) return;

        final Packet<?> p = event.getPacket();

        if (p instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) p;
            final double x = s08.getX();
            final double y = s08.getY();
            final double z = s08.getZ();

            if (mc.thePlayer.getDistance(x, y, z) > 40) {
                respawnPoint = new Vec3(x, y, z);
            }
        }
    }

    public ArrayList<Integer> getBlock(final Block b, final int r) {
        final ArrayList<Integer> pos = new ArrayList<>();

        for (int x = -r; x < r; ++x) {
            for (int y = r; y > -r; --y) {
                for (int z = -r; z < r; ++z) {
                    if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z)).getBlock() == b) {
                        pos.add(x);
                        pos.add(y);
                        pos.add(z);
                        return pos;
                    }
                }
            }
        }
        return null;
    }
}
