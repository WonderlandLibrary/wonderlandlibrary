package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.game.EventUpdate;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class VerusFlight extends ModeFlight {

    public VerusFlight() { super("Verus"); }

    @Listen
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.motionY < 0.4) {
            mc.thePlayer.motionY = 0.0;
        }

        mc.thePlayer.onGround = true;
    }

    @Listen
    public void onPacket(EventPacketSend event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) packet;

            c03PacketPlayer.onGround = true;
        }
    }
}
