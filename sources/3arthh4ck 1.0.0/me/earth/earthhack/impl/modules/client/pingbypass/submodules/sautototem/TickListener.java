package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautototem;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.misc.TickEvent;

public class TickListener extends ModuleListener<ServerAutoTotem, TickEvent>
{
    protected TickListener(ServerAutoTotem module)
    {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event)
    {
        module.onTick();
    }

}
