// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class AntiVelocity extends Module
{
    public static String HORIZONTAL;
    public static String VERTICAL;
    
    public AntiVelocity(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Integer>>)this.settings).put(AntiVelocity.HORIZONTAL, new Setting<Integer>(AntiVelocity.HORIZONTAL, 0, "Horizontal velocity factor.", 10.0, 0.0, 100.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(AntiVelocity.VERTICAL, new Setting<Integer>(AntiVelocity.VERTICAL, 0, "Vertical velocity factor.", 10.0, 0.0, 100.0));
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        final EventPacket ep = (EventPacket)event;
        if (ep.isOutgoing()) {
            return;
        }
        if (ep.getPacket() instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity packet = (S12PacketEntityVelocity)ep.getPacket();
            if (packet.getEntityID() == AntiVelocity.mc.thePlayer.getEntityId()) {
                final int vertical = ((HashMap<K, Setting<Number>>)this.settings).get(AntiVelocity.VERTICAL).getValue().intValue();
                final int horizontal = ((HashMap<K, Setting<Number>>)this.settings).get(AntiVelocity.HORIZONTAL).getValue().intValue();
                if (vertical != 0 || horizontal != 0) {
                    packet.setMotX(horizontal * packet.getX() / 100);
                    packet.setMotY(vertical * packet.getY() / 100);
                    packet.setMotZ(horizontal * packet.getZ() / 100);
                }
                else {
                    ep.setCancelled(true);
                }
            }
        }
    }
    
    static {
        AntiVelocity.HORIZONTAL = "HORIZONTAL";
        AntiVelocity.VERTICAL = "VERTICAL";
    }
}
