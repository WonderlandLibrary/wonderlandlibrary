/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.apache.commons.lang3.RandomUtils;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutoBypass extends Feature {

    public static NumberSetting delay;
    private final List<Packet<?>> packets = new CopyOnWriteArrayList<>();
    private final TimerHelper timerHelper = new TimerHelper();
    public long StopDecompilingClientFaggot = 0L;
    public ListSetting mode = new ListSetting("Disabler Mode", "PingFreeze", () -> true, "PingFreeze", "Destruction", "LimitNet");

    public AutoBypass() {
        super("Auto Bypass", "Отключает некоторые чеки анти читов", Type.Misc);
        delay = new NumberSetting("PingFreezeDelay", 1000, 0, 3000, 0.1F, () -> mode.currentMode.equals("PingFreeze"), NumberSetting.NumberType.MS);
        addSettings(delay);
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setSuffix(mode.currentMode);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mode.currentMode.equals("Matrix")) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook posLook = new SPacketPlayerPosLook();
                posLook.yaw = 90;
            }
        } else if (mode.currentMode.equals("PingFreeze")) {
            if (mc.player.ticksExisted % 100 != 0) {
                if (event.getPacket() instanceof SPacketKeepAlive) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof SPacketCustomPayload) {
                    event.setCancelled(true);
                }
            } else if (mode.currentMode.equals("LimitNet")) {
                StopDecompilingClientFaggot = RandomUtils.nextLong(0L, 10L);
                if (event.getPacket() instanceof SPacketKeepAlive)
                    try {
                        Thread.sleep(150L * StopDecompilingClientFaggot);
                    } catch (Exception exception) {

                    }
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (timerHelper.hasReached(delay.getNumberValue()) && mode.currentMode.equals("PingFreeze")) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cPacketPlayer = (CPacketPlayer) event.getPacket();
                cPacketPlayer.x = mc.player.posX;
                cPacketPlayer.y = mc.player.posY;
                cPacketPlayer.z = mc.player.posZ;
                mc.player.posX = mc.player.prevPosX;
                mc.player.posY = mc.player.prevPosY;
                mc.player.posZ = mc.player.prevPosZ;
                timerHelper.reset();
                if (mc.player.ticksExisted % 100 != 0) {
                    if (event.getPacket() instanceof C00Handshake) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof CPacketClientSettings) {
                        event.setCancelled(true);
                    }
                } else if (mode.currentMode.equals("Destruction")) {
                    if (event.getPacket() instanceof C00Handshake) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof SPacketKeepAlive) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof CPacketClientSettings) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}