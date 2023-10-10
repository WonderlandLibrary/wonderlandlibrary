/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.util.ArrayList;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.DirectInputEnvironmentPlugin;
import net.java.games.input.RawInputEnvironmentPlugin;

public class DirectAndRawInputEnvironmentPlugin
extends ControllerEnvironment {
    private RawInputEnvironmentPlugin rawPlugin;
    private DirectInputEnvironmentPlugin dinputPlugin = new DirectInputEnvironmentPlugin();
    private Controller[] controllers = null;

    public DirectAndRawInputEnvironmentPlugin() {
        this.rawPlugin = new RawInputEnvironmentPlugin();
    }

    @Override
    public Controller[] getControllers() {
        if (this.controllers == null) {
            int i2;
            boolean rawKeyboardFound = false;
            boolean rawMouseFound = false;
            ArrayList<Controller> tempControllers = new ArrayList<Controller>();
            Controller[] dinputControllers = this.dinputPlugin.getControllers();
            Controller[] rawControllers = this.rawPlugin.getControllers();
            for (i2 = 0; i2 < rawControllers.length; ++i2) {
                tempControllers.add(rawControllers[i2]);
                if (rawControllers[i2].getType() == Controller.Type.KEYBOARD) {
                    rawKeyboardFound = true;
                    continue;
                }
                if (rawControllers[i2].getType() != Controller.Type.MOUSE) continue;
                rawMouseFound = true;
            }
            for (i2 = 0; i2 < dinputControllers.length; ++i2) {
                if (dinputControllers[i2].getType() == Controller.Type.KEYBOARD) {
                    if (rawKeyboardFound) continue;
                    tempControllers.add(dinputControllers[i2]);
                    continue;
                }
                if (dinputControllers[i2].getType() == Controller.Type.MOUSE) {
                    if (rawMouseFound) continue;
                    tempControllers.add(dinputControllers[i2]);
                    continue;
                }
                tempControllers.add(dinputControllers[i2]);
            }
            this.controllers = tempControllers.toArray(new Controller[0]);
        }
        return this.controllers;
    }

    @Override
    public boolean isSupported() {
        return this.rawPlugin.isSupported() || this.dinputPlugin.isSupported();
    }
}

