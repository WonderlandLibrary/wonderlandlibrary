package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class AntiVelocity extends Module {

    public AntiVelocity(ModuleData data) {
        super(data);
        settings.put(HORIZONTAL, new Setting<>(HORIZONTAL, 0, "Horizontal velocity factor.", 10, 0, 100));
        settings.put(VERTICAL, new Setting<>(VERTICAL, 0, "Vertical velocity factor.", 10, 0, 100));
    }

    public static String HORIZONTAL = "HORIZONTAL";
    public static String VERTICAL = "VERTICAL";

    @Override
    @RegisterEvent(events = {EventPacket.class})
    public void onEvent(Event event) {
        // Check for incoming packets only
        EventPacket ep = (EventPacket) event;
        if (ep.isOutgoing()) {
            if(ep.getPacket() instanceof C03PacketPlayer)
            return;
        }
        // If the packet handles velocity
        if (ep.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) ep.getPacket();
            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                int vertical = ((Number) settings.get(VERTICAL).getValue()).intValue();
                int horizontal = ((Number) settings.get(HORIZONTAL).getValue()).intValue();
                if (vertical != 0 || horizontal != 0) {
                    packet.setMotX(horizontal * packet.getX() / 100);
                    packet.setMotY(vertical * packet.getY() / 100);
                    packet.setMotZ(horizontal * packet.getZ() / 100);
                } else {
                    ep.setCancelled(true);
                }
            }
        }
        if (ep.getPacket() instanceof S27PacketExplosion) {
            ep.setCancelled(true);
        }
    }

}
