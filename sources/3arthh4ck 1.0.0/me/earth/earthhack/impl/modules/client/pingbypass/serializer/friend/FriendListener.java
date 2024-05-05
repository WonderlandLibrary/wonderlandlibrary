package me.earth.earthhack.impl.modules.client.pingbypass.serializer.friend;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.client.FriendEvent;

public class FriendListener extends EventListener<FriendEvent>
{
    private final FriendSerializer serializer;

    protected FriendListener(FriendSerializer serializer)
    {
        super(FriendEvent.class, Integer.MIN_VALUE);
        this.serializer = serializer;
    }

    @Override
    public void invoke(FriendEvent event)
    {
        serializer.onChange(event);
    }

}
