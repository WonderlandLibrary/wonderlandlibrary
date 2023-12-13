package me.earth.earthhack.impl.modules.client.pingbypass.submodules.ssurround;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.misc.TickEvent;

public class TickListener extends ModuleListener<ServerSurround, TickEvent>
{
    protected TickListener(ServerSurround module)
    {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event)
    {
        module.onTick();
    }

}
