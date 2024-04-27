package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "NoRotateSet", description = "Prevents the server from rotating your head.", category = ModuleCategory.MISC)
public class NoRotateSet extends Module {
    private BoolValue confirmValue = new BoolValue("Confirm", true);
    private BoolValue illegalRotationValue = new BoolValue("ConfirmIllegalRotation", false);
    private BoolValue noZeroValue = new BoolValue("NoZero", false);

    @EventTarget
    public void onPacket(PacketEvent event){
        Packet<?> packet = event.getPacket();

        if (mc.thePlayer == null)return;

        if (packet instanceof S08PacketPlayerPosLook) {
            if (noZeroValue.get() && ((S08PacketPlayerPosLook) packet).getYaw() == 0F && ((S08PacketPlayerPosLook) packet).getPitch() == 0F)
                return;

            if (illegalRotationValue.get() || ((S08PacketPlayerPosLook) packet).getPitch() <= 90 && ((S08PacketPlayerPosLook) packet).getPitch() >= -90 &&
                    RotationUtils.serverRotation != null && ((S08PacketPlayerPosLook) packet).getYaw() != RotationUtils.serverRotation.getYaw() &&
                    ((S08PacketPlayerPosLook) packet).getPitch() != RotationUtils.serverRotation.getPitch()) {

                if (confirmValue.get())
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(((S08PacketPlayerPosLook) packet).getYaw(), ((S08PacketPlayerPosLook) packet).getPitch(), mc.thePlayer.onGround));
            }

            ((S08PacketPlayerPosLook)packet).yaw = mc.thePlayer.rotationYaw;
            ((S08PacketPlayerPosLook)packet).pitch = mc.thePlayer.rotationPitch;
        }
    }
}
