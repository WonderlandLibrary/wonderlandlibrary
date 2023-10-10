/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;

public abstract class AbstractController
implements Controller {
    static final int EVENT_QUEUE_DEPTH = 32;
    private static final Event event = new Event();
    private final String name;
    private final Component[] components;
    private final Controller[] children;
    private final Rumbler[] rumblers;
    private final Map<Component.Identifier, Component> id_to_components = new HashMap<Component.Identifier, Component>();
    private EventQueue event_queue = new EventQueue(32);

    protected AbstractController(String name, Component[] components, Controller[] children, Rumbler[] rumblers) {
        this.name = name;
        this.components = components;
        this.children = children;
        this.rumblers = rumblers;
        for (int i2 = components.length - 1; i2 >= 0; --i2) {
            this.id_to_components.put(components[i2].getIdentifier(), components[i2]);
        }
    }

    @Override
    public final Controller[] getControllers() {
        return this.children;
    }

    @Override
    public final Component[] getComponents() {
        return this.components;
    }

    @Override
    public final Component getComponent(Component.Identifier id) {
        return this.id_to_components.get(id);
    }

    @Override
    public final Rumbler[] getRumblers() {
        return this.rumblers;
    }

    @Override
    public Controller.PortType getPortType() {
        return Controller.PortType.UNKNOWN;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public Controller.Type getType() {
        return Controller.Type.UNKNOWN;
    }

    @Override
    public final void setEventQueueSize(int size) {
        try {
            this.setDeviceEventQueueSize(size);
            this.event_queue = new EventQueue(size);
        }
        catch (IOException e2) {
            ControllerEnvironment.log("Failed to create new event queue of size " + size + ": " + e2);
        }
    }

    protected void setDeviceEventQueueSize(int size) throws IOException {
    }

    @Override
    public final EventQueue getEventQueue() {
        return this.event_queue;
    }

    protected abstract boolean getNextDeviceEvent(Event var1) throws IOException;

    protected void pollDevice() throws IOException {
    }

    @Override
    public synchronized boolean poll() {
        Component[] components = this.getComponents();
        try {
            this.pollDevice();
            for (int i2 = 0; i2 < components.length; ++i2) {
                AbstractComponent component = (AbstractComponent)components[i2];
                if (component.isRelative()) {
                    component.setPollData(0.0f);
                    continue;
                }
                component.resetHasPolled();
            }
            while (this.getNextDeviceEvent(event)) {
                AbstractComponent component = (AbstractComponent)event.getComponent();
                float value = event.getValue();
                if (component.isRelative()) {
                    if (value == 0.0f) continue;
                    component.setPollData(component.getPollData() + value);
                } else {
                    if (value == component.getEventValue()) continue;
                    component.setEventValue(value);
                }
                if (this.event_queue.isFull()) continue;
                this.event_queue.add(event);
            }
            return true;
        }
        catch (IOException e2) {
            ControllerEnvironment.log("Failed to poll device: " + e2.getMessage());
            return false;
        }
    }
}

