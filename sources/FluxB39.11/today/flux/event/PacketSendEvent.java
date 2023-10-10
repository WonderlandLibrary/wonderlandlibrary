package today.flux.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.utility.RotationUtils;

public class PacketSendEvent extends EventCancellable {
    @Getter
    @Setter
	public Packet packet;

    public PacketSendEvent(Packet packet) {
        this.packet = packet;
        if (packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            float yaw = ((C03PacketPlayer) packet).getYaw();
            float pitch = ((C03PacketPlayer) packet).getPitch();
            RotationUtils.serverRotation.setYaw(yaw);
            RotationUtils.serverRotation.setPitch(pitch);
        }
    }
}
