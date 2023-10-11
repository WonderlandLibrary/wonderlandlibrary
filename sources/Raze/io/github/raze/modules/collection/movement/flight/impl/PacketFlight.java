package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.utilities.collection.math.TimeUtil;
import io.github.raze.utilities.collection.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketFlight extends ModeFlight {

    public PacketFlight() { super("Packet"); }

    private final TimeUtil timer = new TimeUtil();

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);

            mc.thePlayer.motionY = 0;

            if(timer.elapsed(200, true)) {
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
            }
        }
    }
}
