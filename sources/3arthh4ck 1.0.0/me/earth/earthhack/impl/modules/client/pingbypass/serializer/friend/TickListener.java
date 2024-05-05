package me.earth.earthhack.impl.modules.client.pingbypass.serializer.friend;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.misc.TickEvent;

public class TickListener extends EventListener<TickEvent>
{
    private final FriendSerializer serializer;

    protected TickListener(FriendSerializer serializer)
    {
        super(TickEvent.class);
        this.serializer = serializer;
    }

    @Override
    public void invoke(TickEvent event)
    {
        serializer.onTick();
    }

}
