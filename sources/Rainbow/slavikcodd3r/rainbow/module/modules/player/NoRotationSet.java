package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Mod(displayName = "NoRotationSet")
public class NoRotationSet extends Module
{
    @EventTarget
    private void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            packet.field_148936_d = ClientUtils.yaw();
            packet.field_148937_e = ClientUtils.pitch();
        }
    }
}
