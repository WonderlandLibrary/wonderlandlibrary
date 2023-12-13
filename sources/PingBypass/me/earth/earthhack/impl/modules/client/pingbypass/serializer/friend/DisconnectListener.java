package me.earth.earthhack.impl.modules.client.pingbypass.serializer.friend;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.network.DisconnectEvent;

public class DisconnectListener extends EventListener<DisconnectEvent>
{
    private final FriendSerializer serializer;

    protected DisconnectListener(FriendSerializer serializer)
    {
        super(DisconnectEvent.class);
        this.serializer = serializer;
    }

    @Override
    public void invoke(DisconnectEvent event)
    {
        serializer.clear();
    }

}
