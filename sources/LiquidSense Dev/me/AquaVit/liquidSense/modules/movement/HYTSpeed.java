package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;
import java.util.List;

import static net.ccbluex.liquidbounce.utils.MovementUtils.isMoving;
import static net.ccbluex.liquidbounce.utils.MovementUtils.strafe;

@ModuleInfo(name = "HYTSpeed", description = "HYTSpeed :/", category = ModuleCategory.MOVEMENT)
public class HYTSpeed extends Module {
    private final FloatValue speed = new FloatValue("Speed", 0.5f, 0.15f, 8f);
    private final FloatValue motionY = new FloatValue("MotionY", 0.42f, 0.1f, 2f);

    private List<Packet<?>> packets = new ArrayList<>();
    boolean doAsFly = false;
    double stage = 0;
    double timer = 0;

    public void move() {
        if(isMoving()){
            strafe(speed.get());
            mc.thePlayer.motionY = motionY.get();
        }
        return;
    }

    @Override
    public void onEnable() {
        timer = 0;
    }

    @Override
    public void onDisable() {
        doAsFly = false;
        if (packets.size() > 0) {
            for (Packet<?> packet : packets) {
                mc.thePlayer.sendQueue.addToSendQueue(packet);
                packets.remove(packet);
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(!isMoving()){
            timer=0;
        } else {
            timer++;
        }

        if (mc.thePlayer.onGround && timer>1) {
            doAsFly = true;
            stage = 0;
            move();
        }
        if (stage >= 1) {
            doAsFly = false;
            if (packets.size() > 0) {
                for (Packet<?> packet : packets) {
                    mc.thePlayer.sendQueue.addToSendQueue(packet);
                    packets.remove(packet);
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!doAsFly) return;
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer) {
            event.cancelEvent();
            packets.add(packet);
            stage++;
        }
    }
}
