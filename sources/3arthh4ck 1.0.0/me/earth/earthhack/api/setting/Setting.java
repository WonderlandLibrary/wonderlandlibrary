package me.earth.earthhack.api.setting;

import com.google.gson.JsonElement;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.event.events.SettingEvent;

import java.util.function.Predicate;

//I feel like for this the observable pattern could be more elegant than posting events.
public abstract class Setting<T>
{
    protected final String name;
    protected final T initial;

    protected SettingContainer container;
    protected Predicate<T> visibility;
    protected boolean sub;
    protected T value;

    protected Setting(String nameIn, T initialValue)
    {
        this.name = nameIn;
        this.initial = initialValue;
        this.value = initialValue;
    }

    public abstract void fromJson(JsonElement element);

    /**
     * Sets this settings value from String.
     *
     * @param string the string to set the value from.
     * @return true if successful.
     */
    public abstract boolean fromString(String string);

    /**
     * Inputs for the command system.
     *
     * @param string command input
     * @return possible inputs.
     */
    public abstract String getInputs(String string);

    public void setValue(T value)
    {
        setValue(value, true);
    }

    public void setValue(T value, boolean withEvent)
    {
        if (withEvent)
        {
            SettingEvent<T> event = new SettingEvent<>(this, value);
            Bus.EVENT_BUS.post(event, value.getClass());
            if (!event.isCancelled())
            {
                this.value = event.getValue();
            }
        }
        else
        {
            this.value = value;
        }
    }

    public T getValue()
    {
        return value;
    }

    public T getInitial()
    {
        return initial;
    }

    public void reset()
    {
        value = initial;
    }

    public Setting<T> withVisibility(Predicate<T> predicate)
    {
        this.visibility = predicate;
        return this;
    }

    public Setting<T> setSub(boolean sub)
    {
        this.sub = sub;
        return this;
    }

    public boolean isVisible()
    {
        return visibility == null || visibility.test(getValue());
    }

    public boolean isSub()
    {
        return sub;
    }

    public String getName()
    {
        return name;
    }

    protected void setContainer(SettingContainer container)
    {
        this.container = container;
    }

    public SettingContainer getContainer()
    {
        return container;
    }

}
