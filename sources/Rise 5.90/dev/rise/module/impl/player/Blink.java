/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This module basically is fake lag, when enabled it cancels every packet and stores them on a list with an ORDER.
 * When the module is disabled all the packets are quickly sent with order.
 * <p>
 * This module has a setting which allows it to only cancel position packets in order to blink infinitely.
 * However this has a cost of being easily detectable with basic checks.
 */
@ModuleInfo(name = "Blink", description = "Lags you serverside", category = Category.PLAYER)
public final class Blink extends Module {

    private final BooleanSetting allPackets = new BooleanSetting("All Packets", this, true);
    private final BooleanSetting showPlayer = new BooleanSetting("Show Player", this, false);
    private final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    private final BooleanSetting pulse = new BooleanSetting("Pulse", this, false);
    private final NumberSetting delayPulse = new NumberSetting("Pulse Delay", this, 20, 4, 100, 0.1);
    private final BooleanSetting randomisePulse = new BooleanSetting("Randomise Pulse", this, false);

    private EntityOtherPlayerMP blinkEntity;

    List<Vec3> path = new ArrayList<>();

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (mc.thePlayer == null || mc.thePlayer.isDead || mc.isSingleplayer() || mc.thePlayer.ticksExisted < 50) {
            packets.clear();
            return;
        }

        if (allPackets.isEnabled()) {
            packets.add(event.getPacket());
            event.setCancelled(true);
        } else {
            if (event.getPacket() instanceof C03PacketPlayer) {
                packets.add(event.getPacket());
                event.setCancelled(true);
            }
        }

        if (pulse.isEnabled()) {
            if (!packets.isEmpty() && mc.thePlayer.ticksExisted % (int) delayPulse.getValue() == 0 && Math.random() > 0.1) {
                packets.forEach(PacketUtil::sendPacketWithoutEvent);
                packets.clear();
            }
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted < 50) return;

        if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
            path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }

        if (pulse.isEnabled()) {
            while (path.size() > (int) delayPulse.getValue()) {
                path.remove(0);
            }
        }

        if (pulse.isEnabled() && blinkEntity != null) {
            mc.theWorld.removeEntityFromWorld(blinkEntity.getEntityId());
        }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        RenderUtil.renderBreadCrumbs(path);
    }

    @Override
    protected void onEnable() {
        path.clear();

        if (!pulse.isEnabled() && showPlayer.isEnabled()) {
            blinkEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            blinkEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            blinkEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
            blinkEntity.setSprinting(mc.thePlayer.isSprinting());
            blinkEntity.setInvisible(mc.thePlayer.isInvisible());
            blinkEntity.setSneaking(mc.thePlayer.isSneaking());

            mc.theWorld.addEntityToWorld(blinkEntity.getEntityId(), blinkEntity);
        }
    }

    @Override
    protected void onDisable() {
        packets.forEach(PacketUtil::sendPacketWithoutEvent);
        packets.clear();

        if (showPlayer.isEnabled()) {
            if (blinkEntity != null) {
                mc.theWorld.removeEntityFromWorld(blinkEntity.getEntityId());
            }
        }
    }

    @Override
    public void onUpdateAlwaysInGui() {
        delayPulse.hidden = !pulse.enabled;
        showPlayer.hidden = pulse.enabled;
    }
}
