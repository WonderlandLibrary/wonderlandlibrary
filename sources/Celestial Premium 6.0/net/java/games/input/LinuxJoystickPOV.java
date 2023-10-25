/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import net.java.games.input.Component;
import net.java.games.input.LinuxEnvironmentPlugin;
import net.java.games.input.LinuxJoystickAxis;

public class LinuxJoystickPOV
extends LinuxJoystickAxis {
    private LinuxJoystickAxis hatX;
    private LinuxJoystickAxis hatY;

    LinuxJoystickPOV(Component.Identifier.Axis id, LinuxJoystickAxis hatX, LinuxJoystickAxis hatY) {
        super(id, false);
        this.hatX = hatX;
        this.hatY = hatY;
    }

    protected LinuxJoystickAxis getXAxis() {
        return this.hatX;
    }

    protected LinuxJoystickAxis getYAxis() {
        return this.hatY;
    }

    protected void updateValue() {
        float last_x = this.hatX.getPollData();
        float last_y = this.hatY.getPollData();
        this.resetHasPolled();
        if (last_x == -1.0f && last_y == -1.0f) {
            this.setValue(0.125f);
        } else if (last_x == -1.0f && last_y == 0.0f) {
            this.setValue(1.0f);
        } else if (last_x == -1.0f && last_y == 1.0f) {
            this.setValue(0.875f);
        } else if (last_x == 0.0f && last_y == -1.0f) {
            this.setValue(0.25f);
        } else if (last_x == 0.0f && last_y == 0.0f) {
            this.setValue(0.0f);
        } else if (last_x == 0.0f && last_y == 1.0f) {
            this.setValue(0.75f);
        } else if (last_x == 1.0f && last_y == -1.0f) {
            this.setValue(0.375f);
        } else if (last_x == 1.0f && last_y == 0.0f) {
            this.setValue(0.5f);
        } else if (last_x == 1.0f && last_y == 1.0f) {
            this.setValue(0.625f);
        } else {
            LinuxEnvironmentPlugin.logln("Unknown values x = " + last_x + " | y = " + last_y);
            this.setValue(0.0f);
        }
    }
}

