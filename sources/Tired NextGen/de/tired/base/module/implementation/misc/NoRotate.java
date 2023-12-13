package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleAnnotation(name = "NoRotate", category = ModuleCategory.PLAYER)
public class NoRotate extends Module {

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();

            MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(playerPosLook.yaw, playerPosLook.pitch, MC.thePlayer.onGround));

            playerPosLook.yaw = MC.thePlayer.rotationYaw;
            playerPosLook.pitch = MC.thePlayer.rotationPitch;

        }

    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
