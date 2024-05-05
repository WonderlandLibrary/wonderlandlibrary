package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "Freeze",description = "Let you stop motion.",category = ModuleCategory.PLAYER)
public class Freeze extends Module {
    private double x;
    private double y;
    private double z;
    private double motionX;
    private double motionY;
    private double motionZ;

    @Override
    public void onEnable() {
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        motionX = mc.thePlayer.motionX;
        motionY = mc.thePlayer.motionY;
        motionZ = mc.thePlayer.motionZ;
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.setPositionAndRotation(x,y,z,mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch);
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            x = ((S08PacketPlayerPosLook) packetEvent.getPacket()).getX();
            y = ((S08PacketPlayerPosLook) packetEvent.getPacket()).getY();
            z = ((S08PacketPlayerPosLook) packetEvent.getPacket()).getZ();
            motionX = 0;
            motionY = 0;
            motionZ = 0;
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.motionX = motionX;
        mc.thePlayer.motionY = motionY;
        mc.thePlayer.motionZ = motionZ;
    }
}
