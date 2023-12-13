package me.earth.earthhack.impl.services.thread;

import java.util.List;
import java.util.UUID;

public abstract class LookUp
{
    protected String name;
    protected UUID uuid;
    protected List<String> names;
    protected Type type;

    public LookUp(Type type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public LookUp(Type type, UUID uuid)
    {
        this.type = type;
        this.uuid = uuid;
    }

    public abstract void onSuccess();

    public abstract void onFailure();

    public enum Type
    {
        NAME,
        UUID,
        HISTORY
    }

}
