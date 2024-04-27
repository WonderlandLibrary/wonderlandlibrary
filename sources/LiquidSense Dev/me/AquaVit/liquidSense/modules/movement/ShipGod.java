package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ShipGod extends Module {
    private Entity ridingTarget;

    @Override
    public void onDisable() {
        if (ridingTarget != null) {
            ridingTarget.isDead = false;
            if (!mc.thePlayer.isRiding()) {
                mc.theWorld.spawnEntityInWorld(ridingTarget);
                mc.thePlayer.mountEntity(ridingTarget);
            }
            ridingTarget = null;
            //ClientUtil.sendMessage("Forced a remount.");
        }
    }

    @Override
    public void onEnable() {
        ridingTarget = null;
        if (!(mc.thePlayer.ridingEntity instanceof EntityBoat)) {
            //ClientUtil.sendMessage("你没有在一个船上");
            return;
        }
        ridingTarget = mc.thePlayer.ridingEntity;
        //Disabler.spoof(true);
        mc.thePlayer.dismountEntity(ridingTarget);
        mc.theWorld.removeEntity(ridingTarget);
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ, true));
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        switch(event.getEventState()) {
            case PRE: {
                if (ridingTarget == null)
                    return;
                if (mc.thePlayer.isRiding())
                    return;
//            Disabler.spoof(true);
//            mc.thePlayer.onGround = true;
                ridingTarget.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
//            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
//            mc.thePlayer.sendQueue.addToSendQueueSilent(new CPacketVehicleMove(ridingTarget));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(ridingTarget.posX, ridingTarget.posY, ridingTarget.posZ, ridingTarget.rotationYaw, ridingTarget.rotationPitch, true));
//            Disabler.spoof(false);
                break;
            }

            case POST:
                break;
        }
    }
}
