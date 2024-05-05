package club.async.module.impl.player;

import club.async.event.impl.EventPacket;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "NoRotate", description = "Block server rotations", category = Category.PLAYER)
public class NoRotate extends Module {

    @Handler
    public void packet(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packetPlayerPosLook = (S08PacketPlayerPosLook) eventPacket.getPacket();
            packetPlayerPosLook.yaw = mc.thePlayer.rotationYaw;
            packetPlayerPosLook.pitch = mc.thePlayer.rotationPitch;
        }
    }

}
