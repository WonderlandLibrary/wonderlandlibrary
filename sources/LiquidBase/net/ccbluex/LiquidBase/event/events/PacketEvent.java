package net.ccbluex.LiquidBase.event.events;

import net.ccbluex.LiquidBase.event.CancellableEvent;
import net.minecraft.network.Packet;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class PacketEvent extends CancellableEvent {

    private Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}