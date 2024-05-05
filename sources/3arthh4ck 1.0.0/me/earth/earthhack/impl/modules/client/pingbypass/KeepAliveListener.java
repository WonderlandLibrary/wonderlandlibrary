package me.earth.earthhack.impl.modules.client.pingbypass;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.server.SPacketKeepAlive;

public class KeepAliveListener extends ModuleListener<PingBypass, PacketEvent.Receive<SPacketKeepAlive>>
{
    protected KeepAliveListener(PingBypass module)
    {
        super(module, PacketEvent.Receive.class, SPacketKeepAlive.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketKeepAlive> event)
    {
        SPacketKeepAlive packet = event.getPacket();
        if (!module.handled && packet.getId() > 0 && packet.getId() < 1000)
        {
            module.startTime = System.currentTimeMillis() - module.startTime;
            module.serverPing = (int) packet.getId();
            module.ping = module.startTime;
            module.handled = true;
            event.setCancelled(true);
        }
    }

}
