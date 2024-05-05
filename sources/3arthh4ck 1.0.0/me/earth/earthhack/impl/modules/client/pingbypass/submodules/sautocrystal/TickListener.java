package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.keyboard.KeyboardEvent;

public class TickListener extends ModuleListener<ServerAutoCrystal, KeyboardEvent.Post>
{
    protected TickListener(ServerAutoCrystal module)
    {
        super(module, KeyboardEvent.Post.class);
    }

    @Override
    public void invoke(KeyboardEvent.Post event)
    {
        module.onTick();
    }

}
