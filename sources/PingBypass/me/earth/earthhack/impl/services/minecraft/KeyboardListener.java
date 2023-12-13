package me.earth.earthhack.impl.services.minecraft;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.impl.event.events.keyboard.KeyboardEvent;
import me.earth.earthhack.impl.services.client.ModuleManager;

public class KeyboardListener extends EventListener<KeyboardEvent>
{
    protected KeyboardListener()
    {
        super(KeyboardEvent.class);
    }

    @Override
    public void invoke(KeyboardEvent event)
    {
        if (event.getEventState())
        {
            for (Module module : ModuleManager.getInstance().getModules())
            {
                if (module.getBind().getKey() == event.getKey())
                {
                    module.toggle();
                }
            }
        }
    }

}
