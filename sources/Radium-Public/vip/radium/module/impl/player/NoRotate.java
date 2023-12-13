package vip.radium.module.impl.player;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "No Rotate", category = ModuleCategory.PLAYER)
public final class NoRotate extends Module {

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            packet.yaw = Wrapper.getPlayer().rotationYaw;
            packet.pitch = Wrapper.getPlayer().rotationPitch;
        }
    };

}
