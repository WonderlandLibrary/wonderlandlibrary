package me.earth.earthhack.api.event.bus;

import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.earthhack.api.event.bus.api.Subscriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple implementation of the Subscriber interface.
 */
public class SubscriberImpl implements Subscriber
{
    protected final List<Listener<?>> listeners = new ArrayList<>();

    @Override
    public Collection<Listener<?>> getListeners()
    {
        return listeners;
    }

}
