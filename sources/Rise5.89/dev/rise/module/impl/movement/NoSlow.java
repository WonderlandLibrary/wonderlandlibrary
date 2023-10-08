/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.combat.Aura;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Makes you automatically sprint without pressing the sprint key.
 * <p>
 * Wtf is this description
 */
@ModuleInfo(name = "NoSlow", description = "Prevents you from slowing down when using items", category = Category.MOVEMENT)
public final class NoSlow extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "NCP", "Reverse NCP", "Hypixel", "Delay", "Intave");

    //EntityPlayerSP 783

    private boolean aBoolean, blocking, intaveFunnyBoolean;
    private final TimeUtil timer = new TimeUtil();
    private long delay;
    private int ticks;

    @Override
    protected void onEnable() {
        ticks = 0;
        blocking = false;
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        switch (mode.getMode()) {
            case "NCP": {
                if (mc.thePlayer.isBlocking()) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                break;
            }

            case "Delay": {
                if (!mc.thePlayer.isBlocking()) aBoolean = false;

                if (mc.thePlayer.isBlocking() && mc.thePlayer.ticksExisted % 5 == 0 && aBoolean) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

                    aBoolean = false;
                }

                if (mc.thePlayer.isBlocking() && mc.thePlayer.ticksExisted % 5 == 1 && !aBoolean) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));

                    aBoolean = true;
                }
                break;
            }

            case "Reverse NCP": {
                if (mc.thePlayer.isBlocking()) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                }

                break;
            }

            case "Intave":
                if (mc.thePlayer.isBlocking() && timer.hasReached(delay)) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                break;
        }

    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
        switch (mode.getMode()) {
            case "NCP": {
                if (mc.thePlayer.isBlocking()) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                }
                break;
            }

            case "Hypixel": {
                if (mc.thePlayer.isUsingItem() && Aura.target == null)
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                break;
            }

            case "Reverse NCP": {
                if (mc.thePlayer.isBlocking()) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }

                break;
            }

            case "Intave":
                if (mc.thePlayer.isBlocking() && timer.hasReached(delay)) {
                    mc.playerController.syncCurrentPlayItem();
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    delay = 200;
                    if (intaveFunnyBoolean) {
                        delay = 100;
                        intaveFunnyBoolean = false;
                    } else
                        intaveFunnyBoolean = true;
                    timer.reset();
                }
                break;
        }
    }
}
