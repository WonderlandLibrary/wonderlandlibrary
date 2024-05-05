package me.earth.earthhack.impl.services.minecraft;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.services.chat.CommandManager;

public class EventManager extends SubscriberImpl
{
    private static final EventManager INSTANCE = new EventManager();

    private EventManager()
    {

    }

    public static EventManager getInstance()
    {
        return INSTANCE;
    }

    public void init()
    {
        Earthhack.EVENT_BUS.register(new KeyboardListener());
        Earthhack.EVENT_BUS.register(new Logo());
        Earthhack.EVENT_BUS.subscribe(ChatManager.getInstance());
        Earthhack.EVENT_BUS.subscribe(CommandManager.getInstance());
    }

}
