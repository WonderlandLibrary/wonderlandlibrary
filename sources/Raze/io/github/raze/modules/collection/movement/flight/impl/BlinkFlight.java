package io.github.raze.modules.collection.movement.flight.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.utilities.collection.math.TimeUtil;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.concurrent.CopyOnWriteArrayList;

public class BlinkFlight extends ModeFlight {

    public BlinkFlight() { super("Blink"); }

    private final CopyOnWriteArrayList<Packet<?>> savedPacketList = new CopyOnWriteArrayList<>();

    private final TimeUtil timer = new TimeUtil();
    private boolean isBlink;

    @Listen
    public void onPacketSend(EventPacketSend eventPacketSend) {
        if(eventPacketSend.getPacket() instanceof C03PacketPlayer && isBlink) {
            savedPacketList.add(eventPacketSend.getPacket());
            eventPacketSend.setCancelled(true);
        }
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if(eventMotion.getState() == Event.State.PRE) {
            mc.thePlayer.motionY = 0;
            mc.timer.timerSpeed = 1.1F;
            MoveUtil.setSpeed(0.25763118F);
            timer.reset();
            if(timer.elapsed(3000, false)){
                eventMotion.onGround = true;
                mc.thePlayer.sendQueue.addToSendQueue(
                        new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true)
                );
                timer.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        isBlink = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        for(Packet<?> packet : savedPacketList){
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        savedPacketList.clear();
        mc.timer.timerSpeed = 1.0f;
        isBlink = false;
        super.onDisable();
    }

}
