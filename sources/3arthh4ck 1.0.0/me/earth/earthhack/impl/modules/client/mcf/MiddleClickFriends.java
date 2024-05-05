package me.earth.earthhack.impl.modules.client.mcf;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;

public class MiddleClickFriends extends Module
{
    private static final MiddleClickFriends INSTANCE = new MiddleClickFriends();

    private MiddleClickFriends()
    {
        super("MCF", Category.Client);
        this.listeners.add(new MiddleClickListener(this));
    }

    public static MiddleClickFriends getInstance()
    {
        return INSTANCE;
    }
}
