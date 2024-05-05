package me.zane.basicbus.api.bus.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.zane.basicbus.api.bus.Bus;
import me.zane.basicbus.api.bus.CallLocation;
import me.zane.basicbus.api.invocation.Invoker;

public final class EventBusImpl<T> implements Bus<T> {
    private final Map<Class<?>, List<CallLocation>> eventClassMethodMap = new ConcurrentHashMap<>();

    private final Invoker invoker;

    public EventBusImpl(Invoker invoker) {
        this.invoker = invoker;
    }

    public Invoker invoker() {
        return this.invoker;
    }

    public Map<Class<?>, List<CallLocation>> map() {
        return this.eventClassMethodMap;
    }
}
