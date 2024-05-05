package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.collection.network.EventPacketReceive;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.utilities.collection.math.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.concurrent.CopyOnWriteArrayList;

public class VulcanAirJumpFlight extends ModeFlight {

    public VulcanAirJumpFlight() { super("Vulcan AirJump"); }

    private final TimeUtil timer = new TimeUtil();
    private final CopyOnWriteArrayList<Packet<?>> savedPacketList = new CopyOnWriteArrayList<>();

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            if(timer.elapsed(500, true)) {
                for(Packet <?> packet : savedPacketList){
                    mc.thePlayer.sendQueue.addToSendQueue(packet);
                }
                savedPacketList.clear();
            }
            mc.thePlayer.motionY = 0;

        }
    }

    @Listen
    public void onPacketSend(EventPacketSend event) {
        if(event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            ((C03PacketPlayer.C05PacketPlayerLook) event.getPacket()).yaw = 50;
        }
    }

    @Listen
    public void onPacketReceive(EventPacketReceive event) {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            ((S08PacketPlayerPosLook) event.getPacket()).yaw = 90;
            ((S08PacketPlayerPosLook) event.getPacket()).pitch = 361;
        }
    }

    @Override
    public void onEnable() {
        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 3.5 + Math.random(), mc.thePlayer.posZ);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }
}
