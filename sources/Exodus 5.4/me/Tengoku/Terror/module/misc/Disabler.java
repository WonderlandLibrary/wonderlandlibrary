/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import java.util.LinkedList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventReceivePacket;
import me.Tengoku.Terror.event.events.EventSendPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

public class Disabler
extends Module {
    private boolean cancel;
    TimerUtils timer;
    private int transactions;
    private ArrayList<Packet> packets = new ArrayList();
    private LinkedList<Packet> packets2 = new LinkedList();
    private Setting strafedelay;
    boolean expectedToTeleport;

    @Override
    public void onEnable() {
        super.onEnable();
        this.expectedToTeleport = false;
        this.packets.clear();
        this.packets2.clear();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Disabler Mode").getValString();
        this.setDisplayName("Disabler \ufffdf" + string);
    }

    @EventTarget
    public void onPacket(EventSendPacket eventSendPacket) {
        Packet packet = eventSendPacket.getPacket();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Disabler Mode").getValString();
        if (string.equalsIgnoreCase("Verus")) {
            if (packet instanceof S3FPacketCustomPayload) {
                eventSendPacket.setCancelled(true);
            }
            if (packet instanceof S00PacketKeepAlive) {
                eventSendPacket.setCancelled(true);
            }
            if (packet instanceof C00Handshake) {
                eventSendPacket.setCancelled(true);
            }
        }
        if (string.equalsIgnoreCase("Spartan")) {
            if (packet instanceof C03PacketPlayer) {
                C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
            }
            if (packet instanceof C00PacketKeepAlive) {
                eventSendPacket.setCancelled(true);
            }
            if (packet instanceof C00Handshake) {
                eventSendPacket.setCancelled(true);
            }
        }
        if (string.equalsIgnoreCase("Verus")) {
            if (packet instanceof C0FPacketConfirmTransaction) {
                eventSendPacket.setCancelled(true);
            }
            if (packet instanceof C08PacketPlayerBlockPlacement) {
                eventSendPacket.setCancelled(true);
            }
            if (packet instanceof C0BPacketEntityAction) {
                eventSendPacket.setCancelled(true);
            }
            if (packet instanceof C07PacketPlayerDigging) {
                eventSendPacket.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Disabler Mode").getValString();
        if (string.equalsIgnoreCase("Verus")) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive());
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C0FPacketConfirmTransaction());
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket eventReceivePacket) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Disabler Mode").getValString();
        Packet packet = eventReceivePacket.getPacket();
    }

    public Disabler() {
        super("Disabler", 0, Category.MISC, "Verus best");
        this.timer = new TimerUtils();
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Spartan");
        arrayList.add("Hypixel");
        arrayList.add("Verus");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Disabler Mode", (Module)this, "Hypixel", arrayList));
    }
}

