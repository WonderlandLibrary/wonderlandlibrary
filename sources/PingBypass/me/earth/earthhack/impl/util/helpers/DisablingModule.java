package me.earth.earthhack.impl.util.helpers;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.impl.event.events.client.ShutDownEvent;
import me.earth.earthhack.impl.event.events.misc.DeathEvent;
import me.earth.earthhack.impl.event.events.network.DisconnectEvent;
import me.earth.earthhack.impl.gui.module.SubModule;

/**
 * A module that turns off when you
 * log off/ close the game/ die.
 */
public class DisablingModule extends SubModule
{
    protected DisablingModule(String name, Category category)
    {
        this(name, category, null);
    }

    protected DisablingModule(String name, Category category, Module parent)
    {
        super(name, category, parent);
        this.listeners.add(new EventListener<DisconnectEvent>(DisconnectEvent.class)
        {
            @Override
            public void invoke(DisconnectEvent event)
            {
                disable();
            }
        });
        this.listeners.add(new EventListener<DeathEvent>(DeathEvent.class)
        {
            @Override
            public void invoke(DeathEvent event)
            {
                if (event.getEntity().equals(mc.player))
                {
                    disable();
                }
            }
        });
        this.listeners.add(new EventListener<ShutDownEvent>(ShutDownEvent.class)
        {
            @Override
            public void invoke(ShutDownEvent event)
            {
                disable();
            }
        });
    }

}
