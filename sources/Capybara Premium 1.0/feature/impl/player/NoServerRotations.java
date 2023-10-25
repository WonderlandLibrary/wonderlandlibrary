package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class NoServerRotations extends Feature {

    public NoServerRotations() {
        super("NoServerRotation", "Убирает ротацию со стороны сервера", FeatureCategory.Player);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    }
}