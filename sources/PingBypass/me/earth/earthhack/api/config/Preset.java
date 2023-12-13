package me.earth.earthhack.api.config;

import me.earth.earthhack.api.setting.SettingContainer;

public abstract class Preset
{
    private final SettingContainer container;
    private final String name;

    public Preset(String name, SettingContainer container)
    {
        this.name = name;
        this.container = container;
    }

    public abstract void apply();

    public String getName()
    {
        return name;
    }

    public SettingContainer getContainer()
    {
        return container;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Preset)
        {
            Preset other = (Preset) o;
            return other.name.equals(this.name) && other.container.equals(this.container);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

}
