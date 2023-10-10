/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Keyboard;
import net.java.games.input.OSXControllers;
import net.java.games.input.OSXHIDDevice;
import net.java.games.input.OSXHIDQueue;
import net.java.games.input.Rumbler;

final class OSXKeyboard
extends Keyboard {
    private final Controller.PortType port;
    private final OSXHIDQueue queue;

    protected OSXKeyboard(OSXHIDDevice device, OSXHIDQueue queue, Component[] components, Controller[] children, Rumbler[] rumblers) {
        super(device.getProductName(), components, children, rumblers);
        this.queue = queue;
        this.port = device.getPortType();
    }

    @Override
    protected final boolean getNextDeviceEvent(Event event) throws IOException {
        return OSXControllers.getNextDeviceEvent(event, this.queue);
    }

    @Override
    protected final void setDeviceEventQueueSize(int size) throws IOException {
        this.queue.setQueueDepth(size);
    }

    @Override
    public final Controller.PortType getPortType() {
        return this.port;
    }
}

