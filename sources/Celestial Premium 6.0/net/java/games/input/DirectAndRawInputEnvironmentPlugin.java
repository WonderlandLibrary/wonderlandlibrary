/*
 * Decompiled with CFR 0.150.
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

    public Controller[] getControllers() {
        if (this.controllers == null) {
            int i;
            boolean rawKeyboardFound = false;
            boolean rawMouseFound = false;
            ArrayList<Controller> tempControllers = new ArrayList<Controller>();
            Controller[] dinputControllers = this.dinputPlugin.getControllers();
            Controller[] rawControllers = this.rawPlugin.getControllers();
            for (i = 0; i < rawControllers.length; ++i) {
                tempControllers.add(rawControllers[i]);
                if (rawControllers[i].getType() == Controller.Type.KEYBOARD) {
                    rawKeyboardFound = true;
                    continue;
                }
                if (rawControllers[i].getType() != Controller.Type.MOUSE) continue;
                rawMouseFound = true;
            }
            for (i = 0; i < dinputControllers.length; ++i) {
                if (dinputControllers[i].getType() == Controller.Type.KEYBOARD) {
                    if (rawKeyboardFound) continue;
                    tempControllers.add(dinputControllers[i]);
                    continue;
                }
                if (dinputControllers[i].getType() == Controller.Type.MOUSE) {
                    if (rawMouseFound) continue;
                    tempControllers.add(dinputControllers[i]);
                    continue;
                }
                tempControllers.add(dinputControllers[i]);
            }
            this.controllers = tempControllers.toArray(new Controller[0]);
        }
        return this.controllers;
    }

    public boolean isSupported() {
        return this.rawPlugin.isSupported() || this.dinputPlugin.isSupported();
    }
}

