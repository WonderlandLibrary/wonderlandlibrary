/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.DIControllers;
import net.java.games.input.Event;
import net.java.games.input.IDirectInputDevice;
import net.java.games.input.Keyboard;
import net.java.games.input.Rumbler;

final class DIKeyboard
extends Keyboard {
    private final IDirectInputDevice device;

    protected DIKeyboard(IDirectInputDevice device, Component[] components, Controller[] children, Rumbler[] rumblers) {
        super(device.getProductName(), components, children, rumblers);
        this.device = device;
    }

    @Override
    protected final boolean getNextDeviceEvent(Event event) throws IOException {
        return DIControllers.getNextDeviceEvent(event, this.device);
    }

    @Override
    public final void pollDevice() throws IOException {
        this.device.pollAll();
    }

    @Override
    protected final void setDeviceEventQueueSize(int size) throws IOException {
        this.device.setBufferSize(size);
    }
}

