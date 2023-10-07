// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.RegisterEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class NoRotate extends Module
{
    public NoRotate(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isIncoming() && ep.getPacket() instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook)ep.getPacket();
                pac.yaw = NoRotate.mc.thePlayer.rotationYaw;
                pac.pitch = NoRotate.mc.thePlayer.rotationPitch;
            }
        }
    }
}
