package me.earth.earthhack.impl.event.events.client;

import me.earth.earthhack.api.event.events.Event;

import java.util.UUID;

public class FriendEvent extends Event
{
    private final FriendType type;
    private final String name;
    private final UUID uuid;

    public FriendEvent(FriendType type, String name, UUID uuid)
    {
        this.type = type;
        this.name = name;
        this.uuid = uuid;

    }

    public FriendType getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public UUID getUuid()
    {
        return uuid;
    }

}
