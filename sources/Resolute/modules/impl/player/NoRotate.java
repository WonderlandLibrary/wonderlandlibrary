// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class NoRotate extends Module
{
    public NoRotate() {
        super("NoRotate", 0, "Cancels server side rotate packets", Category.PLAYER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket) {
            if (NoRotate.mc.thePlayer == null || NoRotate.mc.theWorld == null) {
                return;
            }
            if (((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook packetPlayerPosLook = ((EventPacket)e).getPacket();
                packetPlayerPosLook.setYaw(NoRotate.mc.thePlayer.rotationYaw);
                packetPlayerPosLook.setPitch(NoRotate.mc.thePlayer.rotationPitch);
            }
        }
    }
}
