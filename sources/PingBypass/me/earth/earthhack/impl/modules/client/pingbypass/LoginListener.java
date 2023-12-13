package me.earth.earthhack.impl.modules.client.pingbypass;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import net.minecraft.network.login.client.CPacketLoginStart;

public class LoginListener extends ModuleListener<PingBypass, PacketEvent.Send<CPacketLoginStart>>
{
    protected LoginListener(PingBypass module)
    {
        super(module, PacketEvent.Send.class, CPacketLoginStart.class);
    }

    @Override
    public void invoke(PacketEvent.Send<CPacketLoginStart> event)
    {
        module.friendSerializer.clear();
        module.serializer.clear();
    }

}
