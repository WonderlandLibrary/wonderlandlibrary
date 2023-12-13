/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.AbstractController;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.LinuxAbstractController;
import net.java.games.input.LinuxJoystickAbstractController;

public class LinuxCombinedController
extends AbstractController {
    private LinuxAbstractController eventController;
    private LinuxJoystickAbstractController joystickController;

    LinuxCombinedController(LinuxAbstractController eventController, LinuxJoystickAbstractController joystickController) {
        super(eventController.getName(), joystickController.getComponents(), eventController.getControllers(), eventController.getRumblers());
        this.eventController = eventController;
        this.joystickController = joystickController;
    }

    @Override
    protected boolean getNextDeviceEvent(Event event) throws IOException {
        return this.joystickController.getNextDeviceEvent(event);
    }

    @Override
    public final Controller.PortType getPortType() {
        return this.eventController.getPortType();
    }

    @Override
    public final void pollDevice() throws IOException {
        this.eventController.pollDevice();
        this.joystickController.pollDevice();
    }

    @Override
    public Controller.Type getType() {
        return this.eventController.getType();
    }
}

