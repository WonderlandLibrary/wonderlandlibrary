package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.util.math.TimerUtil;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleAnnotation(name = "Disabler", category = ModuleCategory.MISC)
public class Disabler extends Module {

    private ModeSetting disablerMode = new ModeSetting("Disabler Mode", this, new String[]{"Hydracraft", "Debug"});

    private final LinkedList<Packet<?>> debugPackets = new LinkedList<>();
    private final LinkedList<Packet<?>> debugPackets1 = new LinkedList<>();
    private final LinkedList<Packet<?>> debugPackets2 = new LinkedList<>();


    private final CopyOnWriteArrayList<C03PacketPlayer> packetPlayers = new CopyOnWriteArrayList<>();

    private C03PacketPlayer lastPacket;

    private final LinkedList<Packet<?>> packets = new LinkedList<>();
    private boolean teleportState;
    private final TimerUtil timerUtil = new TimerUtil();
    private final LinkedList<Packet<?>> transactions = new LinkedList<>();

    private TimerUtil timerUtil2 = new TimerUtil();


    @EventTarget
    public void onUpdate(UpdateEvent event) {

        if (disablerMode.getValue().equalsIgnoreCase("Debug")) {
            if (MC.thePlayer.fallDistance != -1 && MC.thePlayer.ticksExisted % 3 == 0) {
                final double motion = 1F;
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.motionY += motion, MC.thePlayer.posZ, true));
                MC.thePlayer.motionY += motion;
            }
        } else {
            MC.thePlayer.fallDistance = -1;

        }

        if (disablerMode.getValue().equalsIgnoreCase("Hydracraft")) {
            if (MC.thePlayer.ticksExisted % 180 == 0) {
                if (transactions.size() >= 20) {
                    for (int i = 0; i < transactions.size(); i++) {
                        sendPacketUnlogged(transactions.poll());
                    }
                }
            }
            if (timerUtil.reachedTime(510L)) {
                timerUtil.doReset();
                if (!packets.isEmpty()) {
                    sendPacketUnlogged(packets.poll());
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (MC.thePlayer.motionY > 0) {
            if (MC.thePlayer.fallDistance != -1 && MC.thePlayer.ticksExisted % 3 == 0) {
                if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition)
                    event.setCancelled(true);
            }
        }
        if (disablerMode.getValue().equalsIgnoreCase("Hydracraft")) {
            if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                {
                    packets.add(event.getPacket());
                    transactions.add(event.getPacket());
                }

                event.setCancelled(true);

                if (packets.size() > 300) {
                    sendPacketUnlogged(packets.poll());
                }
            }

            if (event.getPacket() instanceof C0BPacketEntityAction)
                event.setCancelled(true);


            if (event.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) event.getPacket();

                if (MC.thePlayer.ticksExisted % 5 == 0) {
                    sendPacketUnlogged(new C0CPacketInput());
                }
                if (MC.thePlayer.ticksExisted % 40 == 0) {
                    teleportState = true;

                    c03PacketPlayer.y = -0.015625;
                    c03PacketPlayer.onGround = false;
                    event.setCancelled(true);
                }
            }

            if (event.getPacket() instanceof S08PacketPlayerPosLook && teleportState) {
                final S08PacketPlayerPosLook packetPlayerPosLook = (S08PacketPlayerPosLook) event.getPacket();
                teleportState = false;

                {
                    event.setCancelled(true);

                    sendPacketUnlogged(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosLook.getX(), packetPlayerPosLook.getY(), packetPlayerPosLook.getZ(), packetPlayerPosLook.getYaw(), packetPlayerPosLook.getPitch(), true));
                }
            }
        }
    }

    @Override
    public void onState() {
        this.transactions.clear();
        this.teleportState = false;
        this.packets.clear();
        this.timerUtil.doReset();
    }

    @Override
    public void onUndo() {

    }
}
